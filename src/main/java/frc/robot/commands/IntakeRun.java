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

public class IntakeRun extends CommandBase {
    private double beltSet = 1.0;
    private double wheelSet = -0;
    private double intakeSet = 0;
    private Delivery delivery;
    private Intake intake;

    private int state;

    /**
     * Creates a new IntakeRun.
     */
    public IntakeRun(Delivery delivery, Intake intake) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(delivery, intake);
        this.delivery = delivery;
        this.intake = intake;

        //if (!delivery.getBreakbeams()[3]) {
        //  state = 4;
        //} else if (!delivery.getBreakbeams()[2]) {
        //  state = 3;
        //} else if (!delivery.getBreakbeams()[1]) {
        //  state = 2;
        //} else
        if (!delivery.getBreakbeams()[0]) {
            state = 1;
        } else {
            state = 0;
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        //intakeSet = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Set Intake");
        //wheelSet = (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Set Wheel");
        //beltSet = - (double) ShuffleboardHelpers.getWidgetValue("Intake and Delivery", "Set Belt");
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "IntakeRun", "Running");
        intake.setIntake(intakeSet);
        delivery.setDeliveryBelt(beltSet);
        delivery.setDeliveryWheel(wheelSet);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        //if (delivery.getBreakbeams()[state]) {
        //    delivery.setDeliveryBelt(beltSet);
        //    delivery.setDeliveryWheel(wheelSet);
        //} else if (!delivery.getBreakbeams()[state]) {
        //    delivery.stopDeliveryBelt();
        //    delivery.stopDeliveryWheel();
        //state++;
        //}
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intake.stopIntake();
        delivery.stopDeliveryBelt();
        delivery.stopDeliveryWheel();
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "IntakeRun", "Ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        //if (!delivery.getBreakbeams()[1]) {
        //    return true;
        //}
        return false;
    }
}
