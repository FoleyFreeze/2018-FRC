package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.ElectroBach;

import edu.wpi.first.wpilibj.Timer;

public class Gatherer extends Component {

	public enum gatherState {
		INIT, SPIN, EJECT, REGATHER, WAIT, CENTER, STOP
	}

	public static final double JAMMED_CURRENT = 10; //40
	public double ejectTime = 0;
	public gatherState gatherS = gatherState.INIT;

	public Gatherer() {

	}

	public void run() {
		if(in.manualOverride) {
			if(in.gather) {
				gather(in.manualGatherLeft, in.manualGatherRight);
			}else {
				gather(0,0);
			}
		}
		

		else if (in.gather) {
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
				gather(0.5, 0.6);
				if (sense.pdp.getCurrent(ElectroBach.LEFT_GATHER_CAN)
						+ sense.pdp.getCurrent(ElectroBach.RIGHT_GATHER_CAN) > JAMMED_CURRENT
						&& Timer.getFPGATimestamp() > ejectTime) {
				if(Math.abs(sense.armPosL) < 70) {
					gatherS = gatherState.CENTER;
				}
					
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
					ejectTime = Timer.getFPGATimestamp() + 1;
				}
				break;
				
			case WAIT:
				gather(0.1,0.1); //was 0,0 now pulls 10%
				if(Math.abs(sense.armPosL) < 70) {
					System.out.println("true");
					gatherS = gatherState.CENTER;
					ejectTime = Timer.getFPGATimestamp() + 0.25;
				} else if(Timer.getFPGATimestamp() > ejectTime) {
					gatherS = gatherState.INIT;
				}
				break;
				
			case CENTER:
				gather(0.95,0.95);//was .8
				if(Math.abs(sense.armPosL) < 35) { //was 35
					gatherS = gatherState.STOP;
				} else if (Timer.getFPGATimestamp() > ejectTime) {
					gatherS = gatherState.INIT;
				}
				break;
				
			case STOP:
				gatherS = gatherState.INIT;
				//gather(0,0);
				break;
			}
			
			//shoot logic
		} else if (in.shoot) {
			gather(-0.6, -0.6);
			gatherS = gatherState.INIT;
		} else if (in.shift && in.shoot) {
			gather(-0.8, -0.8);
			gatherS = gatherState.INIT;
			
			//if no button pressed
		} else {
			switch(gatherS) {
			case INIT:
			case SPIN:
			case EJECT:
			case REGATHER:
			case WAIT:
				gather(0,0);
				if(Math.abs(sense.armPosL) < 70) {
					gatherS = gatherState.CENTER;
					ejectTime = Timer.getFPGATimestamp() + 0.25;
				} else if(Timer.getFPGATimestamp() > ejectTime) {
					gatherS = gatherState.STOP;
				}
				break;
				
			case CENTER:
				gather(0.8,0.8);
				if(Math.abs(sense.armPosL) < 35) {
					gatherS = gatherState.STOP;
				} else if(Timer.getFPGATimestamp() > ejectTime) {
					gatherS = gatherState.STOP;
				}
				break;
				
			default:
				gather(0.1,0.1); //was 0,0 now %10 pull
				gatherS = gatherState.STOP;
			}
			
			//gather(0, 0);
			//gatherS = gatherState.INIT;
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
