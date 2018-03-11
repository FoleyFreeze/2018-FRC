package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.auton.AutonMain;

public class IfSet extends AutonSet {
	
	IfInterface choice;
	
	AutonStep trueStep;
	AutonStep falseStep;
	AutonStep activeStep;
	
	public IfSet(IfInterface choice) {
		this.choice = choice;
	}
	
	@Override
	public void addStep(AutonStep step) {
		if(trueStep == null) {
			trueStep = step;
		} else {
			falseStep = step;
		}
	}

	@Override
	public void init() {
		if(choice.choice()) {
			activeStep = trueStep;
		} else {
			activeStep = falseStep;
		}
		
		activeStep.init();
	}

	@Override
	public void run() {
		activeStep.run();
	}

	@Override
	public boolean isDone() {
		return activeStep.isDone();
	}
	
	@Override
	public boolean isError() {
		return activeStep.isError();
	}

}
