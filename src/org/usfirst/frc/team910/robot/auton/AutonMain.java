package org.usfirst.frc.team910.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.auton.steps.DriveForward;

public class AutonMain {

	private ArrayList<AutonStep> steps;
	
	public AutonMain() {
		steps = new ArrayList<AutonStep>();
		steps.add(new DriveForward());
			
	}
	public void run() {
		
	}

}
