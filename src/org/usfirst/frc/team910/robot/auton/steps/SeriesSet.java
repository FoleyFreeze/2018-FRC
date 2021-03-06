package org.usfirst.frc.team910.robot.auton.steps;

import java.util.ArrayList;

public class SeriesSet extends AutonSet {
	private ArrayList<AutonStep> steps;
	
	public SeriesSet() {
		steps = new ArrayList<>();
	}
	
	@Override
	public void addStep(AutonStep newStep) {
		steps.add(newStep);
	}

	@Override
	public void init() {
		steps.get(0).init();//initializing the first step
		
	}
	int currentStep = 0;//int is step that currently needs to happen
	@Override
	public void run() {//run step that is supposed to be happening at time
		steps.get(currentStep).run();
		
		if(steps.get(currentStep).isError()) {//if there is an error, everything stops
			error=true;
		}else if(steps.get(currentStep).isDone()) {//go to and initialize next step
			currentStep++;
			if(currentStep < steps.size()) steps.get(currentStep).init();
		}
	}

	@Override
	public boolean isDone() {//stops when it's done
		if(currentStep==steps.size()) {//correct amount of steps are done when true
			return true;
		}else {
			return false;
		}
	}
	
	boolean error = false;//will change in case that requires complete stopping
	
	@Override
	public boolean isError() {//if this is true, there is an error requiring robot to stop
		return error;
	}
}
