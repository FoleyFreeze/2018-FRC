package org.usfirst.frc.team910.robot.io;

import edu.wpi.first.wpilibj.Joystick;

public class Inputs {
	private Joystick leftStick;
	private Joystick rightStick;
	public double leftDrive;
	public double rightDrive;
	public boolean dynamicBrake = false;

	public Inputs() {
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}

	public void read() {
		leftDrive = -leftStick.getY();
		rightDrive = -rightStick.getY();
		dynamicBrake = leftStick.getTrigger();
	}
}
