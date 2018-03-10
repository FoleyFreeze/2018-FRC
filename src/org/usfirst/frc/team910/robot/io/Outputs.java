package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.components.Elevator;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Outputs extends Component {
	TalonSRX leftDrive1;
	TalonSRX leftDrive2;
	TalonSRX leftDrive3;
	TalonSRX rightDrive1;
	TalonSRX rightDrive2;
	TalonSRX rightDrive3;
	
	TalonSRX armMotor1;
	TalonSRX armMotor2;
	
	TalonSRX elevator1;
	TalonSRX elevator2;
	
	TalonSRX gatherLeft;
	TalonSRX gatherRight;
	
	TalonSRX climber1;
	TalonSRX climber2;
	
	public MotionProfile driveMP;
	


	public Outputs() {
		leftDrive1 = new TalonSRX(ElectroBach.LEFT_DRIVE_CAN_1);
		leftDrive2 = new TalonSRX(ElectroBach.LEFT_DRIVE_CAN_2);
		leftDrive3 = new TalonSRX(ElectroBach.LEFT_DRIVE_CAN_3);
		rightDrive1 = new TalonSRX(ElectroBach.RIGHT_DRIVE_CAN_1);
		rightDrive2 = new TalonSRX(ElectroBach.RIGHT_DRIVE_CAN_2);
		rightDrive3 = new TalonSRX(ElectroBach.RIGHT_DRIVE_CAN_3);
		
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
		
		elevator2.set(ControlMode.Follower, ElectroBach.ELEVATOR_CAN_1);
		
		elevator1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		elevator1.setSensorPhase(false);
		elevator1.setInverted(false); 
		elevator2.setInverted(true); 
		
		elevator1.configOpenloopRamp(0.2, 0);
		elevator2.configOpenloopRamp(0.2, 0);
		
		armMotor1 = new TalonSRX(ElectroBach.ARM_CAN_1);
		armMotor2 = new TalonSRX(ElectroBach.ARM_CAN_2);
		
		armMotor2.follow(armMotor1);
		
		armMotor1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		armMotor1.setSensorPhase(false); 
		armMotor2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		armMotor2.setSensorPhase(false);
		
		armMotor1.setInverted(true);
		armMotor2.setInverted(false);
		
		gatherLeft = new TalonSRX(ElectroBach.LEFT_GATHER_CAN);
		//gatherLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		//gatherLeft.setSensorPhase(true);
		gatherRight = new TalonSRX(ElectroBach.RIGHT_GATHER_CAN);
		//gatherRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		//gatherRight.setSensorPhase(true);
		
		climber1 = new TalonSRX(ElectroBach.CLIMBER_CAN_1);
		climber2 = new TalonSRX(ElectroBach.CLIMBER_CAN_2);
		
	}

	public void readEncoders() {
		sense.leftDist = leftDrive1.getSelectedSensorPosition(0) / ElectroBach.TICKS_PER_INCH;
		sense.rightDist = rightDrive1.getSelectedSensorPosition(0) / ElectroBach.TICKS_PER_INCH;
		sense.liftPos = elevator1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH_HEIGHT;
		sense.armPosL = armMotor1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_DEGREE;
		sense.armPosR = armMotor2.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_DEGREE;
		//sense.gatherLeftPos = gatherLeft.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH;
		//sense.gatherRightPos = gatherRight.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH;
		//System.out.format("L:%.2f R:%.2f\n",sense.leftDist,sense.rightDist);
	}
	
	public void resetEncoders() {
		if(leftDrive1.setSelectedSensorPosition(0, 0, 50) != ErrorCode.OK) {
			System.out.println("Error in resetting left encoder position");
			//TODO: instead of crashing, propegate this error to auton isError() 
			//in order to prevent auton from running when encoders are broken
			System.exit(-1); 
		}
		if(rightDrive1.setSelectedSensorPosition(0, 0, 50) != ErrorCode.OK) {
			System.out.println("Error in resetting right encoder position");
			System.exit(-1);
		}
		
		armMotor1.setSelectedSensorPosition(0, 0, 0);
		armMotor2.setSelectedSensorPosition(0, 0, 0);
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
		leftDrive1.set(ControlMode.PercentOutput, leftStorage);
		leftDrive2.set(ControlMode.PercentOutput, leftStorage);
		leftDrive3.set(ControlMode.PercentOutput, leftStorage);
		rightDrive1.set(ControlMode.PercentOutput, rightStorage);
		rightDrive2.set(ControlMode.PercentOutput, rightStorage);
		rightDrive3.set(ControlMode.PercentOutput, rightStorage);
		SmartDashboard.putNumber("L Drive Pwr", leftStorage);//TODO correct if needed
		SmartDashboard.putNumber("R Drive Pwr", rightStorage);//TODO correct if needed
	}

	public void setArmPower(double armPower) {
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
		double power = 1;
		if(in.manualOverride) power = in.manualHeight;
		elevatorPower=power*elevatorPower;
		
		//limiting logic
		if(elevatorPower>in.powerLimiter) {
			elevatorPower=in.powerLimiter;
		}else if(elevatorPower<-in.powerLimiter) {
			elevatorPower=-in.powerLimiter;
		}
		
		elevator1.set(ControlMode.PercentOutput, elevatorPower); //TODO please good 
		elevator2.set(ControlMode.PercentOutput, elevatorPower);
		SmartDashboard.putNumber("Lift 1 Pwr", elevatorPower);//TODO correct if needed
		SmartDashboard.putNumber("Lift 2 Pwr", elevatorPower);//TODO correct if needed
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
	
	public void setClimberPower(double power1, double power2) {
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
		climber1.set(ControlMode.PercentOutput, power1);
		climber2.set(ControlMode.PercentOutput, power2);
		SmartDashboard.putNumber("Climb 1 Pwr", power1);//TODO correct if needed
		SmartDashboard.putNumber("Climb 2 Pwr", power2);//TODO correct if needed
	}
	
}
