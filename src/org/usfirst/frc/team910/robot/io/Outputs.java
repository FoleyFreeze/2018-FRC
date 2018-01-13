package org.usfirst.frc.team910.robot.io;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Talon;

public class Outputs {
	Talon leftDrive1;
	Talon leftDrive2;
	Talon rightDrive1;
	Talon rightDrive2;
	

	public Outputs() {
		leftDrive1 = new Talon(Port.LEFT_DRIVE_1);
		leftDrive2 = new Talon(Port.LEFT_DRIVE_2);
		rightDrive1 = new Talon(Port.RIGHT_DRIVE_1);
		rightDrive2 = new Talon(Port.RIGHT_DRIVE_2);
	}

	public void setDrivePower(double leftPower, double rightPower) {
		double power = 0.25;
		leftDrive1.set(leftPower*power);
		leftDrive2.set(leftPower*power);
		rightDrive1.set(rightPower*power);
		rightDrive2.set(rightPower*power);
	}

}
