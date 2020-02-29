package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class OffsetTestingHelper extends CommandBase {
    public double min;
    public double max;

    public OffsetTestingHelper() {
    }

    @Override
    public void initialize() {
        this.min = LimelightHelper.getRawA();
        this.max = LimelightHelper.getRawA();
    }

    @Override
    public void execute() {
        double area = LimelightHelper.getRawA();
        if (Math.abs(area) < 0.001) return;
        if (area < min) min = area;
        if (area > max) max = area;
        ShuffleboardHelpers.setWidgetValue("Hood","TA", (max + min) / 2);
        ShuffleboardHelpers.setWidgetValue("Hood","δTA", max - ((max + min) / 2));
        ShuffleboardHelpers.setWidgetValue("Hood","TA LOGS", "RUNNING");
    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        ShuffleboardHelpers.setWidgetValue("Hood","TA LOGS", "ENDED");
    }
}
