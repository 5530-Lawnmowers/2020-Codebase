/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.helpers.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
    if(RobotContainer.XBController2.getStartButton()){
      climber.setClimb(.4, Constants.CLIMB_L);
      climber.setClimb(.4, Constants.CLIMB_R);
    }
    else{
      climber.setClimb(RobotContainer.XBController2.getX(Hand.kLeft), Constants.CLIMB_L);
      climber.setClimb(RobotContainer.XBController2.getX(Hand.kRight), Constants.CLIMB_R);
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
