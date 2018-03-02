package org.usfirst.frc.team910.robot.auton;

import java.util.Stack;

import org.usfirst.frc.team910.robot.auton.steps.AutonSet;
import org.usfirst.frc.team910.robot.auton.steps.AutonStep;

public class AutonBuilder {
	
	private Stack<AutonSet> setStack;
	
	//putting the base set to the stack
	public AutonBuilder(AutonSet set) {
		setStack = new Stack<>();
		setStack.push(set);
	}
	
	//adds a step to the most recent set
	public void add(AutonStep step) {
		setStack.peek().addStep(step);
	}
	
	//puts a new set on stack
	public void add(AutonSet set) {
		setStack.peek().addStep(set);
		setStack.push(set);
	}
	
	//pop off of step and return it back to calling function
	public AutonSet end() {
		return setStack.pop(); 	
	}
	
}
