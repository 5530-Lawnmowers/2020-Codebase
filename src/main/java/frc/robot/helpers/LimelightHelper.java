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
    private static final double D_HEIGHT = 23.25;

    public static double getFrontRawY() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-front");
        NetworkTableEntry ty = table.getEntry("ty");
        double y = ty.getDouble(0.0);
        return y;
    }
    public static double getBackRawY(){
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-back");
        NetworkTableEntry ty = table.getEntry("ty");
        double y = ty.getDouble(0.0);
        return y;
    }

    public static double getFrontRawX() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-front");
        NetworkTableEntry tx = table.getEntry("tx");
        double x = tx.getDouble(0.0);
        return x;
    }

    public static double getBackRawX() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-back");
        NetworkTableEntry tx = table.getEntry("tx");
        double x = tx.getDouble(0.0);
        return x;
    }

    public static double getFrontRawA() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-front");
        NetworkTableEntry ta = table.getEntry("ta");
        double a = ta.getDouble(0.0);
        return a;
    }

    public static double getBackRawA() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-back");
        NetworkTableEntry ta = table.getEntry("ta");
        double a = ta.getDouble(0.0);
        return a;
    }

    /**
     * Gets the horizontal distance from the limelight to the target for shooting balls.
     * <p> TODO: verify D_HEIGHT
     *
     * @param angleOffset the angle at which the limelight is mounted from the horizontal in degrees
     * @return the distance to the target
     */
    public static double getDistance(double angleOffset) {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-front");
        NetworkTableEntry ty = table.getEntry("ty");
        double theta = ty.getDouble(0.0) + angleOffset;
        return D_HEIGHT / Math.tan(Math.toRadians(theta));
    }


    public static void updateRumble() {
        /*
        if (Math.abs(getFrontRawX()) < 2 && getFrontRawA() >= .0001) {
            RobotContainer.XBController2.setRumble(RumbleType.kRightRumble, 1);
        } 
        else{
            RobotContainer.XBController2.setRumble(RumbleType.kRightRumble, 0);
        }    
        */
    }
    public static void onLight(){
        NetworkTableInstance.getDefault().getTable("limelight-front").getEntry("ledMode").setNumber(3);

    }
    public static void offLight(){
       // NetworkTableInstance.getDefault().getTable("limelight-front").getEntry("ledMode").setNumber(1);

    }
}
