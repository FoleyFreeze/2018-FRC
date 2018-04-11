package org.usfirst.frc.team910.robot.auton.steps;

public class AutoGather extends AutonStep {

	
	boolean side;
	public AutoGather(boolean back) {
		side = back;
	}

	@Override
	public void init() {
		
		
	}

	public void run() {
		in.gather = true;
		in.actionButton = true;
		in.liftFlip = side;
		
	}

	@Override
	public boolean isDone() {
		if(sense.hasCube) return true;
		else return false;
	}

}
