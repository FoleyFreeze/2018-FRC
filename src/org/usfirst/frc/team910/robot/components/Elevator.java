package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

public class Elevator extends Component {
	public static final double ARM_KP=0;
	public static final double LIFT_KP=0;
	public static final double ARM_KD=0;
	public static final double LIFT_KD=0;
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
	
	public static final double[] FEED_FORWARD_AXIS= {0,0,0,0};
	public static final double[] FEED_FORWARD_TABLE= {0,0,0,0};
	
	public static final double LIFT_MAX = -1;
	public static final double LIFT_SCALE_MIN = -1;
	public static final double LIFT_SWITCH_MIN = -1;
	public static final double LIFT_EXCHANGE_MIN = -1;
	public static final double LIFT_MIN = -1;
	public static final double ARM_REAR = -1;
	public static final double ARM_FRONT = -1;
	public static final double ARM_REST_MAX = -1;
	public static final double ARM_REST_MID=-1;
	public static final double ARM_REST_MIN=-1;
	public static final double R_SCALE_ARM_MIN=-1;
	
	public static final double LIFT_SCALE=-1;
	public static final double LIFT_SWITCH=-1;
	public static final double LIFT_EXCHANGE=-1;
	public static final double LIFT_FLOOR=-1;
	public static final double LIFT_REST=LIFT_FLOOR;
	public static final double ARM_SCALE=-1;
	public static final double ARM_SWITCH=-1;
	public static final double ARM_EXCHANGE=-1;
	public static final double ARM_FLOOR=-1;
	public static final double ARM_REST=-1;
	
	public Elevator() {

	}
	private enum liftState {
		F_FLOOR_POSITION, R_FLOOR_POSITION, F_EXCHANGE_POSITION, R_EXCHANGE_POSITION, F_SWITCH_POSITION, R_SWITCH_POSITION, F_SCALE_POSITION, R_SCALE_POSITION, REST_POSITION;

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
		else if (in.elevatorHeight == 1) {
			goalState = liftState.F_FLOOR_POSITION;
		}
		//goal to raise elevator to front switch
		else if (in.elevatorHeight == 2) {
			goalState = liftState.F_SWITCH_POSITION;
		}
		//goal to raise elevator to front scale
		else if (in.elevatorHeight == 3) {
			goalState = liftState.F_SCALE_POSITION;
		}else {
			goalState = liftState.REST_POSITION;
		}
		
		//liftflip is the switch on control board
		//when it is in the front position it is on
		if(in.liftFlip) {
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
		if (liftEncoder > LIFT_MIN && liftEncoder < LIFT_EXCHANGE_MIN) {
			if(armEncoder >= ARM_REST_MIN && armEncoder <= ARM_REST_MAX) {
				currentState=liftState.REST_POSITION;
			}else {
				currentState=liftState.F_FLOOR_POSITION;
			}
		}
		else if (liftEncoder < LIFT_SWITCH_MIN) {
			currentState=liftState.F_EXCHANGE_POSITION;
		}
		else if(liftEncoder<LIFT_SCALE_MIN) {
			currentState=liftState.F_SWITCH_POSITION;
		}
		else{
			currentState=liftState.F_SCALE_POSITION;
		}
		
		if(armEncoder<ARM_REST_MID) {
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
		
			default:
				break;
				
			}
		}
		else if(armEncoder>=R_SCALE_ARM_MIN) {
			if(currentState==liftState.F_SCALE_POSITION) {
				currentState=liftState.R_SCALE_POSITION;
			}
		}
		
		//PART 3: figure out where to move to so we get closer to our goal
		switch (currentState) {
		case REST_POSITION:
			if(currentState==goalState) {
				setPosition(goalState);
			}else if(goalState==liftState.F_FLOOR_POSITION || goalState==liftState.R_FLOOR_POSITION) {
				setPosition(goalState);
			}else {
				if(goalState==liftState.F_EXCHANGE_POSITION||goalState==liftState.F_SCALE_POSITION||goalState==liftState.F_SWITCH_POSITION||goalState==liftState.R_SCALE_POSITION) {
					setPosition(liftState.F_FLOOR_POSITION);
				}else {
					setPosition(liftState.R_FLOOR_POSITION);
				}
					
			}
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
	
	private double prevArmError = 0;
	private double prevLiftError = 0;
	private void setPosition(liftState position) {
		double targetArm=0;
		double targetLift=0;
		
		switch(position) {
			case REST_POSITION:
				targetArm=ARM_REST;
				targetLift=LIFT_REST;
				break;
			case F_FLOOR_POSITION:
				targetArm=ARM_FLOOR;
				targetLift=LIFT_FLOOR;
				break;
			case F_EXCHANGE_POSITION:
				targetArm=ARM_EXCHANGE;
				targetLift=LIFT_EXCHANGE;
				break;
			case F_SWITCH_POSITION:
				targetArm=ARM_SWITCH;
				targetLift=LIFT_SWITCH;
				break;
			case F_SCALE_POSITION:
				targetArm=ARM_SCALE;
				targetLift=LIFT_SCALE;
				break;
			case R_FLOOR_POSITION:
				targetArm=ARM_FLOOR;
				targetLift=LIFT_FLOOR;
				break;
			case R_EXCHANGE_POSITION:
				targetArm=ARM_EXCHANGE;
				targetLift=LIFT_EXCHANGE;
				break;
			case R_SWITCH_POSITION:
				targetArm=ARM_SWITCH;
				targetLift=LIFT_SWITCH;
				break;
			case R_SCALE_POSITION:
				targetArm=ARM_SCALE;
				targetLift=LIFT_SCALE;
				break;
		}	
		//Setting lift boundaries, by interpolation
		double liftMax = interp(ARM_AXIS, LIFT_TABLE_MAX, sense.armPos);
		double liftMin = interp(ARM_AXIS, LIFT_TABLE_MIN, sense.armPos);
		
		double armMax=0;
		double armMin=0;
		//Set arm boundaries based on arm positions, by interpolation
		if(sense.armPos < ARM_REST_MID) {
			armMax = interp(LIFT_AXIS, ARM_TABLE_MAX_REAR, sense.liftPos);
			armMin = interp(LIFT_AXIS, ARM_TABLE_MIN_REAR, sense.liftPos);
		}else {
			armMax = interp(LIFT_AXIS, ARM_TABLE_MAX_FRONT, sense.liftPos);
			armMin = interp(LIFT_AXIS, ARM_TABLE_MIN_FRONT, sense.liftPos);
		}
		
		//logic to stay in boundaries
		if(targetArm < armMin) {
			targetArm = armMin;
		} else if(targetArm > armMax) {
			targetArm = armMax;
		}
		if(targetLift < liftMin) {
			targetLift = liftMin;
		} else if(targetLift > liftMax) {
			targetLift = liftMax;
		}
		
		//find error
		double armError = targetArm - sense.armPos;
		double liftError = targetLift - sense.liftPos;
		
		//find error from previous error
		double deltaArmError = armError - prevArmError;
		double deltaLiftError = liftError - prevLiftError;
		
		double armFeedFwd = interp(FEED_FORWARD_AXIS, FEED_FORWARD_TABLE, targetArm);
		
		double armPower = armError * ARM_KP + deltaArmError * ARM_KD + armFeedFwd;
		double liftPower = liftError * LIFT_KP + deltaLiftError * LIFT_KD;
		
		//drive motors
		out.setArmPower(armPower);
		out.setElevatorPower(liftPower);
		//out.setElevatorPosition(elevatorPosition);
		
		//hold onto error values
		prevArmError = armError;
		prevLiftError = liftError;
	}

	public void elevate(double power) {
		out.setElevatorPower(power);
	}
	
	public static double interp(double[] axis, double[] table, double x) {
		//if out of bounds
		if(x <= axis[0]) {
			return table[0];
		}
		//if we are at a point of the axis of the elevator height
		else if(x>=axis[axis.length-1]) {
			//go to corresponding table position
			return table[table.length-1];
		}
		else {
			int index=0;
			//if not on axis point, create points
			for(int i=1; i<axis.length; i++) {
				if(x<axis[i]) {
					index = i;
					break;
				}
			}
			//slope of the arm positions
			double slopeAxis = axis[index] - axis[index-1];
			//slope of the elevator positions
			double slopeTbl = table[index]-table[index-1];
			double indexFraction= (x-axis[index-1])/slopeAxis;
			double y = indexFraction * slopeTbl + table[index-1];
			return y;
		}
	}
}
