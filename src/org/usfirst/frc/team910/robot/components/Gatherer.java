package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.Angle;
import org.usfirst.frc.team910.robot.io.ElectroBach;

import edu.wpi.first.wpilibj.Timer;

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
	public static final double SEARCH_DIST = 20;
	public static final double DIST_TOLERANCE = 4;

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
				out.setGatherPower(in.manualGatherLeft, in.manualGatherRight);
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
				if (distR < SEARCH_DIST && distL < SEARCH_DIST && distC < SEARCH_DIST) {
					gatherS_IR = gatherStateIR.SPIN;
				}
				break;

			// spin cube to correct orientation
			case SPIN:
				double maxDist = Math.max(Math.max(distR, distC), distL);
				double minDist = Math.min(Math.min(distR, distC), distL);

				if (maxDist - minDist < DIST_TOLERANCE) { // if cube straight, just suck it in
					gather(.8, .8);
					gatherS_IR = gatherStateIR.SUCK;
					stepTimer_IR = Timer.getFPGATimestamp() + .75;
				} else if (distC < distR && distC < distL) { // if corner of cube is in center
					gather(-.4, .8);
				} else if (distC < distR && distC > distL) { // if cube more left than right sucked in
					out.setGatherPower(.4, .8);
				} else if (distC > distR && distC < distL) { // if cube more right than left sucked in
					out.setGatherPower(.8, .4);
				} else {
					gather(-.4, .8); // if nothing else, just gather
				}
				break;

			// gathers the cube
			case SUCK:
				gather(.8, .8);
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
			gather(-0.35, -0.35);
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
				out.setGatherPower(in.manualGatherLeft, in.manualGatherRight);
			} else if (in.shoot) {
				out.setGatherPower(-in.manualGatherLeft, -in.manualGatherRight);
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
				gather(0.4, 0.5);
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
				gather(0.8, 0.8);
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
				gather(0.8, 0.8);
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
		boolean flipSides = false;

		// if (!in.liftFlip) {
		if (robotAngle > 90 && robotAngle < 180) {
			flipSides = true;
		} else if (robotAngle > 270 && robotAngle < 360) {
			flipSides = true;
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
