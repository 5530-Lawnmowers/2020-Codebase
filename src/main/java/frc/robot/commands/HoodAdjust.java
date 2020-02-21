package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class HoodAdjust extends CommandBase {

    private final Hood hood;
    private final double MARGIN = 1;
    private double counter;

    public HoodAdjust(Hood hood) {
        this.hood = hood;
        addRequirements(hood);
    }

    /**
     * The initial subroutine of a command.  Called once when the command is initially scheduled.
     */
    @Override
    public void initialize() {
        ShuffleboardHelpers.setWidgetValue("Hood", "HoodAdjust", "Running");
        counter = 0;
    }

    /**
     * The main body of a command.  Called repeatedly while the command is scheduled.
     * (That is, it is called repeatedly until {@link #isFinished()}) returns true.)
     */
    @Override
    public void execute() {
        double offset = hood.getOffsetConstY(0, 0); //From shuffleboard
                                                    //Eventually should be a function of getRawA()
        
        //TODO: only run if getRawA() is above a certain size (determine empirically)
        if (LimelightHelper.getRawY() > 0 + offset) {
            if (LimelightHelper.getRawY() < 3 + offset) {
                hood.setHood(LimelightHelper.getRawY() * -0.3);
            } else {
                hood.setHood(-1.0);
            }
        } else if (LimelightHelper.getRawY() < 0 + offset) {
            if (LimelightHelper.getRawY() > -3 + offset) {
                hood.setHood(LimelightHelper.getRawY() * 0.3);
            } else {
                hood.stopHood();
            }
        }

        if (Math.abs(LimelightHelper.getRawY() - offset) <= MARGIN) {
            counter++;
        } else {
            counter = 0;
        }

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
        ShuffleboardHelpers.setWidgetValue("Hood", "HoodAdjust", "Ended");
        hood.stopHood();
    }

    public boolean isAligned() {
        return counter > 10;
    }
}
