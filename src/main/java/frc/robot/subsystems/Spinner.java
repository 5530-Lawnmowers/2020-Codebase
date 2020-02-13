/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.helpers.ShuffleboardHelpers;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Servo;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;

public class Spinner extends SubsystemBase {
    private final CANSparkMax spinner = new CANSparkMax(Constants.SPIN, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final Servo spinnerActuation = new Servo(Constants.SPIN_ACT);
    private final ColorSensorV3 colorSensor = new ColorSensorV3(Constants.I2C_PORT);
    private final ColorMatch colorMatch = new ColorMatch();

    /**
     * Creates a new Spinner.
     */
    public Spinner() {
        colorMatch.addColorMatch(Constants.blueTarget);
        colorMatch.addColorMatch(Constants.greenTarget);
        colorMatch.addColorMatch(Constants.redTarget);
        colorMatch.addColorMatch(Constants.yellowTarget);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // In theory, this should schedule the command to output the RGB values.
    }

    /**
     * Get the color detected by the sensor
     *
     * @return detected Color
     */
    public Color getColor() {
        return colorSensor.getColor();
    }

    /**
     * Get the result of the color match
     *
     * @return matched ColorMathResult
     */
    public ColorMatchResult getColorMatch() {
        return colorMatch.matchClosestColor(colorSensor.getColor());
    }

    /**
     * Sets the spinner motor speed
     *
     * @param speed The speed to set
     */
    public void setSpinner(double speed) {
        spinner.set(speed);
    }

    /**
     * Stops the spinner motor
     */
    public void stopSpinner() {
        spinner.stopMotor();
    }

    /**
     * Sets the spinner actuation motor speed
     *
     * @param speed The speed to set
     */
    public void setSpinAct(double speed) {
        spinnerActuation.set(speed);
    }

    public void setSpinnerUp() {
        spinnerActuation.set(1);
    }

    public void setSpinnerDown() {
        spinnerActuation.set(0);
    }

    /**
     * Stops the spinner actuation motor
     */
    public void stopSpinAct() {
        spinnerActuation.stopMotor();
    }

}
