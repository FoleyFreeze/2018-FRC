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
	
	public static final double WIDTH_LIMIT = 25;
	public static final double HEIGHT_LIMIT = 25;

	LimitedStack<VisionData> blocks = new LimitedStack<VisionData>(MAX_SIZE);

	public Vision() {
		Component.view = this;
		
		VisionObjectDataPixyListener listener = new VisionObjectDataPixyListener();
		listener.saveValuesFront(true);

		// PIXY ID #8
		listener.setPixyIDFront(-1225198266);
		
		// PIXY ID #6
		//listener.setPixyIDFront(-1028254399);

		listener.listForPixyFront(blocks);

		listener.setDebug(false);
		listener.start(); // vision data going into <blocks> now

	}
	
	public boolean getLatestAngle(Angle targetAngle) {
		if(blocks.isEmpty()) return false;
		
		VisionData vd = blocks.getLast();
		
		if(vd.timestamp < Timer.getFPGATimestamp() - IMAGE_TIMEOUT) {
			return false;
		}

		double angle = sense.robotAngle.get();
		
		angle += ((X_RES / 2) - vd.x) * X_DEG_PER_PIXEL; 
		
		targetAngle.set(angle);
		return true;
	}

	public void run() {
		
	}
}
