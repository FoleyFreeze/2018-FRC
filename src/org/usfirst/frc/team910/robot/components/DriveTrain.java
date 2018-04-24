package org.usfirst.frc.team910.robot.components;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.Angle;
import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;
import org.usfirst.frc.team910.robot.io.Sensors;
import org.usfirst.frc.team910.robot.io.MotionProfile.MotionProfileThread;
import org.usfirst.frc.team910.robot.util.Path;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Component implements Runnable {
	public static final double DYN_BRAKE_KP = 0.0005; // This is power per inch of error
	public static final double DRIVE_STRAIGHT_KP = 0.1; // Distance difference by inch
	public static final double DRIVE_STRAIGHT_TURN = 30.0 / 50.0; // 5 inches per second / 50
	public static final double DRIVE_STRAIGHTNAVX_KP = 0.025; // Distance difference by inch
	public static final double DRIVE_STRAIGHTNAVX_TURN = 45.0 / 50.0; // 45deg per second / 50

	public static final double DRIVEMP_KP = 0.15;
	public static final double DRIVEMP_KD = 0.1;
	public static final double DRIVEMP_KP_ANGLE = 0.9 / 45; //50% per 45deg
	public static final double DRIVEMP_KP_ANGLE_REC = 2.5 / 45; //50% per 45deg
	public static final double DRIVEMP_KFV = 0.9 / 200.0; //200 in/sec at max pwr
	public static final double DRIVEMP_KFA = 1.0 / 300.0; //full power is 420 in/sec/sec
	public static final double DRIVEMP_KFV_INT = 0.1;
	public static final double DRIVEMP_REC_PWR = 0.75;

	public static final double CAM_DRIVE_KP = 3.0 / 25.0; // Ex: 1/25 is 100% power per 25 degree of error
	public static final double CAM_DRIVE_KD = 1.5; // .5/45 * 50. This is power per degree per 20 milliseconds
	public static final double CAM_DRIVE_PWR = 0.6;  //Power cap
	public static final double CAM_DRIVE_DIST_KP = 0.01; //max height of camera is 240 pixels
	public static final double GATHERED_DIST = 110; //Was 140 - Chgd 4/9 Mr C; //pixels
	
	public static final double[] CAM_DRIVE_TABLE = { 1, 0.3, 0.25,  0 };//dist error percent of drive straight power to apply: 
	public static final double[] CAM_DRIVE_AXIS =  { 1,  10,   25, 50 };//angle error axis

	public DriveTrain() {
		Component.drive = this;
		
		Notifier n = new Notifier(this);
		n.startPeriodic(Path.DT);
	}

	public double dt = 0;
	private double lastTime = 0;
	
	private Angle targetAngle = new Angle(0);
	
	public void run() {
		double time = Timer.getFPGATimestamp(); 
		dt = time - lastTime;
		lastTime = time;
		SmartDashboard.putNumber("driveDT", dt);
		
		out.readDriveEncoders();
		
		if(sense.robotDisabled) {
			return;
		}
		
		//if recording, record
		if(in.recordPath) recordMp();
		
		// If motion profiling don't do any other run functions
		if (in.enableMP) {
			//only mp if we still have points
			if(!isMpDoneYet()) driveMp();
			// out.driveMP.run(in.enableMP);
			
		} else if (in.autoGather && view.getLatestAngle(targetAngle)) {
			driveAngle(targetAngle, CAM_DRIVE_PWR, view.getLatestDist());	
			
		} else if (in.dynamicBrake) {
			boolean first = !prevBrake && in.dynamicBrake;
			dynamicBrake(sense.leftDist, sense.rightDist, first);
			
		} else if (in.driveStraight && sense.navx.isConnected()) {
			boolean first = !prevDriveStraight && in.driveStraight;
			//driveStraightEnc(sense.leftDist, sense.rightDist, first, in.driveStraightForward);
			driveStraightNavx(sense.robotAngle,in.driveStraightForward, first);
		
		}else if(in.driveStraight) {
			boolean first = !prevDriveStraight && in.driveStraight;
			driveStraightEnc(sense.leftDist, sense.rightDist, first, in.driveStraightForward);
		} else {
			tankDrive(in.leftDrive, in.rightDrive);
		}

		prevBrake = in.dynamicBrake;
		prevDriveStraight = in.driveStraight;
		prevCamError = sense.robotAngle.get();

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
		
		initAngle.add(-DRIVE_STRAIGHTNAVX_TURN * in.driveStraightTurn); // use right joystick to turn while driving straight
		
		
		// how far off we are from initial angle
		double angleError = currentAngle.subtract(initAngle);
		double powerDiff = -DRIVE_STRAIGHTNAVX_KP * angleError;

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
		
		//if recorded path, start at the end and run backwards
		if(in.mpRecPath) {
			index = leftPath.positions.size()-1;
		} else {
			index = 0;
		}
		
		// prevLError = 0;
		// prevRError = 0;

		this.leftPath = leftPath;
		this.rightPath = rightPath;

	}
	
	private void recordMp() {
		Path.recordPath();
	}

	public int index = 0;
	private int delayIndex = 0;
	private double prevLError = 0;
	private double prevRError = 0;
	public double lError = 0;
	public double rError = 0;
	public double angleError = 0;
	
	private int counter = 0;
	
	public int recIdx = 0;
	public double powerLs[] = new double[800];
	public double powerRs[] = new double[800];
	public double errLs[] = new double[800];
	public double errRs[] = new double[800];
	public double deltaLs[] = new double[800];
	public double deltaRs[] = new double[800];
	public double ffPwrL[] = new double[800];
	public double ffPwrR[] = new double[800];
	public double angleErrs[] = new double[800];
	public double dts[] = new double[800];

	private void driveMp() {

		double leftPosition;
		double rightPosition;
		double leftVelocity;
		double rightVelocity;
		double leftAccel;
		double rightAccel;
		double targetAngle = 90;
		
		if(in.mpRecPath) {
			//if rec path, run backwards
			leftPosition = leftPath.positions.get(index);
			rightPosition = rightPath.positions.get(index);

			leftVelocity = -leftPath.velocities.get(index);
			rightVelocity = -rightPath.velocities.get(index);

			leftAccel = -leftPath.accelerations.get(index);
			rightAccel = -rightPath.accelerations.get(index);
			
			if(leftPath.angles != null) {
				targetAngle = leftPath.angles.get(index);
			}			
			
			index--;
		} else {
			leftPosition = leftPath.positions.get(index);
			rightPosition = rightPath.positions.get(index);

			leftVelocity = leftPath.velocities.get(index);
			rightVelocity = rightPath.velocities.get(index);

			leftAccel = leftPath.accelerations.get(index);
			rightAccel = rightPath.accelerations.get(index);
			
			if(leftPath.angles != null) {
				targetAngle = leftPath.angles.get(index);
			}
			
			index++;
		}
		
		counter++;
		
		//cant use this unless we change isMpDoneYet()
		//if (index >= leftPath.positions.size())
		//	index = leftPath.positions.size() - 1;

		lError = (leftPosition - sense.leftDist);// + lError) / 2;
		rError = (rightPosition - sense.rightDist);// + rError) / 2;
		
		//give 2 steps when behind a lot
		if(lError + rError > 10 && delayIndex < 4 && in.mpRecPath) {
			delayIndex++;
			index++;
			//leftVelocity /= 4;
			//rightVelocity /= 4;
			//leftAccel/= 4;
			//rightAccel /= 4;
		} else {
			delayIndex = 0;
		}
		
		angleError = sense.robotAngle.get() - targetAngle;
		if(angleError > 180) {
			angleError -= 360;
		} else if (angleError < -180) {
			angleError += 360;
		}
		
		//double angleError = sense.robotAngle.subtract(targetAngle);

		double deltaLError = sense.leftDist - prevLError;
		double deltaRError = sense.rightDist - prevRError;
		
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
		
		double anglePowerDiff;
		if(in.mpRecPath) {
			anglePowerDiff = DRIVEMP_KP_ANGLE_REC * angleError;
		} else {
			anglePowerDiff = DRIVEMP_KP_ANGLE * angleError;
		}
		
		double powerL = (DRIVEMP_KP * lError) - (DRIVEMP_KD * deltaLError) + anglePowerDiff + ffPowerL;
		double powerR = (DRIVEMP_KP * rError) - (DRIVEMP_KD * deltaRError) - anglePowerDiff + ffPowerR;

		prevLError = sense.leftDist;
		prevRError = sense.rightDist;

		if(in.mpRecPath) {
			if (powerL > DRIVEMP_REC_PWR)
				powerL = DRIVEMP_REC_PWR;
			else if (powerL < -DRIVEMP_REC_PWR)
				powerL = -DRIVEMP_REC_PWR;
			if (powerR > DRIVEMP_REC_PWR)
				powerR = DRIVEMP_REC_PWR;
			else if (powerR < -DRIVEMP_REC_PWR)
				powerR = -DRIVEMP_REC_PWR;

		} else {
			if (powerL > 1)
				powerL = 1;
			else if (powerL < -1)
				powerL = -1;
			if (powerR > 1)
				powerR = 1;
			else if (powerR < -1)
				powerR = -1;
		}
		
		
		//apparently this is blocking io... dont use
		//System.out.format("idx:\t%d\tlpwr:\t%.2f\tlerr:\t%.2f\tderr:\t%.2f\tlvel:\t%.2f\tlff:\t%.5f\tang\t%.2f\n", counter, powerR, rError,
		//		deltaRError, rightVelocity, dt, angleError);
		//SmartDashboard.putNumber("leftError", lError);
		//SmartDashboard.putNumber("rightError", rError);

		//instead, record these arrays and then print them in disabled
		powerLs[recIdx] = powerL;
		powerRs[recIdx] = powerR;
		errLs[recIdx] = lError;
		errRs[recIdx] = rError;
		deltaLs[recIdx] = deltaLError;
		deltaRs[recIdx] = deltaRError;
		ffPwrL[recIdx] = ffPowerL;
		ffPwrR[recIdx] = ffPowerR;
		angleErrs[recIdx] = angleError;
		dts[recIdx] = dt;
		recIdx++;
		
		out.setDrivePower(powerL, powerR);

	}

	public boolean isMpDoneYet() {
		if(in.mpRecPath) {
			return index == -1;//we decrement after reading the index, so we will be one past 0
		} else {
			return index == leftPath.positions.size() || index == rightPath.positions.size();
		}
		
	}
	
	public boolean isMpDoneYet(int stepThreshold) {
		if(in.mpRecPath) {
			return index <= -1 + stepThreshold;//we decrement after reading the index, so we will be one past 0
		} else {
			return index == leftPath.positions.size() || index == rightPath.positions.size();
		}
	}

	private double prevCamError;

	public void driveAngle(Angle targetAngle, double powerLimit, double dist) {
		// error is the target angle minus the robot angle
		double error = targetAngle.subtract(sense.robotAngle);

		//switch to measuring the change in process variable instead of change in error to avoid jumps
		double deltaError = sense.robotAngle.subtract(prevCamError);
		
		SmartDashboard.putNumber("camError", error);
		SmartDashboard.putNumber("camDeltaError", deltaError);

		// PD for the given power
		double powerDiff = CAM_DRIVE_KP * error - CAM_DRIVE_KD * deltaError;

		double power = Elevator.interp(CAM_DRIVE_AXIS, CAM_DRIVE_TABLE, error);
	
		power *= (GATHERED_DIST - dist) * CAM_DRIVE_DIST_KP;
		
		// setting powers
		double powerL = power - powerDiff;
		double powerR = power + powerDiff;

		//normalize powers
		double maxPwr = Math.max(powerL, powerR);
		double minPwr = Math.min(powerL, powerR);
		
		if (maxPwr > powerLimit) {
			powerL = powerL / Math.abs(maxPwr) * powerLimit;
			powerR = powerR / Math.abs(maxPwr) * powerLimit;
		}
		else if (maxPwr < -powerLimit) {
			powerL = powerL / Math.abs(minPwr) * powerLimit;
			powerR = powerR / Math.abs(minPwr) * powerLimit;
		}
		
		SmartDashboard.putNumber("camLpwr", powerL);
		SmartDashboard.putNumber("camRpwr", powerR);

//		SmartDashboard.putNumber("dist", dist);
		
		if(in.liftFlip) {
			//rear case
			if(!sense.touchingCube)						//If we don't have a cube in sight yet
				out.setDrivePower(-powerR, -powerL); 	//   back towards it until we're close
			else {										//Else a cube has been spotted! 
				if(!sense.halfGathered) {					//	If cube is in jaws but not fully brought in yet
					out.setDrivePower(0.45, 0.45); 		//  	Slam on brakes and start to reverse
				} else {								//  Else we've got the cube fully in
					out.setDrivePower(0.05, 0.05);			//      So slow down our backup to this value
				}
			}
		} else {
			//front case
			if(!sense.touchingCube)						//If we don't have a cube in sight yet
				out.setDrivePower(powerL, powerR); 	//   back towards it until we're close
			else {										//Else a cube has been spotted! 
				if(!sense.halfGathered) {					//	If cube is in jaws but not fully brought in yet
					out.setDrivePower(-0.45, -0.45); 		//  	Slam on brakes and start to reverse
				} else {								//  Else we've got the cube fully in
					out.setDrivePower(-0.05, -0.05);			//      So slow down our backup to this value
				}
			}
		}
	}

}
