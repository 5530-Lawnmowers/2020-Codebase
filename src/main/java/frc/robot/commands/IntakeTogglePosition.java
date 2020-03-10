/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motion.TrajectoryPoint;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.*;

public class IntakeTogglePosition extends CommandBase {
  IntakeActuation intakeAct;
  /**
   * Creates a new IntakeTogglePosition.
   */
  public IntakeTogglePosition(IntakeActuation intakeAct) {
    this.intakeAct = intakeAct;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeAct.toggleActuationState();
    //ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Act Target L", targetPositionL);
    //ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Act Target R", targetPositionR);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
