package org.usfirst.frc.team910.robot.auton;

public abstract class AutonStep {

	public abstract void init();
	
	public abstract void run();
	
	public abstract boolean isDone();
	
	public boolean isError() {
		return false;
	}
	
}
