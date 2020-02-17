/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.helpers.ShuffleboardHelpers;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;


public class Intake extends SubsystemBase {
    //Intake has 2 motors: 2 NEO motors on the intake (Spark)
    private final CANSparkMax intake = new CANSparkMax(Constants.INTAKE, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final Servo releaseServo = new Servo(6);

    private final DigitalInput intakeSwitch = new DigitalInput(Constants.INTAKE_SWITCH);

    /**
     * Creates a new Intake.
     */
    public Intake() {
        releaseServo.set(0);
        intake.setIdleMode(IdleMode.kBrake);
        intake.setSmartCurrentLimit(40);
    }

    @Override
    public void periodic() {
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Breakbeam Intake", intakeSwitch.get());
        // This method will be called once per scheduler run
    }

    /**
     * Sets the speed of the intake motor
     *
     * @param speed The speed to set
     */
    public void setIntake(double speed) {
        intake.set(speed);
    }

    /**
     * Stops the intake motor
     */
    public void stopIntake() {
        intake.stopMotor();
    }

    /**
     * Returns the state of the intake limit switch
     *
     * @return {@code true} if the switch is tripped, {@code false} otherwise
     */
    public boolean getSwitch() {
        return intakeSwitch.get();
    }

    /**
     * Get the position of the motor
     * @return position of the intake encoder
     */
    public double getIntakePosition() {
        return intake.getEncoder().getPosition();
    }
}
