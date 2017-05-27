package warehouse.robot.t4.Ev3warehouse;

import warehouse.robot.t4.Ev3warehouse.Wheel;
import warehouse.robot.t4.Ev3warehouse.Fork;

public class Actions {
	
	static public void Forward(Wheel wheel){
		wheel.up(wheel.default_speed, 800); //get out of black spot
		wheel.forwardUntiHitSpot();
	}
	
	static public void Turn90(Wheel wheel, boolean left){
		float curAngle = wheel.getGyroAngleRaw();
		if(left){
			wheel.leftMotor.setSpeed(20);
			wheel.rightMotor.setSpeed(150);
			while(wheel.getGyroAngleRaw() < curAngle+85.0f){
				wheel.leftMotor.forward();
				wheel.rightMotor.forward();
			}
		}else{
			wheel.leftMotor.setSpeed(150);
			wheel.rightMotor.setSpeed(20);			
			while(wheel.getGyroAngleRaw() > curAngle-85.0f){
				wheel.leftMotor.forward();
				wheel.rightMotor.forward();
			}
		}
		wheel.down(100, 200);
		wheel.leftMotor.stop(true);
		wheel.rightMotor.stop();
	}
	
	static public void Spin(Wheel wheel){
		wheel.leftMotor.setSpeed(100);
		wheel.rightMotor.setSpeed(100);
		float curAngle = wheel.getGyroAngleRaw();
		while(wheel.getGyroAngleRaw() < curAngle+175.0f){
			wheel.rightMotor.forward();
			wheel.leftMotor.backward();
		}
		wheel.leftMotor.stop(true);
		wheel.rightMotor.stop();
	}
	
	static public void ForkUp(Fork fork){
		fork.frontMotor.rotate(-1000);
	}
	
	static public void ForkDown(Fork fork){
		fork.frontMotor.rotate(1000);
	}
	
	static public void Pickup(Wheel wheel, Fork fork){
		
	}
	
	static public void Drop(Wheel wheel, Fork fork){
		
	}
}
