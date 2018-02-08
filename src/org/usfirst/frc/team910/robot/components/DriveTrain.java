package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.Angle;
import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;
import org.usfirst.frc.team910.robot.io.Sensors;

public class DriveTrain extends Component {
	public static final double DYN_BRAKE_KP = 0.0005; // This is power per inch of error
	public static final double DRIVE_STRAIGHT_KP = 0.1; // Distance difference by inch
	public static final double DRIVE_STRAIGHTNAVX_KP = 0.1;  //Distance difference by inch

	

	public DriveTrain() {
		
	}

	public void run() {
		//If motion profiling don't do any other run functions
		if(in.enableMP) {
			out.driveMP.run(in.enableMP);
		}
		else if (in.dynamicBrake) {
			boolean first = !prevBrake && in.dynamicBrake;
			dynamicBrake(sense.leftDist, sense.rightDist, first);
		} 
		else if(in.driveStraight) {
			boolean first = !prevDriveStraight && in.driveStraight;
			driveStraightEnc(sense.leftDist, sense.rightDist, first, in.rightDrive);
			//driveStraightNavx(sense.robotAngle,in.rightDrive, first);
		}
		else {
			tankDrive(in.leftDrive, in.rightDrive);
		}
		
		prevBrake = in.dynamicBrake;
		prevDriveStraight = in.driveStraight;

	}

	private void tankDrive(double left, double right) {
		out.setDrivePower(left, right);
	}
		

	/**
	 * Dynamic braking ensures that the robot stays in place even if pushed
	 *
	 * @param leftEncoder current distance measured of left drive
	 * @param rightEncoder current distance measured of right drive
	 * @param first if true initializing the set points of the first time
	 * @return nothing
	 */
	private double setPointL;
	private double setPointR;
	private boolean prevBrake = false;

	private void dynamicBrake(double leftEncoder, double rightEncoder, boolean first) {
		//once trigger pulled sets position
		if (first) {
			setPointL = leftEncoder;
			setPointR = rightEncoder;
		}
		//calculate error sets power motors based on error
		double errorL = setPointL - leftEncoder;
		double errorR = setPointR - rightEncoder;
		double powerL = DYN_BRAKE_KP * errorL;
		double powerR = DYN_BRAKE_KP * errorR;

		if (powerL > 1)
			powerL = 1;
		else if (powerL < -1)
			powerL = -1;
		if (powerR > 1)
			powerR = 1;
		else if (powerR < -1)
			powerR = -1;

		out.setDrivePower(powerL, powerR);

	}
/**
 * Press right trigger, initialize drive straight
 * 
 * @param leftEncoder is the number of rotations on the left
 * @param rightEncoder is the number of rotations on the right
 * @param first determines whether you just pressed the trigger
 * @param rightJoystick is the right joystick axis(y axis)
 */
	private boolean prevDriveStraight = false;
	private double initDiff;

	private void driveStraightEnc(double leftEncoder, double rightEncoder, boolean first, double rightJoystick) {
		
		//Checks trigger pulled to find initial difference between encoders 
		if (first) {
			 initDiff = leftEncoder - rightEncoder;
		}
		//encoder tick difference between left and right encoder
		double encDiff = leftEncoder - rightEncoder;
		//how bad our initial difference is from current difference
		double dispError = encDiff - initDiff;
		
		//sets power difference with P from PID
		double powerDiff = DRIVE_STRAIGHT_KP * dispError;
		
		//power for each motor
		double powerL = rightJoystick - powerDiff;
		double powerR = rightJoystick + powerDiff;
		
		if (powerL > 1)
			powerL = 1;
		else if (powerL < -1)
			powerL = -1;
		if (powerR > 1)
			powerR = 1;
		else if (powerR < -1)
			powerR = -1;

		out.setDrivePower(powerL, powerR);
	}
	
	/**
	 * corrects for angle errors when driving straight
	 * 
	 * @param currentableAngle is the angle the robot is currently at
	 * @param rightJoystick the reading of the right Joystick y-axis
	 * @param First determines whether you just pressed the trigger
	 */
	private Angle initAngle = new Angle(0); 
	private void driveStraightNavx(Angle currentAngle, double rightJoystick, boolean first) {
	
		//check trigger pulled sets starting angle
		if(first) {
			initAngle.set(currentAngle);	
		}
		//how far off we are from initial angle
		double angleError = currentAngle.subtract(initAngle);
		double powerDiff = DRIVE_STRAIGHTNAVX_KP * angleError;
		
		//power for each motor
		double powerL = rightJoystick - powerDiff;
		double powerR = rightJoystick + powerDiff;
		
		if (powerL > 1)
			powerL = 1;
		else if (powerL < -1)
			powerL = -1;
		if (powerR > 1)
			powerR = 1;
		else if (powerR < -1)
			powerR = -1;

		out.setDrivePower(powerL, powerR);
	}
	
}
