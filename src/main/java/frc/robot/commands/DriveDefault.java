package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.helpers.ShuffleboardHelpers;
import frc.robot.subsystems.Drivetrain;


public class DriveDefault extends CommandBase {
    private final Drivetrain drivetrain;
    public static double oldTriggerR;
    public static double oldTriggerL;
    private static double driveWeight = 0.85;

    /**
     * Creates a new DriveDefault
     */
    public DriveDefault(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    /**
     * Limits a value between -1.0 and 1.0
     */
    public double limit(double n) {
        return Math.min(Math.max(n, -1.0), 1.0);
    }

    /**
     * Slowly increases oldTriggerL to newTriggerVal
     *
     * @return next iteration closer to newTriggerVal
     */
    double accelerateL(double newTriggerVal) {
        oldTriggerL = 0.09516 * newTriggerVal + 0.9048 * oldTriggerL;
        return oldTriggerL;
    }

    /**
     * Slowly increases oldTriggerR to newTriggerVal
     *
     * @return next iteration closer to newTriggerVal
     */
    double accelerateR(double newTriggerVal) {
        oldTriggerR = 0.09516 * newTriggerVal + 0.9048 * oldTriggerR;
        return oldTriggerR;
    }

    /**
     * Get the lateral value for a stick side on XBox Controller
     */
    public double getLateral(GenericHID.Hand side) {
        GenericHID.Hand opposite = side.equals(GenericHID.Hand.kLeft) ? GenericHID.Hand.kRight : GenericHID.Hand.kLeft;
        if (Math.abs(RobotContainer.XBController1.getX(side)) < 0.001)
            return RobotContainer.XBController1.getX(opposite);
        return RobotContainer.XBController1.getX(side);
    }

    /**
     * Get the trigger value for a trigger side on XBox Controller
     */
    public double getTrigger(GenericHID.Hand side) {
        return RobotContainer.XBController1.getTriggerAxis(side) * Math.abs(RobotContainer.XBController1.getTriggerAxis(side));
    }

    /**
     * Calculates right speed based on XBox Controller output
     *
     * @return speed = accelerator - decelerator - lateral input
     */
    public double speedR(double lateral, double accelerator, double decelerator) {
        return driveWeight * limit(accelerator - decelerator - lateral);
    }

    /**
     * Calculates left speed based on XBox Controller output
     *
     * @return speed = accelerator - decelerator + lateral input
     */
    public double speedL(double lateral, double accelerator, double decelerator) {
        return driveWeight * limit(accelerator - decelerator + lateral);
    }

    /**
     * Sets the speed for both sides
     */
    public void setSpeeds(double lStick, double rTrigger, double lTrigger) {
        // Right Thrust
        double rightPower = speedR(lStick, rTrigger, lTrigger);
        // If the difference from the old throttle is greater than some constant, slowly
        // accelerate to new value.
        if (Math.abs(oldTriggerR - rightPower) > 0.2)
            rightPower = accelerateR(rightPower);
        // Set the right side to the next iteration closer to leftPower
        drivetrain.setDrivetrainMotor(-rightPower, Constants.DT_R1);
        drivetrain.setDrivetrainMotor(-rightPower, Constants.DT_R2);

        // Left Thrust
        double leftPower = speedL(lStick, rTrigger, lTrigger);
        // If the difference from the old throttle is greater than some constant, slowly
        // accelerate to new value.
        if (Math.abs(oldTriggerL - leftPower) > 0.2)
            leftPower = accelerateL(leftPower);
        // Set the left side to the next iteration closer to leftPower
        drivetrain.setDrivetrainMotor(leftPower, Constants.DT_L1);
        drivetrain.setDrivetrainMotor(leftPower, Constants.DT_L2);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        driveWeight = 0.85;
        if (RobotContainer.XBController1.getBumper(GenericHID.Hand.kLeft))
            driveWeight = (double) ShuffleboardHelpers.getWidgetValue("Drivetrain", "Precision Weight");
        setSpeeds(getLateral(GenericHID.Hand.kLeft), getTrigger(GenericHID.Hand.kRight), getTrigger(GenericHID.Hand.kLeft));
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
