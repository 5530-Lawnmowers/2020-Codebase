/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

public class Turret extends SubsystemBase {
    private final WPI_TalonSRX turretSpin = new WPI_TalonSRX(Constants.TURRET);

    //Physical stops for the turret
    private final DigitalInput hardStop1 = new DigitalInput(Constants.TURRET_S1);
    private final DigitalInput hardStop2 = new DigitalInput(Constants.TURRET_S2);

    //Constants
    private final int REL_ZERO = 189; //Forward-facing encoder reading mod 4096
    private int cycleZero;     //Forward-facing encoder reading for this cycle
    private int lowerLimit;
    private int upperLimit;

    /**
     * Creates a new Turret.
     */
    public Turret() {
        turretSpin.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
        turretSpin.config_kF(0, .05, 10);
        turretSpin.config_kP(0, .01, 10);
        turretSpin.config_kI(0, .007, 10);
        turretSpin.config_kD(0, .007, 10);

        if (((turretSpin.getSelectedSensorPosition() % 4096) + 4096) % 4096 > REL_ZERO + 2048) {
            cycleZero = (((turretSpin.getSelectedSensorPosition() / 4096) + 1) * 4096) + REL_ZERO;
        } else {
            cycleZero = ((turretSpin.getSelectedSensorPosition() / 4096) * 4096) + REL_ZERO;
        }

        lowerLimit = cycleZero - 1536;
        upperLimit = cycleZero + 1536;

        ShuffleboardHelpers.setWidgetValue("Turret", "Turret Zero", cycleZero);
        ShuffleboardHelpers.setWidgetValue("Turret", "Initial Position", turretSpin.getSelectedSensorPosition());
    }

    /**
     * Sets the turret motor speed
     * @param speed The speed to set
     */
    public void setTurret(double speed) {
         turretSpin.set(ControlMode.PercentOutput, speed);
         if (turretSpin.getSelectedSensorPosition() >= upperLimit && speed > 0) {
            turretSpin.set(ControlMode.PercentOutput, -.3);
         } else if (turretSpin.getSelectedSensorPosition() <= lowerLimit && speed < 0) {
             turretSpin.set(ControlMode.PercentOutput, +.3);
         } else {
             turretSpin.set(ControlMode.PercentOutput, speed);
         }
        //Positive direction of encoder is positive speed
        //TODO: Confirm that the hardstops correspond correctly to motor direction
    }

    /**
     * Moves the turret to a set position. If target position is outside of acceptable range,
     * turret moves to limit in that direction.
     * @param targetPosition The position to set
     */
    public void setPosition(int targetPosition){
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
        turretSpin.stopMotor();
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

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (((turretSpin.getSelectedSensorPosition() % 4096) + 4096) % 4096 > REL_ZERO + 2048) {
            cycleZero = (((turretSpin.getSelectedSensorPosition() / 4096) + 1) * 4096) + REL_ZERO;
        } else {
            cycleZero = ((turretSpin.getSelectedSensorPosition() / 4096) * 4096) + REL_ZERO;
        }

        lowerLimit = cycleZero - 1536;
        upperLimit = cycleZero + 1536;

        if (turretSpin.getSelectedSensorPosition() >= upperLimit && turretSpin.get() > 0) {
            turretSpin.stopMotor();
        } else if (turretSpin.getSelectedSensorPosition() <= lowerLimit && turretSpin.get() < 0) {
            turretSpin.stopMotor();
        }

        ShuffleboardHelpers.setWidgetValue("Turret", "Turret Zero", cycleZero);
        ShuffleboardHelpers.setWidgetValue("Turret", "Position", turretSpin.getSelectedSensorPosition());
        ShuffleboardHelpers.setWidgetValue("Turret", "Offset X", LimelightHelper.getRawX());
    }
}
