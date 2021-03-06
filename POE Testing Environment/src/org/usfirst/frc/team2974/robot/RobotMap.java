package org.usfirst.frc.team2974.robot;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.DISTANCE_PER_PULSE;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * This class is where you initialize every motor, talon, solenoid, digital inputs, analog inputs
 * etc... The variables should all be static final. <p> This is the Hardware Map of the robot.
 */
public final class RobotMap {

	public static final Talon leftMotor;
	public static final Talon rightMotor;

	public static final Encoder leftEncoder;
	public static final Encoder rightEncoder;

	public static final Solenoid pneumaticsShifter;

	public static final Solenoid gearIntakeSolenoid;
	public static final Talon climberMotor;

	public static final Compressor compressor;

	public static final Gyro gyroscope;

	public static final DigitalInput gearSensor;

	static {
		// the front is where the camera is now. deal with it.
		rightMotor = new Talon(0);
		leftMotor = new Talon(1);

		leftMotor.setInverted(true);

		leftEncoder = new Encoder(new DigitalInput(0),
			new DigitalInput(1)); // comp: 0, 1 prac: same
		rightEncoder = new Encoder(new DigitalInput(2),
			new DigitalInput(3)); // comp: 2, 3 prac: same

		rightEncoder.setReverseDirection(true);

		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

		pneumaticsShifter = new Solenoid(0);
		gearIntakeSolenoid = new Solenoid(2);

		climberMotor = new Talon(4);

		compressor = new Compressor();
		compressor.stop();

		// gyro is trash, but it is there.
		gyroscope = new AnalogGyro(1);

		gearSensor = new DigitalInput(5);
	}

	private RobotMap() {
	}
}
