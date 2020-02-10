/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.helpers.ShuffleboardHelpers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.*;


public class Shooter extends SubsystemBase {
    private final CANSparkMax shooter1 = new CANSparkMax(Constants.SHOOTER_1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax shooter2 = new CANSparkMax(Constants.SHOOTER_2, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final WPI_TalonSRX hoodAdjust = new WPI_TalonSRX(Constants.ADJUST);

    private final Encoder angleEnc = new Encoder(Constants.ANGLE_ENCODER_A, Constants.ANGLE_ENCODER_B);

    private final DutyCycleEncoder angleAbs = new DutyCycleEncoder(Constants.DUTY_CYCLE_SOURCE);


    /**
     * Creates a new Shooter.
     */
    public Shooter() {

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    /**
     * Sets the speed of both the shooter motors
     * @param speed The speed to set
     */
    public void setShooter(double speed) {
        shooter1.set(speed);
        shooter2.set(speed);
    }

    /**
     * Sets the speed of the hood motor
     * @param speed The speed to set
     */
    public void setHood(double speed) {
        hoodAdjust.set(speed);
    }

    /**
     * Stops the hood motor
     */
    public void stopHood() {
        hoodAdjust.stopMotor();
    }

    /**
     * Stops the shooter motors
     */
    public void stopShooter() {
        shooter1.stopMotor();
        shooter2.stopMotor();
    }

    public double getShooterVelocity() {
        return shooter1.getEncoder().getVelocity();
    }
}
