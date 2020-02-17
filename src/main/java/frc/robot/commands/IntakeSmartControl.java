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
    if (!intake.getSwitch()) {
      intake.setIntake(intakeSet);
    } else {
      intake.setIntake(intakeSet);
      if (furthestTrigger <= 3 && delivery.getBreakbeams()[furthestTrigger]) {
        delivery.stopDeliveryWheel();
        delivery.stopDeliveryBelt();
        furthestTrigger++;
      } else {
        delivery.setDeliveryBelt(beltSet);
        delivery.setDeliveryWheel(wheelSet);
      }
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
    if (delivery.getBreakbeams()[3]) {
      return true;
    }
    return false;
  }
}
