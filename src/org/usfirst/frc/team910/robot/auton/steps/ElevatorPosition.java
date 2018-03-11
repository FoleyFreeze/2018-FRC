package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.components.Elevator;

public class ElevatorPosition extends AutonStep {

	Elevator.liftState position;
	
	public ElevatorPosition(Elevator.liftState position) {
		this.position = position;	
	}
	
	@Override
	public void init() {
		in.elevatorCommand = position;
	}

	@Override
	public void run() {
		
	}

	@Override
	public boolean isDone() {
		return true;
	}

	
}
