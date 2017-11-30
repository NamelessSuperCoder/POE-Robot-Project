package org.usfirst.frc.team2974.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.command.GearIntakeCommand;

/**
 * Gear Intake
 */
public class GearIntake extends Subsystem {

  private final Solenoid piston;

  private final DigitalInput gearSensor;

  public GearIntake() {
    super("Gear Intake");

    this.piston = RobotMap.gearIntakeSolenoid;
    this.gearSensor = RobotMap.gearSensor;
  }

  @Override
  protected void initDefaultCommand() {
    this.setDefaultCommand(new GearIntakeCommand());
  }

  public void setDeployed(boolean deployed) {
    this.piston.set(deployed);
  }

  public void toggleDeployed() {
    this.piston.set(!this.piston.get());
  }

  public boolean hasGear() {
    return !this.gearSensor.get();
  }
}
