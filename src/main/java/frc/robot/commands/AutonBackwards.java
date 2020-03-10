/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonBackwards extends ParallelRaceGroup {
  /**
   * Creates a new AutonBackwards.
   */
  public AutonBackwards(Intake intake, IntakeActuation intakeAct, Delivery delivery, Drivetrain drivetrain, int distance) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new IntakeNew(intake, delivery, intakeAct), new SimpleForward(drivetrain, true, distance));
  }
}
