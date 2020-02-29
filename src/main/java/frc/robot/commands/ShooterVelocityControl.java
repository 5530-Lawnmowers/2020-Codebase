/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class ShooterVelocityControl extends CommandBase {
  private Delivery delivery;
  private Shooter shooter;
  private final double targetVelocity = 4500;
  private final double thresholdVelocity = 4450;
  private double kP = 6E-5;
  private final double kI = 0;
  private final double kD = 0;
  private final double kFF = 0.00015;
  private final double kMaxOutput = 1;
  private final double kMinOutput = -1;
  private boolean feed;
  private boolean resetBeltRef;
  private double beltRefPosition;

  /**
   * Creates a new ShooterVelocityControl.
   */
  public ShooterVelocityControl(Shooter shooter, Delivery delivery) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter, delivery);
    this.shooter = shooter;
    this.delivery = delivery;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.shooterPID1.setP(kP);
    shooter.shooterPID1.setI(kI);
    shooter.shooterPID1.setD(kD);
    shooter.shooterPID1.setFF(kFF);
    shooter.shooterPID1.setOutputRange(kMinOutput, kMaxOutput);

    shooter.shooterPID2.setP(kP);
    shooter.shooterPID2.setI(kI);
    shooter.shooterPID2.setD(kD);
    shooter.shooterPID2.setFF(kFF);
    shooter.shooterPID2.setOutputRange(kMinOutput, kMaxOutput);

    feed = false;
    resetBeltRef = false;
    beltRefPosition = delivery.getDeliveryBeltPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (shooter.getShooterVelocity() < thresholdVelocity) {
      shooter.setShooter(1.0);
      feed = false;
    } else if (shooter.getShooterVelocity() < targetVelocity - 10) {
      shooter.shooterPID1.setReference(-targetVelocity, ControlType.kVelocity);
      shooter.shooterPID2.setReference(targetVelocity, ControlType.kVelocity);
      feed = false;
    } else {
      shooter.shooterPID1.setReference(-targetVelocity, ControlType.kVelocity);
      shooter.shooterPID2.setReference(targetVelocity, ControlType.kVelocity);
      feed = true;
    }

    if (feed) {
      delivery.setDeliveryBelt(1.0);
      resetBeltRef = true;
    } else {
      if (resetBeltRef) {
        beltRefPosition = delivery.getDeliveryBeltPosition();
        resetBeltRef = false;
      }
      if (Math.abs(delivery.getDeliveryBeltPosition() - beltRefPosition) > 1) {
        delivery.stopDeliveryBelt();
      } else {
        delivery.setDeliveryBelt(-0.5);
      }
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
