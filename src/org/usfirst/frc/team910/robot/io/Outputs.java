package org.usfirst.frc.team910.robot.io;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Outputs {
	TalonSRX leftDrive1;
	TalonSRX leftDrive2;
	TalonSRX rightDrive1;
	TalonSRX rightDrive2;

	public Outputs() {
		leftDrive1 = new TalonSRX(Port.LEFT_DRIVE_1);
		leftDrive2 = new TalonSRX(Port.LEFT_DRIVE_2);
		rightDrive1 = new TalonSRX(Port.RIGHT_DRIVE_1);
		rightDrive2 = new TalonSRX(Port.RIGHT_DRIVE_2);
	}

	public void setDrivePower(double leftPower, double rightPower) {
		leftDrive1.set(ControlMode.PercentOutput, leftPower);
		leftDrive2.set(ControlMode.PercentOutput, leftPower);
		rightDrive1.set(ControlMode.PercentOutput, rightPower);
		rightDrive2.set(ControlMode.PercentOutput, rightPower);
	}

}
