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

public class IntakeSmartControl extends CommandBase {
  private Intake intake;
  private Delivery delivery;

  private double intakeSet;
  private double wheelSet;
  private double beltSet;

  private int furthestTrigger;
  private double feedOffset = 0.25;
  private double triggerPosition;
  private boolean triggerReset;
  private boolean newBall = false;

  /**
   * Creates a new IntakeSmartControl.
   */
  public IntakeSmartControl(Intake intake, Delivery delivery) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake, delivery);
    this.intake = intake;
    this.delivery = delivery;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeSet = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Set Intake");
    wheelSet = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Set Wheel");
    beltSet = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Set Belt");
    feedOffset = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Feed Offset");

    ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "IntakeSmartControl", "Running");

    if (delivery.getBreakbeams()[2]) {
      furthestTrigger = 3;
    } else if (delivery.getBreakbeams()[1]) {
      furthestTrigger = 2;
    } else if (delivery.getBreakbeams()[0]) {
      furthestTrigger = 1;
    } else {
      furthestTrigger = 0;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // Intake control logic
    if (!intake.getSwitch()) { // If intake switch is not tripped
      intake.setIntake(intakeSet); // Run intake
      triggerReset = true; // Allow the triggerPosition to be reset
    } else { // If intake switch is tripped
      if (triggerReset) { // If triggerPosition reset is allowed
        triggerPosition = intake.getIntakePosition(); // Reset triggerPosition
        triggerReset = false; // Disallow reset until switch untripped
      } // If triggerPosition reset not allowed, do nothing (switch has yet to be let go)
      newBall = true; // New ball in the system not yet placed
      intake.setIntake(intakeSet); // If intake switch is tripped run intake at (maybe?) different level
    }

    // Delivery Logic
    if (furthestTrigger <= 3 && delivery.getBreakbeams()[furthestTrigger]) { // If new Breakbeam triggered
      delivery.stopDeliveryWheel(); // Stop delivery
      delivery.stopDeliveryBelt();
      furthestTrigger++; // New target breakbeam
      newBall = false; // Ball is in place
    } else if (newBall && intake.getIntakePosition() >= triggerPosition + feedOffset) { // If intake reach threshold
      delivery.setDeliveryBelt(beltSet); // Run delivery
      delivery.setDeliveryWheel(wheelSet);
    } // Otherwise no change

    //Slower control
    // if (!newBall && !intake.getSwitch()) { //If no new ball and no trigger, run intake
    //   intake.setIntake(intakeSet);
    //   triggerReset = true;
    // } else { //if new ball or trigger
    //   if (triggerReset) {
    //     triggerPosition = intake.getIntakePosition(); //reset position
    //     triggerReset = false;
    //     newBall = true; //is new ball
    //   }
    //   if (intake.getIntakePosition() >= triggerPosition + feedOffset) { //once reach offset, run delivery
    //     intake.stopIntake();
    //     delivery.setDeliveryBelt(beltSet);
    //     delivery.setDeliveryWheel(wheelSet);
    //   }

    //   if (furthestTrigger <= 3 && delivery.getBreakbeams()[furthestTrigger]) { //if new Breakbeam triggered
    //     delivery.stopDeliveryWheel(); //stop delivery
    //     delivery.stopDeliveryBelt();
    //     furthestTrigger++; // New target breakbeam
    //     newBall = false; // Ball is in place
    //   }
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    delivery.stopDeliveryBelt();
    delivery.stopDeliveryWheel();

    ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "IntakeSmartControl", "Ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (delivery.getBreakbeams()[3]) {
      return true;
    }
    return false;
  }
}
