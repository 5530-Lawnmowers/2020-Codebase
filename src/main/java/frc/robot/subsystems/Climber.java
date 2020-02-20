/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.*;
import frc.robot.RobotContainer;
import com.ctre.phoenix.motorcontrol.can.*;

public class Climber extends SubsystemBase {
    private final Servo leftRelease = new Servo(Constants.CLIMB_RELEASE_L);
    private final Servo rightRelease = new Servo(Constants.CLIMB_RELEASE_R);

    private final WPI_TalonSRX climbLeft = new WPI_TalonSRX(Constants.CLIMB_L);
    private final WPI_TalonSRX climbRight = new WPI_TalonSRX(Constants.CLIMB_R);


    /**
     * Creates a new Climber.
     */
    public Climber() {
        rightRelease.set(1);
        leftRelease.set(0);
    }
    

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    /**
     * Sets the speed of the Left Climb motor.
     *
     * @param speed The speed to set.
     * @param motor The motor to set. Use Constants class for motor values.
     */
    public void setClimb(double speed, int motor) {
        if (motor == Constants.CLIMB_L) {
            climbLeft.set(speed);
        } else if (motor == Constants.CLIMB_R) {
            climbRight.set(speed);
        }
    }

    /**
     * Stops Left Climb and Right Climb
     */
    public void stopAll() {
        climbLeft.stopMotor();
        climbRight.stopMotor();
    }

    public void realeaseArm() {
        rightRelease.set(0);
        leftRelease.set(1);
    }

}
