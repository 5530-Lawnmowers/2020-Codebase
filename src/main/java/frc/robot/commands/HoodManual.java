/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.*;
import frc.robot.RobotContainer;

public class HoodManual extends CommandBase {
    private Hood hood;
    private double hoodSpeed = 0;
    private final double kDeadband = 0.2;

    /**
     * Creates a new HoodManual.
     */
    public HoodManual(Hood hood) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(hood);
        this.hood = hood;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        RobotContainer.XBController2.setRumble(RumbleType.kRightRumble, 0);

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
       hoodSpeed = deadband(RobotContainer.XBController2.getY(Hand.kLeft), kDeadband) * 0.5;
       hood.setHood(hoodSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //ShuffleboardHelpers.setWidgetValue("Hood", "HoodManual", "Ended");
        hood.stopHood();
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
