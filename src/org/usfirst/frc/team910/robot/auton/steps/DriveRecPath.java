package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class DriveRecPath extends AutonStep {

	public DriveRecPath() {
		
	}
	
	@Override
	public void init() {
		in.mpRecPath = true;
		drive.initMp(Path.recPathL, Path.recPathR);
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
			in.mpRecPath = false;
			return true;
		}else {
			return false;
		}
	}
	
}
