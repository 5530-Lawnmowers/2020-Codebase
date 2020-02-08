/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class ShootAll extends CommandBase {
  private Delivery delivery;
  private Shooter shooter;
  private double accelSpeed = 1.0;
  private double shootSpeed = 0.9;
  private double feedSpeed = 0.8;
  private final double TARGET_VELOCITY = 4850;
  private final double THRESHOLD_VELOCITY = 4650;

  /**
   * Creates a new ShootAll.
   */
  public ShootAll(Delivery delivery, Shooter shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(delivery, shooter);
    this.shooter = shooter;
    this.delivery = delivery;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (shooter.getShooterVelocity() < THRESHOLD_VELOCITY) {
      shooter.setShooter(accelSpeed);
      delivery.stopDeliveryBelt();
    } else if (shooter.getShooterVelocity() >= THRESHOLD_VELOCITY && shooter.getShooterVelocity() < TARGET_VELOCITY) {
      shooter.setShooter(shootSpeed);
      delivery.stopDeliveryBelt();
    } else if (shooter.getShooterVelocity() >= TARGET_VELOCITY) {
      delivery.setDeliveryBelt(feedSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    delivery.stopDeliveryBelt();
    shooter.stopShooter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
