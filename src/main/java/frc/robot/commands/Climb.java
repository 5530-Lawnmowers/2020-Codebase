/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import frc.robot.RobotContainer;
import frc.robot.helpers.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;


public class Climb extends CommandBase {
  private final Climber climber;
  /**
   * Creates a new Climb.
   */
  public Climb(Climber climber) {
    this.climber = climber;
    addRequirements(climber);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(!TimerHelper.getEndgame()){
      climber.realeaseArm();
    }
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(RobotContainer.XBController.getStartButton()){
      climber.setClimb(.4, 1);
      climber.setClimb(.4, 2);
    }
    else{
      climber.setClimb(0, 1);
      climber.setClimb(0, 2);
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(!TimerHelper.getEndgame()){
      return true;
    }
    return false;
  }
}
