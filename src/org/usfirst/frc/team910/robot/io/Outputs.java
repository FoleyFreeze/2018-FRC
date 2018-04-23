package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Outputs extends Component {
	public TalonSRX leftDrive1;
	public TalonSRX leftDrive2;
	public TalonSRX leftDrive3;
	public TalonSRX rightDrive1;
	public TalonSRX rightDrive2;
	public TalonSRX rightDrive3;
	
	TalonSRX armMotor1;
	TalonSRX armMotor2;

	public TalonSRX elevator1;
	public TalonSRX elevator2;
	public TalonSRX elevator3;
	public TalonSRX elevator4;
	
	TalonSRX gatherLeft;
	TalonSRX gatherRight;
	
	Servo climbRatchet;
	Servo forkRatchetL;
	Servo forkRatchetR;
	
	public MotionProfile driveMP;
	
	public double totalElevatorError = 0;

	public Outputs() {
		Component.out = this;
		
		leftDrive1 = new TalonSRX(ElectroBach.LEFT_DRIVE_CAN_1);
		leftDrive2 = new TalonSRX(ElectroBach.LEFT_DRIVE_CAN_2);
		leftDrive3 = new TalonSRX(ElectroBach.LEFT_DRIVE_CAN_3);
		rightDrive1 = new TalonSRX(ElectroBach.RIGHT_DRIVE_CAN_1);
		rightDrive2 = new TalonSRX(ElectroBach.RIGHT_DRIVE_CAN_2);
		rightDrive3 = new TalonSRX(ElectroBach.RIGHT_DRIVE_CAN_3);
		
		double ramprate = 0.05;
		out.leftDrive1.configOpenloopRamp(ramprate, 0);
		out.leftDrive2.configOpenloopRamp(ramprate, 0);
		out.leftDrive3.configOpenloopRamp(ramprate, 0);
		out.rightDrive1.configOpenloopRamp(ramprate, 0);
		out.rightDrive2.configOpenloopRamp(ramprate, 0);
		out.rightDrive3.configOpenloopRamp(ramprate, 0);
		
		rightDrive2.follow(rightDrive1);
		rightDrive3.follow(rightDrive1);
		leftDrive2.follow(leftDrive1);
		leftDrive3.follow(leftDrive1);
		
		rightDrive1.setInverted(true);
		rightDrive2.setInverted(true);
		rightDrive3.setInverted(true);
				
		leftDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftDrive1.setSensorPhase(true);
		//leftDrive1.setSelectedSensorPosition(0, 0, 0);
		rightDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightDrive1.setSensorPhase(true);
		//rightDrive1.setSelectedSensorPosition(0, 0, 0);
		
		/* not needed when not using talons for motion profiling
		driveMP = new MotionProfile(leftDrive1, rightDrive1);
		rightDrive1.configPeakOutputForward(0.7, 0);
		rightDrive1.configPeakOutputReverse(-0.7, 0);
		leftDrive1.configPeakOutputForward(0.7, 0);
		leftDrive1.configPeakOutputReverse(-0.7, 0);
		*/
		

		
		elevator1 = new TalonSRX(ElectroBach.ELEVATOR_CAN_1);
		elevator2 = new TalonSRX(ElectroBach.ELEVATOR_CAN_2);
		elevator3 = new TalonSRX(ElectroBach.ELEVATOR_CAN_3);
		elevator4 = new TalonSRX(ElectroBach.ELEVATOR_CAN_4);
		
		elevator2.set(ControlMode.Follower, ElectroBach.ELEVATOR_CAN_1);
		elevator3.set(ControlMode.Follower, ElectroBach.ELEVATOR_CAN_1);
		elevator4.set(ControlMode.Follower, ElectroBach.ELEVATOR_CAN_1);
		
		elevator1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		elevator1.setSensorPhase(false);
		elevator1.setInverted(false); 
		elevator2.setInverted(true);
		elevator3.setInverted(true);
		elevator4.setInverted(true);
		
		//was 0.2 MSC semis
		elevator1.configOpenloopRamp(0.4, 0);
		elevator2.configOpenloopRamp(0.4, 0);
		elevator3.configOpenloopRamp(0.4, 0);
		elevator4.configOpenloopRamp(0.4, 0);
		
		armMotor1 = new TalonSRX(ElectroBach.ARM_CAN_1);
		armMotor2 = new TalonSRX(ElectroBach.ARM_CAN_2);
		
		armMotor2.follow(armMotor1);
		
		armMotor1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		armMotor1.setSensorPhase(false); 
		armMotor2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		armMotor2.setSensorPhase(false);
		
		armMotor1.setInverted(true);
		armMotor2.setInverted(false);
		
		armMotor1.configOpenloopRamp(0.1, 0);
		armMotor2.configOpenloopRamp(0.1, 0);
		
		gatherLeft = new TalonSRX(ElectroBach.LEFT_GATHER_CAN);
		//gatherLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		//gatherLeft.setSensorPhase(true);
		gatherRight = new TalonSRX(ElectroBach.RIGHT_GATHER_CAN);
		//gatherRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		//gatherRight.setSensorPhase(true);
		
		climbRatchet = new Servo(0);
		forkRatchetL = new Servo(1);
		forkRatchetL = new Servo(2);
	}
	
	public void setRatchetServo(boolean on) {
		if(on) climbRatchet.set(0);
		else climbRatchet.set(0.75);
	}
	
	public void deployForkServo(boolean on) {
		if(on) {
			forkRatchetL.set(1);
			forkRatchetR.set(1);
		}else {
			forkRatchetL.set(0);
			forkRatchetR.set(0);
		}
	}
	
	public void readDriveEncoders() {
		sense.leftDist = leftDrive1.getSelectedSensorPosition(0) / ElectroBach.TICKS_PER_INCH;
		sense.rightDist = rightDrive1.getSelectedSensorPosition(0) / ElectroBach.TICKS_PER_INCH;
	}

	public void readEncoders() {
		sense.liftPos = elevator1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH_HEIGHT;
		sense.armPosL = -armMotor1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_DEGREE;
		sense.armPosR = -armMotor2.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_DEGREE;
		//sense.gatherLeftPos = gatherLeft.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH;
		//sense.gatherRightPos = gatherRight.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH;
		//System.out.format("L:%.2f R:%.2f\n",sense.leftDist,sense.rightDist);
		SmartDashboard.putNumber("Servo 0", climbRatchet.get());
		//SmartDashboard.putNumber("Servo 1", forkRatchetL.get());
		//SmartDashboard.putNumber("Servo 2", forkRatchetR.get());
	}
	
	public void resetDriveEncoders() {
		if(leftDrive1.setSelectedSensorPosition(0, 0, 50) != ErrorCode.OK) {
			System.out.println("Error in resetting left encoder position");
			//TODO: instead of crashing, propagate this error to auton isError() 
			//in order to prevent auton from running when encoders are broken
			//System.exit(-1); 
		}
		if(rightDrive1.setSelectedSensorPosition(0, 0, 50) != ErrorCode.OK) {
			System.out.println("Error in resetting right encoder position");
			//System.exit(-1);
		}
	}
	
	public void resetEncoders() {
		if(leftDrive1.setSelectedSensorPosition(0, 0, 50) != ErrorCode.OK) {
			System.out.println("Error in resetting left encoder position");
			//TODO: instead of crashing, propagate this error to auton isError() 
			//in order to prevent auton from running when encoders are broken
			//System.exit(-1); 
		}
		if(rightDrive1.setSelectedSensorPosition(0, 0, 50) != ErrorCode.OK) {
			System.out.println("Error in resetting right encoder position");
			//System.exit(-1);
		}
		
		armMotor1.setSelectedSensorPosition(0, 0, 0);
		armMotor2.setSelectedSensorPosition(0, 0, 0);
		elevator1.setSelectedSensorPosition(0, 0, 0);
		totalElevatorError = 0;
	}
	public void resetElevator() {
		totalElevatorError = totalElevatorError + elevator1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH_HEIGHT;
		SmartDashboard.putNumber("LiftEncError", totalElevatorError);
		elevator1.setSelectedSensorPosition(0, 0, 0);
	}
	
	public void setDrivePower(double leftPower, double rightPower) {
		double leftStorage;
		double rightStorage;
		//limiting logic
		if(leftPower>in.powerLimiter) {
			leftStorage=in.powerLimiter;
		}else if(leftPower<-in.powerLimiter) {
			leftStorage=-in.powerLimiter;
		}else {
			leftStorage=leftPower;
		}
		
		//limiting logic
		if(rightPower>in.powerLimiter) {
			rightStorage=in.powerLimiter;
		}else if(rightPower<-in.powerLimiter) {
			rightStorage=-in.powerLimiter;
		}else {
			rightStorage=rightPower;
		}
		
		//FIXME: Use follow mode, calling separately is causing weird sync issues
		leftDrive1.set(ControlMode.PercentOutput, leftStorage);
		//leftDrive2.set(ControlMode.PercentOutput, leftStorage);
		//leftDrive3.set(ControlMode.PercentOutput, leftStorage);
		rightDrive1.set(ControlMode.PercentOutput, rightStorage);
		//rightDrive2.set(ControlMode.PercentOutput, rightStorage);
		//rightDrive3.set(ControlMode.PercentOutput, rightStorage);
		SmartDashboard.putNumber("L Drive Pwr", leftStorage);//TODO correct if needed
		SmartDashboard.putNumber("R Drive Pwr", rightStorage);//TODO correct if needed
	}

	public void setArmPower(double armPower) {
		//Do not drive if motor is burning out
		if (overloaded[ElectroBach.ARM_CAN_1] || overloaded[ElectroBach.ARM_CAN_2]) {
			armMotor1.set(ControlMode.PercentOutput, 0);
			return;
		}
		
		double power = 1;
		if(in.manualOverride) {
			power=in.manualHeight;
		}
		
		//limiting logic
		if(armPower*power>in.powerLimiter) {
			armPower=in.powerLimiter;
		}else if(armPower*power<-in.powerLimiter) {
			armPower=-in.powerLimiter;
		}else {
			armPower=armPower*power;
		}
		armMotor1.set(ControlMode.PercentOutput, armPower);
		//armMotor2.set(ControlMode.PercentOutput, armPower*power);
		//System.out.println(in.manualHeight);
		SmartDashboard.putNumber("L Arm Pwr", armPower);//TODO correct if needed
		SmartDashboard.putNumber("R Arm Pwr", armPower);//TODO correct if needed
	}

	
	public void setElevatorPower(double elevatorPower) {
		//Do not drive if motor is burning out
		if (overloaded[ElectroBach.ELEVATOR_CAN_1] || overloaded[ElectroBach.ELEVATOR_CAN_2] 
				|| overloaded[ElectroBach.ELEVATOR_CAN_3] || overloaded[ElectroBach.ELEVATOR_CAN_4]) {
			elevator1.set(ControlMode.PercentOutput, 0);
			//elevator2.set(ControlMode.PercentOutput, 0);
			//elevator3.set(ControlMode.PercentOutput, 0);
			//elevator4.set(ControlMode.PercentOutput, 0);
			return;
		}
		
		double power = 1;
		if(in.manualOverride && !in.climb) power = in.manualHeight;
		elevatorPower=power*elevatorPower;
		
		//limiting logic
		if(elevatorPower>in.powerLimiter) {
			elevatorPower=in.powerLimiter;
		}else if(elevatorPower<-in.powerLimiter) {
			elevatorPower=-in.powerLimiter;
		}
		
		elevator1.set(ControlMode.PercentOutput, elevatorPower); //TODO please good 
		//elevator2.set(ControlMode.PercentOutput, elevatorPower);
		//elevator3.set(ControlMode.PercentOutput, elevatorPower);
		//elevator4.set(ControlMode.PercentOutput, elevatorPower);
		SmartDashboard.putNumber("Lift 1 Pwr", elevatorPower);//TODO correct if needed
		//SmartDashboard.putNumber("Lift 2 Pwr", elevatorPower);//TODO correct if needed
		//SmartDashboard.putNumber("Lift 3 Pwr", elevatorPower);//TODO correct if needed
		//SmartDashboard.putNumber("Lift 4 Pwr", elevatorPower);//TODO correct if needed
	}
	
	public void setElevatorPosition(double elevatorPosition) {
		//elevator1.set(ControlMode.Position,  elevatorPosition * ElectroBach.TICKS_PER_INCH_HEIGHT); //sets position for the elevation device (lift for british)
		//SmartDashboard.putNumber("Elevator Height", elevatorPosition*ElectroBach.TICKS_PER_INCH_HEIGHT);//TODO correct if needed
	}
		
	
	public void setArmPosition(double armPosition) {
		//armMotor1.set(ControlMode.Position, armPosition * ElectroBach.TICKS_PER_DEGREE); 
		//SmartDashboard.putNumber("Arm Position", armPosition*ElectroBach.TICKS_PER_DEGREE);//TODO correct if needed
	}

	public void setGatherPower(double leftPower, double rightPower) {
		//Do not drive if motor is burning out
		if(overloaded[ElectroBach.LEFT_GATHER_CAN] || overloaded[ElectroBach.RIGHT_GATHER_CAN]) {
			gatherLeft.set(ControlMode.PercentOutput, 0);
			gatherRight.set(ControlMode.PercentOutput, 0);
			return;
		}
		
		//double restriction = in.manualHeight;
		
		//limiting logic
		if(leftPower>in.powerLimiter) {
			leftPower=in.powerLimiter;
		}else if(leftPower<-in.powerLimiter) {
			leftPower=in.powerLimiter;
		}
		
		//limiting logic
		if(rightPower>in.powerLimiter) {
			rightPower=in.powerLimiter;
		}else if(rightPower<-in.powerLimiter) {
			rightPower=-in.powerLimiter;
		}
		gatherLeft.set(ControlMode.PercentOutput, leftPower);
		gatherRight.set(ControlMode.PercentOutput, rightPower);
		SmartDashboard.putNumber("L Gather Pwr", leftPower);//TODO correct if needed
		SmartDashboard.putNumber("R Gather Pwr", rightPower);//TODO correct if needed
		//System.out.println(in.manualHeight);

	}
	
	/*public void setClimberPower(double power1, double power2) {
		//Do not drive if motor is burning out
		if(overloaded[ElectroBach.ELEVATOR_CAN_3] || overloaded[ElectroBach.ELEVATOR_CAN_4]) {
			elevator3.set(ControlMode.PercentOutput, 0);
			elevator4.set(ControlMode.PercentOutput, 0);
			return;
		}
		
		double power = 1;
		if(in.manualOverride)power=in.manualHeight;
		power1=power1*power;
		power2=power2*power;
		//limiting logic
		if(power1>in.powerLimiter) {
			power1=in.powerLimiter;
		}else if(power1<-in.powerLimiter) {
			power1=-in.powerLimiter;
		}
		
		if(power2>in.powerLimiter) {
			power2=in.powerLimiter;
		}else if(power2<-in.powerLimiter) {
			power2=-in.powerLimiter;
		}
		elevator3.set(ControlMode.PercentOutput, power1);
		elevator4.set(ControlMode.PercentOutput, power2);
		SmartDashboard.putNumber("Climb 1 Pwr", power1);//TODO correct if needed
		SmartDashboard.putNumber("Climb 2 Pwr", power2);//TODO correct if needed
	}
	No longer in use as climber uses elevator motors*/ 
	
	private double[] filteredCurrent = new double[16];
	private boolean[] overloaded = new boolean[16];
	public static final double[] CURRENT_LIMITS = {100, 100, 100, 100, 100, 30, 100, 100, 30, 100, 100, 100, 100, 100, 100, 100};
	public static final double[] TIME_LIMIT = {0.5, 0.5, 0.5, 0.5, 0.5, 5, 0.5, 0.5, 5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
	private double[] overloadTime = new double[16];
	public static final double FILT = 0.5;
	public static final double RESET_TIME = 3;
	
	public void circuitBreaker() {
		for (int i = 0; i < 16; i++) { //for every pdp channel
			filteredCurrent[i] += FILT * (sense.pdp.getCurrent(i) - filteredCurrent[i]); //filter current
			
			if(overloaded[i]) { //if burning out, wait until reset
				if (Timer.getFPGATimestamp() > overloadTime[i] || in.resetBreaker) {
					overloaded[i] = false;
				}
			} else { //if running
				if (filteredCurrent[i] > CURRENT_LIMITS[i]) { //if violating limits
					if (overloadTime[i] < Timer.getFPGATimestamp()) {//if violates for too long, overloaded
						overloaded[i] = true;
						overloadTime[i] = Timer.getFPGATimestamp() + RESET_TIME;
					}
				}else { //keep reseting time when limits met
					overloadTime[i] = Timer.getFPGATimestamp() + TIME_LIMIT[i];
				}
			}
		}
		SmartDashboard.putBooleanArray("Overloaded", overloaded);
		SmartDashboard.putNumberArray("Overload Time", overloadTime);
		
	}
	
	public void enableBrake() {
		elevator1.setNeutralMode(NeutralMode.Brake);
		elevator2.setNeutralMode(NeutralMode.Brake);
		elevator3.setNeutralMode(NeutralMode.Brake);
		elevator4.setNeutralMode(NeutralMode.Brake);
		armMotor1.setNeutralMode(NeutralMode.Brake);
		armMotor2.setNeutralMode(NeutralMode.Brake);
	}
	
	public void disableBrake() {
		elevator1.setNeutralMode(NeutralMode.Brake);
		elevator2.setNeutralMode(NeutralMode.Brake);
		elevator3.setNeutralMode(NeutralMode.Brake);
		elevator4.setNeutralMode(NeutralMode.Brake);
			
		armMotor1.setNeutralMode(NeutralMode.Coast);
		armMotor2.setNeutralMode(NeutralMode.Coast);
	}
}
