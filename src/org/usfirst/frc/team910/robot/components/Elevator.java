package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Elevator extends Component {
	//TODO figure this out properly
	public static final double[] ARM_AXIS= {0, 0, 0, 0, 0, 0};
	public static final double[] LIFT_TABLE_MIN= {0,0,0,0,0,0};
	public static final double[] LIFT_TABLE_MAX= {0,0,0,0,0,0};
	//TODO ""
	public static final double[] LIFT_AXIS= {0,0,0,0,0,0,0};
	public static final double[] ARM_TABLE_MIN_FRONT= {0,0,0,0,0,0,0};
	public static final double[] ARM_TABLE_MAX_FRONT= {0,0,0,0,0,0,0};
	public static final double[] ARM_TABLE_MIN_REAR= {0,0,0,0,0,0,0};
	public static final double[] ARM_TABLE_MAX_REAR= {0,0,0,0,0,0,0};
	
	public static final double LIFT_MAX = -1;
	public static final double LIFT_SCALE = -1;
	public static final double LIFT_SWITCH = -1;
	public static final double LIFT_EXCHANGE = -1;
	public static final double LIFT_MIN = -1;
	public static final double ARM_REAR = -1;
	public static final double ARM_FRONT = -1;
	public static final double ARM_REST = -1;
	
	public Elevator() {

	}
	private enum liftState {
		F_FLOOR_POSITION, R_FLOOR_POSITION, F_EXCHANGE_POSITION, R_EXCHANGE_POSITION, F_SWITCH_POSITION, R_SWITCH_POSITION, F_SCALE_POSITION, R_SCALE_POSITION;

	}

	private liftState goalState = liftState.F_FLOOR_POSITION;
	private liftState currentState = liftState.F_FLOOR_POSITION;
	private boolean flipState;
	
	
	public void run() {
		/*if (in.elevatorUp) {
			elevate(.45);
		}
		if (in.elevatorDown) {
			elevate(-.45);
		}*/

		//PART 1: determine our goal position based on controller input
		
		if(in.liftExchange) {
			goalState = liftState.F_EXCHANGE_POSITION;
		}
		//goal to raise elevator to front exchange
		else if (in.liftExchange) {
			goalState = liftState.F_FLOOR_POSITION;
		}
		//goal to raise elevator to front switch
		else if (in.liftExchange) {
			goalState = liftState.F_SWITCH_POSITION;
		}
		//goal to raise elevator to front scale
		else if (in.liftExchange) {
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
		if(in.liftExchange) {
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
			default:
				break;
				
			}
		}
		
		
		//PART 2: figure out where we are based on encoder values
		double liftEncoder=sense.liftPos;
		double armEncoder=sense.armPos;
		if (liftEncoder > LIFT_MIN && liftEncoder < LIFT_EXCHANGE) {
			currentState=liftState.F_FLOOR_POSITION;
		}
		else if (liftEncoder < LIFT_SWITCH) {
			currentState=liftState.F_EXCHANGE_POSITION;
		}
		else if(liftEncoder<LIFT_SCALE) {
			currentState=liftState.F_SWITCH_POSITION;
		}
		else{
			currentState=liftState.F_SCALE_POSITION;
		}
		
		if(armEncoder<ARM_REST) {
			switch (currentState) {
			case F_EXCHANGE_POSITION:
				currentState = liftState.R_EXCHANGE_POSITION;
				
				break;
			case F_FLOOR_POSITION:
				currentState = liftState.R_EXCHANGE_POSITION;
			
				break;
			case F_SWITCH_POSITION:
				currentState = liftState.R_SWITCH_POSITION;
				
				break;
			case F_SCALE_POSITION:
				currentState = liftState.R_SCALE_POSITION;
				
				break;
			default:
				break;
				
			}
		}
		
		//PART 3: figure out where to move to so we get closer to our goal
		switch (currentState) {
		case F_FLOOR_POSITION:
			if (currentState == goalState) {
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
			if (currentState == goalState) {
				setPosition(goalState);
			}else if (goalState == liftState.R_EXCHANGE_POSITION || goalState == liftState.R_SWITCH_POSITION || goalState == liftState.F_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_FLOOR_POSITION);
			
			}
		
			break;

		case F_EXCHANGE_POSITION:
			if (currentState == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.F_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_FLOOR_POSITION);
			}
			
			
				
			break;

		case R_EXCHANGE_POSITION:
			if (currentState == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.R_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.R_FLOOR_POSITION);
			}
			
		
			

			break;
		case F_SWITCH_POSITION:
			if (currentState == goalState) {
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
			if (currentState == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.R_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.R_FLOOR_POSITION);
			}
			
					
			break;

		case F_SCALE_POSITION:
			if (currentState == goalState) {
			setPosition(goalState);
			}else if (goalState == liftState.R_SCALE_POSITION || goalState == liftState.F_SWITCH_POSITION) {
				setPosition(goalState);
			}else {
				setPosition(liftState.F_SWITCH_POSITION);
			}
					

			break;

		case R_SCALE_POSITION:
			if (currentState == goalState) {
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
	public double interp(double[] axis, double[] table, double x) {
		if(x <= axis[0]) {
			return table[0];
		}
		else if(x>=axis[axis.length-1]) {
			return table[table.length-1];
		}
		else {
			int index=0;
			for(int i=1; i<axis.length; i++) {
				if(x<axis[i]) {
					index = i;
					break;
				}
			}
			double slopeAxis = axis[index] - axis[index-1];
			double slopeTbl = table[index]-table[index-1];
			double indexFraction= x/slopeAxis;
			double y = indexFraction * slopeTbl+table[index-1];
			return y;
		}
	}
}
