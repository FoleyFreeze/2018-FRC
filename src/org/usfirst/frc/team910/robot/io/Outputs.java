package org.usfirst.frc.team910.robot.io;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.components.Elevator;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

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
		
		driveMP = new MotionProfile(leftDrive1, rightDrive1);
		rightDrive1.configPeakOutputForward(0.7, 0);
		rightDrive1.configPeakOutputReverse(-0.7, 0);
		leftDrive1.configPeakOutputForward(0.7, 0);
		leftDrive1.configPeakOutputReverse(-0.7, 0);
		
		

		
		elevator1 = new TalonSRX(ElectroBach.ELEVATOR_CAN_1);
		elevator2 = new TalonSRX(ElectroBach.ELEVATOR_CAN_2);
		
		elevator2.set(ControlMode.Follower, ElectroBach.ELEVATOR_CAN_1);
		
		elevator1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		elevator1.setSensorPhase(false); //TODO look at gear box and find out if directions match
		elevator1.setInverted(false); 
		elevator2.setInverted(false); 
		
		armMotor1 = new TalonSRX(ElectroBach.ARM_CAN_1);
		armMotor2 = new TalonSRX(ElectroBach.ARM_CAN_2);
		
		armMotor2.set(ControlMode.Follower, ElectroBach.ARM_CAN_1);
		
		armMotor1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		armMotor1.setSensorPhase(false); //TODO look at gear box and find out if directions match
		armMotor1.setInverted(false);
		armMotor2.setInverted(false);
		
		gatherLeft = new TalonSRX(ElectroBach.LEFT_GATHER_CAN);
		gatherRight = new TalonSRX(ElectroBach.RIGHT_GATHER_CAN);
		
		climber1 = new TalonSRX(ElectroBach.CLIMBER_CAN_1);
		climber2 = new TalonSRX(ElectroBach.CLIMBER_CAN_2);
		
	}

	public void readEncoders() {
		sense.leftDist = leftDrive1.getSelectedSensorPosition(0) / ElectroBach.TICKS_PER_INCH;
		sense.rightDist = rightDrive1.getSelectedSensorPosition(0) / ElectroBach.TICKS_PER_INCH;
		sense.liftPos = elevator1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_INCH_HEIGHT;
		sense.armPos = armMotor1.getSelectedSensorPosition(0)/ElectroBach.TICKS_PER_DEGREE;
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
	}
	
	public void setDrivePower(double leftPower, double rightPower) {
		double power = 0.75;
		leftDrive1.set(ControlMode.PercentOutput, leftPower*power);
		leftDrive2.set(ControlMode.PercentOutput, leftPower*power);
		leftDrive3.set(ControlMode.PercentOutput, leftPower*power);
		rightDrive1.set(ControlMode.PercentOutput, rightPower*power);
		rightDrive2.set(ControlMode.PercentOutput, rightPower*power);
		rightDrive3.set(ControlMode.PercentOutput, rightPower*power);
	}

	public void setArmPower(double armPower) {
		double power = in.manualHeight;//FIXME: this is a hack
		armMotor1.set(ControlMode.PercentOutput, armPower*power);
		armMotor2.set(ControlMode.PercentOutput, armPower*power);
	}

	
	public void setElevatorPower(double power) {
		double restriction = in.manualHeight;//FIXME: this is a hack
		elevator1.set(ControlMode.PercentOutput, power * restriction); //TODO please good 
		elevator2.set(ControlMode.PercentOutput, power * restriction);
	}
	
	public void setElevatorPosition(double elevatorPosition) {
		elevator1.set(ControlMode.Position,  elevatorPosition * ElectroBach.TICKS_PER_INCH_HEIGHT); //sets position for the elevation device (lift for british)
	}
		
	
	public void setArmPosition(double armPosition) {
		armMotor1.set(ControlMode.Position, armPosition * ElectroBach.TICKS_PER_DEGREE); 
		
	}

	public void setGatherPower(double leftPower, double rightPower) {
		double restriction = 0.25;
		gatherLeft.set(ControlMode.PercentOutput, leftPower*restriction);
		gatherRight.set(ControlMode.PercentOutput, rightPower*restriction);

	}
	
	public void setClimberPower(double power1, double power2) {
		climber1.set(ControlMode.PercentOutput, power1);
		climber2.set(ControlMode.PercentOutput, power2);
	}
	
}
