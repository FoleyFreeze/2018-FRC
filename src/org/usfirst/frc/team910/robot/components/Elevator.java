package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Elevator extends Component {

	public Elevator() {

	}

	private enum liftState {
		F_FLOOR_POSITION, R_FLOOR_POSITION, F_EXCHANGE_POSITION, R_EXCHANGE_POSITION, F_SWITCH_POSITION, R_SWITCH_POSITION, F_SCALE_POSITION, R_SCALE_POSITION;

	}

	private liftState goalState = liftState.F_FLOOR_POSITION;
	private liftState lift = liftState.F_FLOOR_POSITION;
	private boolean flipState;
	
	
	public void run() {
		/*if (in.elevatorUp) {
			elevate(.45);
		}
		if (in.elevatorDown) {
			elevate(-.45);
		}*/

		if(in.liftExchangeTrigger) {
			goalState = liftState.F_EXCHANGE_POSITION;
		}
		//goal to raise elevator to front exchange
		else if (in.liftToFloorTrigger) {
			goalState = liftState.F_FLOOR_POSITION;
		}
		//goal to raise elevator to front switch
		else if (in.liftToSwitchTrigger) {
			goalState = liftState.F_SWITCH_POSITION;
		}
		//goal to raise elevator to front scale
		else if (in.liftToScaleTrigger) {
			goalState = liftState.F_SCALE_POSITION;
		}
		
		
		//FIXME with the below code the goalState switch would never run
		/*
		if(in.liftFlipTrigger) {
			flipState = !flipState;
		}
		*/
		
		//FIXME replaced this if with if two lines below
		//if(flipState) {
		if(in.liftFlipTrigger) {
			switch (goalState) {
			case F_EXCHANGE_POSITION:
				goalState = liftState.R_EXCHANGE_POSITION;
				
				break;
			case F_FLOOR_POSITION:
				goalState = liftState.R_EXCHANGE_POSITION;
			
				break;
			case F_SWITCH_POSITION:
				goalState = liftState.R_SWITCH_POSITION;
				
				break;
			case F_SCALE_POSITION:
				goalState = liftState.R_SCALE_POSITION;
				
				break;
				
			}
		}
		
		
		switch (lift) {
		case F_FLOOR_POSITION:
			if (lift == goalState) {
				setPosition(goalState);
				
			}else if (goalState == liftState.F_EXCHANGE_POSITION || goalState == liftState.F_SWITCH_POSITION || goalState == liftState.R_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				if (goalState == liftState.R_EXCHANGE_POSITION || goalState == liftState.R_SWITCH_POSITION) {
					setPosition(liftState.R_FLOOR_POSITION);
				}
				else if(goalState == liftState.F_SCALE_POSITION || goalState == liftState.R_SCALE_POSITION){
					setPosition(liftState.F_SWITCH_POSITION);
					
				}
			}
		
			break;
			
		case R_FLOOR_POSITION:
			if (lift == goalState) {
				setPosition(goalState);
			}else if (goalState == liftState.R_EXCHANGE_POSITION || goalState == liftState.R_SWITCH_POSITION || goalState == liftState.F_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_FLOOR_POSITION);
			
			}
		
			break;

		case F_EXCHANGE_POSITION:
			if (lift == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.F_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_FLOOR_POSITION);
			}
			
			
				
			break;

		case R_EXCHANGE_POSITION:
			if (lift == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.R_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.R_FLOOR_POSITION);
			}
			
		
			

			break;
		case F_SWITCH_POSITION:
			if (lift == goalState) {
				setPosition(goalState);
			}else if (goalState == liftState.F_FLOOR_POSITION || goalState == liftState.F_SCALE_POSITION) {
				setPosition(goalState);
			}else { 
				if(goalState == liftState.R_SCALE_POSITION) {
					setPosition(liftState.F_SCALE_POSITION);
				}else {
					setPosition(liftState.F_FLOOR_POSITION);
				}
			}
				
			break;

		case R_SWITCH_POSITION:
			if (lift == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.R_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.R_FLOOR_POSITION);
			}
			
					
			break;

		case F_SCALE_POSITION:
			if (lift == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.R_SCALE_POSITION || goalState == liftState.F_SWITCH_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_SWITCH_POSITION);
			}
					

			break;

		case R_SCALE_POSITION:
			if (lift == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.F_SCALE_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_SCALE_POSITION);
			}
			
				
			break;

		}

	}
	private void setPosition(liftState position) {
		
	}

	public void elevate(double power) {
		out.setElevatorPower(power);
	}
}
