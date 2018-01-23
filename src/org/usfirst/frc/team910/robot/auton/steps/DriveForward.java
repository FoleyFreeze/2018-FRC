package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.auton.AutonStep;

public class DriveForward extends AutonStep {
	double initDist;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		in.driveStraight = true;
		initDist = sense.leftDist;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		in.rightDrive = 0.8;
		drive.run();
	}
	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return sense.leftDist - initDist >= 60;
	}

	
}
