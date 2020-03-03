/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class IntakeNew extends CommandBase {
  private Intake intake;
  private Delivery delivery;
  private final double intakeSpeed = -1.0;
  private final double beltSpeed = 0.5;

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
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setIntake(intakeSpeed);
    if (delivery.getBreakbeams()[3]) {
      delivery.stopDeliveryBelt();
    } else if (intake.getSwitch()) {
      delivery.setDeliveryBelt(beltSpeed);
    } else {
      delivery.stopDeliveryBelt();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    delivery.stopDeliveryBelt();
    intake.stopIntake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
