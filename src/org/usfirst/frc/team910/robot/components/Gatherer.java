package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Gatherer extends Component {
	
	public Gatherer() {

	}

	public void run() {
		if(in.gather) {
			gather(0.5,1);
		//}else if(in.gather && in.climb) {
			//gather(1,0.5);}
		}else if(in.shoot) {
			gather(-0.5,-0.5);
		//} else if(in.shift){
			//gather(0.5,-1);
		}	else {
			gather(0,0);
		}
	}

	private void gather(double leftPower, double rightPower) {
		out.setGatherPower(leftPower, rightPower);
	}
}
