package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class DriveDefault extends CommandBase {
    private final Drivetrain drivetrain;
    public static double oldTriggerR;
    public static double oldTriggerL;
    private static double driveWeight = 0.85;

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
       /* if(RobotContainer.XBController1.getX(side) >= 0){
            return Math.pow(RobotContainer.XBController1.getX(side), 2);
        }
        else{
            return -Math.pow(RobotContainer.XBController1.getX(side), 2);
        }*/
        return Math.pow(RobotContainer.XBController1.getX(side), 3);
    }

    /**
     * Get the trigger value for a trigger side on XBox Controller
     */
    public double getTrigger(GenericHID.Hand side) {
        if(RobotContainer.XBController1.getTriggerAxis(side) >= 0){
            return Math.pow(RobotContainer.XBController1.getTriggerAxis(side), 2);
        }
        else {
            return -Math.pow(RobotContainer.XBController1.getTriggerAxis(side), 2);
        }
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

    /**
     * The initial subroutine of a command.  Called once when the command is initially scheduled.
     */
    @Override
    public void initialize() {
    }

    /**
     * The main body of a command.  Called repeatedly while the command is scheduled.
     * (That is, it is called repeatedly until {@link #isFinished()}) returns true.)
     */
    @Override
    public void execute() {
        setSpeeds(getLateral(GenericHID.Hand.kLeft), getTrigger(GenericHID.Hand.kRight), getTrigger(GenericHID.Hand.kLeft));
    }

    /**
     * <p>
     * Returns whether this command has finished. Once a command finishes -- indicated by
     * this method returning true -- the scheduler will call its {@link #end(boolean)} method.
     * </p><p>
     * Returning false will result in the command never ending automatically. It may still be
     * cancelled manually or interrupted by another command. Hard coding this command to always
     * return true will result in the command executing once and finishing immediately. It is
     * recommended to use * {@link edu.wpi.first.wpilibj2.command.InstantCommand InstantCommand}
     * for such an operation.
     * </p>
     *
     * @return whether this command has finished.
     */
    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

    /**
     * The action to take when the command ends. Called when either the command
     * finishes normally -- that is it is called when {@link #isFinished()} returns
     * true -- or when  it is interrupted/canceled. This is where you may want to
     * wrap up loose ends, like shutting off a motor that was being used in the command.
     *
     * @param interrupted whether the command was interrupted/canceled
     */
    @Override
    public void end(boolean interrupted) {

    }
}
