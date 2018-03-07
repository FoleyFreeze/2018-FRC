package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.components.Elevator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Inputs extends Component {

	public static final double DEADBAND = 0.1;
	public static double RAMP_LIMIT = .02; //Power per 20 ms
	//public static final double[] ELEVATOR_HEIGHT_AXIS = {  0,    10,   20,   30,   40,    50,     60,    70}; 
	// public static final double[] RAMP_LIMIT_TABLE =     {.02, .0185, .017, .0165, .015, .0135, .0120, .0105};
	
	private Joystick leftStick;
	private Joystick rightStick;
	private Joystick controlBoard;
	
	public boolean enableMP = false;
	/*

	public boolean liftToScaleTrigger = false; //TODO set these based on buttons 
	public boolean liftToSwitchTrigger = false;
	public boolean liftToFloorTrigger = false; 
	public boolean liftFlipTrigger = false; 
	*/
	
	//----------------------------------Driver Functions--------------------------------
	public double leftDrive;
	public double rightDrive;
	public double driveStraightTurn;
	public boolean dynamicBrake = false;
	public boolean driveStraight = false;

	//----------------------------Operator Functions--------------------------------------
	
	public boolean shift = false;
	public boolean manualOverride = false;
	public boolean liftFlip = false;
	public int scaleAngle = 0;
	public double manualHeight = 0;
	public boolean restButton = false;
	public boolean switchButton = false;
	public boolean scaleButton = false;
	public boolean shoot = false;
	public boolean gather = false;
	public boolean autoGather = false;
	public boolean liftExchange = false;
	public boolean cameraLights = false;
	public boolean deployClimb = false;
	public boolean climb = false;
	public boolean modeSwitch = false;
	public boolean autoCube1 = false;
	public boolean autoCube2 = false;
	public boolean autoCube3 = false;
	public boolean resetEnc = false;
	public Elevator.liftState elevatorCommand = Elevator.liftState.REST_POSITION;
	
	
	public int liftHeightMod = 0; //this can be 0, -1, 1 
	

	public Inputs() {
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
		controlBoard = new Joystick(2);
	}
	
	boolean prevLowAngle;
	boolean prevMiddleAngle;
	boolean prevHighAngle;

	public void read() {
		double leftYAxis = -leftStick.getY();
		double rightYAxis = -rightStick.getY();
		
		enableMP = false;
		
		resetEnc = leftStick.getRawButton(12) && rightStick.getRawButton(12);
		
		
		leftDrive = ramp(leftYAxis, leftDrive);
		rightDrive = ramp(rightYAxis, rightDrive);
		driveStraightTurn = rightStick.getX();
		
		
		
		if(Math.abs(leftDrive) < DEADBAND) leftDrive = 0;
		if(Math.abs(rightDrive) < DEADBAND) rightDrive = 0;
		
		dynamicBrake = leftStick.getTrigger();
		driveStraight = rightStick.getTrigger();
		//all elevator heights 
		//goes in order of high to low
		if(controlBoard.getRawButton(19)){
			//scale height
			scaleAngle = 3;
		} else if(controlBoard.getRawButton(16)){
			//floor height
			scaleAngle = 1;
		} else {
			//switch height
			scaleAngle = 2;
		}
		
		//rising edge for angle buttons so you don't have to hold them
		//lowAngle = !prevLowAngle && controlBoard.getRawButton(2);
		//middleAngle = !prevMiddleAngle && controlBoard.getRawButton(3);
		//highAngle = !prevHighAngle && controlBoard.getRawButton(4);
		
		restButton = controlBoard.getRawButton(2);
		switchButton = controlBoard.getRawButton(3);
		scaleButton = controlBoard.getRawButton(4);
		gather = controlBoard.getRawButton(6);
		liftExchange = controlBoard.getRawButton(8);
		
		//use last button pressed to set correct position
		if(restButton) elevatorCommand = Elevator.liftState.REST_POSITION;
		else if(switchButton) elevatorCommand = Elevator.liftState.F_SWITCH_POSITION;
		else if(scaleButton) elevatorCommand = Elevator.liftState.F_SCALE_POSITION;
		else if(liftExchange) elevatorCommand = Elevator.liftState.F_EXCHANGE_POSITION;
		
		//require gather button to be held down to gather
		if(gather) elevatorCommand = Elevator.liftState.F_FLOOR_POSITION;
		else if( elevatorCommand == Elevator.liftState.F_FLOOR_POSITION||
				 elevatorCommand == Elevator.liftState.R_FLOOR_POSITION) {
			elevatorCommand = Elevator.liftState.REST_POSITION;
		}
		
		shift = controlBoard.getRawButton(1);
		manualOverride = controlBoard.getRawButton(14);
		liftFlip = !controlBoard.getRawButton(15);
		shoot = controlBoard.getRawButton(5);
		autoGather = controlBoard.getRawButton(9);
		cameraLights = controlBoard.getRawButton(12);
		deployClimb = controlBoard.getRawButton(7);
		climb = controlBoard.getRawButton(10);
		modeSwitch = controlBoard.getRawButton(13);
		autoCube1 = controlBoard.getRawButton(18);
		autoCube2 = controlBoard.getRawButton(17);
		autoCube3 = controlBoard.getRawButton(20);
		
		manualHeight = (controlBoard.getRawAxis(5) + 1) / 2;//TODO: double check this is the right axis
		
		SmartDashboard.putNumber("Manual Pwr", manualHeight);
	}
	public double ramp(double input, double output) { //Limit the motor output of the robot, prevents flipping
		double diff = input - output;
		
	//	Elevator.interp(ELEVATOR_HEIGHT_AXIS, RAMP_LIMIT_TABLE, RAMP_LIMIT);
		
		
		if(output > 0) {
			if (diff>0) {
				diff = Math.min(diff, RAMP_LIMIT);
			}else{
				diff = Math.max(diff, -output);
			}
		}else {
			if(diff < 0) {
				diff = Math.max(diff, -RAMP_LIMIT);
			}else {
				diff = Math.min(diff, -output);
			}
		}
		
		
		output +=diff;
		
		return output;
		
	}
}
