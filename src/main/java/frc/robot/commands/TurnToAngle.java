/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TurnToAngle extends PIDCommand {
  /**
   * Creates a new TurnToAngle.
   */
  public TurnToAngle(double targetAngleDegrees, Drivetrain drivetrain) {
    super(
        // The controller that the command will use
        new PIDController(0, 0, 0),
        // This should return the measurement
        drivetrain::getYaw,
        // This should return the setpoint (can also be a constant)
        targetAngleDegrees,
        // This uses the output
        output -> drivetrain.arcadeDrive(0.f, output));

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);

    // Configure additional PID options by calling `getController` here.
    getController().enableContinuousInput(-180.f, 180.f);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
