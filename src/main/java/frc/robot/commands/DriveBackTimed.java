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

public class DriveBackTimed extends CommandBase {
  private Drivetrain drivetrain;
  private int counter = 0;
  /**
   * Creates a new DriveBackTimed.
   */
  public DriveBackTimed(Drivetrain drivetrain) {
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
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
    drivetrain.setDrivetrainMotor(-0.25, Constants.DT_L1);
    drivetrain.setDrivetrainMotor(-0.25, Constants.DT_L2);
    drivetrain.setDrivetrainMotor(0.25, Constants.DT_R1);
    drivetrain.setDrivetrainMotor(0.25, Constants.DT_R2);
    counter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setDrivetrainMotor(0, Constants.DT_L1);
    drivetrain.setDrivetrainMotor(0, Constants.DT_L2);
    drivetrain.setDrivetrainMotor(0, Constants.DT_R1);
    drivetrain.setDrivetrainMotor(0, Constants.DT_R2);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return counter >= 50;
  }
}
