/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.TurretLimitInterrupt;
import frc.robot.commands.TurretManual;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class Turret extends SubsystemBase {
    private final WPI_TalonSRX turretSpin = new WPI_TalonSRX(Constants.TURRET);

    //Physical stops for the turret
    private final DigitalInput hardStop1 = new DigitalInput(Constants.TURRET_S1);
    private final DigitalInput hardStop2 = new DigitalInput(Constants.TURRET_S2);

    //Constants
    private final int REL_ZERO = 1330; //Forward-facing encoder reading mod 4096
    private int cycleZero;     //Forward-facing encoder reading for this cycle
    private int lowerLimit;
    private int upperLimit;
    private boolean ignoreSoftwareLimit = false;

    //Perhaps necessary for software limits
    private double iterationSet = 0;

    /**
     * Creates a new Turret.
     */
    public Turret() {
        turretSpin.setNeutralMode(NeutralMode.Brake);
        turretSpin.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
        turretSpin.config_kF(0, .05, 10);
        turretSpin.config_kP(0, .01, 10);
        turretSpin.config_kI(0, .007, 10);
        turretSpin.config_kD(0, .007, 10);

        turretSpin.configFeedbackNotContinuous(true, 10); // New limit code
        //resetCycleZero();

        //ShuffleboardHelpers.setWidgetValue("Turret", "Turret Zero", cycleZero);
        //ShuffleboardHelpers.setWidgetValue("Turret", "Initial Position", turretSpin.getSelectedSensorPosition());

        iterationSet = 0; // New limit code

        setDefaultCommand(new TurretManual(this));
    }

    /**
     * Sets the turret motor speed
     *
     * @param speed The speed to set
     */
    public void setTurret(double speed) {
        iterationSet = speed; // New limit code
        //turretSpin.set(ControlMode.PercentOutput, speed);
        //Positive direction of encoder is positive speed
    }

    /**
     * Moves the turret to a set position. If target position is outside of acceptable range,
     * turret moves to limit in that direction.
     * setTurret() is prefered as encoder values jump.
     *
     * @param targetPosition The position to set
     */
    public void setPosition(int targetPosition) {
        if (targetPosition > upperLimit) {
            turretSpin.set(ControlMode.Position, upperLimit);
        } else if (targetPosition < lowerLimit) {
            turretSpin.set(ControlMode.Position, lowerLimit);
        } else {
            turretSpin.set(ControlMode.Position, targetPosition);
        }
    }

    /**
     * Stops the turret motor
     */
    public void stopTurret() {
        iterationSet = 0; // New limit code
        //turretSpin.stopMotor();
    }

    /**
     * Gets the current limit state of the turret
     *
     * @return {@code true} if a stop is reached, {@code false} if turret is free to move
     */
    public boolean reachedLimit() {
        return !hardStop1.get() || !hardStop2.get();
    }

    /**
     * Gets the current encoder value
     *
     * @return Position of turret
     */
    public int getEncoderValue() {
        return turretSpin.getSelectedSensorPosition();
    }

    /**
     * Toggles the state of software limits on the turret
     *
     * @param isSet {@code true} to abide by software limits, {@code false} to ignore software limits
     */
    public void setSoftwareLimit(boolean isSet) {
        ignoreSoftwareLimit = !isSet;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        //resetCycleZero();

        if (!ignoreSoftwareLimit) {
            // if (turretSpin.getSelectedSensorPosition() >= upperLimit) {
            //     CommandScheduler.getInstance().schedule(new TurretLimitInterrupt(this, true));
            //     ShuffleboardHelpers.setWidgetValue("Turret", "TurretLimitInterrupt", "Interrupt Over");
            // } else if (turretSpin.getSelectedSensorPosition() <= lowerLimit) {
            //     CommandScheduler.getInstance().schedule(new TurretLimitInterrupt(this, false));
            //     ShuffleboardHelpers.setWidgetValue("Turret", "TurretLimitInterrupt", "Interrupt Under");
            // } else {
            //     ShuffleboardHelpers.setWidgetValue("Turret", "TurretLimitInterrupt", "Safe");
            // }
            if (getEncoderValue() >= REL_ZERO + 1024 && getEncoderValue() <= REL_ZERO + 2048) {
                iterationSet = -.1;
                //ShuffleboardHelpers.setWidgetValue("Turret", "TurretLimitInterrupt", "Interrupt Over");
            } else if (getEncoderValue() <= REL_ZERO - 1024 || getEncoderValue() > REL_ZERO + 2048) {
                iterationSet = .1;
                //ShuffleboardHelpers.setWidgetValue("Turret", "TurretLimitInterrupt", "Interrupt Under");
            } else {
                //ShuffleboardHelpers.setWidgetValue("Turret", "TurretLimitInterrupt", "Safe");
            }
        }
        //ShuffleboardHelpers.setWidgetValue("Turret", "Position", getEncoderValue());
        turretSpin.set(iterationSet); // New limit code
        iterationSet = 0; // New limit code

        //ShuffleboardHelpers.setWidgetValue("Turret", "Turret Zero", cycleZero);
        //ShuffleboardHelpers.setWidgetValue("Turret", "Position", turretSpin.getSelectedSensorPosition());
        //ShuffleboardHelpers.setWidgetValue("Turret", "Offset X", LimelightHelper.getRawX());
    }

    /**
     * Gets the positive form of the encoder reading mod 4096
     *
     * @return turret position positive mod 4096
     */
    private int turretEncoderPositive() {
        return ((turretSpin.getSelectedSensorPosition() % 4096) + 4096) % 4096;
    }

    /**
     * Reset the encoder value of the zero-position and resets the limits.
     * Use regularly as encoder ticks will jump.
     */
    @SuppressWarnings("unused")
    private void resetCycleZero() {
        if (turretEncoderPositive() == REL_ZERO) {
            cycleZero = turretSpin.getSelectedSensorPosition();
        } else if (turretEncoderPositive() > REL_ZERO && turretEncoderPositive() <= REL_ZERO + 2048) {
            cycleZero = turretSpin.getSelectedSensorPosition() - (turretEncoderPositive() - REL_ZERO);
        } else if (REL_ZERO % 4096 > 2048 && turretEncoderPositive() < (REL_ZERO + 2048) % 4096) {
            cycleZero = turretSpin.getSelectedSensorPosition() - turretEncoderPositive() - (4096 - REL_ZERO);
        } else if (turretEncoderPositive() < REL_ZERO && turretEncoderPositive() >= REL_ZERO - 2048) {
            cycleZero = turretSpin.getSelectedSensorPosition() + (REL_ZERO - turretEncoderPositive());
        } else if (REL_ZERO % 4096 < 2048 && turretEncoderPositive() > (((REL_ZERO - 2048) % 4096) + 4096) % 4096) {
            cycleZero = turretSpin.getSelectedSensorPosition() + (4096 - turretEncoderPositive()) + REL_ZERO;
        }

        lowerLimit = cycleZero - 1024;
        upperLimit = cycleZero + 1024;
    }
}
