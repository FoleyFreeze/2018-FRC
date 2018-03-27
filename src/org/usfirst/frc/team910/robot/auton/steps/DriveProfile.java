package org.usfirst.frc.team910.robot.auton.steps;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.util.Path;

public class DriveProfile extends AutonStep {

	public static final double REST_TIME = .25;

	public Path[] pathsL;
	public Path[] pathsR;

	public int pathIdx;

	public DriveProfile(double[][] profile) {

		pathsL = new Path[profile.length / 2];
		pathsR = new Path[profile.length / 2];

		double lastAngle = 90;//start all profiles facing forward
		
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

		int lastPath = pathsL.length - 1;
		double lastPosL = pathsL[lastPath].positions.get(pathsL[lastPath].positions.size() - 1);
		double lastPosR = pathsR[lastPath].positions.get(pathsR[lastPath].positions.size() - 1);
		double lastVelL = pathsL[lastPath].velocities.get(pathsL[lastPath].positions.size() - 1);
		double lastVelR = pathsR[lastPath].velocities.get(pathsR[lastPath].positions.size() - 1);

		if (lastVelL == 0 && lastVelR == 0) {
			// stick to last point to make sure we actually get there
			for (int i = 0; i * Path.DT < REST_TIME; i++) {
				pathsL[lastPath].positions.add(lastPosL);
				pathsR[lastPath].positions.add(lastPosR);
				pathsL[lastPath].velocities.add(0.0);
				pathsR[lastPath].velocities.add(0.0);
				pathsL[lastPath].accelerations.add(0.0);
				pathsR[lastPath].accelerations.add(0.0);
				pathsL[lastPath].angles.add(lastAngle);
			}
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
		if (drive.isMpDoneYet()) {
			pathIdx++;
			if (pathIdx < pathsL.length) {
				drive.initMp(pathsL[pathIdx], pathsR[pathIdx]);
			} else
				return true;
		}
		return false;
	}

}
