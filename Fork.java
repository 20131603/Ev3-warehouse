package main;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Fork {
	protected RegulatedMotor frontMotor = Motor.A;

	public Fork() {
		frontMotor.setAcceleration(400);
	}

	public void up(int speed, int time) {
		frontMotor.setSpeed(speed);
		frontMotor.forward();
		Delay.msDelay(time);
		frontMotor.setSpeed(0);
	}

	public void down(int speed, int time) {
		frontMotor.setSpeed(speed);
		frontMotor.backward();
		Delay.msDelay(time);
		frontMotor.setSpeed(0);
	}
}
