package org.usfirst.frc.team910.robot.auton.steps;

import edu.wpi.first.wpilibj.Timer;

public class ShootStep extends AutonStep {

	double startTime;
	double targetTime;

	public ShootStep() {

	}

	@Override
	public void init() {
		startTime = Timer.getFPGATimestamp();
		targetTime = 1;
	}

	@Override
	public void run() {
		in.shoot = true;
	}

	@Override
	public boolean isDone() {
		if (Timer.getFPGATimestamp() > startTime + targetTime) {
			in.shoot = false;
			return true;
		} else {
			return false;
		}
	}
}
