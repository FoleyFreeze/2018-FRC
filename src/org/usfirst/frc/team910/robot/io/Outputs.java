package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.components.Elevator;

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
	TalonSRX armMotor;

	TalonSRX elevator1;
	TalonSRX elevator2;

	TalonSRX gatherLeft;
	TalonSRX gatherRight;
	

	public Outputs() {
		leftDrive1 = new TalonSRX(Port.LEFT_DRIVE_CAN_1);
		leftDrive2 = new TalonSRX(Port.LEFT_DRIVE_CAN_2);
		leftDrive3 = new TalonSRX(Port.LEFT_DRIVE_CAN_3);
		rightDrive1 = new TalonSRX(Port.RIGHT_DRIVE_CAN_1);
		rightDrive2 = new TalonSRX(Port.RIGHT_DRIVE_CAN_2);
		rightDrive3 = new TalonSRX(Port.RIGHT_DRIVE_CAN_3);
		armMotor = new TalonSRX(Port.ARM_CAN);


		elevator1 = new TalonSRX(Port.ELEVATOR_CAN_1);
		elevator2 = new TalonSRX(Port.ELEVATOR_CAN_2);
	

		gatherLeft = new TalonSRX(Port.LEFT_GATHER_CAN);
		gatherRight = new TalonSRX(Port.RIGHT_GATHER_CAN);
		

		rightDrive1.setInverted(true);
		rightDrive2.setInverted(true);
		rightDrive3.setInverted(true);
		
		
		leftDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftDrive1.setSensorPhase(true);
		rightDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightDrive1.setSensorPhase(true);
		
		
	}

	public void readEncoders() {
		sense.leftDist = leftDrive1.getSelectedSensorPosition(0) * (60/12000);
		sense.rightDist = rightDrive1.getSelectedSensorPosition(0) * (60/12000);
	}
	
	public void setDrivePower(double leftPower, double rightPower) {
		double power = 0.2;
		leftDrive1.set(ControlMode.PercentOutput, leftPower*power);
		leftDrive2.set(ControlMode.PercentOutput, leftPower*power);
		leftDrive3.set(ControlMode.PercentOutput, leftPower*power);
		rightDrive1.set(ControlMode.PercentOutput, rightPower*power);
		rightDrive2.set(ControlMode.PercentOutput, rightPower*power);
		rightDrive3.set(ControlMode.PercentOutput, rightPower*power);
	}

	public void setArmPower(double armPower) {
		double power = 0.2;
		armMotor.set(ControlMode.PercentOutput, armPower*power);
	}

	
	public void setElevatorPower(double power) {
		double restriction = 0.2;
		elevator1.set(ControlMode.PercentOutput, power * restriction); //FIXME please good 
		elevator2.set(ControlMode.PercentOutput, power * restriction);
	}
		

	public void setGatherPower(double leftPower, double rightPower) {
		double restriction = 0.25;
		gatherLeft.set(ControlMode.PercentOutput, leftPower*restriction);
		gatherRight.set(ControlMode.PercentOutput, rightPower*restriction);

	}

}
