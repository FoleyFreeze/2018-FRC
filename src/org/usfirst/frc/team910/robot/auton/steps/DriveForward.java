package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.auton.steps.AutonStep;

public class DriveForward extends AutonStep {
	double initDist;
	@Override
	public void init() {
		in.driveStraight = true;
		initDist = sense.leftDist;
	}

	@Override
	public void run() {
		in.rightDrive = 0.8;
		drive.run();
	}
	@Override
	public boolean isDone() {
		if(sense.leftDist - initDist >= 60) {
			in.rightDrive = 0.0;
			in.driveStraight = false;
			return true;
		}else {
			return false;
		}
	}

	
}
