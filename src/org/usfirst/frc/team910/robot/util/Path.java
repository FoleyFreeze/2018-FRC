package org.usfirst.frc.team910.robot.util;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Path {
	
	public static boolean print = false;

	public ArrayList<Double> positions; //array of the recorded positions per loop
	public ArrayList<Double> velocities;//array of the recorded velocities per loop
	public ArrayList<Double> accelerations; // array of the recorded accelerations per loop
	public ArrayList<Double> motorPower; //array of the recorded left motor power per loop
	public ArrayList<Double> angles;


	public static final double TOP_VELOC = 60.1; // inches per second, highest capable velocity of robot
	public static final double ACCEL = 60; // inches per second squared
	public static final double DT = 0.02; // 10 milliseconds
	
	//tolerance constants
	public static final double POS_TOL = 0.01;
	public static final double VEL_TOL = 0.01;

	public Path() { //constructor to create the arrays
		positions = new ArrayList<Double>();
		velocities = new ArrayList<Double>();
		accelerations = new ArrayList<Double>();
		motorPower = new ArrayList<Double>();

		
	}
	
	/**
	 * Builds the path for motion profiling 
	 *
	 * @param accelMax is the max acceleration
	 * @param velocMax is the max velocity
	 * @param dist is the distance you want to go in inches
	 * @param startPos where we start a path
	 * @param startVel velocity we start with at start of path
	 * @param endVel velocity we end with at end of path
	 */
	double accelMax;
	double velocMax;
	double dist;
	double startPos;
	double startVel;
	double endVel;
	
	public void buildPath(double accelMax, double velocMax, double dist, double startPos, double startVel, double endVel ) { // distance as an input, total path distance

		this.accelMax = accelMax;
		this.velocMax = velocMax;
		this.dist = dist;
		this.startPos = startPos;
		this.startVel = startVel;
		this.endVel = endVel;
		
		positions = new ArrayList<Double>();
		velocities = new ArrayList<Double>();
		accelerations = new ArrayList<Double>();
		motorPower = new ArrayList<Double>();
		
		double velocity = startVel; //initial velocity
		double position = startPos; //initial position
		 
		double endPosition = dist + startPos;
		
		double step1Accel = accelMax; 
		if(velocity > velocMax) {
			step1Accel = -accelMax; 
		}
		
		double step3Accel = -accelMax; 
		if(endVel > velocMax) {
			step3Accel = accelMax; 
		}
		
		double step3dist = (endVel * endVel - velocity * velocity) / (2 * step3Accel); 
		if(step3dist < 0) step3dist = 0;
		
		//Step 1: Go from start velocity to cruise speed
		while (velocity != velocMax && endPosition - position > step3dist) { //Step 1: Accelerate, while under the top speed
			position = position + velocity * DT + 0.5 * step1Accel * DT * DT; //distance formula, x = xi + vi*t + .5*a*t^2
			velocity = velocity + step1Accel * DT; // velocity formula, vf = vi+at
			step3dist = (endVel * endVel - velocity * velocity) / (2 * step3Accel); 
			if(step3dist < 0) step3dist = 0;
			double tempAccel = step1Accel;
			
			//check for step 1 to done transition
			if(position > endPosition) {
				//don't change position or velocity, let it overshoot slightly as it is still accelerating
				
			}
			//check if this is the last dt before we trasition into step 2
			else if((velocity > velocMax)^(step1Accel<0)) { //accel is set to positive or negative based on velocity being greater than velocMax 
				//at what time does velocity reach top_veloc?
				double prevVel = 0; //last recorded velocity
				if(velocities.size() == 0) prevVel = startVel; 
				else prevVel = velocities.get(velocities.size() -1); //last record velocity
				double transTime = (velocMax - prevVel)/step1Accel;
				//at that time, what is the position?
				double prevPos = 0;
				if(positions.size() == 0)prevPos = startPos;
				else prevPos = positions.get(positions.size()- 1);
				double transPos = prevPos + prevVel*transTime + 0.5 * step1Accel * transTime * transTime;
				//holding top_veloc for the remaining time, what is the total position at the end?
				position = transPos + (velocMax*(DT-transTime));
				//what is the velocity at the end?
				velocity = velocMax;
				tempAccel = (velocity - prevVel) / DT;
			} else if(endPosition - position < step3dist) { //checking for transition from step 1 to step 3
				double prevVel = velocities.get(velocities.size() -1);
				double prevPos = positions.get(positions.size()- 1);
				double vMax = velocity;
				double vMin =  prevVel;
				//binary search for transition velocity
				double testVel = 0;
				double transTime = 0;
				double transPos = 0;
				for(int i=0; i<10; i++) {
					testVel = (vMax + vMin) / 2;
					transTime = (testVel - prevVel) / step1Accel;
					step3dist = (endVel * endVel - testVel * testVel) / (2 * step3Accel);
					transPos = prevPos + prevVel * transTime + 0.5 * step1Accel * (transTime * transTime);
					double distError = transPos + step3dist - endPosition;
					
					if(distError >= 0 && distError <= POS_TOL) break;
					else if(distError > 0) vMax = testVel;
					else vMin = testVel;
				}
				velocity = testVel + step3Accel * (DT - transTime);
				position = transPos + testVel * (DT - transTime) + 0.5 * step3Accel * (DT - transTime) * (DT - transTime);
				tempAccel = (velocity - prevVel) / DT;
				
				/* HUGE MATH PROBLEM THAT BREAKS THINGS IN THIS TRANSITION LOGIC DONT USE THIS
				double prevVel = velocities.get(velocities.size() -1); 
				step3dist = (endVel * endVel - prevVel * prevVel) / (2 * step3Accel);
				double positionOvershoot = position - (endPosition - step3dist);
				double transVel = Math.sqrt(2 * step1Accel * positionOvershoot + prevVel * prevVel);
				double transTime = (transVel - prevVel)/ step1Accel;
				velocity = transVel + step3Accel * (DT - transTime);
				position = endPosition - step3dist + transVel * (DT - transTime) + 0.5 * step3Accel * (DT - transTime * transTime);
				tempAccel = (velocity - prevVel) / DT;
				*/
			}
			
			//System.out.format("X:%.3f V:%.3f\n", position,velocity);
			positions.add(position); //record position
			velocities.add(velocity); //record velocity
			accelerations.add(tempAccel); //record acceleration
			
			if(print) System.out.format("P: %.3f  V: %.2f  A: %.0f\n", position, velocity, tempAccel);
		}
		
		step3dist = (endVel * endVel - velocity * velocity) / (2 * step3Accel); 
		if(step3dist < 0) step3dist = 0;
	
		while (endPosition - position > step3dist && (position - startPos) < dist) { //Step 2: Constant Velocity, while position is less than total path - step1
			position = position + velocMax * DT; //distance formula w/ constant velocity
			double tempAccel = 0;
			
			//if the time step passed the point to slow down, change so that it is slowing down
			
			if(position - startPos >= dist) {
				//velocity = endVel;
				//transition to done, dont change position, vel, or accel.
			}
			else if (endPosition - position < step3dist) { 
				double prevVel = velocities.get(velocities.size()-1);
				//position at which you start slowing down
				double prevPos = positions.get(positions.size()-1);
				//distance covered before slowing down;
				double transDist = (dist - step3dist) - (prevPos - startPos);
				//time that we should begin slowing down
				double transTime = transDist/velocMax;  
				//part from previous position to the decel point
				//double transPos = prevPos+TOP_VELOC*transTime;
				double transPos = dist - step3dist + startPos;
				position = transPos + velocMax*(DT-transTime) + 0.5 * step3Accel * (DT - transTime) * (DT - transTime);
				velocity = velocMax + step3Accel * (DT-transTime);
				tempAccel = (velocity - prevVel) / DT;
			}

			//System.out.format("X:%.3f V:%.3f\n", position,velocity);
			positions.add(position); //record position
			velocities.add(velocity); //record velocity
			accelerations.add(tempAccel); //record acceleration

			if(print) System.out.format("P: %.3f  V: %.2f  A: %.0f\n", position, velocity, tempAccel);
		}
		

		while (position < dist + startPos + POS_TOL && velocity != 0) { //Step 3: Decelerate, while position is less than the total path
			position = position + velocity * DT + 0.5 * step3Accel * DT * DT; //distance formula with negative acceleration 
			velocity = velocity + step3Accel * DT;//velocity formula with negative acceleration
			double tempAccel = step3Accel;
			
			//if the time step went past end point, stop at the end point
			//or if we start going backwards
			if (position - startPos >= dist - POS_TOL || velocity <= 0) {
				//position = dist + startPos;
				//velocity = endVel;
				//tempAccel = 0;
				if(velocity < 0) {
					velocity = 0;
					tempAccel = (velocity - velocities.get(velocities.size()-1)) / DT;
				}
			}
			
			//System.out.format("X:%.3f V:%.3f\n", position,velocity);
			positions.add(position); //record position
			velocities.add(velocity); //record velocity
			accelerations.add(tempAccel); //record accelerations
			
			if(print) System.out.format("P: %.3f  V: %.2f  A: %.0f\n", position, velocity, tempAccel);
		} 

	}

	public static final double SCRUB_FACTOR = 2.4;
	public static final double TURNING_WIDTH = 23.2375 * SCRUB_FACTOR;

	public void calcAngles(Path l, Path r, double startAngle){
		angles = new ArrayList<>();  
		double lastL = l.startPos;
		double lastR = r.startPos;
		double lastT = startAngle;

		for(int i=0; i<l.positions.size(); i++) {
			double deltaL = l.positions.get(i) - lastL;
			double deltaR = r.positions.get(i) - lastR;

			double deltaTheta = Math.atan2(deltaR - deltaL, TURNING_WIDTH / 2) * 180 / Math.PI;
			//double averageTheta = lastT + deltaTheta / 2;

			double cT = lastT + deltaTheta;
			angles.add(cT);
			
			lastL = l.positions.get(i);
			lastR = r.positions.get(i);
			lastT = cT;

			if(print) System.out.println(cT);
		}
	  
	}

	
	/*
	static boolean prevRecord = false;
	static double prevPosL = 0;
	static double prevPosR = 0;
	static double prevVelL = 0;
	static double prevVelR = 0;
	static Path recPathL;
	static Path recPathR;
	
	public static void recordPath(boolean record) {
		//record the following - position , velocity, acceleration, motor powers
		//rising edge to record a new path
		if(record && !prevRecord) {
			recPathL = new Path();
			recPathR = new Path();
		}
		
		double positionL = sense.leftDist;
		double positionR = sense.rightDist;
		
		double velocityL = (sense.leftDist - prevPosL) / DT;
		double velocityR = (sense.rightDist - prevPosR) / DT;
		
		double accelL = (velocityL - prevVelL) / DT;
		double accelR = (velocityR - prevVelR) / DT;
		
		prevPosL = positionL;
		prevPosR = positionR;
		prevVelL = velocityL;
		prevVelR = velocityR;
		
		if(record) {
			recPathL.positions.add(positionL);
			recPathR.positions.add(positionR);
			
			recPathL.velocities.add(velocityL);
			recPathR.velocities.add(velocityR);
			
			recPathL.accelerations.add(accelL);
			recPathR.accelerations.add(accelR);
		}
		
		if(!record && prevRecord) {
			//ctrl + s;
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/home/lvuser/testPath.txt")));
				
				for(int i=0;i<recPathL.positions.size(); i++) {
					bw.write(recPathL.positions.get(i).toString());
					bw.newLine();
					bw.write(recPathR.positions.get(i).toString());
					bw.newLine();
					bw.write(recPathL.velocities.get(i).toString());
					bw.newLine();
					bw.write(recPathR.velocities.get(i).toString());
					bw.newLine();
					bw.write(recPathL.accelerations.get(i).toString());
					bw.newLine();
					bw.write(recPathR.accelerations.get(i).toString());
					bw.newLine();
					bw.write(recPathL.motorPower.get(i).toString());
					bw.newLine();
					bw.write(recPathR.motorPower.get(i).toString());
					bw.newLine();
				}
				//our auton is a toilet
				bw.flush();
				bw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	*/

}
