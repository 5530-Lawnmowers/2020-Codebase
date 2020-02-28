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

public class HoodManual extends CommandBase {
    private Hood hood;
    private double hoodSpeed = 0.8;

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
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        //ShuffleboardHelpers.setWidgetValue("Hood", "HoodManual", "Running");
        if (RobotContainer.XBController2.getStickButton(Hand.kLeft)) {
            hood.setHood(hoodSpeed);
        } else if (RobotContainer.XBController2.getStickButton(Hand.kRight)) {
            hood.setHood(-hoodSpeed);
        } else {
            hood.stopHood();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //ShuffleboardHelpers.setWidgetValue("Hood", "HoodManual", "Ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
