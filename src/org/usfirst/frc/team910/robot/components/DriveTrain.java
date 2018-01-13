package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;
import org.usfirst.frc.team910.robot.io.Sensors;

public class DriveTrain {
	public static final double DYN_BRAKE_KP = 0.1; // This is power per inch of error

	Outputs out;
	

	public DriveTrain(Outputs out) {
		this.out = out;
	}

	public void run(Inputs in, Sensors sense) {
		tankDrive(in.leftDrive, in.rightDrive);
		if (in.dynamicBrake) {
			dynamicBrake(sense.leftDist, sense.rightDist, true);
		}

	}

	private void tankDrive(double left, double right) {
		out.setDrivePower(left, right);
	}

	private double setPointL;
	private double setPointR;

	/**
	 *Dynamic braking ensures that the robot stays in place even if pushed
	 *
	 *@param leftEncoder current distance measured of left drive     
	 * @param rightEncoder current distance measured of right drive
	 * @param first if true initializing the set points of the first time
	 * @return nothing
	 */
	
	private void dynamicBrake(double leftEncoder, double rightEncoder, boolean first) {
		if (first) {
			setPointL = leftEncoder;
			setPointR = rightEncoder;
		}
		double errorL = setPointL - leftEncoder;
		double errorR = setPointR - rightEncoder;
		double powerL = DYN_BRAKE_KP * errorL;
		double powerR = DYN_BRAKE_KP * errorR;

		out.setDrivePower(powerL, powerR);

	}
}
