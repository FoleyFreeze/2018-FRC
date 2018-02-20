package org.usfirst.frc.team910.robot.util;

import java.util.ArrayList;

public class Path {

	public ArrayList<Double> positions; //array of the recorded positions per loop
	public ArrayList<Double> velocities;//array of the recorded velocities per loop
	

	public static final double TOP_VELOC = 60.1; // inches per second, highest capable velocity of robot
	public static final double ACCEL = 60; // inches per second squared
	public static final double DT = 0.02; // 10 milliseconds
	
	//tolerance constants
	public static final double POS_TOL = 0.01;
	public static final double VEL_TOL = 0.01;

	public Path() { //constructor to create the arrays
		positions = new ArrayList<Double>();
		velocities = new ArrayList<Double>();
	}
	
	public void buildPath(double accelMax, double velocMax, double velocity, double position, double dist, double startDist) { // distance as an input, total path distance

		
		
		velocMax = Math.sqrt(ACCEL * dist); // highest velocity before you start decelerating
		velocity = 0; //initial velocity
		position = 0; //initial position
		
		if (velocMax > TOP_VELOC) { //if robot can reach its top speed, use 3 step method
			while (velocity < TOP_VELOC) { //Step 1: Accelerate, while under the top speed
				position = position + velocity * DT + 0.5 * ACCEL * DT * DT; //distance formula, x = xi + vi*t + .5*a*t^2
				velocity = velocity + ACCEL * DT; // velocity formula, vf = vi+at

				//check if this is the last dt in this step
				if(velocity > TOP_VELOC) {
					//at what time does velocity reach top_veloc?
					double prevVel = velocities.get(velocities.size() -1); //last record velocity
					double transTime = (TOP_VELOC - prevVel)/ACCEL;
					//at that time, what is the position?
					double prevPos = positions.get(positions.size() -1);
					double transPos = prevPos + prevVel*transTime + 0.5 * ACCEL * transTime * transTime;
					//holding top_veloc for the remaining time, what is the total position at the end?
					position = transPos + (TOP_VELOC*(DT-transTime));
					//what is the velocity at the end?
					velocity = TOP_VELOC;
				}
				
				//System.out.format("X:%.3f V:%.3f\n", position,velocity);
				positions.add(position); //record position
				velocities.add(velocity); //record velocity
			}
			
			double step1Dist = (TOP_VELOC*TOP_VELOC)/(2*ACCEL); //record the position during first phase, 
			while (position < dist - step1Dist) { //Step 2: Constant Velocity, while position is less than total path - step1
				position = position + TOP_VELOC * DT; //distance formula w/ constant velocity
				
				//if the time step passed the point to slow down, change so that it is slowing down
				if (position > (dist - step1Dist)) {
					//position at which you start slowing down
					double prevPos = positions.get(positions.size()-1);
					//distance covered before slowing down;
					double transDist = (dist -step1Dist) - prevPos;
					//time that we should begin slowing down
					double transTime = transDist/TOP_VELOC;
					//part from previous position to the decel point
					//double transPos = prevPos+TOP_VELOC*transTime;
					double transPos = dist - step1Dist;
					position = transPos + TOP_VELOC*(DT-transTime) - 0.5 * ACCEL * (DT - transTime) * (DT - transTime);
					velocity = TOP_VELOC - ACCEL*(DT-transTime);
				}

				//System.out.format("X:%.3f V:%.3f\n", position,velocity);
				positions.add(position); //record position
				velocities.add(velocity); //record velocity

			}
			
			while (position < dist + startDist) { //Step 3: Decelerate, while position is less than the total path
				position = position + velocity * DT - 0.5 * ACCEL * DT * DT; //distance formula with negative acceleration 
				velocity = velocity - ACCEL * DT;//velocity formula with negative acceleration
				if(velocity <= 0) {
					velocity = 0;
				}
				
				//if the time step went past end point, stop at the end point 
				if (position >= dist - POS_TOL && velocity <= 0) {
					position = dist + startDist;
					velocity = 0;
				}
				
				//System.out.format("X:%.3f V:%.3f\n", position,velocity);
				positions.add(position); //record position
				velocities.add(velocity); //record velocity
			}
			
		} else { //if robot will not reach top speed before it needs to slow down, use 2 step method
		
			double step1Dist = (velocMax*velocMax)/(2*ACCEL); //record the position during first phase,
			
			while (velocity < velocMax) { //Step 1: Accelerate, while velocity has not reached highest capable velocity (not robot's top speed)
				position = position + velocity * DT + 0.5 * ACCEL * DT * DT; //distance formula, x = xi + vit + .5at^2
				velocity = velocity + ACCEL * DT; //velocity formula, vf = vi+at
				
				//if the time step passed the point to slow down, change so that it is slowing down
				if (position > (dist - step1Dist) + startDist) {
					//position at which you start slowing down
					double prevPos = positions.get(positions.size()-1);
					//distance covered before slowing down;
					double transDist = (dist -step1Dist) - prevPos;
					//time that we should begin slowing down
					double transTime = transDist/velocMax;
					//part from previous position to the decel point
					//double transPos = prevPos+TOP_VELOC*transTime;
					double transPos = dist - step1Dist;
					position = transPos + velocMax*(DT-transTime) - 0.5 * ACCEL * (DT - transTime) * (DT - transTime);
					velocity = velocMax - ACCEL*(DT-transTime);
					positions.add(position);//record position
					velocities.add(velocity);//record velocity
					break;
				}
				
				//System.out.format("X:%.3f V:%.3f\n", position,velocity);
				positions.add(position);//record position
				velocities.add(velocity);//record velocity
			}
			
			
			while (velocity > 0) { //Step 2: Decelerate, while velocity is greater than zero
				position = position + velocity * DT - 0.5 * ACCEL * DT * DT; //distance formula w/ negative acceleration
				velocity = velocity - ACCEL * DT; //velocity formula with negative acceleration
				
				if(velocity <= 0) {
					velocity = 0;
				}
				
				//if the time step went past end point, stop at the end point 
				if (position >= (dist + startDist) - POS_TOL && velocity <= 0) {
					position = dist + startDist;
					velocity = 0;
				}

				//System.out.format("X:%.3f V:%.3f\n", position,velocity);
				positions.add(position);//record position
				velocities.add(velocity);//record position
			}
		}

	}

}
