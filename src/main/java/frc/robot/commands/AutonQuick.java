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
public class AutonQuick extends SequentialCommandGroup {

  /**
   * Creates a new AutonQuick.
   */
  public AutonQuick(Hood hood, Turret turret, Delivery delivery, Shooter shooter, Drivetrain drivetrain, Intake intake) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new AlignAll(hood, turret), new ShootSmart(delivery, shooter), new DriveBackTimed(drivetrain), new IntakeTogglePosition(intake));
  }
}
