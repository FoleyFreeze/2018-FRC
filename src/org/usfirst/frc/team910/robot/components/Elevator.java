package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Elevator extends Component {
	
	public Elevator(){
		
	}
	
	private enum liftState {
		F_FLOOR_POSITION, R_FLOOR_POSITION, F_SWITCH_POSITION, R_SWITCH_POSITION, F_SCALE_POSITION, R_SCALE_POSITION; 
		
		
	}
	private liftState lift = liftState.F_FLOOR_POSITION;
	public void run() {
		if(in.elevatorUp) {
			elevate(.45);
		} 
		if (in.elevatorDown) {
			elevate(-.45);
		}
	
		switch(lift) {
			case F_FLOOR_POSITION:
				
				break;
			
			case R_FLOOR_POSITION:
				
				break;
			case F_SWITCH_POSITION:
				
				break;
			case R_SWITCH_POSITION:
				
				break;
			case F_SCALE_POSITION:
				
				break;
			case R_SCALE_POSITION:
				
				break;
		}
		
	}
	public void elevate(double power) {
		out.setElevatorPower(power);
	}
}
