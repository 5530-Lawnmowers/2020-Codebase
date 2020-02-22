/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.util.Units;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.commands.DriveDefault;
import org.opencv.core.RotatedRect;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.helpers.ShuffleboardHelpers;
import com.revrobotics.*;
import com.ctre.phoenix.motorcontrol.can.*;
import com.kauailabs.navx.frc.AHRS;

public class Drivetrain extends SubsystemBase {
    //Drive test
    private final double driveMultiplier = 0.9;

    private final WPI_TalonFX drivetrainLeft1 = new WPI_TalonFX(Constants.DT_L1);
    private final WPI_TalonFX drivetrainLeft2 = new WPI_TalonFX(Constants.DT_L2);
    private final WPI_TalonFX drivetrainRight1 = new WPI_TalonFX(Constants.DT_R1);
    private final WPI_TalonFX drivetrainRight2 = new WPI_TalonFX(Constants.DT_R2);

    //Drive test
    private final SpeedControllerGroup drivetrainLeft;
    private final SpeedControllerGroup drivetrainRight;
    private final DifferentialDrive diffDrive;

    public static double StartingPose;

    public static float WheelCircumference = (float) .160;//in m
    public static DifferentialDriveOdometry DDO = new DifferentialDriveOdometry(new Rotation2d());
    public static PigeonIMU pigeon = new PigeonIMU(15);
    public static Rotation2d heading = new Rotation2d();
    public static float leftDistance = 0;
    public static float rightDistance = 0;
    private final AHRS gyro = new AHRS(SerialPort.Port.kMXP);

    /**
     * Creates a new Drivetrain.
     */
    public Drivetrain() {
        drivetrainLeft1.setInverted(false);
        drivetrainLeft2.setInverted(false);
        drivetrainRight1.setInverted(false);
        drivetrainRight2.setInverted(false);

        drivetrainLeft1.setNeutralMode(NeutralMode.Coast);
        drivetrainLeft2.setNeutralMode(NeutralMode.Coast);
        drivetrainRight1.setNeutralMode(NeutralMode.Coast);
        drivetrainRight2.setNeutralMode(NeutralMode.Coast);

        drivetrainLeft1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
        drivetrainRight1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
        drivetrainLeft1.setSelectedSensorPosition(0);
        drivetrainRight1.setSelectedSensorPosition(0);
        drivetrainRight1.config_kF(0, .05, 10);
        drivetrainRight1.config_kP(0, .01, 10);
        drivetrainRight1.config_kI(0, .007, 10);
        drivetrainRight1.config_kD(0, .007, 10);

        drivetrainLeft1.config_kF(0, .05, 10);
        drivetrainLeft1.config_kP(0, .01, 10);
        drivetrainLeft1.config_kI(0, .007, 10);
        drivetrainLeft1.config_kD(0, .007, 10);

        drivetrainRight2.config_kF(0, .05, 10);
        drivetrainRight2.config_kP(0, .01, 10);
        drivetrainRight2.config_kI(0, .007, 10);
        drivetrainRight2.config_kD(0, .007, 10);

        drivetrainLeft2.config_kF(0, .05, 10);
        drivetrainLeft2.config_kP(0, .01, 10);
        drivetrainLeft2.config_kI(0, .007, 10);
        drivetrainLeft2.config_kD(0, .007, 10);

        drivetrainLeft = new SpeedControllerGroup(drivetrainLeft1, drivetrainLeft2);
        drivetrainRight = new SpeedControllerGroup(drivetrainRight1, drivetrainRight2);
        diffDrive = new DifferentialDrive(drivetrainLeft, drivetrainRight);

        gyro.zeroYaw();
        //setDefaultCommand(new ThrottleMotorTest(this)); //Use this for motor tests
        setDefaultCommand(new DriveDefault(this));
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    /**
     * Sers the neutral mode for all drivetrain motors
     * @param brake {@code true} to set drivetrain to brake, {@code false} to set drivetrain to coast
     */
    public void setBrakeMode(boolean brake) {
        if (brake) {
            drivetrainLeft1.setNeutralMode(NeutralMode.Brake);
            drivetrainLeft2.setNeutralMode(NeutralMode.Brake);
            drivetrainRight1.setNeutralMode(NeutralMode.Brake);
            drivetrainRight2.setNeutralMode(NeutralMode.Brake);
        } else {
            drivetrainLeft1.setNeutralMode(NeutralMode.Coast);
            drivetrainLeft2.setNeutralMode(NeutralMode.Coast);
            drivetrainRight1.setNeutralMode(NeutralMode.Coast);
            drivetrainRight1.setNeutralMode(NeutralMode.Coast);
        }
    }

    //Drive test only
    public void testDrive(double throttle, double turn) {
        if (Math.abs(throttle) > 1)
            throttle = Math.abs(throttle) / throttle; // if the value given was too high, set it to the max
        throttle *= driveMultiplier; // scale down the speed


        if (Math.abs(turn) > 1)
            turn = Math.abs(turn) / turn; // if the value given was too high, set it to the max
        turn *= driveMultiplier; // scale down the speed

        diffDrive.arcadeDrive(throttle, turn); // function provided by the  controls y and turn speed at the same time.
    }

    //Drive test only
    public void testDriveStop() {
        drivetrainLeft.stopMotor();
        drivetrainRight.stopMotor();
    }

    private double getThrottle() {
        double n = RobotContainer.XBController1.getTriggerAxis(GenericHID.Hand.kRight) -
                RobotContainer.XBController1.getTriggerAxis(GenericHID.Hand.kLeft);
        return Math.abs(n) < 0.1 ? 0 : n;
    }

    private double getTurn() {
        double n = RobotContainer.XBController1.getX(GenericHID.Hand.kLeft);
        return Math.abs(n) < 0.1 ? 0 : n;
    }

    /**
     * Set the speed of a drivetrain motor
     *
     * @param speed      The speed to set
     * @param controller Constants.DT_L1, DT_L2, DT_R1, DT_R2
     */
    public void setDrivetrainMotor(double speed, int controller) {
        if (controller == Constants.DT_L1) {
            drivetrainLeft1.set(speed);
        } else if (controller == Constants.DT_L2) {
            drivetrainLeft2.set(speed);
        } else if (controller == Constants.DT_R1) {
            drivetrainRight1.set(speed);
        } else if (controller == Constants.DT_R2) {
            drivetrainRight2.set(speed);
        }
    }

    public void setVelocity(double left, double right) {
        drivetrainRight1.set(ControlMode.Velocity, MetersToUnits(right));
        drivetrainRight2.set(ControlMode.Velocity, MetersToUnits(right));
        drivetrainLeft1.set(ControlMode.Velocity, MetersToUnits(left));
        drivetrainLeft2.set(ControlMode.Velocity, MetersToUnits(left));
    }

    public double MetersToUnits(double meters) {
        meters = Units.metersToInches(meters);
        meters = meters / (6 * Math.PI);
        meters = meters * 2048;
        meters = meters / 10;
        return meters;
    }

    public double getDistanceLeft() {
        leftDistance = (float) (drivetrainLeft1.getSelectedSensorPosition());
        leftDistance = leftDistance / 2048;
        leftDistance = leftDistance * WheelCircumference;
        return leftDistance;
    }

    public double getDistanceRight() {
        rightDistance = (float) (drivetrainRight1.getSelectedSensorPosition());
        rightDistance = rightDistance / 2048;
        rightDistance = rightDistance * WheelCircumference;
        return leftDistance;
    }

    public void resetSensors() {
        DDO.resetPosition(new Pose2d(), new Rotation2d());
        drivetrainLeft1.setSelectedSensorPosition(0);
        drivetrainRight1.setSelectedSensorPosition(0);
    }

    public static double getPigeonCompassHeading() {
        if (getPigeonYaw() < 0) {
            return 360 - (getPigeonYaw() % 360.0 + 360.0);
        }
        return 360 - (getPigeonYaw() % 360.0);
    }

    public static double getPigeonYaw() {
        double[] output = new double[3];
        pigeon.getYawPitchRoll(output);
        return output[0];
    }

    public static double getModPigeonYaw() {
        if (getPigeonCompassHeading() - StartingPose < 0) {
            return getPigeonCompassHeading() - StartingPose + 360;
        }
        return getPigeonCompassHeading() - StartingPose;
    }

    public void updatePose() {
        DDO.update(new Rotation2d(Units.degreesToRadians(getModPigeonYaw())), Units.inchesToMeters(getDistanceLeft()), Units.inchesToMeters(getDistanceRight()));
    }

    public Pose2d getPose() {
        updatePose();
        return DDO.getPoseMeters();
    }

    public void initPose(double start) {
        StartingPose = start;
    }

    /**
     * Stops all drivetrain motors
     */
    public void testDrivetrainStop() {
        drivetrainLeft1.stopMotor();
        drivetrainLeft2.stopMotor();
        drivetrainRight1.stopMotor();
        drivetrainRight2.stopMotor();
    }
}
