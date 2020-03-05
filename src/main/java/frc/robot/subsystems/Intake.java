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
    private final CANSparkMax intakeActuationL = new CANSparkMax(Constants.IN_ACT_L, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeActuationR = new CANSparkMax(Constants.IN_ACT_R, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final Servo releaseServo = new Servo(Constants.INTAKE_RELEASE);

    private final DigitalInput intakeSwitch = new DigitalInput(Constants.INTAKE_SWITCH);

    private boolean isUp = true;
    public final double START_L;
    public final double START_R;
    public final double DIFF = 11;

    /**
     * Creates a new Intake.
     */
    public Intake() {
        releaseServo.set(1);
        intake.setIdleMode(IdleMode.kBrake);
        intake.setSmartCurrentLimit(40);
        intakeActuationL.setSmartCurrentLimit(40);
        intakeActuationR.setSmartCurrentLimit(40);
        intakeActuationL.setIdleMode(IdleMode.kBrake);
        intakeActuationR.setIdleMode(IdleMode.kBrake);
        START_L = intakeActuationL.getEncoder().getPosition();
        START_R = intakeActuationR.getEncoder().getPosition();
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "L Start", START_L);
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "R Start", START_R);
    }

    @Override
    public void periodic() {
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Breakbeam Intake", intakeSwitch.get());
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Act Position L", getActuationPositionL());
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Act Position R", getActuationPositionR());
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
     * Sets the speed of the left intake actuation motor
     *
     * @param speed The speed to set
     */
    public void setIntakeActuationL(double speed) {
        intakeActuationL.set(speed);
    }

    /**
     * Sets the speed of the right intake actuation motor
     *
     * @param speed The speed to set
     */
    public void setIntakeActuationR(double speed) {
        intakeActuationR.set(speed);
    }

    /**
     * Stops the intake motor
     */
    public void stopIntake() {
        intake.stopMotor();
    }

    /**
     * Stops the intake actuation motor
     */
    public void stopIntakeActuation() {
        intakeActuationL.stopMotor();
        intakeActuationR.stopMotor();
    }

    /**
     * Returns the state of the intake limit switch
     *
     * @return {@code true} if the switch is tripped, {@code false} otherwise
     */
    public boolean getSwitch() {
        return !intakeSwitch.get();
    }

    /**
     * Get the position of the motor
     *
     * @return position of the intake encoder
     */
    public double getIntakePosition() {
        return intake.getEncoder().getPosition();
    }

    /**
     * Get the position of the intake
     * 
     * @return the position of the left intake actuation motor
     */
    public double getActuationPositionL() {
        return intakeActuationL.getEncoder().getPosition();
    }

    /**
     * Get the position of the intake
     * 
     * @return the position of the right intake actuation motor
     */
    public double getActuationPositionR() {
        return intakeActuationR.getEncoder().getPosition();
    }

    /**
     * Switches the intake's expected acutation state
     */
    public void toggleActuationState() {
        isUp = !isUp;
    }
    
    /**
     * Returns the intake's expected actuation state
     * @retunr {@code true} if the intake is up, {@code false} if the intake is down
     */
    public boolean getActuationState() {
        return isUp;
    }
}
