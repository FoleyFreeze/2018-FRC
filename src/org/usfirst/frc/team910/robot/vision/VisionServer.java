package org.usfirst.frc.team910.robot.vision;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

public class VisionServer {

	public VisionServer() {
	    
		camera1ID = -1;
		camera1 = null;

		camera2ID = -1;
		camera2 = null;
	    
		debug = false; // No debug output by default
		
	    pixyObjectDataNT = NetworkTableInstance.getDefault().getTable("Pixy");

	}

	// TODO: ping the vision system
	// steps:
	// 1. send a message to the Pi
	// 2. wait for a response to this message within <timeout> amount of time
	// 3. make sure that the message received can be determined to have been 
	// just now received, and not stale; question for vision team members: how can we do this ?
	// 4. convert the message into some abstract data type
	// 5. put the message data into an abstract data type instance supplied by 
	// the code that called ping()
    // 6. setup a listener for the cameras that exist; the listeners that 
	// get setup will take the object recognized messages and put them in the queues
	// 
	// Other things that could happen here:
	// Start a background thread that runs a function periodically to ping automatically
	// Save the result of the ping inside VisionServer and compare it with other pings that 
	// happen in the background and detect differences 
	// timeout in ms: up to timeout amount of time allowed for ping to complete
	synchronized public VisionPingInfo ping(int timeout) {

		VisionPingInfo pInfo = new VisionPingInfo();
		
		// Initialize with fail
		pInfo.result = ~0;
		
		// TODO: do stuff here
		
		return (pInfo);
		
	}

	// TODO: Set debug mode
	// flag == true, debug enabled; flag == false, debug disabled
	public void debug(boolean flag) {
		
		if(flag) { debug = true; } else { debug = false; }
		
	}
	
	// TODO: associate camera with a queue
	public boolean queueForCameraN(int cameraNumber, int pixyID, BlockingQueue queue) {
		
		boolean result = false;
		
        switch(cameraNumber) {
        case 1:
        	this.camera1ID = pixyID;
        	this.camera1 = queue;
        	result = true;
        	break;
        case 2:
        	this.camera2ID = pixyID;
        	this.camera2 = queue;
        	result = true;
        	break;
        default:
        	if(debug) { 
        		System.out.printf("VisionServer.queueForCameraN: invalid camera number %d", cameraNumber);
        	}
		}
        return (result);
    }
	
	private int camera1ID;
	private BlockingQueue camera1 = null;

	private int camera2ID;
	private BlockingQueue camera2 = null;
    
	private boolean debug = false;
	
    private NetworkTable pixyObjectDataNT;
    private NetworkTableEntry pixyObjectEntry;

}

