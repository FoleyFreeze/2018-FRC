package org.usfirst.frc.team910.robot.io;

//SmartDashboard
public class Angle { // math to create angles throughout NavX-related and Auto function
	private double angle;

	public Angle(double angle) {
		this.angle = mod(angle);
	}

	public double get() {
		return angle;
	}

	public void set(double newAngle) {
		angle = mod(newAngle);
	}

	public void set(Angle newAngle) {
		angle = newAngle.get();
	}
	
	// returns shortest distance around the circle
	public double subtract(Angle other) {
		double diff = angle - other.get(); // target minus actual

		if (diff > 180) {
			diff = diff - 360;
		} else if (diff < -180) {
			diff = 360 + diff;
		}

		return diff;

	}

	// return positive numbers for negative angles 
	private double mod(double value) {
		return (value % 360 + 360) % 360;
	}
}
