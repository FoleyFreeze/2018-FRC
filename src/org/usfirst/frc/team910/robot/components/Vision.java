package org.usfirst.frc.team910.robot.components;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.io.Angle;
import org.usfirst.frc.team910.robot.util.LimitedStack;
import org.usfirst.frc.team910.robot.vision.*;

import edu.wpi.first.wpilibj.Timer;

public class Vision extends Component {
	
	public static final int MAX_SIZE = 100;
	public static final double IMAGE_TIMEOUT = 0.5;
	
	public static final double X_RES = 320;
	public static final double X_FOV = 85;
	public static final double X_DEG_PER_PIXEL = X_FOV / X_RES;
	
	public static final double WIDTH_LIMIT = 20;
	public static final double HEIGHT_LIMIT = 20;
	
	public static final double AREA_CLOSE_FRAC = 0.5; //within 80 percent area, select based on center-ness instead
	
	LimitedStack<VisionData> front_blocks = new LimitedStack<VisionData>(MAX_SIZE);
	LimitedStack<VisionData> rear_blocks = new LimitedStack<VisionData>(MAX_SIZE);

	public Vision() {
		Component.view = this;
		
		VisionObjectDataPixyListener listener = new VisionObjectDataPixyListener();
		
		listener.saveValuesBack(true);
		listener.saveValuesFront(false);
		// PIXY ID #8
		listener.setPixyIDFront(-1225198266); //-1225198266 pixy id //109106 ip
		
		// PIXY ID #6
		listener.setPixyIDBack(109109); //changed back for prac bot b4 worlds //Was 109109 MSC Comp Bot. Chgd for Prac bot!!!

		listener.listForPixyFront(front_blocks);
		listener.listForPixyBack(rear_blocks);

		listener.setDebug(false);
		listener.start(); // vision data going into <blocks> now

	}
	
	public boolean getLatestAngle(Angle targetAngle) {
		LimitedStack<VisionData> blocks;
		if(in.liftFlip) {
			blocks = rear_blocks;
			//blocks = front_blocks;
		} else {
			blocks = front_blocks;
			//blocks = rear_blocks;
		}
		
		if(blocks.isEmpty()) return false;
		
		VisionData vd = blocks.getLast();
		
		if(vd.timestamp < Timer.getFPGATimestamp() - IMAGE_TIMEOUT) {
			return false;
		}

		//double angle = sense.robotAngle.get();
		double angle = vd.robotAngle;
		
		angle += ((X_RES / 2) - vd.x) * X_DEG_PER_PIXEL; 
		
		targetAngle.set(angle);
		return true;
	}
	
	public double getLatestDist() {
		LimitedStack<VisionData> blocks;
		if(in.liftFlip) {
			blocks = rear_blocks;
			//blocks = front_blocks;
			
		} else {
			blocks = front_blocks;
			//blocks = rear_blocks;
		}
		
		if(blocks.isEmpty()) return 0;
		
		VisionData vd = blocks.getLast();
		
		if(vd.timestamp < Timer.getFPGATimestamp() - IMAGE_TIMEOUT) {
			return 0;
		}
		
		//get bottom edge of cube
		return vd.y - vd.h/2;
	}

	public void run() {
		
	}
}
