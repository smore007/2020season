/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {
  
  public enum TalonGroups {
    kAll,
    kLeft,
    kRight,
    kMasters,
    kSlaves
  }
 
  private final AHRS m_ahrs = new AHRS(SerialPort.Port.kMXP);

  private final WPI_TalonSRX m_leftMasterTalon = new WPI_TalonSRX(Constants.DrivetrainMap.kBackLeftTalonPort);
  private final WPI_TalonSRX m_leftSlaveTalon = new WPI_TalonSRX(Constants.DrivetrainMap.kFrontLeftTalonPort);
  private final WPI_TalonSRX m_rightMasterTalon = new WPI_TalonSRX(Constants.DrivetrainMap.kBackRightTalonPort);
  private final WPI_TalonSRX m_rightSlaveTalon = new WPI_TalonSRX(Constants.DrivetrainMap.kFrontRightTalonPort); 

  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMasterTalon, m_rightMasterTalon);

  /**
   * Accepts consumer on each talon in specified group
   * @param action
   */
  private void applyToTalons(TalonGroups groups, Consumer<WPI_TalonSRX> action) {

    switch(groups) {
      case kAll:
        action.accept(m_leftMasterTalon);
        action.accept(m_leftSlaveTalon);
        action.accept(m_rightMasterTalon);
        action.accept(m_rightSlaveTalon);
        break;

      case kLeft:
        action.accept(m_leftMasterTalon);
        action.accept(m_leftSlaveTalon);
        break;

      case kRight:        
        action.accept(m_rightMasterTalon);
        action.accept(m_rightSlaveTalon);
        break;

      case kMasters:
        action.accept(m_leftMasterTalon);
        action.accept(m_rightMasterTalon);
        break;

      case kSlaves:
        action.accept(m_leftSlaveTalon);
        action.accept(m_rightSlaveTalon);
        break;
    }
  }

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {

    applyToTalons(TalonGroups.kAll, talon -> {
      
      // reset to factory defaults every time
      talon.configFactoryDefault();

      // braking neutral mode
      talon.setNeutralMode(NeutralMode.Brake);
    });

    m_leftSlaveTalon.follow(m_leftMasterTalon);
    m_rightSlaveTalon.follow(m_rightMasterTalon);

    // invert appropriate motors
    m_drive.setRightSideInverted(false);
    applyToTalons(TalonGroups.kLeft, talon -> talon.setInverted(InvertType.InvertMotorOutput));

    // setup encoders
    applyToTalons(TalonGroups.kAll, talon -> talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder));

    // lastly set default command, which is drive
    // setDefaultCommand(defaultCommand);
  }

  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void arcadeDrive(double speed, double rotation) {
    m_drive.arcadeDrive(speed, rotation);
  }

  public double getYaw() {
    return m_ahrs.getYaw();
  }
}
