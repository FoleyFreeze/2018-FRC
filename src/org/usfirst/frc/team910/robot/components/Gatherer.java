package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.Angle;
import org.usfirst.frc.team910.robot.io.ElectroBach;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gatherer extends Component {

	public enum gatherStateIR {
		INIT, SEARCH, SPIN, SUCK, WAIT, CENTER, STOP
	}

	public enum gatherStateC {
		INIT, SPIN, EJECT, REGATHER, WAIT, CENTER, STOP
	}

	public double stepTimer_C = 0;
	public gatherStateC gatherS_C = gatherStateC.INIT;

	public static final double JAMMED_CURRENT = 30;
	public static final double SEARCH_DIST = 11.5;
	public static final double DIST_TOLERANCE = 1;
	public static final double DIST_FRAME = 6;
	
	public static final double PWR_REV_GATHER = -0.3;
	public static final double PWR_FWD_GATHER = 0.6;

	public double stepTimer_IR = 0;
	public gatherStateIR gatherS_IR = gatherStateIR.INIT;

	public Gatherer() {

	}

	public void run() {
		if (in.cameraLights) {
			run_IR();
		} else {
			run_current();
		}
	}

	public void run_IR() {
		
		double distL = sense.distFL;
		double distR = sense.distFR;
		double distC = sense.distFC;		

		if (in.manualOverride) {
			if (in.gather) {
				if(in.manualGatherThrottle) {
					out.setGatherPower(in.manualGatherLeft, in.manualGatherRight);
				} else {
					gather(0.6, 0.4);
				}
			} else if (in.shoot) {
				if(in.manualGatherThrottle) {
					out.setGatherPower(-in.manualGatherLeft, -in.manualGatherRight);
				} else {
					gather(-0.35, -0.35);
				}
			} else {
				out.setGatherPower(0, 0);
			}
		} 
		
		else if (in.gather) {
			// gather(0.5, 1);
			// gatherS = gatherState.INIT;

			switch (gatherS_IR) {

			// initially, run through
			case INIT:
				gather(0.6, 0.6);
				stepTimer_IR = Timer.getFPGATimestamp() + 1;
				gatherS_IR = gatherStateIR.SEARCH;

				break;

			// looks for cube with infrared sensors and moves towards it
			case SEARCH:
				gather(0.6, 0.6);
				SmartDashboard.putString("GatherIRstate", "search");
				if (distR < SEARCH_DIST && distL < SEARCH_DIST && distC < SEARCH_DIST) {
					gatherS_IR = gatherStateIR.SPIN;
				}
				break;

			// spin cube to correct orientation
			case SPIN:
				double maxDist = Math.max(Math.max(distR, distC), distL);
				double minDist = Math.min(Math.min(distR, distC), distL);

				if (maxDist - minDist < DIST_TOLERANCE && maxDist < DIST_FRAME) { // if cube straight, just suck it in
					gather(PWR_FWD_GATHER, PWR_FWD_GATHER);
					gatherS_IR = gatherStateIR.SUCK;
					stepTimer_IR = Timer.getFPGATimestamp() + .75;
				} else if (distC < distR && distC < distL) { // if corner of cube is in center
					gather(PWR_REV_GATHER, PWR_FWD_GATHER);
					SmartDashboard.putString("GatherIRstate", "rotate");
				} else if (distC < distR && distC > distL) { // if cube more left than right sucked in
					out.setGatherPower(.4, PWR_FWD_GATHER);
					SmartDashboard.putString("GatherIRstate", "more right");
				} else if (distC > distR && distC < distL) { // if cube more right than left sucked in
					out.setGatherPower(PWR_FWD_GATHER, .4);
					SmartDashboard.putString("GatherIRstate", "more left");
				} else {
					gather(PWR_REV_GATHER, PWR_FWD_GATHER); // if nothing else, just gather
				}
				break;

			// gathers the cube
			case SUCK:
				gather(.6, .6);
				SmartDashboard.putString("GatherIRstate", "suck");
				if (Timer.getFPGATimestamp() > stepTimer_IR) {
					gatherS_IR = gatherStateIR.WAIT;
					stepTimer_IR = Timer.getFPGATimestamp() + 1;
				}
				break;

			// when arm comes 70 degrees, gather
			case WAIT:
				gather(0, 0);
				if (Math.abs(sense.armPosL) < 70) {
					gatherS_IR = gatherStateIR.CENTER;
					stepTimer_IR = Timer.getFPGATimestamp() + 0.25;
				} else if (Timer.getFPGATimestamp() > stepTimer_IR) {
					gatherS_IR = gatherStateIR.INIT;
				}
				break;

			// when arm comes 35 degrees from the center gather
			case CENTER:
				gather(0.8, 0.8);
				if (Math.abs(sense.armPosL) < 35) {
					gatherS_IR = gatherStateIR.STOP;
				} else if (Timer.getFPGATimestamp() > stepTimer_IR) {
					gatherS_IR = gatherStateIR.INIT;
				}
				break;

			// stops gatherer
			case STOP:
				gatherS_IR = gatherStateIR.INIT;
				// gather(0,0);
				break;
			}

			// shoot logic
		} else if (in.shoot && in.shift) {
			gather(-0.6, -0.6);
			gatherS_IR = gatherStateIR.INIT;
		} else if (in.shoot) {
			if(elevate.currentState == Elevator.liftState.R_EXCHANGE_POSITION || elevate.currentState == Elevator.liftState.F_EXCHANGE_POSITION)
				gather(-0.5, -0.5);
			else gather(-0.35, -0.35);
			gatherS_IR = gatherStateIR.INIT;

			// if no button pressed
		} else {
			switch (gatherS_IR) {
			case INIT:
			case SEARCH:
			case SPIN:
			case SUCK:
				// if we were not in WAIT state, set the timer as if we were
				stepTimer_IR = Timer.getFPGATimestamp() + 1;
				gatherS_IR = gatherStateIR.WAIT;
			case WAIT:
				gather(0.1, 0.1);
				if (Math.abs(sense.armPosL) < 70) {
					gatherS_IR = gatherStateIR.CENTER;
					stepTimer_IR = Timer.getFPGATimestamp() + 0.25;
				} else if (Timer.getFPGATimestamp() > stepTimer_IR) {
					gatherS_IR = gatherStateIR.STOP;
				}
				break;

			case CENTER:
				gather(0.8, 0.8);
				if (Math.abs(sense.armPosL) < 35) {
					gatherS_IR = gatherStateIR.STOP;
				} else if (Timer.getFPGATimestamp() > stepTimer_IR) {
					gatherS_IR = gatherStateIR.STOP;
				}
				break;

			default:
				gather(0.1, 0.1);
				gatherS_IR = gatherStateIR.STOP;
			}

			// gather(0, 0);
			// gatherS = gatherState.INIT;
		}

	}

	public void run_current() {
		if (in.manualOverride) {
			if (in.gather) {
				if(in.manualGatherThrottle) {
					out.setGatherPower(in.manualGatherLeft, in.manualGatherRight);
				} else {
					gather(0.6, 0.4);
				}
			} else if (in.shoot) {
				if(in.manualGatherThrottle) {
					out.setGatherPower(-in.manualGatherLeft, -in.manualGatherRight);
				} else {
					gather(-0.35, -0.35);
				}
			} else {
				out.setGatherPower(0, 0);
			}
		}

		else if (in.gather) {
			// gather(0.5, 1);
			// gatherS = gatherState.INIT;

			switch (gatherS_C) {
			// initially, run through
			case INIT:
				gather(0.45, 0.55); //added 5%
				stepTimer_C = Timer.getFPGATimestamp() + 1;
				gatherS_C = gatherStateC.SPIN;

				break;
			// take in the cube
			case SPIN:
				gather(0.4, 0.5);
				if (sense.pdp.getCurrent(ElectroBach.LEFT_GATHER_CAN)
						+ sense.pdp.getCurrent(ElectroBach.RIGHT_GATHER_CAN) > JAMMED_CURRENT
						&& Timer.getFPGATimestamp() > stepTimer_C) {

					gatherS_C = gatherStateC.EJECT;
					stepTimer_C = Timer.getFPGATimestamp() + 0.10;
				}
				break;
			// spit out cube briefly if stuck
			case EJECT:
				gather(-.55, .7);
				if (Timer.getFPGATimestamp() > stepTimer_C) {
					gatherS_C = gatherStateC.REGATHER;
					stepTimer_C = Timer.getFPGATimestamp() + 0.5;
				}
				break;
			// retake-in cube, currently not used
			case REGATHER:
				gather(.7, .7);
				if (Timer.getFPGATimestamp() > stepTimer_C) {
					gatherS_C = gatherStateC.WAIT;
					stepTimer_C = Timer.getFPGATimestamp() + 1;
				}
				break;

			case WAIT:
				gather(0, 0);
				if (Math.abs(sense.armPosL) < 70) {
					gatherS_C = gatherStateC.CENTER;
					stepTimer_C = Timer.getFPGATimestamp() + 0.25;
				} else if (Timer.getFPGATimestamp() > stepTimer_C) {
					gatherS_C = gatherStateC.INIT;
				}
				break;

			case CENTER:
				//only center if in floor or rest position
				if(elevate.currentState == Elevator.liftState.F_FLOOR_POSITION || elevate.currentState == Elevator.liftState.R_FLOOR_POSITION
							|| elevate.currentState == Elevator.liftState.REST_POSITION) {
					gather(0.8, 0.8);
				} else {
					gather(0, 0);
				}

				if (Math.abs(sense.armPosL) < 35) {
					gatherS_C = gatherStateC.STOP;
				} else if (Timer.getFPGATimestamp() > stepTimer_C) {
					gatherS_C = gatherStateC.INIT;
				}
				break;

			case STOP:
				gatherS_C = gatherStateC.INIT;
				// gather(0,0);
				break;
			}

			// shoot logic
		} else if (in.shoot && in.shift) {
			gather(-0.6, -0.6);
			gatherS_C = gatherStateC.INIT;
		} else if (in.shoot) {
			gather(-0.35, -0.35);
			gatherS_C = gatherStateC.INIT;

			// if no button pressed
		} else {
			switch (gatherS_C) {
			case INIT:
			case SPIN:
			case EJECT:
			case REGATHER:
				// if we were not in WAIT state, set the timer as if we were
				stepTimer_C = Timer.getFPGATimestamp() + 1;
				gatherS_C = gatherStateC.WAIT;
			case WAIT:
				gather(0, 0);
				if (Math.abs(sense.armPosL) < 70) {
					gatherS_C = gatherStateC.CENTER;
					stepTimer_C = Timer.getFPGATimestamp() + 0.25;
				} else if (Timer.getFPGATimestamp() > stepTimer_C) {
					gatherS_C = gatherStateC.STOP;
				}
				break;

			case CENTER:
				//only center if in floor or rest position
				if(elevate.currentState == Elevator.liftState.F_FLOOR_POSITION || elevate.currentState == Elevator.liftState.R_FLOOR_POSITION
							|| elevate.currentState == Elevator.liftState.REST_POSITION) {
					gather(0.8, 0.8);
				} else {
					gather(0, 0);
				}
				
				if (Math.abs(sense.armPosL) < 35) {
					gatherS_C = gatherStateC.STOP;
				} else if (Timer.getFPGATimestamp() > stepTimer_C) {
					gatherS_C = gatherStateC.STOP;
				}
				break;

			default:
				gather(0.1, 0.1);
				gatherS_C = gatherStateC.STOP;
			}

			// gather(0, 0);
			// gatherS = gatherState.INIT;
		}

	}

	private void gather(double leftPower, double rightPower) {
		double robotAngle = sense.robotAngle.get();
		boolean flipSides = true;

		//based on robot orientation, spin the cube away from the wall
		if (robotAngle > 90 && robotAngle < 180) {
			flipSides = false;
		} else if (robotAngle > 270 && robotAngle < 360) {
			flipSides = false;
		}

		if (in.liftFlip) {
			flipSides = !flipSides;
		}

		if (flipSides) {
			out.setGatherPower(rightPower, leftPower);
		} else {
			out.setGatherPower(leftPower, rightPower);
		}
	}

}
