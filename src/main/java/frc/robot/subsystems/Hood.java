package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.HoodAdjust;
import frc.robot.commands.HoodLimitInterrupt;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class Hood extends SubsystemBase {
    private final WPI_TalonSRX hoodAdjust = new WPI_TalonSRX(Constants.ANGLE);
    private final PIDController hoodController = new PIDController(.05, 0, 0, 10);

    private final DutyCycleEncoder angleAbs = new DutyCycleEncoder(Constants.DUTY_CYCLE_SOURCE);

    private double upperLimit;
    private double lowerLimit;

    public Hood() {
        resetLimits();
    }

    @Override
    public void periodic() {
        ShuffleboardHelpers.setWidgetValue("Hood", "Hood Position", angleAbs.get());
        resetLimits();

        if (getAngleAbs() > upperLimit) {
            CommandScheduler.getInstance().schedule(new HoodLimitInterrupt(this, true));
            ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Interrupt Over");
        } else if (getAngleAbs() < lowerLimit) {
            CommandScheduler.getInstance().schedule(new HoodLimitInterrupt(this, false));
            ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Interrupt Under");
        } else {
            ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Safe");
        }
    }

    /**
     * Sets the speed of the hood motor
     *
     * @param speed The speed to set
     */
    public void setHood(double speed) {
        hoodAdjust.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Adjust the hood to a new target
     *
     * @return The calculated speed the hood should be set to based on the current Limelight offset
     */
    public double hoodControllerCalculate() {
        double offsetX = LimelightHelper.getRawX();
        double offsetY = LimelightHelper.getRawY();
        return hoodController.calculate(offsetY, getOffsetConstY(offsetX, offsetY));
    }

    /**
     * Get the offset constant in the Y Direction
     * <br>Used to adjust for air resistance and other factors
     *
     * @param offsetX The current Limelight offset in the X Direction
     * @param offsetY The current Limelight offset in the Y Direction
     * @return
     */
    public double getOffsetConstY(double offsetX, double offsetY) {
        return 0;
    }

    /**
     * Stops the hood motor
     */
    public void stopHood() {
        hoodAdjust.stopMotor();
    }

    public double getAngleAbs() {
        return angleAbs.get();
    }

    private void resetLimits() {
        double frac = getAngleAbs() - Math.floor(getAngleAbs());
        if (frac > 0.45) {
            lowerLimit = Math.floor(getAngleAbs()) + 0.45;
            upperLimit = Math.floor(getAngleAbs()) + 1.38;
        } else {
            lowerLimit = Math.floor(getAngleAbs()) - 0.55;
            upperLimit = Math.floor(getAngleAbs()) + 0.38;
        }
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }
}

