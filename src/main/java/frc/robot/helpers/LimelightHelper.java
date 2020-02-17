/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.helpers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.GenericHID.HIDType;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

/**
 * Add your docs here.
 */
public class LimelightHelper {
    public static double getRawY() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry ty = table.getEntry("ty");
        double y = ty.getDouble(0.0);
        return y;
    }

    public static double getRawX() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry tx = table.getEntry("tx");
        double x = tx.getDouble(0.0);
        return x;
    }

    public static double getRawA() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry ta = table.getEntry("ta");
        double a = ta.getDouble(0.0);
        return a;
    }

    public static double getDistance() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry ty = table.getEntry("ty");
        double y = ty.getDouble(0.0);
        double distance = 23.2576688011 + Math.abs(y);
        distance = Math.toRadians(distance);
        distance = Math.tan(distance);
        distance = 41 / distance;
        return distance;
    }

    public static double getTargetX(double angle) {
        return getDistance() * Math.cos(angle);
    }

    public static double getTargetY(double angle) {
        return getDistance() * Math.sin(angle);
    }

    public static void updateRumble() {
        if (getRawA() > 0) {
            RobotContainer.XBController1.setRumble(RumbleType.kRightRumble, 1);
            RobotContainer.XBController1.setRumble(RumbleType.kLeftRumble, 0);
        }
        else if(TimerHelper.getEndgame()) {
            RobotContainer.XBController1.setRumble(RumbleType.kRightRumble, 1);
            RobotContainer.XBController1.setRumble(RumbleType.kLeftRumble, 1);
        }
        else{
            RobotContainer.XBController1.setRumble(RumbleType.kRightRumble, 0);
            RobotContainer.XBController1.setRumble(RumbleType.kLeftRumble, 0);
        }


    }
}
