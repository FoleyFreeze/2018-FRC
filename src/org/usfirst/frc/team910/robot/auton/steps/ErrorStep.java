package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.components.Elevator;

public class ErrorStep extends AutonStep {

	@Override
	public void init() {
		
	}

	boolean first = true;
	
	@Override
	public void run() {
		if(first) {
			first = false;
			System.out.println("Error somewhere in auton");
		}
		
		//turn off everything that auton turned on
		in.enableMP = false;
		in.mpRecPath = false;
		in.recordPath = false;
		in.rightDrive = 0;
		in.leftDrive = 0;
		in.driveStraight = false;
		in.shoot = false;
		in.autonShootPwr = 0;
		in.elevatorCommand = Elevator.liftState.REST_POSITION;
		in.manualOverride = true;
	}

	@Override
	public boolean isDone() {
		// Errors never recover
		return false;
	}

}
