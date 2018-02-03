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
	
	public Inputs() {
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}

	public void read() {
		leftDrive = -leftStick.getY();
		rightDrive = -rightStick.getY();
		dynamicBrake = leftStick.getTrigger();
		driveStraight = rightStick.getTrigger();
		arm = rightStick.getRawButton(6); 

	}
}
