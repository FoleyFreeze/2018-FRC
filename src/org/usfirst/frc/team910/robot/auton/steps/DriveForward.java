package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.auton.steps.AutonStep;

public class DriveForward extends AutonStep {
	double initDist;
	@Override
	public void init() {
		initDist = sense.leftDist; //set where we start to ensure accuracy
	}

	@Override
	public void run() {
		in.driveStraight = true; //don't drive crooked
		in.dynamicBrake = false; //we are driving, not braking, just making sure
		in.rightDrive = 0.8; //power for DriveForward 
	}
	
	@Override
	public boolean isDone() {
		//we are done after 60 inches / 5ft
		if(sense.leftDist - initDist >= 60) {
			in.rightDrive = 0.0; //ensures we stop driving when at desired distance
			in.driveStraight = false; //turn this off
			return true; 
		}else {
			return false;
		}
	}

	
}
