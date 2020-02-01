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
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.DigitalInput;

public class Delivery extends SubsystemBase {
    private final WPI_TalonSRX delivery1 = new WPI_TalonSRX(Constants.DELIVERY_1);
    private final WPI_TalonSRX delivery2 = new WPI_TalonSRX(Constants.DELIVERY_2);

    //Delivery has assorted digital triggers
    private final DigitalInput deliverySensor1 = new DigitalInput(Constants.DELIVERY_S1);
    private final DigitalInput deliverySensor2 = new DigitalInput(Constants.DELIVERY_S2);

    /**
     * Creates a new Delivery.
     */
    public Delivery() {
        delivery1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        delivery2.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        delivery1.setSelectedSensorPosition(0);
        delivery2.setSelectedSensorPosition(0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        ShuffleboardHelpers.setWidgetValue("Encoders", "Delivery 1", delivery1.getSelectedSensorPosition());
        ShuffleboardHelpers.setWidgetValue("Encoders", "Delivery 2", delivery2.getSelectedSensorPosition());
        ShuffleboardHelpers.setWidgetValue("Sensors", "Delivery Breakbeam 1", deliverySensor1.get());
        ShuffleboardHelpers.setWidgetValue("Sensors", "Delivery Breakbeam 2", deliverySensor2.get());
    }


    public void testDeliverySet(double input) {
        delivery1.set(input);
        delivery2.set(input);
    }

    public void testDeliveryStop() {
        delivery1.stopMotor();
        delivery2.stopMotor();
    }
}
