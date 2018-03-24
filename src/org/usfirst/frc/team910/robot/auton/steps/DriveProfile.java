package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

public class DriveProfile extends AutonStep {
	
	public Path [] pathsL;
	public Path [] pathsR;
	
	public int pathIdx;
	
	public DriveProfile(double [][] profile) {
		
		pathsL = new Path[profile.length/2];
		pathsR = new Path[profile.length/2];
		
		//add values to paths(Left and Right)
		for(int i=0; i<profile.length; i+=2) {
			pathsL[i/2] = new Path();
			pathsL[i/2].buildPath(profile[i][0], profile[i][1], profile[i][2], profile[i][3], profile[i][4], profile[i][5]);
			pathsR[i/2] = new Path();
			pathsR[i/2].buildPath(profile[i+1][0], profile[i+1][1], profile[i+1][2], profile[i+1][3], profile[i+1][4], profile[i+1][5]);
		}
	}
	
	@Override
	public void init() {
		pathIdx = 0;
		drive.initMp(pathsL[0], pathsR[0]);
	}

	@Override
	public void run() {
		in.enableMP = true;
		
		
	}

	@Override
	public boolean isDone() {
		if(drive.isMpDoneYet()) {
			pathIdx++;
			if(pathIdx < pathsL.length) {
				drive.initMp(pathsL[pathIdx], pathsR[pathIdx]);
			}else return true;
		}
		return false;
	}
	
	
}
