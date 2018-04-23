package org.usfirst.frc.team910.robot.auton.steps;

public class RecordStop extends AutonStep {

	@Override
	public void init() {
		in.recordPath = false;
	}

	@Override
	public void run() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
