/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class DeliveryEject extends CommandBase {
  Intake intake;
  Delivery delivery;

  /**
   * Creates a new DeliveryEject.
   */
  public DeliveryEject(Intake intake, Delivery delivery) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake, delivery);
    this.intake = intake;
    this.delivery = delivery;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setIntake(0.4);
    delivery.setDeliveryBelt(-0.8);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    delivery.stopDeliveryBelt();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
