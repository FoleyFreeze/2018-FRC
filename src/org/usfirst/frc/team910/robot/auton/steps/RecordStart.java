package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class RecordStart extends AutonStep{

	@Override
	public void init() {
		Path.startRecord();
		in.recordPath = true;
	}

	@Override
	public void run() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
