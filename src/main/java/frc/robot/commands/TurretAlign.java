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
import frc.robot.helpers.ShuffleboardHelpers;

public class TurretAlign extends CommandBase {
    private Turret turret;
    private final int MARGIN = 3;
    private double previousOffset;
    private double offset;
    private int counter;
    private final double kp = .03;
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
        counter = 0;
        offset = LimelightHelper.getRawX();
        previousOffset = offset;
        ShuffleboardHelpers.setWidgetValue("Turret", "TurretAlign", "Running");

        turret.setPosition(currentPosition + (int) Math.round(offset * 4096 / 360)); //Instant set
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        offset = LimelightHelper.getRawX();
        double offsetVelocity = Math.abs(offset - previousOffset);
        //Align via continous power set
        //turret.setTurret(kp * offset - kd * offsetVelocity);
        previousOffset = offset;
        if (offset < MARGIN) {counter++;}
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        turret.stopTurret();
        ShuffleboardHelpers.setWidgetValue("Turret", "TurretAlign", "Ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (counter > 10) {
            return true;
        }
        return false;
    }
}
