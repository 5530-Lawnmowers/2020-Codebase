/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.*;
import frc.robot.RobotContainer;

public class TurretManual extends CommandBase {
    private Turret turret;
    private double turretSpeed = 0;
    private final double kDeadband = 0.2;

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
        turretSpeed = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        //ShuffleboardHelpers.setWidgetValue("Turret", "TurretManual", "Running");
        //turretSpeed = (double) ShuffleboardHelpers.getWidgetValue("Turret", "Set Turret");
        turretSpeed = deadband(RobotContainer.XBController2.getX(Hand.kLeft), kDeadband) * 0.5;
        turret.setTurret(turretSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //ShuffleboardHelpers.setWidgetValue("Turret", "TurretManual", "Ended");
        turret.stopTurret();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * Takes care of deadbanding
     * 
     * @param input     the input value
     * @param threshold the deadband value
     * @return {@code 0} if input is within deadband, {@code input} otherwise
     */
    private double deadband(double input, double threshold) {
        if (Math.abs(input) > threshold) {
            return input;
        } else {
            return 0;
        }
    }
}
