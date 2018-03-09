package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.ElectroBach;

import edu.wpi.first.wpilibj.Timer;

public class Gatherer extends Component {

	public enum gatherState {
		INIT, SPIN, EJECT, REGATHER, WAIT, CENTER, STOP
	}

	public static final double JAMMED_CURRENT = 40;
	public double ejectTime = 0;
	public gatherState gatherS = gatherState.INIT;

	public Gatherer() {

	}

	public void run() {

		if (elevate.currentState == Elevator.liftState.F_FLOOR_POSITION || elevate.currentState == Elevator.liftState.R_FLOOR_POSITION) {
			//gather(0.5, 1);
			//gatherS = gatherState.INIT;

			switch (gatherS) {
			// initially, run through
			case INIT:
				gather(0.5, 0.6);
				ejectTime = Timer.getFPGATimestamp() + 1;
				gatherS = gatherState.SPIN;
				
				break;
			// take in the cube
			case SPIN:
				gather(.5, 0.6);
				if (sense.pdp.getCurrent(ElectroBach.LEFT_GATHER_CAN)
						+ sense.pdp.getCurrent(ElectroBach.RIGHT_GATHER_CAN) > JAMMED_CURRENT
						&& Timer.getFPGATimestamp() > ejectTime) {
					
					gatherS = gatherState.EJECT;
					ejectTime = Timer.getFPGATimestamp() + 0.10;
				}
				break;
			// spit out cube briefly if stuck
			case EJECT:
				gather(-.55, .7);
				if (Timer.getFPGATimestamp() > ejectTime) {
					gatherS = gatherState.REGATHER;
					ejectTime = Timer.getFPGATimestamp() + 0.5;
				}
				break;
			// retake-in cube, currently not used
			case REGATHER:
				gather(.7, .7);
				if(Timer.getFPGATimestamp() > ejectTime) {
					gatherS = gatherState.WAIT;
				}
				break;
				
			case WAIT:
				gather(0,0);
				if(Math.abs(sense.armPosL) < 70) {
					gatherS = gatherState.CENTER;
				}
				break;
				
			case CENTER:
				gather(0.8,0.8);
				if(Math.abs(sense.armPosL) < 15) {
					gatherS = gatherState.STOP;
				}
				break;
				
			case STOP:
				gather(0,0);
				break;
			}
		} else if (in.shoot) {
			gather(-0.6, -0.6);
			gatherS = gatherState.INIT;
		} else if (in.shift && in.shoot) {
			gather(-0.8, -0.8);
			gatherS = gatherState.INIT;
		} else {
			gather(0, 0);
			gatherS = gatherState.INIT;
		}

	}

	private void gather(double leftPower, double rightPower) {
		//System.out.println("gather right: " + in.manualGatherRight);
		//System.out.println("in.gather: " + in.gather);
		//if(in.manualOverride && in.gather) out.setGatherPower(in.manualGatherLeft, in.manualGatherRight);
			//System.out.println("gather left: " + in.manualGatherLeft);
			//System.out.println("gather right: " + in.manualGatherRight);
		//}else {
			out.setGatherPower(leftPower, rightPower);
		//}
	}

}
