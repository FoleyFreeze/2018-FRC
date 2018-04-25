package org.usfirst.frc.team910.robot.auton.steps;

import java.awt.Robot;

import edu.wpi.first.wpilibj.DriverStation;

public class CheckMatchTime extends AutonStep {

	double elapsedTime = 0;
	
	public CheckMatchTime(double time) {
		elapsedTime = time;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void run() {
	}

	@Override
	public boolean isDone() {
		return true;
	}
	
	public boolean isError() {
		return DriverStation.getInstance().getMatchTime() > elapsedTime;
	}

}
