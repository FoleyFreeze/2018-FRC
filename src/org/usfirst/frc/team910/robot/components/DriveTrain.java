package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;

public class DriveTrain {
	Outputs out;
	public DriveTrain(Outputs out) {
		this.out = out;
	}

	public void run(Inputs in) {
		tankDrive(in.leftDrive, in.rightDrive);
	}

	private void tankDrive(double left, double right) {
		out.setDrivePower(left, right);
	}
}
