package org.usfirst.frc.team910.robot;

import org.usfirst.frc.team910.robot.components.Climber;
import org.usfirst.frc.team910.robot.components.DriveTrain;
import org.usfirst.frc.team910.robot.components.Elevator;
import org.usfirst.frc.team910.robot.components.Gatherer;
import org.usfirst.frc.team910.robot.components.Vision;
import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;
import org.usfirst.frc.team910.robot.io.Sensors;

public class Component {

	protected static Inputs in;
	protected static Outputs out;
	public static Sensors sense;
	protected static Climber climb;
	protected static DriveTrain drive;
	protected static Elevator elevate;
	protected static Gatherer gather;
	protected static Vision view;
	
	public static void set(Inputs in, Outputs out, Sensors sense, Climber climb,
			DriveTrain drive, Elevator elevate, Gatherer gather, Vision view) {
		
		Component.in = in;
		Component.out = out;
		Component.sense = sense;
		Component.climb = climb;
		Component.drive = drive;
		Component.elevate = elevate;
		Component.gather = gather;
		Component.view = view;
		
		
	}
}
