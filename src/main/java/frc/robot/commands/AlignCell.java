/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.helpers.LimelightHelper;
import frc.robot.subsystems.Drivetrain;

public class AlignCell extends CommandBase {
  /**
   * Creates a new AlignCell.
   */

  private Drivetrain drivetrain;
  private double kp = .005;
  private double prevoffset = 0;
  private double kd = .005;
  public AlignCell(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double offsetVelocity = Math.abs(LimelightHelper.getBackRawX() - prevoffset);
    prevoffset = LimelightHelper.getBackRawX();
    if(LimelightHelper.getBackRawY() < 0){
    drivetrain.setDrivetrainMotor((kp * LimelightHelper.getBackRawX() - kd *offsetVelocity) + RobotContainer.XBController1.getTriggerAxis(GenericHID.Hand.kRight), Constants.DT_L1);
    drivetrain.setDrivetrainMotor((kp * LimelightHelper.getBackRawX() - kd *offsetVelocity) + RobotContainer.XBController1.getTriggerAxis(GenericHID.Hand.kRight), Constants.DT_L2);
    drivetrain.setDrivetrainMotor((-(kp * LimelightHelper.getBackRawX() - kd *offsetVelocity) - RobotContainer.XBController1.getTriggerAxis(GenericHID.Hand.kRight)), Constants.DT_R1);
    drivetrain.setDrivetrainMotor((-(kp * LimelightHelper.getBackRawX() - kd *offsetVelocity) - RobotContainer.XBController1.getTriggerAxis(GenericHID.Hand.kRight)), Constants.DT_R2);
    }
    else{
      drivetrain.setDrivetrainMotor(0, Constants.DT_L1);
      drivetrain.setDrivetrainMotor(0, Constants.DT_L2);
      drivetrain.setDrivetrainMotor(0, Constants.DT_R1);
      drivetrain.setDrivetrainMotor(0, Constants.DT_R2);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
