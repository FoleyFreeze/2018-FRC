package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.Angle;
import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;
import org.usfirst.frc.team910.robot.io.Sensors;
import org.usfirst.frc.team910.robot.util.Path;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Component {
	public static final double DYN_BRAKE_KP = 0.0005; // This is power per inch of error
	public static final double DRIVE_STRAIGHT_KP = 0.1; // Distance difference by inch
	public static final double DRIVE_STRAIGHT_TURN = 30.0 / 50.0; // 5 inches per second / 50
	public static final double DRIVE_STRAIGHTNAVX_KP = 0.1; // Distance difference by inch

	public static final double DRIVEMP_KP = 0.1;
	public static final double DRIVEMP_KD = 0.0;
	public static final double DRIVEMP_KFV = 0.9 / 210.0; //150 in/sec at max pwr
	public static final double DRIVEMP_KFA = 1.0 / 600.0; //full power is ~1000 in/sec/sec
	public static final double DRIVEMP_KFV_INT = 0.1;

	public static final double CAM_DRIVE_KP = 0.5 / 45; // This is power per degree of error
	public static final double CAM_DRIVE_KD = 0; // .5/45 * 50. This is power per degree per 20 milliseconds

	public static final double[] CAM_DRIVE_TABLE = { 1, 0.75, 0.5, 0 };
	public static final double[] CAM_DRIVE_AXIS = { 5, 10, 25, 50 };

	public DriveTrain() {

	}

	public void run() {
		// If motion profiling don't do any other run functions
		if (in.enableMP) {
			driveMp();
			// out.driveMP.run(in.enableMP);
		} else if (in.dynamicBrake) {
			boolean first = !prevBrake && in.dynamicBrake;
			dynamicBrake(sense.leftDist, sense.rightDist, first);
		} else if (in.driveStraight) {
			boolean first = !prevDriveStraight && in.driveStraight;
			driveStraightEnc(sense.leftDist, sense.rightDist, first, in.rightDrive);
			// driveStraightNavx(sense.robotAngle,in.rightDrive, first);
		} else {
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
	 * @param leftEncoder
	 *            current distance measured of left drive
	 * @param rightEncoder
	 *            current distance measured of right drive
	 * @param first
	 *            if true initializing the set points of the first time
	 * @return nothing
	 */
	private double setPointL;
	private double setPointR;
	private boolean prevBrake = false;

	private void dynamicBrake(double leftEncoder, double rightEncoder, boolean first) {
		// once trigger pulled sets position
		if (first) {
			setPointL = leftEncoder;
			setPointR = rightEncoder;
		}
		// calculate error sets power motors based on error
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
	 * @param leftEncoder
	 *            is the number of rotations on the left
	 * @param rightEncoder
	 *            is the number of rotations on the right
	 * @param first
	 *            determines whether you just pressed the trigger
	 * @param rightJoystick
	 *            is the right joystick axis(y axis)
	 */
	private boolean prevDriveStraight = false;
	private double initDiff;

	private void driveStraightEnc(double leftEncoder, double rightEncoder, boolean first, double rightJoystick) {

		// Checks trigger pulled to find initial difference between encoders
		if (first) {
			initDiff = leftEncoder - rightEncoder;
		}

		initDiff += DRIVE_STRAIGHT_TURN * in.driveStraightTurn; // use right joystick to turn while driving straight

		// encoder tick difference between left and right encoder
		double encDiff = leftEncoder - rightEncoder;
		// how bad our initial difference is from current difference
		double dispError = encDiff - initDiff;

		// sets power difference with P from PID
		double powerDiff = DRIVE_STRAIGHT_KP * dispError;

		// power for each motor
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
	 * @param currentableAngle
	 *            is the angle the robot is currently at
	 * @param rightJoystick
	 *            the reading of the right Joystick y-axis
	 * @param First
	 *            determines whether you just pressed the trigger
	 */
	private Angle initAngle = new Angle(0);

	private void driveStraightNavx(Angle currentAngle, double rightJoystick, boolean first) {

		// check trigger pulled sets starting angle
		if (first) {
			initAngle.set(currentAngle);
		}
		// how far off we are from initial angle
		double angleError = currentAngle.subtract(initAngle);
		double powerDiff = DRIVE_STRAIGHTNAVX_KP * angleError;

		// power for each motor
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

	private Path leftPath;
	private Path rightPath;

	public void initMp(Path leftPath, Path rightPath) {
		index = 0;
		// prevLError = 0;
		// prevRError = 0;

		this.leftPath = leftPath;
		this.rightPath = rightPath;

	}

	private int index = 0;
	private double prevLError = 0;
	private double prevRError = 0;
	private double lError = 0;
	private double rError = 0;

	private void driveMp() {

		double leftPosition = leftPath.positions.get(index);
		double rightPosition = rightPath.positions.get(index);

		double leftVelocity = leftPath.velocities.get(index);
		double rightVelocity = rightPath.velocities.get(index);

		double leftAccel = leftPath.accelerations.get(index);
		double rightAccel = rightPath.accelerations.get(index);

		index++;
		if (index >= leftPath.positions.size())
			index = leftPath.positions.size() - 1;

		lError = (leftPosition - sense.leftDist);// + lError) / 2;
		rError = (rightPosition - sense.rightDist);// + rError) / 2;

		double deltaLError = sense.leftDist - prevLError;
		double deltaRError = sense.prevRightPos - prevRError;
		
		double ffPowerL = (DRIVEMP_KFV * leftVelocity) + (DRIVEMP_KFA * leftAccel);
		double ffPowerR = (DRIVEMP_KFV * rightVelocity) + (DRIVEMP_KFA * rightAccel);

		if(leftVelocity > 0) ffPowerL += DRIVEMP_KFV_INT;
		else if(leftVelocity < 0) ffPowerL -= DRIVEMP_KFV_INT;
		if(rightVelocity > 0) ffPowerR += DRIVEMP_KFV_INT;
		else if(rightVelocity < 0) ffPowerR -= DRIVEMP_KFV_INT;
		
		if (ffPowerL > 1)
			ffPowerL = 1;
		else if (ffPowerL < -1)
			ffPowerL = -1;
		if (ffPowerR > 1)
			ffPowerR = 1;
		else if (ffPowerR < -1)
			ffPowerR = -1;
		
		double powerL = (DRIVEMP_KP * lError) - (DRIVEMP_KD * deltaLError) + ffPowerL;
		double powerR = (DRIVEMP_KP * rError) - (DRIVEMP_KD * deltaRError) + ffPowerR;

		prevLError = sense.leftDist;
		prevRError = sense.rightDist;

		if (powerL > 1)
			powerL = 1;
		else if (powerL < -1)
			powerL = -1;
		if (powerR > 1)
			powerR = 1;
		else if (powerR < -1)
			powerR = -1;

		System.out.format("idx:\t%d\tlpwr:\t%.2f\tlerr:\t%.2f\tderr:\t%.2f\tlvel:\t%.2f\tlff:\t%.2f\n", index, powerL, lError,
				deltaLError, leftVelocity, ffPowerL);
		SmartDashboard.putNumber("leftError", lError);
		SmartDashboard.putNumber("rightError", rError);

		out.setDrivePower(powerL, powerR);

	}

	public boolean isMpDoneYet() {
		return index == leftPath.positions.size();
	}

	private double prevCamError;

	public void driveAngle(Angle targetAngle, double power) {
		// error is the target angle minus the robot angle
		double error = targetAngle.subtract(sense.robotAngle);

		// deltaError is the current error minus the previous camError
		double deltaError = error - prevCamError;

		// PD for the given power
		double powerDiff = CAM_DRIVE_KP * error + CAM_DRIVE_KD * deltaError;

		power = power * Elevator.interp(CAM_DRIVE_AXIS, CAM_DRIVE_TABLE, error);

		// setting powers
		double powerL = power - powerDiff;
		double powerR = power + powerDiff;

		prevCamError = error;

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
