package org.usfirst.frc.team910.robot.auton.steps;

public class StartStep extends AutonStep {

	@Override
	public void init() {
		
	}

	@Override
	public void run() {
		sense.reset();	//set start position to 0 degrees	
	}

	@Override
	public boolean isDone() {
		return true; //move one right away
	}
}
