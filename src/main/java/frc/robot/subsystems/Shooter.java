/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.AdjustHood;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.ShuffleboardHelpers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.*;
import edu.wpi.first.wpilibj.controller.PIDController;


public class Shooter extends SubsystemBase {
    private final CANSparkMax shooter1 = new CANSparkMax(Constants.SHOOTER_1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax shooter2 = new CANSparkMax(Constants.SHOOTER_2, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final WPI_TalonSRX hoodAdjust = new WPI_TalonSRX(Constants.ADJUST);
    private final PIDController hoodController = new PIDController(.01, 0, 0, 10);

    private final DutyCycleEncoder angleAbs = new DutyCycleEncoder(Constants.DUTY_CYCLE_SOURCE);
    

    /**
     * Creates a new Shooter.
     */
    public Shooter() {
        setDefaultCommand(new AdjustHood(this));
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        ShuffleboardHelpers.setWidgetValue("Shooter", "Hood Position", angleAbs.get());
    }

    /**
     * Sets the speed of both the shooter motors
     *
     * @param speed The speed to set
     */
    public void setShooter(double speed) {
        shooter1.set(speed);
        shooter2.set(-speed);
    }

    /**
     * Sets the speed of the hood motor
     *
     * @param speed The speed to set
     */
    public void setHood(double speed) {
        hoodAdjust.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Adjust the hood to a new target
     *
     * @return The calculated speed the hood should be set to based on the current Limelight offset
     */
    public double hoodControllerCalculate() {
        double offsetX = LimelightHelper.getRawX();
        double offsetY = LimelightHelper.getRawY();
        return hoodController.calculate(offsetY, getOffsetConstY(offsetX, offsetY));
    }

    /**
     * Get the offset constant in the Y Direction
     * <br>Used to adjust for air resistance and other factors
     * @param offsetX The current Limelight offset in the X Direction
     * @param offsetY The current Limelight offset in the Y Direction
     * @return
     */
    public double getOffsetConstY(double offsetX, double offsetY) {
        return 0;
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

    public double getAngleAbs() {
        return angleAbs.get();
    }
}
