/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.RobotContainer;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.*;

public class TurretDefault extends CommandBase {
    private Turret turret;
    private double kP = 0.03;
    private double kD = 0;
    private double manualSet = 0.3;
    private double offset;
    private double previousOffset;

    /**
     * Creates a new TurretDefault.
     */
    public TurretDefault(Turret turret) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.turret = turret;
        addRequirements(turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        offset = LimelightHelper.getRawX();
        previousOffset = offset;
        //ShuffleboardHelpers.setWidgetValue("Turret", "TurretDefault", "Running");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        offset = LimelightHelper.getRawX();
        //manualSet = (double) ShuffleboardHelpers.getWidgetValue("Turret", "Set Turret");

        if (RobotContainer.XBController2.getBumper(Hand.kRight)) { //Manual clockwise
            turret.setTurret(manualSet);
        } else if (RobotContainer.XBController2.getBumper(Hand.kLeft)) { //Manual counter-clockwise
            turret.setTurret(-manualSet);
        } else if (LimelightHelper.getRawA() > 0) { //Default to auto-align
            double offsetVelocity = Math.abs(offset - previousOffset);
            //kP = (double) ShuffleboardHelpers.getWidgetValue("Turret", "kP");
            //kD = (double) ShuffleboardHelpers.getWidgetValue("Turret", "kD");
            //Align via continous power set
            turret.setTurret(kP * offset - kD * offsetVelocity);
        } else {
            turret.stopTurret();
        }

        previousOffset = offset;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //ShuffleboardHelpers.setWidgetValue("Turret", "TurretDefault", "Interrupted");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
