package org.usfirst.frc.team910.robot.io;

import edu.wpi.first.wpilibj.Encoder;

public class Sensors {
	
	Encoder leftEncoder;
	Encoder rightEncoder;
	
	public double leftDist;
	public double rightDist;
	
	public Sensors() {
		
		leftEncoder = new Encoder(3,4);
		rightEncoder = new Encoder(6,5);
		leftEncoder.setDistancePerPulse(1);
		rightEncoder.setDistancePerPulse(1);
			
	}
	public void read() {
		leftDist = leftEncoder.getDistance(); 
		rightDist = rightEncoder.getDistance();
		
		
	}
}

