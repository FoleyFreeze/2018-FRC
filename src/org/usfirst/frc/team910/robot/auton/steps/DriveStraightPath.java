package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class DriveStraightPath extends AutonStep {

	private Path profile;
		
	public DriveStraightPath() {
		profile = new Path();					//driving 170 out of 230 total on prac field
		profile.buildPath(100, 80, 246, 0, 0, 0); //drive 256in, that leaves 1ft of room before the scale
		
		/*
		for(Double d : profile.positions) {
			System.out.println(d);
		}
		*/
	}
	
	@Override
	public void init() {
		out.resetDriveEncoders();
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
