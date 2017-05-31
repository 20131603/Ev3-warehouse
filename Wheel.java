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
	protected int default_speed = 100;
	protected int default_time = 50;
	protected float[] sample;
	protected boolean mode = true;

	float[] angle = { 0.0f };
	float gyroTacho;

	public Wheel() {
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		colorSensor = new EV3ColorSensor(SensorPort.S4); // cannot have color in
															// s2 on uppsala
															// robot
		gyro = new EV3GyroSensor(SensorPort.S1);
		colorSamples = colorSensor.getRGBMode();
		sampleSize = colorSamples.sampleSize();
		gyroSamples = gyro.getAngleMode();
		leftMotor.setAcceleration(200);
		rightMotor.setAcceleration(200);
		leftMotor.setSpeed(default_speed);
		rightMotor.setSpeed(default_speed);
		mode = true;
	}

	public void setMode(boolean newMode) {
		mode = newMode;
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

	public void left() {
		Delay.msDelay(300);
		leftMotor.setSpeed(default_speed);
		leftMotor.rotate(-30);
		leftMotor.setSpeed(0);
		Delay.msDelay(300);
		up(300,350);
	}

	public void right() {
		Delay.msDelay(300);
		rightMotor.setSpeed(default_speed);
		rightMotor.rotate(-30);
		rightMotor.setSpeed(0);
		Delay.msDelay(300);
		up(300,350);

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
			Delay.msDelay(300); // make sure robot is standing still before
								// reset
			gyro.reset();
			gyroSamples = gyro.getAngleMode();
			gyroTacho = 0;
		}
	}

	public float[] getSample() {
		float[] sample = new float[sampleSize];
		colorSamples.fetchSample(sample, 0);
		return sample;
	}

	public void forwardUntiHitSpot() {
		while (true) {
			sample = getSample();
			if (sample[0] <= 0.07 && sample[1] <= 0.07 && sample[2] <= 0.07) {
				up(default_speed, 400);
			} else {
				break;
			}
		}

		// lets move
		while (true) {
			sample = getSample();
			// white
			if (sample[0] > 0.2 && sample[1] > 2 && sample[2] > 0.2) {
				System.out.println("white");
				right();
			} else
			// black
			if (sample[0] <= 0.08 && sample[1] <= 0.08 && sample[2] <= 0.08) {
				System.out.println("black");
				break;
			} else
			// red
			if (sample[0] > sample[1] && sample[0] > sample[2] && sample[0] > 0.18) {
				System.out.println("red");
				up(default_speed, default_time);
			} else
			// green
			if (sample[1] > 0.15 && sample[2] < 0.12) {
				System.out.println("green");
				if (mode) {
					left();
				} else {
					right();
				}
			} else
			// blue
			if (sample[2] > 0.10 && sample[1] < 0.8) {
				System.out.println("blue");
				if (mode) {
					right();
				} else {
					left();
				}
			}else{
				up((int) (default_speed*0.5), default_time);
			}
		}
	}

	void backwardUntilGetRed() {
		while (true) {
			sample = getSample();
			if (sample[0] > 0.15) {
				break;
			} else {
				down(default_speed, default_time);
			}
		}
	}
	
	boolean checkRed() {
		sample = getSample();
		if (sample[0] > 0.13 && sample[1] > 0.13 && sample[2] > 0.13) {
			return false;
		} else if (sample[0] > 0.15) {
			return true;
		} else {
			return false;
		}
	}
}
