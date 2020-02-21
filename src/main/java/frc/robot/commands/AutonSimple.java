/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class AutonSimple extends CommandBase {
    private Drivetrain drivetrain;
    private Intake intake;
    private Delivery delivery;
    private Shooter shooter;
    private Hood hood;
    private Turret turret;

    private TurretAlign turretAlign;
    private HoodAlign hoodAlign;
    private ShootAll shoot;

    /**
     * Creates a new AutonSimple.
     */
    public AutonSimple(Drivetrain drivetrain, Intake intake, Delivery delivery, Shooter shooter, Hood hood, Turret turret) {
        addRequirements(drivetrain, intake, delivery, shooter, hood, turret);
        this.drivetrain = drivetrain;
        this.intake = intake;
        this.delivery = delivery;
        this.shooter = shooter;
        this.hood = hood;
        this.turret = turret;
        turretAlign = new TurretAlign(turret);
        hoodAlign = new HoodAlign(hood);
        shoot = new ShootAll(delivery, shooter, intake);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        CommandScheduler.getInstance().schedule(turretAlign, hoodAlign);
        shooter.setShooter(1.0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (turretAlign.isAligned() && hoodAlign.isAligned()) {
            CommandScheduler.getInstance().cancel(turretAlign, hoodAlign);
            CommandScheduler.getInstance().schedule(shoot);
        }

        if (!delivery.getBreakbeams()[3] && !delivery.getBreakbeams()[2] && !delivery.getBreakbeams()[1] && !delivery.getBreakbeams()[0] && intake.getSwitch()) {
            CommandScheduler.getInstance().cancel(shoot);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
