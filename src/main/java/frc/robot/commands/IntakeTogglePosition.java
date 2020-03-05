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
  private Intake intake;
  private boolean goDown;

  private double targetPositionR;
  private double targetPositionL;
  private double setSpeedL = 0;
  private double setSpeedR = 0;

  private final double kP = 0.05;
  private final double kD = 0;
  private double lastPositionL = 0;
  private double lastPositionR = 0;

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
    setSpeedL = 0;
    setSpeedR = 0;
    lastPositionL = intake.getActuationPositionL();
    lastPositionR = intake.getActuationPositionR();

    if (goDown) {
      targetPositionR = intake.START_R + intake.DIFF;
      targetPositionL = intake.START_L - intake.DIFF;
    } else {
      targetPositionR = intake.START_R + 0.5;
      targetPositionL = intake.START_L - 0.5;
    }

    ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Act Target L", targetPositionL);
    ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Act Target R", targetPositionR);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double velocityL = Math.abs(intake.getActuationPositionL() - lastPositionL);
    double velocityR = Math.abs(intake.getActuationPositionR() - lastPositionR);
    setSpeedL = kP * (targetPositionL - intake.getActuationPositionL()) + kD * velocityL;
    setSpeedR = (kP) * (targetPositionR - intake.getActuationPositionR()) + kD * velocityR;

    intake.setIntakeActuationL(setSpeedL);
    intake.setIntakeActuationR(setSpeedR);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntakeActuation();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(intake.getActuationPositionL() - targetPositionL) <= 1 
      && Math.abs(intake.getActuationPositionR() - targetPositionR) <= 1;
  }
}
