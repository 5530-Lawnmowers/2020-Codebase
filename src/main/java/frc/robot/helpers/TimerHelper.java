/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.helpers;

import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class TimerHelper {
    public static Timer timer = new Timer();

    public static double getTimeLeft() {
        return Timer.getMatchTime();
    }

    public static boolean getEndgame() {
        if (Timer.getMatchTime() <= 30) {
            return true;
        } else {
            return false;
        }
    }
}
