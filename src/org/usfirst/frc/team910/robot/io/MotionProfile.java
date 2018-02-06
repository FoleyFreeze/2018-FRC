package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.util.Path;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Notifier;

public class MotionProfile {

	private static final int MIN_PTS_TO_START = 10;
	
	
	private Path leftPath;
	private Path rightPath;
	private TalonSRX leftMotor;
	private TalonSRX rightMotor;
	private  MotionProfileStatus statusL = new MotionProfileStatus();
	private  MotionProfileStatus statusR = new MotionProfileStatus();
	
	public MotionProfile(TalonSRX leftMotor, TalonSRX rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		
		//start motion profile streaming threads
		leftMotor.changeMotionControlFramePeriod((int) (Path.DT * 500));
		rightMotor.changeMotionControlFramePeriod((int) (Path.DT * 500));
		//add more if more talons are being motion profiled
		Notifier n = new Notifier(new MotionProfileThread(leftMotor));
		n.startPeriodic(Path.DT / 2);
		n = new Notifier(new MotionProfileThread(rightMotor));
		n.startPeriodic(Path.DT / 2);
	}

	public enum MpState {
		OFF, INIT, RUNNING, ERROR, FINISHED
	}
	//where we start
	private MpState state = MpState.OFF;
	
	public void init(Path leftPath, Path rightPath) {
		//if we are running path, stop init
		if(state == MpState.RUNNING) {
			return;
		}
		//send points
		this.leftPath = leftPath;
		this.rightPath = rightPath;
		leftMotor.configMotionProfileTrajectoryPeriod((int)(Path.DT*1000), 0);
		fillTopBuffer(leftPath, leftMotor);
			
		rightMotor.configMotionProfileTrajectoryPeriod((int)(Path.DT*1000), 0);
		fillTopBuffer(rightPath, rightMotor);
		//if in certain states, go to init(state)
		if(state == MpState.FINISHED || state == MpState.ERROR) {
			state = MpState.INIT;
		}
	}
	
	public void run(boolean enable) {
		leftMotor.getMotionProfileStatus(statusL);
		rightMotor.getMotionProfileStatus(statusR);
		
		switch(state) {
		case OFF:
			//move to init when enabled
			if(enable) {
				state = MpState.INIT;
			}
		
			break;
			
		case INIT:
			//if buffer empty, fill
			if(statusL.topBufferCnt == 0) {
				leftMotor.configMotionProfileTrajectoryPeriod((int)(Path.DT*1000), 0);
				fillTopBuffer(leftPath, leftMotor);
			}//if buffer empty, fill
			if(statusR.topBufferCnt == 0) {
				rightMotor.configMotionProfileTrajectoryPeriod((int)(Path.DT*1000), 0);
				fillTopBuffer(rightPath, rightMotor);
			}//if enable false, turn off
			if(!enable) {
				state = MpState.OFF;
				//if ready, go to running
			}else if(statusL.btmBufferCnt > MIN_PTS_TO_START && statusR.btmBufferCnt > MIN_PTS_TO_START) {
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
				state = MpState.RUNNING;
			}else {//if not ready, keep filling
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
			}
			
			break;
			
		case RUNNING:
			
			if(statusL.hasUnderrun || statusR.hasUnderrun) {
				//if underrun, go to error
				leftMotor.clearMotionProfileHasUnderrun(0);
				rightMotor.clearMotionProfileHasUnderrun(0);
				leftMotor.clearMotionProfileTrajectories();
				rightMotor.clearMotionProfileTrajectories();
				
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				
				state = MpState.ERROR;
			}
			//if on last point, finish
			else if(statusL.isLast && statusR.isLast) {
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				
				state = MpState.FINISHED;
			} //if not enabled, stop
			else if(!enable) {
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				
				state = MpState.OFF;
			} else {//otherwise, keep going
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
			}
			
			
			break;
			
		case FINISHED:
			//stop moving
			leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
			rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
			//if not enabled, go to off
			if(!enable) {
				state = MpState.OFF;
			}
			break;
			
		case ERROR:
			//if underrun, stop
			if(statusL.hasUnderrun || statusR.hasUnderrun) {
				leftMotor.clearMotionProfileHasUnderrun(0);
				rightMotor.clearMotionProfileHasUnderrun(0);
				leftMotor.clearMotionProfileTrajectories();
				rightMotor.clearMotionProfileTrajectories();
				
				leftMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
				rightMotor.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
			}
			if(!enable) {
				state = MpState.OFF;
			}
			break;
			
		}
	
	}
	public boolean doneYet() {
		if(state == MpState.FINISHED) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean weFailed() {
		if(state == MpState.ERROR) {
			return true;
		}else {
			return false;
		}
	}
	
	private TrajectoryPoint point = new TrajectoryPoint();
	public void fillTopBuffer(Path path, TalonSRX motor) {
		//for every point, set these values
		for(int i = 0; i < path.positions.size(); i++) {
			point.position = path.positions.get(i) * Port.TICKS_PER_INCH;
			point.velocity = path.velocities.get(i) * Port.TICKS_PER_INCH / 10;
			point.headingDeg = 0;
			point.isLastPoint = i == path.positions.size()-1;//only true on the last point
			point.profileSlotSelect0 = 0;
			point.profileSlotSelect1 = 0;
			point.timeDur = TrajectoryDuration.Trajectory_Duration_0ms;
			point.zeroPos = i == 0;//only true on the first point
			
			motor.pushMotionProfileTrajectory(point);
		}
		
	}
	
	
	public void setPath(Path leftPath, Path rightPath) {
		this.leftPath = leftPath;
		this.rightPath = rightPath;
	}
	
	// One thread per motor for accuracy
	public class MotionProfileThread implements Runnable {
		
		private TalonSRX localMotor;
		
		public MotionProfileThread(TalonSRX motor) {
			localMotor = motor;
		}
		
		@Override
		public void run() {
			localMotor.processMotionProfileBuffer();
		}
		
	}
	
}
