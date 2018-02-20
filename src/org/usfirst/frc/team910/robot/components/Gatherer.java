package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Gatherer extends Component {
	
	public Gatherer() {

	}

	public void run() {
		if(in.gather) {//if you want to take in a pwr^3
			gather(1,1);//suck in pwr^3
		}else if(in.shoot) {//if you want to shoot out a pwr^3
			gather(-1,-1);//shoot out pwr^3
		} else {
			gather(0,0);
		}
	}

	private void gather(double leftPower, double rightPower) {
		out.setGatherPower(leftPower, rightPower);//set the powers to correctly operate pwr^3
	}
}
