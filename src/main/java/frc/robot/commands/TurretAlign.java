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
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class TurretAlign extends CommandBase {
    private Turret turret;
    private final int MARGIN = 1;
    private double autonCounter;
    private double previousOffset;
    private double offset;
    private int counter;
    private double kp = .035;
    private double kd = 0;


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
        //LimelightHelper.onLight();
        int currentPosition = turret.getEncoderValue();
        counter = 0;
        offset = LimelightHelper.getFrontRawX();
        previousOffset = offset;
        autonCounter = 0;
        //ShuffleboardHelpers.setWidgetValue("Turret", "TurretAlign", "Running");

        //turret.setPosition(currentPosition + (int) Math.round(offset * 4096 / 360)); //Instant set
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        
        offset = LimelightHelper.getFrontRawX();
        double offsetVelocity = Math.abs(offset - previousOffset);
        //kp = (double) ShuffleboardHelpers.getWidgetValue("Turret", "kP");
        //kd = (double) ShuffleboardHelpers.getWidgetValue("Turret", "kD");
        //Align via continous power set
        //TODO: only run if getRawA() is above a certain size (determine empirically)
        turret.setTurret(kp * offset - kd * offsetVelocity);
        previousOffset = offset;
        if (Math.abs(offset) < MARGIN) {
            counter++;
        } else {
            counter = 0;
        }

        if (Robot.auton) {
            autonCounter++;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //LimelightHelper.offLight();

        turret.stopTurret();
        //ShuffleboardHelpers.setWidgetValue("Turret", "TurretAlign", "Ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        //if (counter > 10) {
        //    return true;
        //}
        if((Robot.auton && isAligned()) || autonCounter >= 75) {
            return true;

        }
        return false;

    }

    public boolean isAligned() {
        return counter > 10;
    }
}
