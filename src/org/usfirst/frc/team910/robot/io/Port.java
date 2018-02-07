package org.usfirst.frc.team910.robot.io;

public class Port {
	public static final int LEFT_DRIVE_CAN_1=2;
	public static final int LEFT_DRIVE_CAN_2=0;
	public static final int LEFT_DRIVE_CAN_3=1;
	public static final int RIGHT_DRIVE_CAN_1=13;
	public static final int RIGHT_DRIVE_CAN_2=14;
	public static final int RIGHT_DRIVE_CAN_3=15;
	
	public static final int ARM_CAN_1=-1;
	public static final int ARM_CAN_2=-1;
	
	public static final int ELEVATOR_CAN_1=-1; //FIXME please
	public static final int ELEVATOR_CAN_2=-1;

	public static final int LEFT_GATHER_CAN=-1; //TODO fix for 2018
	public static final int RIGHT_GATHER_CAN=-1;//TODO fix for 2018
	
	public static final double TICKS_PER_INCH = 317.2;
	public static final double TICKS_PER_REV = 0; //TODO calculate or measure real value 
	public static final double TICKS_PER_INCH_HEIGHT = 0; //TODO calculate or measure real value please
}
