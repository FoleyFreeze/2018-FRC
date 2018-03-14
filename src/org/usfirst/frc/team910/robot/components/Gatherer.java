package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.ElectroBach;

import edu.wpi.first.wpilibj.Timer;

public class Gatherer extends Component {

	public enum gatherState {
		INIT, SEARCH, SPIN, SUCK, WAIT, CENTER, STOP
	}

	public static final double JAMMED_CURRENT = 40;
	public static final double SEARCH_DIST = 20;
	public static final double DIST_TOLERANCE = 4;
	
	public static final double FAST_SPEED = 0.8;
	public static final double MED_SPEED = 0.6;
	public static final double SLOW_SPEED = 0.4;

	public double stepTimer = 0;
	public gatherState gatherS = gatherState.INIT;

	public Gatherer() {

	}

	public void run() {
		double distL;
		double distR;
		double distC;
		if (in.liftFlip) {
			distL = sense.distRL;
			distR = sense.distRR;
			distC = sense.distRC;
		} else {
			distL = sense.distFL;
			distR = sense.distFR;
			distC = sense.distFC;
		}

		if (in.manualOverride) {
			if (in.gather) {
				gather(in.manualGatherLeft, in.manualGatherRight);
			} else {
				gather(0, 0);
			}
		}

		else if (in.gather) {
			// gather(0.5, 1);
			// gatherS = gatherState.INIT;

			switch (gatherS) {

			// initially, run through
			case INIT:
				gather(MED_SPEED, MED_SPEED);
				stepTimer = Timer.getFPGATimestamp() + 1;
				gatherS = gatherState.SEARCH;

				break;

			// looks for cube with infrared sensors and moves towards it
			case SEARCH:
				gather(MED_SPEED, MED_SPEED);
				if (distR < SEARCH_DIST && distL < SEARCH_DIST && distC < SEARCH_DIST) {
					gatherS = gatherState.SPIN;
				}
				break;
				
			//spin cube to correct orientation	
			case SPIN:
				double maxDist = Math.max(Math.max(distR, distC), distL);
				double minDist = Math.min(Math.min(distR, distC), distL);

				if (maxDist - minDist < DIST_TOLERANCE) {
					gather(FAST_SPEED, FAST_SPEED);
					gatherS = gatherState.SUCK;
					stepTimer = Timer.getFPGATimestamp() + .75;
				} else if (distC < distR && distC < distL) {
					gather(-SLOW_SPEED, FAST_SPEED);
				} else if (distC < distR && distC > distL) {
					gather(SLOW_SPEED, FAST_SPEED);
				} else if (distC > distR && distC < distL) {
					gather(FAST_SPEED, SLOW_SPEED);
				} else {
					gather(-SLOW_SPEED, FAST_SPEED);
				}
				break;
			
			//gathers the cube	
			case SUCK:
				gather(FAST_SPEED, FAST_SPEED);
				if(Timer.getFPGATimestamp() > stepTimer) {
					gatherS = gatherState.WAIT;
					stepTimer = Timer.getFPGATimestamp() + 1;
				}
				break;
			
			//when arm comes 70 degrees, gather 
			case WAIT:
				gather(0.1, 0.1);
				if (Math.abs(sense.armPosL) < 70) {
					gatherS = gatherState.CENTER;
					stepTimer = Timer.getFPGATimestamp() + 0.25;
				} else if (Timer.getFPGATimestamp() > stepTimer) {
					gatherS = gatherState.INIT;
				}
				break;
			
			//when arm comes 35 degrees from the center gather
			case CENTER:
				gather(FAST_SPEED, FAST_SPEED);
				if (Math.abs(sense.armPosL) < 35) {
					gatherS = gatherState.STOP;
				} else if (Timer.getFPGATimestamp() > stepTimer) {
					gatherS = gatherState.INIT;
				}
				break;

			//stops gatherer	
			case STOP:
				gatherS = gatherState.INIT;
				// gather(0,0);
				break;
			}

			// shoot logic
		} else if (in.shoot) {
			gather(-0.6, -0.6);
			gatherS = gatherState.INIT;
		} else if (in.shift && in.shoot) {
			gather(-0.8, -0.8);
			gatherS = gatherState.INIT;

			// if no button pressed
		} else {
			switch (gatherS) {
			case INIT:
			case SEARCH:
			case SPIN:
			case SUCK:
				//if we were not in WAIT state, set the timer as if we were
				stepTimer = Timer.getFPGATimestamp() + 1;
			case WAIT:
				gather(0.1, 0.1);
				if (Math.abs(sense.armPosL) < 70) {
					gatherS = gatherState.CENTER;
					stepTimer = Timer.getFPGATimestamp() + 0.25;
				} else if (Timer.getFPGATimestamp() > stepTimer) {
					gatherS = gatherState.STOP;
				}
				break;

			case CENTER:
				gather(FAST_SPEED, FAST_SPEED);
				if (Math.abs(sense.armPosL) < 35) {
					gatherS = gatherState.STOP;
				} else if (Timer.getFPGATimestamp() > stepTimer) {
					gatherS = gatherState.STOP;
				}
				break;

			default:
				gather(0.1, 0.1);
				gatherS = gatherState.STOP;
			}

			// gather(0, 0);
			// gatherS = gatherState.INIT;
		}

	}

	private void gather(double leftPower, double rightPower) {
		out.setGatherPower(leftPower, rightPower);
	}

}
