package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Climber extends Component {

	
	public Climber(){
		Component.climb = this;
	}
	
	public void run() {
		/*
		if(in.climb) {
			climb(0.5,0.5);
		} else {
			climb(0,0);
		}
		if(in.climb && in.shift) {
			climb(1,1); //FIXME set to tested power
		}
		*/
	}
	
	public void climb(double power1, double power2) {
		//out.setClimberPower(power1, power2);
	}
}
