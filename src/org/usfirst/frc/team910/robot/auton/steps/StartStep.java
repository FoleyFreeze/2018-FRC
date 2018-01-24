package org.usfirst.frc.team910.robot.auton.steps;

public class StartStep extends AutonStep {

	@Override
	public void init() {
		
	}

	@Override
	public void run() {
		sense.resetnavx();		
	}

	@Override
	public boolean isDone() {
		return true;
	}
}
