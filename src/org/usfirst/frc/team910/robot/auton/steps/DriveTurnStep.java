package org.usfirst.frc.team910.robot.auton.steps;

import edu.wpi.first.wpilibj.Timer;

public class DriveTurnStep extends AutonStep {

	double leftPwr;
	double rightPwr;
	double targetTime;
	
	double startLeft;
	double startRight;
	double startTime;
	
	public DriveTurnStep(double left, double right, double time) {
		leftPwr = left;
		rightPwr = right;
		targetTime = time;
	}
	
	@Override
	public void init() {
		startTime = Timer.getFPGATimestamp();
		startLeft = sense.leftDist;
		startRight = sense.rightDist;
	}

	@Override
	public void run() {
		in.dynamicBrake = false;
		in.driveStraight = false;
		in.leftDrive = leftPwr;
		in.rightDrive = rightPwr;
	}

	@Override
	public boolean isDone() {
		if(Timer.getFPGATimestamp() > startTime + targetTime) {
			in.leftDrive = 0;
			in.rightDrive = 0;
			return true;
		} else {
			return false;
		}
		
	}

}
