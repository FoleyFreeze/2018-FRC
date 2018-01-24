package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.Component;

public abstract class AutonStep extends Component {

	public abstract void init();
	
	public abstract void run();
	
	public abstract boolean isDone();
	
	public boolean isError() {
		return false;
	}
	
}
