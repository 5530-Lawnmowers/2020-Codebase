/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.*;

public class IntakeNew extends CommandBase {
  private Intake intake;
  private Delivery delivery;
  private final double intakeSpeed = 1.0;
  private final double beltSpeed = 0.5;
  private int triggerCounter = 0;

  private final double OFFSET = 0.2;
  private boolean moveBall;
  private double targetPosition;

  /**
   * Creates a new IntakeNew.
   */
  public IntakeNew(Intake intake, Delivery delivery) {
    addRequirements(intake, delivery);
    this.intake = intake;
    this.delivery = delivery;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    triggerCounter = 0;
    moveBall = false;

    delivery.stopDeliveryBelt();
    intake.stopIntake();
    ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Intake Status", true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setIntake(intakeSpeed); // Intake always on

    if (delivery.getBreakbeams()[3]) {
      delivery.stopDeliveryBelt(); // Stop if last breakbeam hit
    } else if (intake.getSwitch()) {
      triggerCounter++; // Increase counter whenever intake breakbeam hit

      if (triggerCounter >= 15){
        delivery.setDeliveryBelt(beltSpeed); // Intake if ball is settled
        moveBall = true;
      }
    } else {
      if (moveBall) {
        targetPosition = delivery.getDeliveryBeltPosition() + OFFSET;
        moveBall = false; // Set target if just moved off of intake breakbeam
      }

      if (delivery.getDeliveryBeltPosition() >= targetPosition) {
        delivery.stopDeliveryBelt(); // If current past target, stop
      }
      triggerCounter = 0; // Reset counter whenever intake not triggered
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    delivery.stopDeliveryBelt();
    intake.stopIntake();
    ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Intake Status", false);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
