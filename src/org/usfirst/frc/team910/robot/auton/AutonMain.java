package org.usfirst.frc.team910.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.auton.steps.AutonStep;
import org.usfirst.frc.team910.robot.auton.steps.DriveForward;
import org.usfirst.frc.team910.robot.auton.steps.DrivePath;
import org.usfirst.frc.team910.robot.auton.steps.EndStep;
import org.usfirst.frc.team910.robot.auton.steps.SeriesSet;
import org.usfirst.frc.team910.robot.auton.steps.StartStep;


public class AutonMain extends Component {

	private ArrayList<AutonStep> steps;
	
	final String startPosition = "default";
	final String PositionLeft = "Left";
	final String PositionCenter = "Center";
	final String PositionRight = "Right";
	
	private SeriesSet driveForward;
	private SeriesSet onlySwitch;
	
	public int currentStep = 0;
	
	public AutonMain() {
		
		driveForward = new SeriesSet();
		AutonBuilder b = new AutonBuilder(driveForward);
			b.add(new StartStep());
			b.add(new DriveForward());
			b.add(new EndStep());
		b.end();
		
		onlySwitch = new SeriesSet();
		b.add(onlySwitch);
			b.add(new StartStep());
			b.add(new EndStep());
		b.end();
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
