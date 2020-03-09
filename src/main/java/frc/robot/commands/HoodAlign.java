package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;
import frc.robot.Robot;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class HoodAlign extends CommandBase {
    private final Hood hood;
    private final double MARGIN = 0.2;
    private double counter;
    private double autonCounter;

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
        //ShuffleboardHelpers.setWidgetValue("Hood", "HoodAlign", "Running");
        counter = 0;
        autonCounter = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double offset = hood.getOffsetConstY(LimelightHelper.getFrontRawA()); //From shuffleboard
        //Eventually should be a function of getRawA()

        //TODO: only run if getRawA() is above a certain size (determine empirically)
        if (Math.abs(LimelightHelper.getFrontRawY() - offset) <= MARGIN) {
            hood.stopHood();
            counter++;
        } else {
            hood.setHood(-hood.hoodControllerCalculate());
            counter = 0;
        }

        if (Robot.auton) {
            autonCounter++;
        }
    }

    public boolean isAligned() {
        return counter > 10;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //ShuffleboardHelpers.setWidgetValue("Hood", "HoodAlign", "Ended");
        hood.stopHood();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if((Robot.auton && isAligned()) || autonCounter >= 75){
            return true;
        }
        return false;
    }
}
