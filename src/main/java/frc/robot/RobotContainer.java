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
    public static final XboxController XBController1 = new XboxController(1);
    //Driver Controller
    public static final XboxController XBController2 = new XboxController(2);
    //Operator Controller

    private final Climber climber = new Climber();
    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();
    private final IntakeActuation intakeAct = new IntakeActuation();
    private final Spinner spinner = new Spinner();
    private final Delivery delivery = new Delivery();
    private final Turret turret = new Turret();
    private final Hood hood = new Hood();

    private final AutonComplex autonTest = new AutonComplex(hood, turret, shooter, delivery, intake, intakeAct, drivetrain);

    public static JoystickButton xb1a = new JoystickButton(XBController1, 1);
    public static JoystickButton xb1b = new JoystickButton(XBController1, 2);
    //public static JoystickButton xb1lb = new JoystickButton(XBController1, 5); Used by turret default command
    //public static JoystickButton xb1rb = new JoystickButton(XBController1, 6); Used by turret default command
    public static JoystickButton xb1y = new JoystickButton(XBController1, 4);
    public static JoystickButton xb1x = new JoystickButton(XBController1, 3);
    //public static JoystickButton xbstart = new JoystickButton(XBController, 8);
    public static JoystickButton xb1back = new JoystickButton(XBController1, 7);
    public static JoystickButton xb1lstick = new JoystickButton(XBController1, 9);
    public static JoystickButton xb1rstick = new JoystickButton(XBController1, 10);

    public static JoystickButton xb2a = new JoystickButton(XBController2, 1);
    public static JoystickButton xb2b = new JoystickButton(XBController2, 2);
    public static JoystickButton xb2lb = new JoystickButton(XBController2, 5);
    public static JoystickButton xb2rb = new JoystickButton(XBController2, 6);
    public static JoystickButton xb2y = new JoystickButton(XBController2, 4);
    public static JoystickButton xb2x = new JoystickButton(XBController2, 3);
    //public static JoystickButton xbstart = new JoystickButton(XBController, 8);
    public static JoystickButton xb2back = new JoystickButton(XBController2, 7);
    public static JoystickButton xb2lstick = new JoystickButton(XBController2, 9);
    public static JoystickButton xb2rstick = new JoystickButton(XBController2, 10);

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
        //xb2y.whenHeld(new IntakeRun(delivery, intake));    
        //xb2b.whenHeld(new IntakeActManual(intake));

        //xb1a.toggleWhenPressed(new OffsetTestingHelper());
        //xb1b.whenHeld(new AlignCell(drivetrain));

        //XB1
        xb1a.toggleWhenPressed(new IntakeNew(intake, delivery, intakeAct));
        xb1b.toggleWhenPressed(new IntakeTogglePosition(intakeAct));

        //XB2
        xb2a.toggleWhenPressed(new ShootManual(shooter));
        //xb2rb.whenHeld(new ShootAll(delivery, shooter, intake)); // Old
        xb2rb.whenHeld(new ShootSmart(delivery, shooter)); // Test
        xb2lb.whenHeld(new TurretAlign(turret));
        xb2lb.whenHeld(new HoodAlign(hood));
        xb2back.whenPressed(new Climb(climber));
        xb2y.toggleWhenPressed(new IntakeTogglePosition(intakeAct));
        xb2x.whenHeld(new IntakeOn(intake));
        xb2b.toggleWhenPressed(new IntakeNew(intake, delivery, intakeAct));
        //xb2y.whenPressed(new ToggleLight());
        //Other buttons in use
        //DriveDefault: xb1 left trigger, xb1 right trigger, xb1 left stick
        //TurretManual: xb2 left stick
        //HoodManual: xb2 left stick
        //DeliveryManual: xb2 right trigger, xb2 left trigger
        //IntakeActManual: xb2 right trigger, xb2 left trigger
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */

    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return autonTest;
    }

    public void setDrivetrainMode(boolean brake) {
        drivetrain.setBrakeMode(brake);
    }
}
