package org.usfirst.frc.team910.robot.io;

public class ElectroBach {
	public static final int LEFT_DRIVE_CAN_1=1;
	public static final int LEFT_DRIVE_CAN_2=0;
	public static final int LEFT_DRIVE_CAN_3=2;
	public static final int RIGHT_DRIVE_CAN_1=14;
	public static final int RIGHT_DRIVE_CAN_2=13;
	public static final int RIGHT_DRIVE_CAN_3=15;
	
	public static final int ARM_CAN_1=10;
	public static final int ARM_CAN_2=11;
	
	public static final int ELEVATOR_CAN_1=6;
	public static final int ELEVATOR_CAN_2=9;

	public static final int LEFT_GATHER_CAN=4;
	public static final int RIGHT_GATHER_CAN=5;
	
	public static final double TICKS_PER_INCH = 317.2;
	public static final double TICKS_PER_DEGREE = 4800/90; //TODO calculate or measure real value 
	public static final double TICKS_PER_INCH_HEIGHT = 88676/70; //TODO calculate or measure real value please
	
	public static final int CLIMBER_CAN_1=3;
	public static final int CLIMBER_CAN_2=12;
	//infrared distance sensors
	public static final int F_CNT_DIST = -1;
	public static final int R_CNT_DIST = -1;
	public static final int F_RGT_DIST = -1;
	public static final int R_RGT_DIST = -1;
}
