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

public class TurretManual extends CommandBase {
private Turret turret;
private double turretSpeed = 0.3;
  
  /**
   * Creates a new TurretManual.
   */
  public TurretManual(Turret turret) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.turret = turret;
    addRequirements(turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turretSpeed = (double) ShuffleboardHelpers.getWidgetValue("Turret", "Set Turret");
    ShuffleboardHelpers.setWidgetValue("Turret", "TurretManual", "Running");
    turret.setTurret(turretSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    ShuffleboardHelpers.setWidgetValue("Turret", "TurretManual", "Ended");
    turret.stopTurret();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
