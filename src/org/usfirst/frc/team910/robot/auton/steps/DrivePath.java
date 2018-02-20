package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class DrivePath extends AutonStep {

	private Path profile;
		
	public DrivePath() {
		profile = new Path();
		//profile.buildPath(120);
	}
	
	@Override
	public void init() {
		drive.initMp(profile, profile);
		//out.driveMP.init(profile, profile);
		
	}

	@Override
	public void run() {
		in.enableMP = true;

	}

	@Override
	public boolean isDone() {
		//if(out.driveMP.doneYet()) {
		if(drive.isMpDoneYet()) {
			in.enableMP = false;
			return true;
		}else {
			return false;
		}
	}
	/*
	@Override
	public boolean isError() {
		if(out.driveMP.weFailed()) {
			in.enableMP = false;
			return true;
		}else {
			return false;
		}
	}
	*/
}
