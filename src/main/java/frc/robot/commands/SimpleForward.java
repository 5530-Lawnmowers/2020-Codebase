/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;

public class SimpleForward extends CommandBase {
  /**
   * Creates a new SimpleForward.
   */
  private Drivetrain drivetrain;
  private int counter;

  public SimpleForward(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    counter = 0;
    drivetrain.setBrakeMode(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.setDrivetrainMotor(-.25, Constants.DT_L1);
    drivetrain.setDrivetrainMotor(-.25, Constants.DT_L2);
    drivetrain.setDrivetrainMotor(.25, Constants.DT_R1);
    drivetrain.setDrivetrainMotor(.25, Constants.DT_R2);
    
    counter++;
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

    return counter >= 50;
    //return false;
  }
}
