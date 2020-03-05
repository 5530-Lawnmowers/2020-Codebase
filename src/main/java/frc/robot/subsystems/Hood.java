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
    private final PIDController hoodController = new PIDController(.4, 0, 0, 10);

    private final DutyCycleEncoder angleAbs = new DutyCycleEncoder(Constants.DUTY_CYCLE_SOURCE);

    private final double LOW = 0.2;
    private final double HIGH = 0;

    private double upperLimit;
    private double lowerLimit;
    private boolean ignoreSoftwareLimits = false;

    private double iterationSet = 0; // New limit code

    public Hood() {
        //resetLimits();
        setDefaultCommand(new HoodManual(this));
        
        iterationSet = 0; // New limit code
    }

    @Override
    public void periodic() {
        ShuffleboardHelpers.setWidgetValue("Hood", "Hood Position", angleAbs.get());
        //resetLimits();

        if (!ignoreSoftwareLimits) {
            // if (getAngleAbs() > upperLimit) {
            //     CommandScheduler.getInstance().schedule(new HoodLimitInterrupt(this, true));
            //     ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Interrupt Over");
            // } else if (getAngleAbs() < lowerLimit) {
            //     CommandScheduler.getInstance().schedule(new HoodLimitInterrupt(this, false));
            //     ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Interrupt Under");
            // } else {
            //     ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Safe");
            // }

            if (positionFrac() > HIGH && positionFrac() < HIGH + 0.05) {
                //ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Interrupt Over");
                iterationSet = 0.3;
            } else if (positionFrac() < LOW && positionFrac() > LOW - 0.05) {
                //ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Interrupt Under");
                iterationSet = -0.3;
            } else {
                //ShuffleboardHelpers.setWidgetValue("Hood", "HoodLimitInterrupt", "Safe");
            }
        }

        hoodAdjust.set(ControlMode.PercentOutput, iterationSet); // New limit code
        iterationSet = 0; // New limit code
    }

    /**
     * Sets the speed of the hood motor
     *
     * @param speed The speed to set
     */
    public void setHood(double speed) {
        //hoodAdjust.set(ControlMode.PercentOutput, speed);
        iterationSet = speed; // New limit code
    }

    /**
     * Adjust the hood to a new target
     *
     * @return The calculated speed the hood should be set to based on the current Limelight offset
     */
    public double hoodControllerCalculate() {
        double offsetA = LimelightHelper.getFrontRawA();
        return hoodController.calculate(offsetA, getOffsetConstY(offsetA));
    }

    /**
     * Get the offset constant in the Y Direction
     * <br>Used to adjust for air resistance and other factors
     *
     * @param offsetA The current visible target area
     * @return
     */
    public double getOffsetConstY(double offsetA) {
        final double FIT_A = -9.039;
        final double FIT_B = 0.4935;
        return FIT_A * Math.log(FIT_B * offsetA);
    }

    /**
     * Stops the hood motor
     */
    public void stopHood() {
        //hoodAdjust.stopMotor();
        iterationSet = 0; // New limit code
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

        //ShuffleboardHelpers.setWidgetValue("Hood", "Upper Limit", upperLimit);
        //ShuffleboardHelpers.setWidgetValue("Hood", "Lower Limit", lowerLimit);
    }

    private double positionFrac() {
        return getAngleAbs() - Math.floor(getAngleAbs());
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }
}

