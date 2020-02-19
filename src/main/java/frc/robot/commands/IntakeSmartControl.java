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
  private double intakeFeedOffset = 8;
  private double intakeTriggerPosition;
  private boolean intakeTriggerReset;

  private boolean newBall = false;
  private boolean beltGood = true;

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
    intakeFeedOffset = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Feed Offset");

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

    newBall = false;
    beltGood = true;
    intakeTriggerReset = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // // First attempt at control (complex, uses all sensors)
    // // Intake control logic
    // if (!intake.getSwitch()) { // If intake switch is not tripped
    //   intake.setIntake(intakeSet); // Run intake
    //   intakeTriggerReset = true; // Allow the triggerPosition to be reset
    // } else { // If intake switch is tripped
    //   if (intakeTriggerReset) { // If triggerPosition reset is allowed
    //     intakeTriggerPosition = intake.getIntakePosition(); // Reset intake trigger position
    //     intakeTriggerReset = false; // Disallow reset until switch untripped
    //   } // If triggerPosition reset not allowed, do nothing (switch has yet to be let go)
    //   newBall = true; // New ball in the system not yet placed
    //   intake.setIntake(intakeSet); // If intake switch is tripped run intake at (maybe?) different level
    //   beltGood = false; //Belt needs to move
    // }

    // // Delivery Belt Logic
    // if (furthestTrigger <= 3 && delivery.getBreakbeams()[furthestTrigger]) { // If new Breakbeam triggered
    //   if (beltTriggerReset) { // If first time triggered
    //     beltTriggerPosition = delivery.getDeliveryBeltPosition(); // Set new trigger position
    //     beltTriggerReset = false; // Note not first time
    //     beltGood = true;
    //   }
    //   if (Math.abs(delivery.getDeliveryBeltPosition() - beltTriggerPosition) >= beltFeedOffset) { // If offset reached
    //     delivery.stopDeliveryBelt(); //Stop Belt
    //     furthestTrigger++; // New target breakbeam
    //     beltGood = true; //Belt is done
    //     beltTriggerReset = true;
    //   }
    // } else if (!beltGood && newBall && Math.abs(intake.getIntakePosition() - intakeTriggerPosition) >= intakeFeedOffset) { // If intake reach threshold
    //   delivery.setDeliveryBelt(beltSet); // Run delivery
    // } // Otherwise no change

    // //Delivery Wheel Logic
    // if (beltGood && delivery.getBreakbeams()[0]) { //If belt done and ball in position
    //   delivery.setDeliveryBelt(beltSet); // Move belt forward
    //   if (wheelTriggerReset) {
    //     wheelTriggerPosition = delivery.getDeliveryWheelPosition();
    //     wheelTriggerReset = false;
    //   }
    //   if (Math.abs(delivery.getDeliveryWheelPosition() - wheelTriggerPosition) >= wheelFeedOffset) {
    //     delivery.stopDeliveryWheel(); //Stop 
    //     delivery.stopDeliveryBelt(); //All
    //     newBall = false; //All in place
    //   }
    // } else if (newBall && Math.abs(intake.getIntakePosition() - intakeTriggerPosition) >= intakeFeedOffset) { //If intake reach threshold
    //   delivery.setDeliveryWheel(wheelSet);
    // }

    // Target Logic
    // if (beltGood && !newBall && delivery.getBreakbeams()[0] && delivery.getBreakbeams()[furthestTrigger]) {
    //   furthestTrigger++;
    // }

    // Second iteration, simpler, should accomplish same results
    if (!intake.getSwitch()) {
      intake.setIntake(intakeSet);
      intakeTriggerReset = true;
    } else {
      if (intakeTriggerReset) {
        intakeTriggerReset = false;
        intakeTriggerPosition = intake.getIntakePosition();
      }
      if (Math.abs(intake.getIntakePosition() - intakeTriggerPosition) >= intakeFeedOffset) {
        intake.stopIntake();
      }
    }

    if (delivery.getBreakbeams()[0]) {
      delivery.stopDeliveryWheel();
    } else {
      delivery.setDeliveryWheel(wheelSet);
    }

    if (delivery.getBreakbeams()[3]) {
      delivery.stopDeliveryBelt();
    } else if (delivery.getBreakbeams()[0]) {
      delivery.setDeliveryBelt(beltSet);
    } else {
      delivery.stopDeliveryBelt();
    }
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
    if (delivery.getBreakbeams()[3] && delivery.getBreakbeams()[0] && intake.getSwitch()) {
      return true;
    }
    return false;
  }
}
