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
import frc.robot.helpers.ShuffleboardHelpers;

public class Turret extends SubsystemBase {
    private final WPI_TalonSRX turretSpin = new WPI_TalonSRX(Constants.TURRET);

    //Physical stops for the turret
    private final DigitalInput hardStop1 = new DigitalInput(Constants.TURRET_S1);
    private final DigitalInput hardStop2 = new DigitalInput(Constants.TURRET_S2);

    //Constants
    private final int REL_ZERO = 189;
    private final int CYCLE_ZERO;

    /**
     * Creates a new Turret.
     */
    public Turret() {
        turretSpin.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
        turretSpin.config_kF(0, .05, 10);
        turretSpin.config_kP(0, .01, 10);
        turretSpin.config_kI(0, .007, 10);
        turretSpin.config_kD(0, .007, 10);

        if (turretSpin.getSelectedSensorPosition() % 4096 > REL_ZERO + 2560) {
            CYCLE_ZERO = (((turretSpin.getSelectedSensorPosition() / 4096) + 1) * 4096) + REL_ZERO;
        } else {
            CYCLE_ZERO = ((turretSpin.getSelectedSensorPosition() / 4096) * 4096) + REL_ZERO;
        }

        ShuffleboardHelpers.setWidgetValue("Turret", "Turret Zero", CYCLE_ZERO);
    }

    /**
     * Sets the turret motor speed
     * @param speed The speed to set
     */
    public void setTurret(double speed) {
         turretSpin.set(speed);
        //TODO: Confirm that the hardstops correspond correctly to motor direction
    }

    /**
     * Moves the turret to a set position
     * @param targetPosition The position to set
     */
    public void setPosition(int targetPosition){
        turretSpin.set(ControlMode.Position, targetPosition);
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
        ShuffleboardHelpers.setWidgetValue("Turret", "Position", turretSpin.getSelectedSensorPosition());
    }
}
