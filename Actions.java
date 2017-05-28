package warehouse.robot.t4.Ev3warehouse;

import warehouse.robot.t4.Ev3warehouse.Wheel;
import warehouse.robot.t4.Ev3warehouse.Fork;

public class Actions {
	
	static public void Forward(Wheel wheel){ //get out of black spot
		wheel.forwardUntiHitSpot();
	}
	
	static public void Turn90(Wheel wheel, boolean left){
		wheel.down(300, 600);
		float curAngle = wheel.getGyroAngleRaw();
		if(left){
			wheel.leftMotor.setSpeed(0);
			wheel.rightMotor.setSpeed(150);
			while(wheel.getGyroAngleRaw() < curAngle+70.0f){
				wheel.rightMotor.forward();
			}
		}else{
			wheel.leftMotor.setSpeed(150);
			wheel.rightMotor.setSpeed(0);			
			while(wheel.getGyroAngleRaw() > curAngle-70.0f){
				wheel.leftMotor.forward();
			}
		}
		wheel.leftMotor.stop();
		wheel.rightMotor.stop();
	}
	
	static public void Spin(Wheel wheel){
		wheel.leftMotor.setSpeed(100);
		wheel.rightMotor.setSpeed(100);
		float curAngle = wheel.getGyroAngleRaw();
		while(wheel.getGyroAngleRaw() < curAngle+170.0f){
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
