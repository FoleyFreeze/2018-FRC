package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class NegativeWait extends AutonStep{

	private double idxFromEnd = 0;
	
	public NegativeWait(double timeFromEnd) {
		idxFromEnd = timeFromEnd / Path.DT;
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void run() {
		
	}

	@Override
	public boolean isDone() {
		if(drive.index < idxFromEnd) {
			return true;
		} else {
			return false;
		}
	}

}
