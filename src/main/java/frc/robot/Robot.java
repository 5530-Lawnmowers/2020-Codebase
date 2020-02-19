/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.Climb;
import frc.robot.helpers.LimelightHelper;
import frc.robot.helpers.SQLHelper;
import frc.robot.subsystems.Climber;

import java.sql.SQLException;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command m_autonomousCommand;
    private Timer timer = new Timer();
    private RobotContainer m_robotContainer;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        Shuffleboard.getTab("Intake and Delivery");
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Set Intake", -0.4));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Set Wheel", 0.6));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Set Belt", 0.8));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("IntakeRun", "Init"));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("IntakeSmartControl", "Init"));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Breakbeam 1", true));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Breakbeam 2", true));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Breakbeam 3", true));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Breakbeam 4", true));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Breakbeam Intake", true));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Intake Feed Offset", 8));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Belt Feed Offset", 1));
        SQLHelper.stageWidget(Shuffleboard.getTab("Intake and Delivery").add("Wheel Feed Offset", 5));



        Shuffleboard.getTab("Turret");
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("Position", 0));
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("Turret Zero", 0));
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("Set Turret", 0.3));
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("TurretManual", "Init"));
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("TurretAlign", "Init"));
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("Offset X", 0));
        SQLHelper.stageWidget(Shuffleboard.getTab("Turret").add("Initial Position", 0));

        Shuffleboard.getTab("Shooter");
        SQLHelper.stageWidget(Shuffleboard.getTab("Shooter").add("Hood Position", 0));
        SQLHelper.stageWidget(Shuffleboard.getTab("Shooter").add("Shooter Velocity", 0));
        SQLHelper.stageWidget(Shuffleboard.getTab("Shooter").add("Set Shoot Speed", 1.0));
        SQLHelper.stageWidget(Shuffleboard.getTab("Shooter").add("ShootAll", "Init"));
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
        try {
            timer.reset();
            timer.stop();
            SQLHelper.backupTable();
            SQLHelper.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disabledPeriodic() {
    }

    /**
     * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        //m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
        try {
            if (!SQLHelper.isOpen()) {
                SQLHelper.openConnection();
                SQLHelper.initTable();
                timer.start();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        try {
            SQLHelper.mySQLperiodic((int) timer.get());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
        try {
            if (!SQLHelper.isOpen()) {
                SQLHelper.openConnection();
                SQLHelper.initTable();
                timer.start();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        LimelightHelper.updateRumble();
        /*
        try {
            SQLHelper.mySQLperiodic((int) (timer.get() * 1000));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
