package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;

import edu.wpi.first.wpilibj.Joystick;

public class Inputs extends Component {
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
	public boolean dynamicBrake = false;
	public boolean driveStraight = false;

	//----------------------------Operator Functions--------------------------------------
	
	public boolean shift = false;
	public boolean manualOverride = false;
	public boolean liftFlip = false;
	public int elevatorHeight = 0;
	public double manualHeight = 0;
	public boolean lowAngle = false;
	public boolean middleAngle = false;
	public boolean highAngle = false;
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
		enableMP = false;
		
		leftDrive = -leftStick.getY();
		rightDrive = -rightStick.getY();
		dynamicBrake = leftStick.getTrigger();
		driveStraight = rightStick.getTrigger();
		//gather = rightStick.getRawButton(3);
		//shoot =rightStick.getRawButton(5);
		//all elevator heights 
		//goes in order of high to low
		if(controlBoard.getRawButton(19)){
			//scale height
			elevatorHeight = 3;
		} else if(controlBoard.getRawButton(16)){
			//floor height
			elevatorHeight = 1;
		} else {
			//switch height
			elevatorHeight = 2;
		}
		
		//rising edge for angle buttons so you dont have to hold them
		lowAngle = !prevLowAngle && controlBoard.getRawButton(2);
		middleAngle = !prevMiddleAngle && controlBoard.getRawButton(3);
		highAngle = !prevHighAngle && controlBoard.getRawButton(4);
		
		prevLowAngle = controlBoard.getRawButton(2);
		prevMiddleAngle = controlBoard.getRawButton(3);
		prevHighAngle = controlBoard.getRawButton(4);
		
		shift = controlBoard.getRawButton(1);
		manualOverride = controlBoard.getRawButton(14);
		liftFlip = controlBoard.getRawButton(15);
		shoot = controlBoard.getRawButton(5);
		gather = controlBoard.getRawButton(6);
		autoGather = controlBoard.getRawButton(9);
		liftExchange = controlBoard.getRawButton(8);
		cameraLights = controlBoard.getRawButton(12);
		deployClimb = controlBoard.getRawButton(7);
		climb = controlBoard.getRawButton(10);
		modeSwitch = controlBoard.getRawButton(13);
		autoCube1 = controlBoard.getRawButton(18);
		autoCube2 = controlBoard.getRawButton(17);
		autoCube3 = controlBoard.getRawButton(20);
		
		manualHeight = (controlBoard.getRawAxis(5) + 1) / 2;//TODO: double check this is the right axis
	}
}
