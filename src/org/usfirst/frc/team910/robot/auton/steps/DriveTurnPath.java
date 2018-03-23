package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class DriveTurnPath extends AutonStep {

	private Path profileL;
	private Path profileR;
		
	public DriveTurnPath() {
		profileL = new Path();
		profileL.buildPath(100, 40, 15, 0, 0, 0); 
		profileR = new Path();
		profileR.buildPath(100, 40, -15, 0, 0, 0); 
	}
	
	@Override
	public void init() {
		out.resetDriveEncoders();
		drive.initMp(profileL, profileR);
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
