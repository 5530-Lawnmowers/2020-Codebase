package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.HoodLimitInterrupt;
import frc.robot.commands.HoodManual;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class Hood extends SubsystemBase {
    private final WPI_TalonSRX hoodAdjust = new WPI_TalonSRX(Constants.ANGLE);
    private final PIDController hoodController = new PIDController(.05, 0, 0, 10);

    private final DutyCycleEncoder angleAbs = new DutyCycleEncoder(Constants.DUTY_CYCLE_SOURCE);

    private final double LOW = 0.45;
    private final double HIGH = 0.38;

    private double upperLimit;
    private double lowerLimit;
    private boolean ignoreSoftwareLimits;

    public Hood() {
        resetLimits();
        setDefaultCommand(new HoodManual(this));
    }

    @Override
    public void periodic() {
        ShuffleboardHelpers.setWidgetValue("Hood", "Hood Position", angleAbs.get());
        resetLimits();

        if (!ignoreSoftwareLimits) {
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
        return (double) ShuffleboardHelpers.getWidgetValue("Hood", "Hood Offset");
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

    @SuppressWarnings("unused")
    private void resetLimits() {
        double frac = getAngleAbs() - Math.floor(getAngleAbs());
        if (LOW > HIGH) {
            if (frac > LOW - 0.1) {
                lowerLimit = Math.floor(getAngleAbs()) + LOW;
                upperLimit = Math.floor(getAngleAbs()) + 1 + HIGH;
            } else {
                lowerLimit = Math.floor(getAngleAbs()) + LOW - 1;
                upperLimit = Math.floor(getAngleAbs()) + HIGH;
            }
        } else { 
            lowerLimit = Math.floor(getAngleAbs()) + LOW;
            upperLimit = Math.floor(getAngleAbs()) + HIGH;
        }

        ShuffleboardHelpers.setWidgetValue("Hood", "Upper Limit", upperLimit);
        ShuffleboardHelpers.setWidgetValue("Hood", "Lower Limit", lowerLimit);
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }
}

