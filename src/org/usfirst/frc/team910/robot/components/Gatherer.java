package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.ElectroBach;

import edu.wpi.first.wpilibj.Timer;

public class Gatherer extends Component {

	public enum gatherState {
		INIT, SPIN, EJECT, REGATHER
	}

	public static final double JAMMED_CURRENT = 20;
	public double ejectTime = 0;
	public gatherState gather = gatherState.INIT;

	public Gatherer() {

	}

	public void run() {

		if (in.gather) {
			gather(0.5, 1);
		} else if (in.shoot) {
			gather(-0.6, -0.6);
		} else if (in.shift && in.shoot) {
			gather(-1, -1);
		} else {
			gather(0, 0);
			gather = gatherState.INIT;
		}
		switch (gather) {
		// initially, run through
		case INIT:
			gather(.5, 1);
			ejectTime = Timer.getFPGATimestamp() + .5;
			gather = gatherState.SPIN;
			break;
		// take in the cube
		case SPIN:
			gather(.5, 1);
			if (sense.pdp.getCurrent(ElectroBach.LEFT_GATHER_CAN)
					+ sense.pdp.getCurrent(ElectroBach.RIGHT_GATHER_CAN) > JAMMED_CURRENT
					&& Timer.getFPGATimestamp() > ejectTime) {
				gather = gatherState.EJECT;
				ejectTime = Timer.getFPGATimestamp() + 0.15;
			}
			break;
		// spit out cube briefly if stuck
		case EJECT:
			gather(-.7, .7);
			if (Timer.getFPGATimestamp() > ejectTime) {
				gather = gatherState.INIT;
			}
			break;
		// retake-in cube, currently not used
		case REGATHER:
			gather(.75, .75);
			break;
		}
	}

	private void gather(double leftPower, double rightPower) {
		out.setGatherPower(leftPower, rightPower);
	}

}
