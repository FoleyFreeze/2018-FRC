package org.usfirst.frc.team910.robot.auton.steps;

import edu.wpi.first.wpilibj.Timer;

public class WaitStep extends AutonStep {

	double waitTime;
	double startTime;
	public WaitStep(double time) {
		this.waitTime = time;
	}
	
	@Override
	public void init() {
		startTime = Timer.getFPGATimestamp();
	}

	@Override
	public void run() {
		
	}

	@Override
	public boolean isDone() {
		if(Timer.getFPGATimestamp() > startTime + waitTime) {
			return true;
		}
		return false;
	}

}
