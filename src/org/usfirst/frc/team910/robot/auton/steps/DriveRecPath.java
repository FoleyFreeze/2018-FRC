package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class DriveRecPath extends AutonStep {

	int numStepsToThrowAway = 0;
	
	public DriveRecPath(int stepsToThrowAway) {
		numStepsToThrowAway = stepsToThrowAway;
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
		if(drive.isMpDoneYet(numStepsToThrowAway)) {
			System.out.println("PathComplete"+drive.index);
			in.enableMP = false;
			in.mpRecPath = false;
			return true;
		}else {
			return false;
		}
	}
	
	boolean first = true;
	
	@Override
	public boolean isError() {
		/*
		if (Math.abs(drive.lError) >= 30 
				|| Math.abs(drive.rError) >= 30 
				|| Math.abs(drive.angleError) >= 90) 
			return true;
		*/
		
		if((Path.recPathL.positions.size() * Path.DT) >= 5) {
			if(first) {
				System.out.println("RecPath Error: Length is " + (Path.recPathL.positions.size() * Path.DT));
				first = false;
			}
			return true;
		}
		
		return false;
		
	}
	
}
