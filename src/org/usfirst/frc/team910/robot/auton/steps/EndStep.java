package org.usfirst.frc.team910.robot.auton.steps;

public class EndStep extends AutonStep {

	@Override
	public void init() {
		
		
	}

	@Override
	public void run() {
		
	}

	@Override
	public boolean isDone() {
		
		return false; 	//never ends on purpose
					  	//always end of our auton, ensures we dont move out of control when we finish.
	}

}
