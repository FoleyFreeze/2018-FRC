package org.usfirst.frc.team910.robot.util;

/*
import java.nio.file.Paths;
import java.util.ArrayList;

public class PathRunner {

	public static final double ROBOT_WIDTH = 23.2375;
	
	
	static double endPosL;
	static double endPosR;
	static double endVelL;
	static double endVelR;
	static double ACCEL = 500;
	static double MAX_VEL = 80;
	
	public static void buildTurn(Path pL, Path pR, double distL, double distR, double endVel) {
		if(distL > distR) {
			pL.buildPath(ACCEL, MAX_VEL, distL, endPosL, endVelL, endVel);
			double targetTime = pL.positions.size() * 0.02;
			
			double maxVel = MAX_VEL;
			double minVel = 1;
			
			//TODO: Have Steven explain how this works
			for(int i = 0; i<10; i++) {
				double testVel = (maxVel + minVel) / 2;
				
				pR.buildPath(ACCEL, testVel, distR, endPosR, endVelR, endVel);
				
				double time = pR.positions.size() * 0.02;
				if(time == targetTime) break;
				if(time < targetTime) maxVel = testVel;
				else minVel = testVel;
				
			}
		} else {
			pR.buildPath(ACCEL, MAX_VEL, distR, endPosR, endVelR, endVel);
			double targetTime = pR.positions.size() * 0.02;
			
			double maxVel = MAX_VEL;
			double minVel = 1;
			
			for(int i = 0; i<10; i++) {
				double testVel = (maxVel + minVel) / 2;
				
				pL.buildPath(ACCEL, testVel, distL, endPosL, endVelL, endVel);
				
				double time = pL.positions.size() * 0.02;
				if(time == targetTime) break;
				if(time < targetTime) maxVel = testVel;
				else minVel = testVel;
				
			}
		}
		
		
 	}
	
	public static void main(String[] args) {
		//

		ArrayList<Path> pathsL = new ArrayList<>();
		ArrayList<Path> pathsR = new ArrayList<>();

		//straight section
		Path pathL = new Path();
		Path pathR = new Path();
		pathL.buildPath(ACCEL, MAX_VEL, 160, 0, 0, 70);//set for drive straight auton
		pathR.buildPath(ACCEL, MAX_VEL, 160, 0, 0, 70);
		pathsL.add(pathL);
		pathsR.add(pathR);
		
		int lastIdxL = pathL.positions.size()-1;
		endPosL = pathL.positions.get(lastIdxL);
		endVelL = pathL.velocities.get(lastIdxL);
		int lastIdxR = pathR.positions.size()-1;
		endPosR = pathR.positions.get(lastIdxR);
		endVelR = pathR.velocities.get(lastIdxR);
		
		if(pathL.positions.size() != pathR.positions.size()) System.out.println("ERROR: BAD THINGS HAVE HAPPENED");
		System.out.println("PathL = buildPath(" + pathL.accelMax + "," + pathL.velocMax + "," + pathL.dist + "," + pathL.startPos + "," + pathL.startVel + "," + pathL.endVel + ")");
		System.out.println("PathR = buildPath(" + pathR.accelMax + "," + pathR.velocMax + "," + pathR.dist + "," + pathR.startPos + "," + pathR.startVel + "," + pathR.endVel + ")");
		
		//turn section
		pathL = new Path();
		pathR = new Path();
		//pathL.buildPath(500, 80, 124, lastPosL, 70, 80);
		//pathR.buildPath(500, 64.5, 100, lastPosR, 70, 80);
		buildTurn(pathL, pathR, 124, 105.5, MAX_VEL);
		
		pathsL.add(pathL);
		pathsR.add(pathR);

		lastIdxL = pathL.positions.size()-1;
		endPosL = pathL.positions.get(lastIdxL);
		endVelL = pathL.velocities.get(lastIdxL);
		lastIdxR = pathR.positions.size()-1;
		endPosR = pathR.positions.get(lastIdxR);
		endVelR = pathR.velocities.get(lastIdxR);
		
		if(pathL.positions.size() != pathR.positions.size()) System.out.println("ERROR: BAD THINGS HAVE HAPPENED");
		System.out.println("PathL = buildPath(" + pathL.accelMax + "," + pathL.velocMax + "," + pathL.dist + "," + pathL.startPos + "," + pathL.startVel + "," + pathL.endVel + ")");
		System.out.println("PathR = buildPath(" + pathR.accelMax + "," + pathR.velocMax + "," + pathR.dist + "," + pathR.startPos + "," + pathR.startVel + "," + pathR.endVel + ")");
		
		//straight section
		pathL = new Path();
		pathR = new Path();
		pathL.buildPath(ACCEL, MAX_VEL, 70, endPosL, endVelL, 40);
		pathR.buildPath(ACCEL, MAX_VEL, 70, endPosR, endVelR, 40);
		pathsL.add(pathL);
		pathsR.add(pathR);
		
		lastIdxL = pathL.positions.size()-1;
		endPosL = pathL.positions.get(lastIdxL);
		endVelL = pathL.velocities.get(lastIdxL);
		lastIdxR = pathR.positions.size()-1;
		endPosR = pathR.positions.get(lastIdxR);
		endVelR = pathR.velocities.get(lastIdxR);
		
		if(pathL.positions.size() != pathR.positions.size()) System.out.println("ERROR: BAD THINGS HAVE HAPPENED");
		System.out.println("PathL = buildPath(" + pathL.accelMax + "," + pathL.velocMax + "," + pathL.dist + "," + pathL.startPos + "," + pathL.startVel + "," + pathL.endVel + ")");
		System.out.println("PathR = buildPath(" + pathR.accelMax + "," + pathR.velocMax + "," + pathR.dist + "," + pathR.startPos + "," + pathR.startVel + "," + pathR.endVel + ")");
		
		//turn section
		pathL = new Path();
		pathR = new Path();
		//pathL.buildPath(500, 63.5, 100, lastPosL, 40, 0);
		//pathR.buildPath(500, 80, 124, lastPosR, 40, 0);
		buildTurn(pathL, pathR, 105, 124, 0);
		pathsL.add(pathL);
		pathsR.add(pathR);

		if(pathL.positions.size() != pathR.positions.size()) System.out.println("ERROR: BAD THINGS HAVE HAPPENED");
		System.out.println("PathL = buildPath(" + pathL.accelMax + "," + pathL.velocMax + "," + pathL.dist + "," + pathL.startPos + "," + pathL.startVel + "," + pathL.endVel + ")");
		System.out.println("PathR = buildPath(" + pathR.accelMax + "," + pathR.velocMax + "," + pathR.dist + "," + pathR.startPos + "," + pathR.startVel + "," + pathR.endVel + ")");
		
		
		
		ArrayList<Double> x = new ArrayList<>();
		ArrayList<Double> y = new ArrayList<>();
		ArrayList<Double> theta = new ArrayList<>();

		// start of robot
		x.add(29.0);
		y.add(0.0);
		theta.add(Math.PI / 2);

		double prevPosL = 0;
		double prevPosR = 0;

		
		for (int i = 0; i < pathsL.size() && i < pathsR.size(); i++) {
			//
			for (int j = 0; j < pathsL.get(i).positions.size() && j < pathsR.get(i).positions.size(); j++) {
				// find where we were
				double prevX = x.get(x.size() - 1);
				double prevY = y.get(y.size() - 1);
				double prevTheta = theta.get(theta.size() - 1);

				double deltaL = pathsL.get(i).positions.get(j) - prevPosL;
				double deltaR = pathsR.get(i).positions.get(j) - prevPosR;

				prevPosL = pathsL.get(i).positions.get(j);
				prevPosR = pathsR.get(i).positions.get(j);

				// find deltaTheta

				double deltaTheta = Math.atan2(deltaR - deltaL, ROBOT_WIDTH / 2);
				double averageTheta = prevTheta + deltaTheta / 2;
				
				
				theta.add(prevTheta + deltaTheta);
				
				//find delta x and y
				
				double deltaX = (deltaL + deltaR)/2 * Math.cos(averageTheta);
				double deltaY = (deltaL + deltaR)/2 * Math.sin(averageTheta);
				
				x.add(prevX+deltaX);
				y.add(prevY+deltaY);
				
				//System.out.println((prevX+deltaX)+","+(prevY+deltaY));
			}
		}
		
		for(int i=0; i<x.size(); i++) {
			System.out.println(x.get(i) + "\t" + y.get(i) + "\t" + theta.get(i));
		}
	}
}
*/