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

public class DeliveryManual extends CommandBase {
    private Delivery delivery;
    private double beltSpeed = 0;
    private final double kDeadband = 0.2;

    /**
     * Creates a new HoodManual.
     */
    public DeliveryManual(Delivery delivery) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(delivery);
        this.delivery = delivery;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      if (!RobotContainer.XBController2.getBButton()) {
        double forward = deadband(RobotContainer.XBController2.getTriggerAxis(Hand.kRight), kDeadband);
        double backward = deadband(RobotContainer.XBController2.getTriggerAxis(Hand.kLeft), kDeadband);
        beltSpeed = (forward - backward);
      } else {
        beltSpeed = 0;
      }
      delivery.setDeliveryBelt(beltSpeed);
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
