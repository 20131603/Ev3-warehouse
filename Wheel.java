package main;

import java.util.Arrays;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Wheel {
	protected RegulatedMotor leftMotor = Motor.B;
	protected RegulatedMotor rightMotor = Motor.C;
	protected EV3GyroSensor gyro = null;
	protected SampleProvider gyroSamples = null;
	protected EV3ColorSensor colorSensor;
	protected SampleProvider sampleProvider;
	protected int sampleSize;
	protected int default_speed = 100;
	protected int default_time = 50;
	protected int default_slow_speed = 50;
	protected float[] sample;
	protected boolean mode = true;

	float[] angle = { 0.0f };
	float gyroTacho = 0;

	public Wheel() {
		colorSensor = new EV3ColorSensor(SensorPort.S2);
		sampleProvider = colorSensor.getRGBMode();
		sampleSize = sampleProvider.sampleSize();
		leftMotor.setAcceleration(400);
		rightMotor.setAcceleration(400);
		gyro = new EV3GyroSensor(SensorPort.S1);
		gyroSamples = gyro.getAngleMode();
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
		sampleProvider.fetchSample(sample, 0);
		return sample;
	}

	public void forwardUntiHitSpot() {
		// lets move
		while (true) {
			sample = getSample();
			// System.out.println(String.format("%.2f", sample[0]) + "-" +
			// String.format("%.2f", sample[1]) + "-"
			// + String.format("%.2f", sample[2]));

			// white
			if (sample[0] > 0.2 && sample[1] > 0.2 && sample[2] > 0.2) {
				if (mode) {
					down(default_speed, default_time);
				} else {
					up(default_speed, default_time);
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
				if (mode) {
					up(default_speed, default_time);
				} else {
					down(default_speed, default_time);
				}
				System.out.println("red");
			} else
			// green
			if (sample[1] > 0.2) {
				if (mode) {
					left(default_speed, default_time);
				} else {
					right(default_speed, default_time);
				}
				System.out.println("green");
			} else
			// blue
			{
				if (mode) {
					right(default_speed, default_time);
				} else {
					left(default_speed, default_time);
				}
				System.out.println("blue");
			}
		}
		mode = true;
	}

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

}
