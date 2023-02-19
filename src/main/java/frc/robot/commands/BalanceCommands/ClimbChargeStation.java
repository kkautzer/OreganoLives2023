// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BalanceCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.Constants.AutonomousConstants;;

public class ClimbChargeStation extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  boolean interactedWithRamp = false;
  /** Creates a new AutoBalance. */
  public ClimbChargeStation() {
    addRequirements(drivetrain);
    addRequirements(gyro);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    gyro.calibrate();
    gyro.resetZRotation();
    drivetrain.disableBrakes();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (gyro.getYRotation() > AutonomousConstants.kMaxYAngleOffset || gyro.getYRotation() < AutonomousConstants.kMaxYAngleOffset) {
      interactedWithRamp = true;
    }

    if (interactedWithRamp) {
      drivetrain.driveArcade(0.65, 0, 0.75, 0);
    } else if (interactedWithRamp) {
      if (gyro.getYRotation() < -7) {
        drivetrain.driveArcade(0.40, 0, 0.65, 0);
      } else if (gyro.getYRotation() > 7) {
        drivetrain.driveArcade(-0.40, 0, 0.65, 0);
      } else {
        drivetrain.enableBrakes();
        drivetrain.driveArcade(0, 0, 0, 0);
      }
    }
    SmartDashboard.putNumber("Gyro Y Rotation", gyro.getYRotation());  
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();

  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Ends if within 3 degrees of flat
    return gyro.getYRotation() < AutonomousConstants.kMaxYAngleOffset && gyro.getYRotation() > -AutonomousConstants.kMaxYAngleOffset;
    // return (drivetrain.getNeutralMode() == NeutralMode.Brake); // Ends if brake mode is enabled
  }
}