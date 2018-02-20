package org.usfirst.frc.team910.robot.vision;

import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.BlockingQueue;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

import edu.wpi.first.wpilibj.Timer;
import java.lang.Math;

public class VisionServer {

    public VisionServer(VisionPing vp) {
    	
        pixy1ID = -1;
        pixy1 = null;
        
        pixy2ID = -1;
        pixy2 = null;
        
        debug = false; // No debug output by default
       
        response = vp;
    
        pingComplete = false;
        
	}

    // ping the vision system
    // remaining steps
    // 
    // Other things that could happen here:
    // Start a background thread that runs a function periodically to ping automatically
    // Save the result of the ping inside VisionServer and compare it with other ping responses that
    // happen in the background and detect differences
    //
    // timeout in seconds: up to timeout amount of time allowed for ping to complete
	synchronized public VisionPingInfo ping(int timeout) {

        VisionPingInfo pInfo = new VisionPingInfo();
        double pingTimer;
        String pingResponse;
		String[] parts; // Each element stores one of the parts of the single string message parse on the delimiter ','
	
        // Put ping request in the table
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable(PIXY_TABLE);
        NetworkTableEntry pingout, pingin;
        pingout = table.getEntry(PING_OUT_KEY);
        pingout.setNumber(PING_OUT_DATA);
        
        // Start timer
        pingTimer = Timer.getFPGATimestamp() + timeout;
        pInfo.result = ~0;

        do {
        	
        	// did we receive pingin message response to pingout request?
            pingin = table.getEntry(PING_IN_KEY);
            pingResponse = pingin.getString(String.valueOf(PING_OUT_DATA));
            
    		parts = pingResponse.split(",");
    		int pingResult = Integer.parseInt(parts[0]);
    		
    		// We sent the Pi PING_OUT_DATA
    		// We expect the Pi to return PING_OUT_DATA + 1
    		// This is simply arbitrary but the point is simply to make the Pi do some calculation so we know it's alive  		
        	if (pingResult == (PING_OUT_DATA + 1)) {
            	pInfo.result = 0;
            	break; // Ping is good, stop waiting for a ping response
        	}
        	        	
        	// Ping response not yet received; if there's time, try again
        } while (pingTimer < (Timer.getFPGATimestamp() + Math.abs(timeout)));
        
        // When we get here, the ping either worked, or timed out
        if(pInfo.result == 0) {
        	
            // ping worked; populate object with ping info
    		convertStringMessageFromAsciiToValues(parts, pInfo);
            
    	    // TODO: setup a listener for the cameras that exist; the listeners that
    	    // get setup will take the object recognized messages and put them in the queue
    		
    		// Once the higher-level robot code calls ping() for the first time,
            // a periodic ping is scheduled to occur in the background on a separate
            // thread
            if(!pingComplete) {
            	
            	// TODO: Start the periodic background ping here 
            	
            	// Periodic ping only needs to be scheduled once
            	pingComplete = true;
            	
            }
            
        }
        
        return (pInfo); 		
	}

	// Set debug mode
	// flag == true, debug enabled; flag == false, debug disabled
	public void debug(boolean flag) {
		
		if(flag) { debug = true; } else { debug = false; }
		
	}
	
	// associate camera with a queue
	public boolean queueForPixyN(int pixyNumber, int pixyID, BlockingQueue queue) {
		
		boolean result = false;
		
        switch(pixyNumber) {
        case 1:
        	this.pixy1ID = pixyID;
        	this.pixy1 = queue;
        	result = true;
        	break;
        case 2:
        	this.pixy2ID = pixyID;
        	this.pixy2 = queue;
        	result = true;
        	break;
        default:
        	if(debug) { 
        		System.out.printf("VisionServer.queueForPixyN: invalid pixy number %d", pixyNumber);
        	}
		}
        return (result);
    }
	private void convertStringMessageFromAsciiToValues(String[] msg, VisionPingInfo info) {
		// TODO
	}
	private int pixy1ID;
	private BlockingQueue pixy1 = null;

	private int pixy2ID;
	private BlockingQueue pixy2 = null;
    
	private boolean debug = false;

    private VisionPing response;

    private boolean pingComplete;
    private static final String PIXY_TABLE = "pixy";
    
    private static final String PING_IN_KEY = "pingin";

    private static final String PING_OUT_KEY = "pingout";
    
    private static final int PING_OUT_DATA  = 909;

}

