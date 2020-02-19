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

public class ShootAll extends CommandBase {
    private Delivery delivery;
    private Shooter shooter;
    private Intake intake;
    private double accelSpeed = 1.0;
    private double shootSpeed = 0.9;
    private double feedSpeed = 1.0;
    private final double TARGET_VELOCITY = 4850;
    private final double THRESHOLD_VELOCITY = 4650;

    /**
     * Creates a new ShootAll.
     */
    public ShootAll(Delivery delivery, Shooter shooter, Intake intake) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(delivery, shooter);
        this.shooter = shooter;
        this.delivery = delivery;
        this.intake = intake;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        ShuffleboardHelpers.setWidgetValue("Shooter", "ShootAll", "Runnning");
        shootSpeed = (double) ShuffleboardHelpers.getWidgetValue("Shooter", "Set Shoot Speed"); //Test
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        
         //If speed too low, stop feed and accelerate at max power
         //If speed close to desired, stop feed and reduce speed to hold power
         //If speed at or above desired, feed ball
        if (shooter.getShooterVelocity() < THRESHOLD_VELOCITY) {
            shooter.setShooter(accelSpeed);
            delivery.stopDeliveryBelt();
        } else if (shooter.getShooterVelocity() >= THRESHOLD_VELOCITY && shooter.getShooterVelocity() < TARGET_VELOCITY) {
            shooter.setShooter(shootSpeed);
            delivery.stopDeliveryBelt();
        } else if (shooter.getShooterVelocity() >= TARGET_VELOCITY) {
            delivery.setDeliveryBelt(feedSpeed);
        }

        //Deliver 5th ball in intake to belt system
        if (!delivery.getBreakbeams()[0] && intake.getSwitch()) {
            delivery.setDeliveryWheel(0.6);
        } else {
            delivery.stopDeliveryWheel();
        }
         
        //shooter.setShooter(shootSpeed); //Test just shoot

        ShuffleboardHelpers.setWidgetValue("Shooter", "Shooter Velocity", shooter.getShooterVelocity());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        delivery.stopDeliveryBelt();
        shooter.stopShooter();
        ShuffleboardHelpers.setWidgetValue("Shooter", "ShootAll", "Ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
