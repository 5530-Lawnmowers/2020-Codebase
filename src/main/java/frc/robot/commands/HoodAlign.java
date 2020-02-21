package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class HoodAlign extends CommandBase {
    private final Hood hood;
    private final double MARGIN = 1;
    private double counter;

    /**
     * Creates a new HoodAlign
     */
    public HoodAlign(Hood hood) {
        this.hood = hood;
        addRequirements(hood);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        ShuffleboardHelpers.setWidgetValue("Hood", "HoodAlign", "Running");
        counter = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double offset = hood.getOffsetConstY(0, 0); //From shuffleboard
        //Eventually should be a function of getRawA()

        //TODO: only run if getRawA() is above a certain size (determine empirically)
        if (LimelightHelper.getRawY() > 0 + offset) {
            if (LimelightHelper.getRawY() < 5 + offset) {
                hood.setHood(LimelightHelper.getRawY() * 0.2);
            } else {
                hood.setHood(1.0);
            }
        } else if (LimelightHelper.getRawY() < 0 + offset) {
            if (LimelightHelper.getRawY() > -5 + offset) {
                hood.setHood(LimelightHelper.getRawY() * 0.2);
            } else {
                hood.setHood(-1.0);
            }
        }

        if (Math.abs(LimelightHelper.getRawY() - offset) < 1) {
            hood.stopHood();
        }

        if (Math.abs(LimelightHelper.getRawY() - offset) <= MARGIN) {
            counter++;
        } else {
            counter = 0;
        }
    }

    public boolean isAligned() {
        return counter > 10;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        ShuffleboardHelpers.setWidgetValue("Hood", "HoodAlign", "Ended");
        hood.stopHood();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
