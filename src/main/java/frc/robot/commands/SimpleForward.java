/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.Drivetrain;

public class SimpleForward extends CommandBase {
  /**
   * Creates a new SimpleForward.
   */
  private Drivetrain drivetrain;
  private int counter;
  private int leftStart;
  private int rightStart;
  private int leftTarget;
  private int rightTarget;
  private int distance;
  private int leftLast;
  private int rightLast;

  private final double kP = 0.003;
  private final double kD = 0;

  private boolean back;

  public SimpleForward(Drivetrain drivetrain, boolean back, int distance) {
    this.drivetrain = drivetrain;
    this.back = back;
    this.distance = distance;
    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    counter = 0;
    leftStart = drivetrain.getLeftEncoder();
    rightStart = drivetrain.getRightEncoder();
    leftLast = drivetrain.getLeftEncoder();
    rightLast = drivetrain.getRightEncoder();

    if (back) {
      leftTarget = leftStart - distance;
      rightTarget = rightStart + distance;
    } else {
      leftTarget = leftStart + distance;
      rightTarget = rightStart - distance;
    }
    
    drivetrain.setBrakeMode(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double leftV = Math.abs(drivetrain.getLeftEncoder() - leftLast);
    double rightV = Math.abs(drivetrain.getRightEncoder() - rightLast);
    double leftSet = kP * (leftTarget - drivetrain.getLeftEncoder()) / 1000 - kD * leftV;
    double rightSet = kP * (rightTarget - drivetrain.getRightEncoder()) / 1000 - kD * rightV;


    double rightPower = Math.max(0, minPower(rightSet, 0.15));
    double leftPower = Math.min(0, minPower(leftSet, 0.15));

    
    ShuffleboardHelpers.setWidgetValue("Drivetrain", "leftSet", leftPower);
    ShuffleboardHelpers.setWidgetValue("Drivetrain", "rightSet", rightPower);

    drivetrain.setDrivetrainMotor(rightPower, Constants.DT_R1);
    drivetrain.setDrivetrainMotor(rightPower, Constants.DT_R2);
    drivetrain.setDrivetrainMotor(leftPower, Constants.DT_L1);
    drivetrain.setDrivetrainMotor(leftPower, Constants.DT_L2);





/*
    double left;
    double right;
    if (back) {
      left = -.265;
      right = .25;
    } else {
      left = .265;
      right = -.25;
    }

    if ((drivetrain.getRightEncoder() - rightStart > distance && back)
    || (rightStart - drivetrain.getRightEncoder() > distance && !back)) {
      drivetrain.setDrivetrainMotor(0, Constants.DT_R1);
      drivetrain.setDrivetrainMotor(0, Constants.DT_R2);
    } else {
      drivetrain.setDrivetrainMotor(right, Constants.DT_R1);
      drivetrain.setDrivetrainMotor(right, Constants.DT_R2);
    }
    if ((leftStart - drivetrain.getLeftEncoder() > distance && back)
    || (drivetrain.getLeftEncoder() - leftStart > distance && !back)) {
      drivetrain.setDrivetrainMotor(0, Constants.DT_L1);
      drivetrain.setDrivetrainMotor(0, Constants.DT_L2);
    } else {
      drivetrain.setDrivetrainMotor(left, Constants.DT_L1);
      drivetrain.setDrivetrainMotor(left, Constants.DT_L2);
    }
    */
    
    if (Math.abs(leftStart - drivetrain.getLeftEncoder()) > distance && Math.abs(drivetrain.getRightEncoder() - rightStart) > distance) {
      counter++;
    } else {
      counter = 0;
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setDrivetrainMotor(0, Constants.DT_L1);
    drivetrain.setDrivetrainMotor(0, Constants.DT_L2);
    drivetrain.setDrivetrainMotor(0, Constants.DT_R1);
    drivetrain.setDrivetrainMotor(0, Constants.DT_R2);
    //drivetrain.setBrakeMode(false); //Change as needed
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //if(drivetrain.getDistanceRight() >= -.5){

    return counter >= 25;
    //return false;
  }

  private double minPower(double power, double min) {
    return Math.signum(power) * Math.max(Math.abs(power), min);
  }
}
