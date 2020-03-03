/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;
import frc.robot.Robot;
import frc.robot.helpers.ShuffleboardHelpers;

public class ShootSmart extends CommandBase {
  private Delivery delivery;
  private Shooter shooter;
  private final double accel = 1.0;
  private final double shoot = 0.9;
  private final double feed = 1.0;
  private final double backdrive = -0.4;
  private final int TARGET = 4500;
  private final int THRESHOLD = 4450;

  private int counter = 0;

  /**
   * Creates a new ShootSmart.
   */
  public ShootSmart(Delivery delivery, Shooter shooter) {
    addRequirements(delivery, shooter);
    this.delivery = delivery;
    this.shooter = shooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    counter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (shooter.getShooterVelocity() < THRESHOLD) {
      shooter.setShooter(accel);
      if (delivery.getBreakbeams()[3]) {
        delivery.setDeliveryBelt(backdrive);
      } else {
        delivery.stopDeliveryBelt();
      }
    } else if (shooter.getShooterVelocity() < TARGET) {
      shooter.setShooter(shoot);
    } else {
      shooter.setShooter(shoot);
      delivery.setDeliveryBelt(feed);
    }


    ShuffleboardHelpers.setWidgetValue("Shooter", "Shooter Velocity", shooter.getShooterVelocity());

    boolean change = true;
    boolean[] breakbeams = delivery.getBreakbeams();
    for (boolean beam : breakbeams)
      if (beam)
        change = false;
    if (change) {
      counter++;
    } else {
      counter = 0;
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
    return Robot.auton && counter > 50;
  }
}
