package org.usfirst.frc.team910.robot.io;

import edu.wpi.first.wpilibj.Encoder;

public class Sensors {
	
	Encoder leftEncoder;
	Encoder rightEncoder;
	
	public double leftDist;
	public double rightDist;
	
	public Sensors() {
		
		leftEncoder = new Encoder(0,0);
		rightEncoder = new Encoder(0,0);
			
	}
	public void read() {
		leftDist = leftEncoder.getDistance(); 
		rightDist = rightEncoder.getDistance();
		
		
	}
}

