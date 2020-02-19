/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.*;

public class TurretLimitInterrupt extends InstantCommand {
  private Turret turret;
  private boolean goRight;
  private final double recoverSpeed = 0.1;

  public TurretLimitInterrupt(Turret turret, boolean overLimit) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turret);
    this.turret = turret;
    goRight = !overLimit;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (goRight) {
      turret.setTurret(recoverSpeed);
    } else {
      turret.setTurret(-recoverSpeed);
    }
  }
}
