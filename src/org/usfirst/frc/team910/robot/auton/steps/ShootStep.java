package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.components.Gatherer;

import edu.wpi.first.wpilibj.Timer;

public class ShootStep extends AutonStep {

	double startTime;
	double targetTime;
	double shootPower = Gatherer.PWR_SHOOT;
	
	public ShootStep() {
		
	}
	
	public ShootStep(double shootPower) {
		this.shootPower = shootPower;
	}

	@Override
	public void init() {
		startTime = Timer.getFPGATimestamp();
		targetTime = 1.75; //2 pre forest hills
		in.shoot = true;
		in.autonShootPwr = shootPower;
	}

	@Override
	public void run() {
		
	}

	boolean prevTouchingCube = true;
	
	@Override
	public boolean isDone() {
		if (Timer.getFPGATimestamp() > startTime + targetTime || (!sense.touchingCube && prevTouchingCube)) {
			in.shoot = false;
			return true;
		} else {
			prevTouchingCube = sense.touchingCube;
			return false;
		}
	}
}
