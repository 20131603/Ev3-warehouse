package warehouse.robot.t4.Ev3warehouse;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Fork {
	protected EV3MediumRegulatedMotor frontMotor = new EV3MediumRegulatedMotor(MotorPort.A);

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
