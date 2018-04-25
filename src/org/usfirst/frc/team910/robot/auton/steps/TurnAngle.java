package org.usfirst.frc.team910.robot.auton.steps;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnAngle extends AutonStep {

	double angleTarget = 0;
	
	public TurnAngle(double angleTarget) {
		this.angleTarget = angleTarget; 
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void run() {
		drive.angleTarget = angleTarget;
		drive.turnAngle = true;
	}

	@Override
	public boolean isDone() {
		if(Math.abs(sense.robotAngle.get() - angleTarget) < 5) {
			drive.turnAngle = false;
			SmartDashboard.putString("AutoTurn", "Complete");
			return true;
		}
		
		return false;
	}

}
