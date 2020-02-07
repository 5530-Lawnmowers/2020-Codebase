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

public class RunIntake extends CommandBase {
  private double beltSet = 0.4;
  private double wheelSet = 0.4;
  private double intakeSet = 0.4;
  private Delivery delivery;
  private Intake intake;
  /**
   * Creates a new RunIntake.
   */
  public RunIntake(Delivery delivery, Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(delivery, intake);
    this.delivery = delivery;
    this.intake = intake;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeSet = (double) ShuffleboardHelpers.getWidgetValue("Test", "Set Intake");
    wheelSet = (double) ShuffleboardHelpers.getWidgetValue("Test", "Set Wheel");
    beltSet = (double) ShuffleboardHelpers.getWidgetValue("Test", "Set Belt");
    ShuffleboardHelpers.setWidgetValue("Test", "RunIntake", "Running");
    intake.setIntake(intakeSet);
    delivery.setDeliveryBelt(beltSet);
    delivery.setDeliveryWheel(wheelSet);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    delivery.stopDeliveryBelt();
    delivery.stopDeliveryWheel();
    ShuffleboardHelpers.setWidgetValue("Test", "RunIntake", "Ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
