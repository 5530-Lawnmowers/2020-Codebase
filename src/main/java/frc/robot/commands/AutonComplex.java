/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonComplex extends SequentialCommandGroup {
  /**
   * Creates a new AutonComplex.
   */
  public AutonComplex(Hood hood, Turret turret, Shooter shooter, Delivery delivery, Intake intake, IntakeActuation intakeAct, Drivetrain drivetrain) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    //addCommands(new AlignAll(hood, turret), new ShootSmart(delivery, shooter), new IntakeTogglePosition(intakeAct), new AutonBackwards(intake, delivery, drivetrain, 93000)/*, new SimpleForward(drivetrain, false, 140000), new AlignAll(hood, turret), new ShootSmart(delivery, shooter)*/);
    //super(new AlignAll(hood, turret), new ShootSmart(delivery, shooter), new DriveBackTimed(drivetrain), new IntakeTogglePosition(intake));
    super(new AlignAll(hood, turret), new ShootSmart(delivery, shooter), new IntakeTogglePosition(intakeAct), new AutonBackwards(intake, intakeAct, delivery, drivetrain, 93000), new AlignAll(hood, turret), new ShootSmart(delivery, shooter));
  }
}
