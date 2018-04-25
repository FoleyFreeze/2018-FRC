package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;
import org.usfirst.frc.team910.robot.util.Profile;

public class ReverseProfile extends AutonStep {

	public Path[] pathsL;
	public Path[] pathsR;

	public int pathIdx;
	
	public ReverseProfile(double[][] profile, double initAngle) {
		
		pathsL = new Path[profile.length / 2];
		pathsR = new Path[profile.length / 2];

		double lastAngle = initAngle;//start all profiles facing forward, except this one
		
		// add values to paths(Left and Right)
		for (int i = 0; i < profile.length; i += 2) {
			pathsL[i / 2] = new Path();
			pathsL[i / 2].buildPath(profile[i][0], profile[i][1], profile[i][2], profile[i][3], profile[i][4],profile[i][5]);
			pathsR[i / 2] = new Path();
			pathsR[i / 2].buildPath(profile[i + 1][0], profile[i + 1][1], profile[i + 1][2], profile[i + 1][3],profile[i + 1][4], profile[i + 1][5]);
			
			pathsL[i/2].calcAngles(pathsL[i/2], pathsR[i/2], lastAngle);
			lastAngle = pathsL[i/2].angles.get(pathsL[i/2].angles.size()-1);
			
			if(pathsL[i/2].positions.size() != pathsR[i/2].positions.size()) {
				System.out.println("paths not equal: " + i/2);
				
			}
			
		}
	}
	
	@Override
	public void init() {
		pathIdx = pathsL.length - 1;
		
		int idx = pathsL.length-1;
		int endpos = pathsL[idx].positions.size()-1;
		double endL = pathsL[idx].positions.get(endpos);
		double endR = pathsR[idx].positions.get(endpos);
		double endA = pathsL[idx].angles.get(endpos);
		
		drive.reversePath(true, endL, endR, endA);
		drive.initMp(pathsL[idx], pathsR[idx]);
	}

	@Override
	public void run() {
		in.enableMP = true;

	}

	@Override
	public boolean isDone() {
		if(pathIdx > 0) {
			if(drive.isMpDoneYet()) {
				pathIdx--;
				drive.initMp(pathsL[pathIdx], pathsR[pathIdx]);
			}
		} else {
			if(drive.isMpDoneYet(0)) {
				in.enableMP = false;
				drive.reversePath(false, 0, 0, 0);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isError() {
		if (Math.abs(drive.lError) >= 30 
				|| Math.abs(drive.rError) >= 30 
				|| Math.abs(drive.angleError) >= 90) {
			//drive.reversePath(false, 0, 0);
			
			System.out.println("lE: " + drive.lError + " rE: " + drive.rError + " aE " + drive.angleError);
			return false;
		}
			
		
		
		return false;
		
	}
	
}
