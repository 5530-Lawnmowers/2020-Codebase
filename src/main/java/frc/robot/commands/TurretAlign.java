/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;
import frc.robot.helpers.LimelightHelper;

public class TurretAlign extends CommandBase {
  private Turret turret;
  private final int MARGIN = 1;
  private double previousOffset;
  private final double kp = .01;
  private final double kd = 0;


  /**
   * Creates a new TurretAlign.
   */
  public TurretAlign(Turret turret) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.turret = turret;
    addRequirements(turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    int currentPosition = turret.getEncoderValue();
    double offset = LimelightHelper.getRawX();
    turret.setPosition(currentPosition + (int) Math.round(offset * 4096 / 360)); //Instant set

    previousOffset = offset;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /**
    //Align via continous power set
    double offset = LimelightHelper.getRawX();
    double offsetVelocity = Math.abs(offset - previousOffset);
    turret.setTurret(kp * offset - kd * offsetVelocity);
    previousOffset = offset;
    */
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stopTurret();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
