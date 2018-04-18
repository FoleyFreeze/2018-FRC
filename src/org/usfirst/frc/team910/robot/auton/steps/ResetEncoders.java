package org.usfirst.frc.team910.robot.auton.steps;

public class ResetEncoders extends AutonStep {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		out.resetDriveEncoders();
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
