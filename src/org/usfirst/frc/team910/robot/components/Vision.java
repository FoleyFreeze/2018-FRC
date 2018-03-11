package org.usfirst.frc.team910.robot.components;

import java.util.concurrent.ArrayBlockingQueue;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.vision.VisionData;
import org.usfirst.frc.team910.robot.vision.VisionServer;

public class Vision extends Component {
	
	ArrayBlockingQueue<VisionData> queueFront;
	ArrayBlockingQueue<VisionData> queueBack;
	
	public Vision(){
		/*
		
		// See the comment for the VisionServer constructor for information about how to initialize vision stuff
		VisionServer vs = new VisionServer();
		vs.toSaveOrNotToSave(1, true);
		//vs.toSaveOrNotToSave(2, true);
		
		//PIXY ID #9
		vs.setPixyIDFront(-488255155);
		//vs.setPixyIDBack();
		
		queueFront = new ArrayBlockingQueue<VisionData>(1024); // object info for front camera goes in this queue
		queueBack = new ArrayBlockingQueue<VisionData>(1024); // object info for back camera goes in this queue
		
		vs.queueForPixyN(1, queueFront);
		//vs.queueForPixyN(2, queueBack);
		
		vs.start();
		
		*/
	}
	
	public void run() {
		
	}
}
