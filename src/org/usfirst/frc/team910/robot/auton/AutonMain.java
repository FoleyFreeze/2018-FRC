package org.usfirst.frc.team910.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.auton.steps.AutonStep;
import org.usfirst.frc.team910.robot.auton.steps.DriveForward;
import org.usfirst.frc.team910.robot.auton.steps.EndStep;
import org.usfirst.frc.team910.robot.auton.steps.StartStep;

public class AutonMain {

	private ArrayList<AutonStep> steps;
	
	
	public int currentStep = 0;
	
	public AutonMain() {
		steps = new ArrayList<AutonStep>();
		steps.add(new StartStep());
		steps.add(new DriveForward());
		steps.add(new EndStep());
		
		
	}
	
	public void run() {
		System.out.println(currentStep);
		steps.get(currentStep).run();
		if(steps.get(currentStep).isError()) {
			currentStep = steps.size() - 1;
		}
		else if(steps.get(currentStep).isDone()) {
			currentStep++;
			steps.get(currentStep).init();
		}

	}

}
