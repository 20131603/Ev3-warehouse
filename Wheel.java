package warehouse.robot.t4.Ev3warehouse;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Wheel {
	protected EV3LargeRegulatedMotor leftMotor;
	protected EV3LargeRegulatedMotor rightMotor;
	protected EV3GyroSensor gyro = null;
	protected SampleProvider gyroSamples = null;
	protected EV3ColorSensor colorSensor;
	protected SampleProvider colorSamples;
	protected int sampleSize;
	protected int default_speed = 200;
	protected int default_time = 50;
	protected int default_slow_speed = 50;
	protected float[] sample;
	protected boolean greenOnRight = true;

	float[] angle = { 0.0f };
	float gyroTacho;

	public Wheel() {
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		colorSensor = new EV3ColorSensor(SensorPort.S4); //cannot have color in s2 on uppsala robot
		gyro = new EV3GyroSensor(SensorPort.S1);
		colorSamples = colorSensor.getRGBMode();
		sampleSize = colorSamples.sampleSize();
		gyroSamples = gyro.getAngleMode();
		leftMotor.setAcceleration(1000);
		rightMotor.setAcceleration(1000);
		leftMotor.setSpeed(default_speed);
		rightMotor.setSpeed(default_speed);
		greenOnRight = true;
	}

	public void setMode(boolean newMode) {
		greenOnRight = newMode;
	}

	public void up(int speed, int time) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		leftMotor.forward();
		rightMotor.forward();
		Delay.msDelay(time);
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
	}

	public void down(int speed, int time) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		leftMotor.backward();
		rightMotor.backward();
		Delay.msDelay(time);
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
	}

	public void left(int speed, int time) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		leftMotor.backward();
		rightMotor.forward();
		Delay.msDelay(time);
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
	}

	public void right(int speed, int time) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		leftMotor.forward();
		rightMotor.backward();
		Delay.msDelay(time);
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);

	}

	public float getGyroAngleRaw() {
		gyroSamples.fetchSample(angle, 0);
		return angle[0];
	}

	public float getGyroAngle() {
		float rawAngle = getGyroAngleRaw();
		return rawAngle - gyroTacho;
	}

	public void resetGyro() {
		if (gyro != null) {
			Delay.msDelay(300); //make sure robot is standing still before reset
			gyro.reset();
			gyroSamples = gyro.getAngleMode();
			gyroTacho = 0;
		}
	}

	public void left90() {
		this.resetGyro();
		while (true) {
			this.left(100, 50);
			if (this.getGyroAngle() >= 70) {
				break;
			}
		}
	}

	public void right90() {
		this.resetGyro();
		while (true) {
			this.right(100, 50);
			if (this.getGyroAngle() <= -70) {
				break;
			}
		}
	}

	public float[] getSample() {
		float[] sample = new float[sampleSize];
		colorSamples.fetchSample(sample, 0);
		return sample;
	}

	public void forwardUntiHitSpot() {
		// lets move
		while (true) {
			sample = getSample();
			//white
			if (sample[0] > 0.2 && sample[1] > 0.2 && sample[2] > 0.2) {
				if (greenOnRight) {
					down(default_speed, 300);
				} else {
					up(default_speed, 300);
				}
				System.out.println("white");
			} else
			// black
			if (sample[0] < 0.07 && sample[1] < 0.07 && sample[2] < 0.07) {
				System.out.println("black");
				break;
			} else
			// red
			if (sample[0] > 0.2) {
				if (greenOnRight) {
					up(default_speed, default_time);
				} else {
					down(default_speed, default_time);
				}
				System.out.println("red");
			} else
			// green
			if (sample[1] > 0.2) {
				if (greenOnRight) {
					left(default_speed, default_time);
				} else {
					right(default_speed, default_time);
				}
				System.out.println("green");
			} else
			// blue
			{
				if (greenOnRight) {
					right(default_speed, default_time);
				} else {
					left(default_speed, default_time);
				}
				System.out.println("blue");
			}
		}
		greenOnRight = true;
	}
/*
	public void moveToSpotCenter() {
		while (true) {
			up(default_speed, default_time);
			sample = getSample();
			// red || white
			if (sample[0] > 0.2) {
				break;
			}
		}
	}

	public void escapeBlack() {
		// escape from black using for turn left|right
		while (true) {
			sample = getSample();
			if (sample[0] < 0.07 && sample[1] < 0.07 && sample[2] < 0.07) {
				up(default_speed, default_time);
			} else {
				break;
			}
		}
	}
*/
	/*
	public void backwardThroughBlack() {
		// escape from black using for turn left|right
		boolean hitBlack = false;
		while (true) {
			sample = getSample();
			if (sample[0] < 0.07 && sample[1] < 0.07 && sample[2] < 0.07) {
				break;
			} else {
				down(default_speed, default_time);
			}
		}
		while (true) {
			sample = getSample();
			if (sample[0] < 0.07 && sample[1] < 0.07 && sample[2] < 0.07) {
				down(default_speed, default_time);
			} else {
				break;
			}
		}
	}
	*/

}
