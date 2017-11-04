package org.usfirst.frc.team2974.robot.subsystems;

import org.usfirst.frc.team2974.robot.commands.Drive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem {

  private Talon rightMotor;
  private Talon leftMotor;

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new Drive());
  }

  /**
   * Sets the power of the wheels, pointing forward.
   * 
   * @param left left motor power
   * @param right right motor power
   */
  public synchronized void setSpeeds(double left, double right) {
    // TODO -> check what values go which direction
  }


  /**
   * Gets the speed for the left motor
   * 
   * @return left motor speed
   */
  public synchronized double getLeftMotorPower() {
    // TODO -> check what method to use to get the power either .get() or .getSpeed()
    return leftMotor.getSpeed();
  }

  /**
   * Gets the speed for the right motor
   * 
   * @return right motor speed
   */
  public synchronized double getLRightMotorPower() {
    // TODO -> check what method to use to get the power either .get() or .getSpeed()
    return rightMotor.getSpeed();
  }
}
