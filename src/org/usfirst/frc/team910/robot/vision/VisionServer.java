package org.usfirst.frc.team910.robot.vision;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.BlockingQueue;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.EntryListenerFlags;


import edu.wpi.first.wpilibj.Timer;
import java.lang.Math;

public class VisionServer {

    public VisionServer() {
    	
    	// Initialize queue references
        pixy1 = null;
        pixy2 = null;
        listener1 = null;
        listener2 = null;  
        pingComplete = false;
        inst = null;
        table = null;
        
        // No debug output by default
        debug = false;

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

        pInfo = new VisionPingInfo();
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
    		int pingResult = Integer.parseInt(parts[PING_IN_RESPONSE]);
    		
    		// We sent the Pi PING_OUT_DATA
    		// We expect the Pi to return PING_OUT_DATA + 1
    		// This is simply arbitrary but the point is simply to make the Pi do some calculation so we know it's alive  		
        	if (pingResult == (PING_OUT_DATA + 1)) {
            	pInfo.result = 0;
            	break; // Ping is good, stop waiting for a ping response
        	}
        	        	
        	// Ping response not yet received; if there's time, try again
        } while (pingTimer < Timer.getFPGATimestamp());
        
        // When we get here, the ping either worked, or timed out
        if(pInfo.result == 0) {
        	
            // ping worked; populate object with ping info
        	convertPingMsgFromAsciiToValues(parts);
            
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
	
	// associate camera with a queue and start listening for messages from the associated network table entyy for the 
	// specified Pixy
	public boolean queueForPixyN(int pixyNumber, BlockingQueue queue) {
		
		boolean result = false;
		
        switch(pixyNumber) {
        case 1:
        	this.pixy1 = queue;
        	table.addEntryListener("pixy1objdata", new VisionObjectDataPixy1Listener(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        	result = true;
        	break;
        case 2:
        	this.pixy2 = queue;
        	table.addEntryListener("pixy2objdata", new VisionObjectDataPixy2Listener(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        	result = true;
        	break;
        default:
        	if(debug) { 
                String s = String.format("VisionServer.queueForPixyN: invalid pixy number %d\n", pixyNumber);
        		System.out.println(s);
        		SmartDashboard.putString("VisionServerDebug",s);
        	}
		}
        return (result);
    }
	// Parse the single ping message string into the type VisionPingInfo
	private void convertPingMsgFromAsciiToValues(String[] msg) {
		
		pInfo.piInfo = msg[PING_IN_PI_INFO];
        pInfo.numCameras = Integer.parseInt(msg[PING_IN_NUM_PIXY]);
        pInfo.cameraIDs[0] = Integer.parseInt(msg[PING_IN_PIXY_ID]);
        pInfo.cameraFWVersions[0] = msg[PING_IN_PIXY_FW_VERSION];
        pInfo.cameraIDs[1] = Integer.parseInt(msg[PING_IN_PIXY_ID_2]);
        pInfo.cameraFWVersions[1] = msg[PING_IN_PIXY_FW_VERSION_2];
        
	}
	private BlockingQueue pixy1;
	private BlockingQueue pixy2;
    
    private boolean pingComplete;
    private VisionPingInfo pInfo;

    private static final String PIXY_TABLE = "pixy";
    private static final String PING_IN_KEY = "pingin";
    private static final String PING_OUT_KEY = "pingout";
    private static final int PING_OUT_DATA  = 909;
    private static final int PING_IN_RESPONSE = 0;
    private static final int PING_IN_PI_INFO = 1;
    private static final int PING_IN_NUM_PIXY = 2;
    private static final int PING_IN_PIXY_ID = 3;
    private static final int PING_IN_PIXY_FW_VERSION = 4;
    private static final int PING_IN_PIXY_ID_2 = 5;
    private static final int PING_IN_PIXY_FW_VERSION_2 = 6;    
 
    private VisionObjectDataPixy1Listener listener1;
    private VisionObjectDataPixy2Listener listener2;
    
    private NetworkTableInstance inst;
    private NetworkTable table;
    
	private boolean debug;

}

