package org.usfirst.frc.team910.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.auton.steps.AutonStep;
import org.usfirst.frc.team910.robot.auton.steps.DrivePath;
import org.usfirst.frc.team910.robot.auton.steps.EndStep;
import org.usfirst.frc.team910.robot.auton.steps.StartStep;

import edu.wpi.first.wpilibj.DriverStation;

public class AutonMain {

	private ArrayList<AutonStep> steps;
	
	public boolean left = false;
	public boolean right = false;
	public boolean zig = false;
	public boolean zag = false;
	String gameData;
	
	public int currentStep = 0;
	
	public AutonMain() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
			left = true;
		}else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
			right = true;
		}else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R' ) {
			zig = true;
		}else {
			zag = true;
		}
		
		
		//declare a new array for our steps
		steps = new ArrayList<AutonStep>();
		//new steps
		steps.add(new StartStep());//always first step
		steps.add(new DrivePath());
		steps.add(new EndStep());//always last step
	}
	
	public void init() {

		currentStep = 0;
		
	}
	
	public void run() {

		steps.get(currentStep).run(); //run the functions of the step we are on
		//if step has error we move to the next step
		if(steps.get(currentStep).isError()) {
			currentStep = steps.size() - 1;
		}
		//if step finished move to next step in array
		else if(steps.get(currentStep).isDone()) {
			currentStep++;
			steps.get(currentStep).init(); //run initial code for next step
		}

	}

}
