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
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.DigitalInput;

public class Delivery extends SubsystemBase {
    private final CANSparkMax deliveryBelt = new CANSparkMax(Constants.DELIVERY_BELT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax deliveryWheel = new CANSparkMax(Constants.DELIVERY_WHEEL, CANSparkMaxLowLevel.MotorType.kBrushless);

    //Delivery has assorted digital triggers
    private final DigitalInput deliverySensor1 = new DigitalInput(Constants.DELIVERY_S1);
    private final DigitalInput deliverySensor2 = new DigitalInput(Constants.DELIVERY_S2);
    private final DigitalInput deliverySensor3 = new DigitalInput(Constants.DELIVERY_S3);
    private final DigitalInput deliverySensor4 = new DigitalInput(Constants.DELIVERY_S4);

    /**
     * Creates a new Delivery.
     */
    public Delivery() {
        deliveryBelt.setIdleMode(IdleMode.kBrake);
        //deliveryBelt.setSmartCurrentLimit(40);
        deliveryWheel.setIdleMode(IdleMode.kBrake);
        deliveryWheel.setSmartCurrentLimit(40);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Breakbeam 1", deliverySensor1.get());
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Breakbeam 2", deliverySensor2.get());
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Breakbeam 3", deliverySensor3.get());
        ShuffleboardHelpers.setWidgetValue("Intake and Delivery", "Breakbeam 4", deliverySensor4.get());
    }

    /**
     * Sets the speed of the inner intake wheel
     *
     * @param speed The speed to set
     */
    public void setDeliveryWheel(double speed) {
        deliveryWheel.set(speed);
    }

    /**
     * Sets the speed of the belt system
     *
     * @param speed The speed to set
     */
    public void setDeliveryBelt(double speed) {
        deliveryBelt.set(speed);
    }

    /**
     * Stops the belt system
     */
    public void stopDeliveryBelt() {
        deliveryBelt.stopMotor();
    }

    /**
     * Stops the inner intake wheel
     */
    public void stopDeliveryWheel() {
        deliveryWheel.stopMotor();
    }

    /**
     * Gets the position of the belt motor
     *
     * @return encoder value of the motor
     */
    public double getDeliveryBeltPosition() {
        return deliveryBelt.getEncoder().getPosition();
    }

    /**
     * Gets the position of the wheel motor
     *
     * @return encoder value of the motor
     */
    public double getDeliveryWheelPosition() {
        return deliveryWheel.getEncoder().getPosition();
    }

    /**
     * Returns the states of the delivery breakbeam sensors. {@code true} if clear, {@code false} otherwise
     *
     * @return An array of size 5 with the state of each breakbeam sensor
     */
    public boolean[] getBreakbeams() {
        boolean[] states = new boolean[4];
        states[0] = !deliverySensor1.get();
        states[1] = !deliverySensor2.get();
        states[2] = !deliverySensor3.get();
        states[3] = !deliverySensor4.get();
        return states;
    }
}
