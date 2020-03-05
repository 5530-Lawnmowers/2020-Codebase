/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class IntakeTogglePosition extends CommandBase {
  private Intake intake;
  private boolean goDown;
  private final double SPEED = 0.5;

  /**
   * Creates a new IntakeTogglePosition.
   */
  public IntakeTogglePosition(Intake intake) {
    addRequirements(intake);
    this.intake = intake;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    goDown = intake.getActuationState();
    intake.toggleActuationState();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (goDown && intake.getActuationPosition() > intake.START - intake.DIFF) {
      intake.setIntakeActuation(-SPEED);
    } else if (!goDown && intake.getActuationPosition() < intake.START - 0.25) {
      intake.setIntakeActuation(SPEED);
    } else {
      intake.stopIntakeActuation();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntakeActuation();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (goDown && intake.getActuationPosition() <= intake.START - intake.DIFF)
      || (!goDown && intake.getActuationPosition() >= intake.START - 0.25);
  }
}
