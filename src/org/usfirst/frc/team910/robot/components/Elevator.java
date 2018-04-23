package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Component {
	public static final double ARM_KP = 0.05; //power per deg
	public static final double LIFT_KP = 0.07; // 0.2 4 mtr chg 3-29  //power per inch
	public static final double ARM_KD = 0.05;//0.2
	public static final double LIFT_KD = 0.5;//0.5 //1.0  4 mtr chg 3-29
	public static final double LIFT_KI = 0.005;//power per in per 20ms
	public static final double LIFT_I_MAX = 0.2;
	public static final double LIFT_I_DEADBAND = 0.25;
	
	public static final double LIFT_FF_PWR = 0.05;//was .185 //seems reasonable (about 18amps over 4 motors)
	
	public static final double LIFT_DEADBAND = 0;
	public static final double ARM_DEADBAND = 0;
	
	public static final double ARM_UP_PWR = 0.8;//was 0.9
	public static final double ARM_UP_GTHR_PWR = 0.8;
	public static final double ARM_DN_PWR = 0.8;
	public static final double ARM_UP_PWR_SHIFT = 0.8;
	public static final double ARM_DN_PWR_SHIFT = 0.5;
	public static final double LIFT_UP_PWR = 0.85;//was 0.6 ON PRAC  //was .5 //was 0.75
	public static final double LIFT_DN_PWR = 0.45;//was .45 //was .3 //was 0.5; too hard!
	public static final double LIFT_UP_PWR_SHIFT = 0.4;
	public static final double LIFT_DN_PWR_SHIFT = 0.25;
	public static final double LIFT_CLIMB_PWR = 0.8;
	public static final double LIFT_CLIMB_PWR_SHIFT = 0.6;
	public static final double LIFT_CLIMB_PWR_HOLD = 0.0;
	
	//TODO figure this out properly
	public static final double[] ARM_AXIS_MIN_HIGH = {-150, -120, -119.9, -108, 108, 106, 165, 277};
	public static final double[] LIFT_TABLE_MIN_HIGH = {28,   10,      9,    0,   0,  32,  67,  70};
	
	public static final double[] ARM_AXIS_MIN_LOW= {-150, -120, -119.9, -108, 108, 141};
	public static final double[] LIFT_TABLE_MIN_LOW= {28,   10,      9,    0,   0,  21};
	
	public static final double[] ARM_AXIS_MAX_HIGH=  {-150, -90, -55, -21, 0, 10, 26, 46, 277};
	public static final double[] LIFT_TABLE_MAX_HIGH= { 28,  27,  24,   8, 3,  6, 41, 70,  70};
	
	public static final double[] ARM_AXIS_MAX_LOW= {-150, -90, -55, -21, 0, 10, 26, 46, 105.9, 106, 141};
	public static final double[] LIFT_TABLE_MAX_LOW= {28,  27,  24,   8, 3,  6, 41, 70,    70,  32,  21};
	
	public static final double[] LIFT_AXIS_MIN_FRONT= {   0, 5.9,  6, 41, 70};
	public static final double[] ARM_TABLE_MIN_FRONT= {-105,-118, 10, 26, 46};
	
	//public static final double[] LIFT_AXIS_MAX_FRONT= {  0, 21, 32, 67, 69};
	//public static final double[] ARM_TABLE_MAX_FRONT= {105,141,106,165,310};
	public static final double[] LIFT_AXIS_MAX_FRONT= {  0, 21, 26, 42, 67, 67.5, 69};
	//public static final double[] ARM_TABLE_MAX_FRONT= {105,141,30,  45,165,310};
	public static final double[] ARM_TABLE_MAX_FRONT= {105,141,80,  80,165, 310, 310}; //changed to stop shaking cube
	
	public static final double[] LIFT_AXIS_MIN_REAR= {   0,   9,  10,  28, 28.1, 70};
	public static final double[] ARM_TABLE_MIN_REAR= {-105,-124,-120,-150,   26, 46};
	
	public static final double[] LIFT_AXIS_MAX_REAR = { 0, 7.9,   8,  24, 27, 32, 67, 69};
	public static final double[] ARM_TABLE_MAX_REAR= {105, 118, -21, -55,-90,106,165,310};
	
	public static final double ARM_AXIS_SWITCH=0;
	public static final double LIFT_AXIS_SWITCH=32;
	
	public static final double[] FEED_FORWARD_AXIS= {0,0,0,0};
	public static final double[] FEED_FORWARD_TABLE= {0,0,0,0};
	
	public static final double LIFT_MAX = 69; //was 70.5
	public static final double LIFT_SCALE_MIN = 64;
	public static final double LIFT_SCALE_LOW_MIN = 50;
	public static final double LIFT_SWITCH_MIN = 4;  //MUST be less than LIFT_SWITCH !!!
	public static final double LIFT_EXCHANGE_MIN = 2;
	public static final double LIFT_MIN = -1; //this is not a temp value, it is supposed to be -1
	//public static final double ARM_REAR = -1;
	//public static final double ARM_FRONT = -1;
	public static final double ARM_REST_MAX = 35;
	public static final double ARM_REST_MID = 0;
	public static final double ARM_REST_MIN = -35;
	public static final double R_SCALE_ARM_MIN = 180;
	
	public static final double LIFT_SCALE = 69;//was 70
	public static final double LIFT_SCALE_LOW = 62;
	public static final double LIFT_SWITCH = 26;//was 24 MSC
	public static final double LIFT_SWITCH_GATHER = 7;//was 6
	public static final double LIFT_EXCHANGE = 3;
	public static final double LIFT_FLOOR = 0.5;
	public static final double LIFT_REST = LIFT_FLOOR;
	public static final double ARM_SCALE = 90;
	public static final double ARM_SCALE_LOW = 110;
	public static final double ARM_SWITCH = 95;//was 90
	public static final double ARM_SWITCH_GATHER = 90;
	public static final double ARM_EXCHANGE = 95;
	public static final double ARM_FLOOR = 104; //104 from prototype testing
	public static final double ARM_FLOOR_SHIFT = 90;
	public static final double F_ARM_REST = 0;
	public static final double R_ARM_REST = 0;
	public static final double ARM_CLIMB = 45; 
	public static final double ARM_CLIMB_DEPLOY = 15;
	public static final double LIFT_CLIMB = 58; 
	public static final double LIFT_AUTON_SCALE = 57;
	public static final double ARM_AUTON_SCALE = 35;
	
	//CLIMB POSITIONS
	public static final double ARM_PRECLIMB_1 = 170;
	public static final double LIFT_PRECLIMB_1 = 30;
	public static final double ARM_PRECLIMB_2 = 170;
	public static final double LIFT_PRECLIMB_2 = 15;
	public static final double ARM_PRECLIMB_3 = 45;
	public static final double LIFT_PRECLIMB_3 = 15;
	public static final double ARM_LIFTHOOK = 45;
	public static final double LIFT_LIFTHOOK = 65;
	
	public Elevator() {
		Component.elevate = this;
	}

	public enum liftState {
		F_FLOOR_POSITION, R_FLOOR_POSITION, F_EXCHANGE_POSITION, R_EXCHANGE_POSITION, F_SWITCH_POSITION, R_SWITCH_POSITION, F_SCALE_LOW_POSITION, R_SCALE_LOW_POSITION, F_SCALE_POSITION, R_SCALE_POSITION, REST_POSITION, AUTON_SCALE_POSITION;
	}

	private liftState goalState;
	public liftState currentState = liftState.F_FLOOR_POSITION;
	private boolean flipState;
	
	private boolean climbAllowed = false;
	
	public double armError;
	public double liftError;
	
	public double liftI = 0;
	
	public void run() {
		
		//once you go climb you never go back
		if(in.preClimbLatch) {
			climbElevator();
			updateDerivatives();
			return;
		}
		
		if(in.manualOverride) {
			firstAuto = true;
			
			if (in.scaleButton) {
				out.setElevatorPower(1);
			} else if (in.switchButton) {
				out.setElevatorPower(-1);
				
			} else {
				out.setElevatorPower(0);
			}
			
			if(in.autoGather) {
				out.setArmPower(1);
			} else if (in.liftExchange){
				out.setArmPower(-1);
			} else {
				out.setArmPower(0);
			}
			
		} else {
		
			//PART 1: determine our goal position based on controller input
			goalState = in.elevatorCommand;
			
			//liftflip is the switch on control board
			//when it is in the front position it is on
			if(in.liftFlip) {
				switch (goalState) {
				case F_EXCHANGE_POSITION:
					goalState = liftState.R_EXCHANGE_POSITION;
					
					break;
				case F_FLOOR_POSITION:
					goalState = liftState.R_FLOOR_POSITION;
					
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
			double armEncoder=sense.armPosL;
			
			if (liftEncoder > LIFT_MIN && liftEncoder < LIFT_EXCHANGE_MIN) {
				if(armEncoder >= ARM_REST_MIN && armEncoder <= ARM_REST_MAX) {
					currentState = liftState.REST_POSITION;
				}else {
					currentState = liftState.F_FLOOR_POSITION;
				}
			}
			else if (liftEncoder < LIFT_SWITCH_MIN) {
				currentState = liftState.F_EXCHANGE_POSITION;
			}
			else if(liftEncoder< LIFT_SCALE_LOW_MIN) {
				currentState = liftState.F_SWITCH_POSITION;
			} 
			else if(liftEncoder < LIFT_SCALE_MIN) {
				if(armEncoder > ARM_REST_MID && in.elevatorCommand == liftState.AUTON_SCALE_POSITION) {
					currentState = liftState.AUTON_SCALE_POSITION;
				} else {
					currentState = liftState.F_SCALE_LOW_POSITION;
				}
			}
			else{
				currentState = liftState.F_SCALE_POSITION;
			}
			
			if(armEncoder < ARM_REST_MID) {
				switch (currentState) {
				case F_EXCHANGE_POSITION:
					currentState = liftState.R_EXCHANGE_POSITION;
					
					break;
				case F_FLOOR_POSITION:
					currentState = liftState.R_FLOOR_POSITION;
				
					break;
				case F_SWITCH_POSITION:
					currentState = liftState.R_SWITCH_POSITION;
					break;
				
				default:
					break;
					
				}
			}
			else if(armEncoder >= R_SCALE_ARM_MIN) {
				if(currentState == liftState.F_SCALE_POSITION) {
					currentState = liftState.R_SCALE_POSITION;
				} else if (currentState == liftState.F_SCALE_LOW_POSITION) {
					currentState = liftState.R_SCALE_LOW_POSITION;
				}
			}
			
			//PART 3: figure out where to move to so we get closer to our goal
			switch (currentState) {
			case REST_POSITION:
				
				switch(goalState) {
				case REST_POSITION:
				case F_FLOOR_POSITION:
				case R_FLOOR_POSITION:
				case F_EXCHANGE_POSITION:
				case R_EXCHANGE_POSITION:
				case F_SWITCH_POSITION:
				case R_SWITCH_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case AUTON_SCALE_POSITION:
					setPosition(goalState);
					break;
				
				case R_SCALE_LOW_POSITION:
				case R_SCALE_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
				}
				break;
				
			case F_FLOOR_POSITION:
				
				switch(goalState) {
				case F_FLOOR_POSITION:
				case F_SWITCH_POSITION:
				case R_FLOOR_POSITION:
				case REST_POSITION:
				case F_EXCHANGE_POSITION:
				case AUTON_SCALE_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
					setPosition(goalState);
					break;
					
				case R_EXCHANGE_POSITION:
				case R_SWITCH_POSITION:
					setPosition(liftState.R_FLOOR_POSITION);
					break;
					
				case R_SCALE_POSITION:
				case R_SCALE_LOW_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
				}
				break;
				
			case R_FLOOR_POSITION:
				
				switch(goalState){
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
				case R_SWITCH_POSITION:
				case F_FLOOR_POSITION:
				case REST_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case R_SCALE_LOW_POSITION:
				case F_EXCHANGE_POSITION:
				case AUTON_SCALE_POSITION:
					setPosition(liftState.F_FLOOR_POSITION);
					break;
				}
				break;
	
			case F_EXCHANGE_POSITION:
				
				switch(goalState) {
				case F_EXCHANGE_POSITION:
				case F_FLOOR_POSITION:
				case F_SWITCH_POSITION:
				case REST_POSITION:
				case AUTON_SCALE_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_POSITION:
				case R_SCALE_LOW_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
					
				case R_SWITCH_POSITION:
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
					setPosition(liftState.R_FLOOR_POSITION);
					break;
				}
				
				break;
	
			case R_EXCHANGE_POSITION:
				
				switch(goalState) {
				case R_EXCHANGE_POSITION:
				case R_FLOOR_POSITION:
				case R_SWITCH_POSITION:
				case REST_POSITION:
				case F_FLOOR_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case AUTON_SCALE_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case R_SCALE_LOW_POSITION:
				case F_EXCHANGE_POSITION:
					setPosition(liftState.F_FLOOR_POSITION);
					break;
				}
				break;
				
			case F_SWITCH_POSITION:
				
				switch(goalState) {
				case F_SWITCH_POSITION:
				case F_FLOOR_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case F_EXCHANGE_POSITION:
				case REST_POSITION:
				case AUTON_SCALE_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_POSITION:
				case R_SCALE_LOW_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
					
				case R_SWITCH_POSITION:
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
					setPosition(liftState.REST_POSITION);
					break;
				}
				break;
	
			case R_SWITCH_POSITION:
				
				switch(goalState) {
				case R_SWITCH_POSITION:
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
				case REST_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case R_SCALE_LOW_POSITION:
				case F_FLOOR_POSITION:
				case F_EXCHANGE_POSITION:
				case AUTON_SCALE_POSITION:
					setPosition(liftState.REST_POSITION);
					break;
				}
				
				break;
				
			case AUTON_SCALE_POSITION:
				switch(goalState) {
				case AUTON_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case F_EXCHANGE_POSITION:
				case F_FLOOR_POSITION:
				case REST_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_POSITION:
				case R_SCALE_LOW_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
					
				case R_FLOOR_POSITION:
				case R_SWITCH_POSITION:
				case R_EXCHANGE_POSITION:
					setPosition(liftState.REST_POSITION);
					break;
				}
				break;
	
			case F_SCALE_POSITION:
				
				switch(goalState) {
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case R_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case AUTON_SCALE_POSITION:
				case REST_POSITION:
				case F_FLOOR_POSITION:
				case F_EXCHANGE_POSITION:
					setPosition(goalState);
					break;
					
				case R_SWITCH_POSITION:
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
					setPosition(liftState.REST_POSITION);
					break;
					
				case R_SCALE_LOW_POSITION:
					setPosition(liftState.R_SCALE_POSITION);
					break;
				}
				break;
	
			case R_SCALE_POSITION:
				
				switch(goalState) {
				case R_SCALE_POSITION:
				case F_SCALE_POSITION:
				case R_SCALE_LOW_POSITION:
					setPosition(goalState);
					break;
					
				case AUTON_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case F_EXCHANGE_POSITION:
				case F_FLOOR_POSITION:
				case REST_POSITION:
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
				case R_SWITCH_POSITION:
				case F_SCALE_LOW_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
				}
				break;
				
			case F_SCALE_LOW_POSITION:
				switch(goalState) {
				case F_SWITCH_POSITION:
				case AUTON_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
				case F_SCALE_POSITION:
				case F_EXCHANGE_POSITION:
				case F_FLOOR_POSITION:
				case REST_POSITION:
					setPosition(goalState);
					break;
					
				case R_SCALE_LOW_POSITION:
				case R_SCALE_POSITION:
					setPosition(liftState.F_SCALE_POSITION);
					break;
					
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
				case R_SWITCH_POSITION:
					setPosition(liftState.REST_POSITION);
					break;
				}
				break;
				
			case R_SCALE_LOW_POSITION:
				switch(goalState) {
				case R_SCALE_POSITION:
				case R_SCALE_LOW_POSITION:
					setPosition(goalState);
					break;
					
				case AUTON_SCALE_POSITION:
				case F_SWITCH_POSITION:
				case F_EXCHANGE_POSITION:
				case F_FLOOR_POSITION:
				case REST_POSITION:
				case R_FLOOR_POSITION:
				case R_EXCHANGE_POSITION:
				case R_SWITCH_POSITION:
				case F_SCALE_POSITION:
				case F_SCALE_LOW_POSITION:
					setPosition(liftState.R_SCALE_POSITION);
					break;
				}
				break;
			}
			
			SmartDashboard.putString("Goal Position", goalState.toString());
			SmartDashboard.putString("Current State", currentState.toString());
		}
			
		updateDerivatives();
	}
	
	public void updateDerivatives() {
		prevArmError = sense.armPosL;
		prevLiftError = sense.liftPos;
	}
	
	
	public double prevArmError = 0;
	public double prevLiftError = 0;
	private double prevArmSetpoint = 0;
	private double prevLiftSetpoint = 0;
	private boolean firstAuto = true;
	private void setPosition(liftState position) {
		double targetArm=0;
		double targetLift=0;
		
		switch(position) {
			case REST_POSITION:
				if(in.liftFlip) targetArm = R_ARM_REST;
				else targetArm = F_ARM_REST;
				targetLift = LIFT_REST;
				
				break;
				
			case F_FLOOR_POSITION:
				if(in.shift) targetArm = ARM_FLOOR_SHIFT;
				else targetArm = ARM_FLOOR;
				targetLift = LIFT_FLOOR;
				break;
				
			case F_EXCHANGE_POSITION:
				targetArm = ARM_EXCHANGE;
				targetLift = LIFT_EXCHANGE;
				break;
				
			case F_SWITCH_POSITION:
				//if we are "passing through" point at the sky
				if(goalState != liftState.F_SWITCH_POSITION) {
					targetArm = 45;
				} else {
					targetArm = ARM_SWITCH;
				}
				
				if(in.switchGather) {
					targetLift = LIFT_SWITCH_GATHER;
					targetArm = ARM_SWITCH_GATHER;
				}
				else {
					targetLift = LIFT_SWITCH;
				}
				
				break;
				
			case AUTON_SCALE_POSITION:
				targetArm = ARM_AUTON_SCALE;
				targetLift = LIFT_AUTON_SCALE;
				break;
				
			case F_SCALE_POSITION:
				switch(in.scaleAngle) {
				case 1://low scale
					targetArm = ARM_SCALE + 30;
					targetLift = LIFT_SCALE;
					
					//on the way up to scale, dont allow the arm to be in the low position
					//TODO: maybe make the switch position go higher so we don't have to do this
					if(targetLift - sense.liftPos > 10) {
						targetArm = ARM_SCALE;
					}
					
					break;
					
				case 2://med scale
					targetArm = ARM_SCALE;
					targetLift = LIFT_SCALE;
					break;
					
				case 3://high scale
					targetArm = ARM_SCALE - 30;
					targetLift = LIFT_SCALE;
					break;
					
				default:
					targetArm = ARM_SCALE;
					targetLift = LIFT_SCALE;
					break;
				}
				break;
				
			case R_FLOOR_POSITION:
				if(in.shift) targetArm = -ARM_FLOOR_SHIFT;
				else targetArm = -ARM_FLOOR;
				targetLift = LIFT_FLOOR;
				break;
				
			case R_EXCHANGE_POSITION:
				targetArm = -ARM_EXCHANGE;
				targetLift = LIFT_EXCHANGE;
				break;
				
			case R_SWITCH_POSITION:
				targetArm = -ARM_SWITCH;
				
				if(in.switchGather) targetLift = LIFT_SWITCH_GATHER;
				else targetLift = LIFT_SWITCH;
				break;
				
			case R_SCALE_POSITION:
				
				switch(in.scaleAngle) {
				case 1://low scale
					targetArm = ARM_SCALE + 150;
					targetLift = LIFT_SCALE;
					break;
					
				case 2://med scale
					targetArm = ARM_SCALE + 180;
					targetLift = LIFT_SCALE;
					break;
					
				case 3://high scale
					targetArm = ARM_SCALE + 210;
					targetLift = LIFT_SCALE;
					break;
					
				default:
					targetArm = ARM_SCALE + 180;
					targetLift = LIFT_SCALE;
					break;
				}
				break;
				
			case F_SCALE_LOW_POSITION:
				targetArm = ARM_SCALE_LOW;
				targetLift = LIFT_SCALE_LOW;
				break;
				
			case R_SCALE_LOW_POSITION:
				targetArm = (180 - ARM_SCALE_LOW) + 180;
				targetLift = LIFT_SCALE_LOW;
				break;
		}	
		
		//Setting lift boundaries, by interpolation
		double liftMax; 
		double liftMin;
		if(sense.liftPos>LIFT_AXIS_SWITCH) {
			liftMax=interp(ARM_AXIS_MAX_HIGH, LIFT_TABLE_MAX_HIGH, sense.armPosL);
			liftMin=interp(ARM_AXIS_MIN_HIGH, LIFT_TABLE_MIN_HIGH, sense.armPosL);
		}else {
			liftMax=interp(ARM_AXIS_MAX_LOW, LIFT_TABLE_MAX_LOW, sense.armPosL);
			liftMin=interp(ARM_AXIS_MIN_LOW, LIFT_TABLE_MIN_LOW, sense.armPosL);
		}
		
		double armMax=0;
		double armMin=0;
		//Set arm boundaries based on arm positions, by interpolation
		if(sense.armPosL < ARM_AXIS_SWITCH) {
			armMax = interp(LIFT_AXIS_MAX_REAR, ARM_TABLE_MAX_REAR, sense.liftPos);
			armMin = interp(LIFT_AXIS_MIN_REAR, ARM_TABLE_MIN_REAR, sense.liftPos);
		}else {
			armMax = interp(LIFT_AXIS_MAX_FRONT, ARM_TABLE_MAX_FRONT, sense.liftPos);
			armMin = interp(LIFT_AXIS_MIN_FRONT, ARM_TABLE_MIN_FRONT, sense.liftPos);
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
		
		//filter targets
		if(!firstAuto) {
			double weight = 0.1;
			prevArmSetpoint += weight * (targetArm - prevArmSetpoint);
			prevLiftSetpoint += weight * (targetLift - prevLiftSetpoint);
		} else {
			prevArmSetpoint = targetArm;
			prevLiftSetpoint = targetLift;
			firstAuto = false;
		}
		
		SmartDashboard.putString("TargetPos", position.toString());
		SmartDashboard.putNumber("Maximum Arm", armMax);
		SmartDashboard.putNumber("Minimum Arm", armMin);
		SmartDashboard.putNumber("Maximum Lift", liftMax);
		SmartDashboard.putNumber("Minimum Lift", liftMin);
		
		pidElevator(targetArm, targetLift);
	}
	
	private void pidElevator(double arm, double lift) {
		
		//find error
		armError = arm - sense.armPosL;
		liftError = lift - sense.liftPos;
		
		//find error from previous error
		//use "process variable" in order to eliminate derivative kick
		double deltaArmError = -sense.armPosL + prevArmError;
		double deltaLiftError = -sense.liftPos + prevLiftError;
		
		double armFeedFwd = interp(FEED_FORWARD_AXIS, FEED_FORWARD_TABLE, arm);
		
		//integrate integral term when we are outside the deadband
		if(Math.abs(liftError) > LIFT_I_DEADBAND) {
			liftI += LIFT_KI * liftError;
		} else {
			liftI = 0;
		}
		
		
		//limit I term
		if(liftI > LIFT_I_MAX) liftI = LIFT_I_MAX;
		else if(liftI < -LIFT_I_MAX) liftI = -LIFT_I_MAX;
		
		//reset I term when the error changes sign
		if(liftError > 0 && liftI < 0) {
			liftI = 0;
		} else if (liftError < 0 && liftI > 0) {
			liftI = 0;
		}
		
		double armPower = armError * ARM_KP + deltaArmError * ARM_KD + armFeedFwd;
		double liftPower = liftError * LIFT_KP + deltaLiftError * LIFT_KD + liftI;
		
		
		if(Math.abs(armError) < ARM_DEADBAND) {
			//is the arm in a location where positive power is up
			if(Math.abs(sense.armPosL) < 20) {
				armPower = 0;
			} else if(sense.armPosL > 0 && sense.armPosL < 180) {
				armPower = -0.06;
			} else {
				armPower = 0.06;
			}
		}
		if(Math.abs(liftError) < LIFT_DEADBAND) liftPower = 0.15;
		
		
		//calculate how much to limit power depending on if shift is held and if we are going up or down
		double armPwrLim = 0;
		double liftPwrLim = 0;
		if(in.shift) {
			if((armError > 0 && sense.armPosL > 0 && sense.armPosL < 180) ||
					armError < 0 && (sense.armPosL < 0 || sense.armPosL > 180)) {
				armPwrLim = ARM_DN_PWR_SHIFT;
			} else {
				armPwrLim = ARM_UP_PWR_SHIFT;
			}
			
			if(liftError > 0) {
				liftPwrLim = LIFT_UP_PWR_SHIFT;
			} else {
				liftPwrLim = LIFT_DN_PWR_SHIFT;
			}
			
		} else {
			if((armError > 0 && sense.armPosL > 0 && sense.armPosL < 180) ||
					armError < 0 && (sense.armPosL < 0 || sense.armPosL > 180)) {
				armPwrLim = ARM_DN_PWR;
			} else {
				//logic to limit power when lifting the arm after gathering
				/*
				if((currentState == liftState.F_FLOOR_POSITION || currentState == liftState.R_FLOOR_POSITION)
						&& position == liftState.REST_POSITION) {
					armPwrLim = ARM_UP_GTHR_PWR;
				} else {
					armPwrLim = ARM_UP_PWR;
				}
				*/
				armPwrLim = ARM_UP_PWR;
			}
			
			if(liftError > 0) {
				liftPwrLim = LIFT_UP_PWR;
			} else {
				liftPwrLim = LIFT_DN_PWR;
			}
		}
		
		
		//limit powers
		if(armPower > armPwrLim) armPower = armPwrLim;
		else if(armPower < -armPwrLim) armPower = -armPwrLim;
		
		if(liftPower > liftPwrLim) liftPower = liftPwrLim;
		else if(liftPower < -liftPwrLim) liftPower = -liftPwrLim;
		
		//shift operating power now that we have no counterweight
		double shiftedLiftPower = liftPower + LIFT_FF_PWR;
		if(liftPower > 1) shiftedLiftPower = 1;
		
		//if in the rest position, apply no power
		if(lift < LIFT_EXCHANGE_MIN && Math.abs(liftError) < LIFT_EXCHANGE_MIN) {
			shiftedLiftPower = 0;
		}
		
		//drive motors
		out.setArmPower(armPower);
		out.setElevatorPower(shiftedLiftPower);
		
		//hold onto error values
		//use sensor data to eliminate derivative kick
		//these are now set above so they will update even in manual mode and disabled to prevent kick
		//prevArmError = sense.armPosL;
		//prevLiftError = sense.liftPos;
		
		
		SmartDashboard.putNumber("Lift Power", liftPower);
		SmartDashboard.putNumber("Lift Power Lim", liftPwrLim);
		SmartDashboard.putNumber("Arm Power Lim", armPwrLim);
		SmartDashboard.putNumber("Arm Power", armPower);
		SmartDashboard.putNumber("Lift Goal", lift);
		SmartDashboard.putNumber("Arm Goal", arm);
		SmartDashboard.putNumber("ArmD", deltaArmError);
		SmartDashboard.putNumber("LiftD", deltaLiftError);
		SmartDashboard.putNumber("LiftError", liftError);
		SmartDashboard.putNumber("ArmError", armError);
		
	}
	
	public enum ClimbState {
		START, PRE_CLIMB_1, PRE_CLIMB_2, PRE_CLIMB_3, LIFT_HOOK, CLIMB
	}
	public ClimbState cState = ClimbState.START;
	
	private void climbElevator() {
		//out.deployForkServo(in.preClimb);
		
		switch(cState) {
		case START:
			//if(in.preClimb) cState = ClimbState.PRE_CLIMB_1;
			if(in.deployClimb) cState = ClimbState.LIFT_HOOK;
			break;
			
		case PRE_CLIMB_1:
			//only move when button is held down
			if(in.preClimb) {
				pidElevator(ARM_PRECLIMB_1, LIFT_PRECLIMB_1);
				if(Math.abs(armError) < 5 && Math.abs(liftError) < 1.5)
					cState = ClimbState.PRE_CLIMB_2;
			}
			else {
				out.setArmPower(0);
				out.setElevatorPower(0);
			}
			
			if(in.deployClimb) cState = ClimbState.LIFT_HOOK;
			break;
			
		case PRE_CLIMB_2:
			
			if(in.preClimb) {
				pidElevator(ARM_PRECLIMB_2, LIFT_PRECLIMB_2);
				if(Math.abs(armError) < 5 && Math.abs(liftError) < 1.5)
					cState = ClimbState.PRE_CLIMB_3;
			}
			else {
				out.setArmPower(0);
				out.setElevatorPower(0);
			}
			
			if(in.deployClimb) cState = ClimbState.LIFT_HOOK;
			break;
			
		case PRE_CLIMB_3:
			
			if(in.preClimb) {
				pidElevator(ARM_PRECLIMB_3, LIFT_PRECLIMB_3);
				if(Math.abs(armError) < 5 && Math.abs(liftError) < 1.5)
					cState = ClimbState.LIFT_HOOK;
			}
			else {
				out.setArmPower(0);
				out.setElevatorPower(0);
			}
			
			if(in.deployClimb) cState = ClimbState.LIFT_HOOK;
			break;
			
		case LIFT_HOOK:
			
			if(in.deployClimb) {
				pidElevator(ARM_LIFTHOOK, LIFT_LIFTHOOK);
				if(Math.abs(sense.armPosL - ARM_LIFTHOOK) < 5 && Math.abs(sense.liftPos - LIFT_LIFTHOOK) < 1.5)
					cState = ClimbState.CLIMB;
			}
			else {
				out.setArmPower(0);
				out.setElevatorPower(LIFT_CLIMB_PWR_HOLD);
			}
			
			if(in.climb) cState = ClimbState.CLIMB;
			break;
			
		case CLIMB:
			out.setArmPower(0);
			
			if(in.climb) {
				double climbPwr;
				if(in.shift) climbPwr = -LIFT_CLIMB_PWR_SHIFT;
				else climbPwr = -LIFT_CLIMB_PWR;
				
				//when climb pressed, drive to 10in and hysteresis to 20in before resuming
				if(sense.liftPos > 15) {
					out.setElevatorPower(climbPwr);
					climbAllowed = true;
				} else if(sense.liftPos > 10) {
					//once we get here, lock the ratchet
					//out.setRatchetServo(true);
					
					if(climbAllowed) {
						out.setElevatorPower(climbPwr);
					} else {
						out.setElevatorPower(LIFT_CLIMB_PWR_HOLD);
					}
				} else {
					climbAllowed = false;
					out.setElevatorPower(LIFT_CLIMB_PWR_HOLD);
				}
			} else {
				out.setElevatorPower(LIFT_CLIMB_PWR_HOLD);
			}
			
			if(in.deployClimb) cState = ClimbState.LIFT_HOOK;
			
			break;
		}	
		
		SmartDashboard.putString("ClimbState", cState.toString());
		
	}

	public void elevate(double power) {
		//out.setElevatorPower(power);
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
