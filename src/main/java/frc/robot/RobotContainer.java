/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static XboxController XBController = new XboxController(1);
  
  private final Climber climber = new Climber();
  private final Drivetrain drivetrain = new Drivetrain(); 
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final Spinner spinner = new Spinner();
  private final Delivery delivery = new Delivery();
  private final Turret turret = new Turret();
  
  public static JoystickButton xba = new JoystickButton(XBController, 1);
  public static JoystickButton xbb = new JoystickButton(XBController, 2);
  public static JoystickButton xblb = new JoystickButton(XBController, 5);
  public static JoystickButton xbrb = new JoystickButton(XBController, 6);
  public static JoystickButton xby = new JoystickButton(XBController, 4);
  public static JoystickButton xbx = new JoystickButton(XBController, 3);
  public static JoystickButton xbstart = new JoystickButton(XBController, 8);
  public static JoystickButton xbback = new JoystickButton(XBController, 7);
  public static JoystickButton xblstick = new JoystickButton(XBController, 9);
  public static JoystickButton xbrstick = new JoystickButton(XBController, 10);






  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    xba.toggleWhenPressed(new IntakeRun(delivery, intake));
    xbb.toggleWhenPressed(new TurretManual(turret));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

  /*
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }*/
}
