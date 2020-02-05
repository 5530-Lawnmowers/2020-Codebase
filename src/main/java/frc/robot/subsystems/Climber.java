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
import com.ctre.phoenix.motorcontrol.can.*;

public class Climber extends SubsystemBase {
    private final Servo pinservo = new Servo(8);

    private final WPI_TalonSRX climbLeft = new WPI_TalonSRX(Constants.CLIMB_L);
    private final WPI_TalonSRX climbRight = new WPI_TalonSRX(Constants.CLIMB_R);
    

    /**
     * Creates a new Climber.
     */
    public Climber() {
        pinservo.set(1);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void testClimbSet(double speed) {
        climbLeft.set(speed);
        climbRight.set(speed);
    }

    public void testClimbStop() {
        climbLeft.stopMotor();
        climbRight.stopMotor();
    }
    public void realeaseArm(){
        pinservo.set(0);
    }
    
}
