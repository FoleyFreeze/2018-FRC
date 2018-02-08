package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;

import edu.wpi.first.wpilibj.Joystick;

public class Inputs extends Component {
	private Joystick leftStick;
	private Joystick rightStick;
	public double leftDrive;
	public double rightDrive;
	public boolean dynamicBrake = false;
	public boolean driveStraight = false;
	public boolean arm = false;
	
	public boolean enableMP = false;
	
	public boolean elevatorUp = false;
	public boolean elevatorDown = false;

	public boolean gather = false;
	public boolean ungather = false;
	
	public boolean liftToScaleTrigger = false; //TODO set these based on buttons 
	public boolean liftToSwitchTrigger = false;
	public boolean liftToFloorTrigger = false; 
	public boolean liftFlipTrigger = false; 
	
	int liftHeightMod = 0; //this can be 0, -1, 1 
	

	public Inputs() {
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}

	public void read() {
		enableMP = false;
		
		leftDrive = -leftStick.getY();
		rightDrive = -rightStick.getY();
		dynamicBrake = leftStick.getTrigger();
		driveStraight = rightStick.getTrigger();
		arm = rightStick.getRawButton(6); 

		elevatorUp = rightStick.getRawButton(4);
		elevatorDown = rightStick.getRawButton(6);

		gather = rightStick.getRawButton(3);
		ungather = rightStick.getRawButton(5);

	}
}
