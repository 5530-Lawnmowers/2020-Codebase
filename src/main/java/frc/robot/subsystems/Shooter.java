/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

import com.revrobotics.*;


public class Shooter extends SubsystemBase {
    private final CANSparkMax shooter1 = new CANSparkMax(Constants.SHOOTER_1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax shooter2 = new CANSparkMax(Constants.SHOOTER_2, CANSparkMaxLowLevel.MotorType.kBrushless);

    public final CANPIDController shooterPID1 = shooter1.getPIDController();
    public final CANPIDController shooterPID2 = shooter2.getPIDController();

    /**
     * Creates a new Shooter.
     */
    public Shooter() {
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if(getShooterVelocity() >= 4500){
            RobotContainer.XBController2.setRumble(RumbleType.kLeftRumble, 1);
            RobotContainer.XBController2.setRumble(RumbleType.kRightRumble, 1);
        }
        else{
        RobotContainer.XBController2.setRumble(RumbleType.kRightRumble, 0);
        RobotContainer.XBController2.setRumble(RumbleType.kLeftRumble, Math.pow((getShooterVelocity() / 6000), 2));
        }
    }

    /**
     * Sets the speed of both the shooter motors
     *
     * @param speed The speed to set
     */
    public void setShooter(double speed) {
        shooter1.set(-speed);
        shooter2.set(speed);
    }

    /**
     * Stops the shooter motors
     */
    public void stopShooter() {
        shooter1.stopMotor();
        shooter2.stopMotor();
    }

    public double getShooterVelocity() {
        return shooter2.getEncoder().getVelocity();
    }
}
