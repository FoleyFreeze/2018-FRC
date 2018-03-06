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

/**
 * HOW TO INITIALIZE ALL THIS STUFF:
 *  
 * 1. Create an instance of the VisionServer (the system should only have one VisionServer object)
 * 2. Ping the Raspberry Pi; the Pi will wait for a ping request and will not send any object info until it receives and replies to a ping
 * 3. Parse the VisionPingInfo object returned by the ping
 * 4. Check the values as you want to see if they make sense in VisionPingInfo
 * 4a. For example, if you're using 2 Pixy cameras, did the ping return 2 Pixy cameras?
 * 5. Associate a queue with each Pixy you want to get data from
 * 6. Enable saving object info for a particular Pixy
 * 7. Start the VisionServer to put object info in the queue
 * 8. Profit
 * 
 * Actual Java code which implements the steps above (not tested):
 * VisionServer vs = new VisionServer();
 * 
 * VisionPingInfo pi;
 * 
 * pi = vs.ping(1000); // Wait 1 second for Pi to respond to ping request
 * 
 * if (!pi.result ) {
 * 
 *     // ping worked
 *     
 *     // At a minimum, check for the number of Pixy cameras
 *     // This code is not here
 *     
 * }
 * 
 * Let's say there are 2 Pixy cameras (1 is Pixy front, 2 is Pixy back)
 *
 * vs.toSaveOrNotToSave(1, true);
 * vs.toSaveOrNorToSave(2, true);
 *
 * vs.sBlockingQueue<VisionData> queueFront = new ArrayBlockingQueue<VisionData>(1024); // queue Front
 * vs.sBlockingQueue<VisionData> queueFront = new ArrayBlockingQueue<VisionData>(1024); // queue Back
 *
 * vs.start();
 * Now object info will be put in the queues, if Pixy recognizes something
 * 
 * You can use any of the methods in the BlockingQueue interface to manipulate the queues that you provided to the VisionServer
 * Please see:
 * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html
 * 
 */
public class VisionServer {

	public VisionServer() {

		// Initialize queue references
		pixyFront = null;
		pixyBack = null;

		pingComplete = false;
		inst = null;
		table = null;

		listener = null;		

		// No debug output by default
		debug = false;

	}

	// ping the vision system
	// remaining steps
	//
	// Other things that could happen here:
	// Start a background thread that runs a function periodically to ping
	// automatically
	// Save the result of the ping inside VisionServer and compare it with other
	// ping responses that
	// happen in the background and detect differences
	//
	// timeout in seconds: up to timeout amount of time allowed for ping to complete
	synchronized public VisionPingInfo ping(int timeout) {

		pInfo = new VisionPingInfo();
		double pingTimer;
		String pingResponse;
		String[] parts; // Each element stores one of the parts of the single string message parse on
						// the delimiter ','

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
			// This is simply arbitrary but the point is simply to make the Pi do some
			// calculation so we know it's alive
			if (pingResult == (PING_OUT_DATA + 1)) {
				pInfo.result = 0;
				break; // Ping is good, stop waiting for a ping response
			}

			// Ping response not yet received; if there's time, try again
		} while (pingTimer < Timer.getFPGATimestamp());

		// When we get here, the ping either worked, or timed out
		if (pInfo.result == 0) {

			// ping worked; populate object with ping info
			convertPingMsgFromAsciiToValues(parts);

			// TODO: setup a listener for the cameras that exist; the listeners that
			// get setup will take the object recognized messages and put them in the queue

			// Once the higher-level robot code calls ping() for the first time,
			// a periodic ping is scheduled to occur in the background on a separate
			// thread
			if (!pingComplete) {

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

		if (flag) {
			debug = true;
		} else {
			debug = false;
		}

	}

	public void setPixyIDFront(int id) {
		
		pixyIDFront = id;
		
	}
	
	public void setPixyIDBack(int id) {
		
		pixyIDBack = id;
		
	}
	
	// Enable / disable saving camera data
	// orientation: 1 = front, 2 = back
	// flag == true, debug enabled; flag == false, debug disabled
	public void toSaveOrNotToSave(int orientation, boolean flag) {

		if (orientation == 1) {
			listener.saveValuesFront(flag);
		} else if (orientation == 2) {
			listener.saveValuesBack(flag);
		}
	}

	
	// associate camera with a queue and start listening for messages from the
	// associated network table entry for the
	// specified Pixy
	public boolean queueForPixyN(int orientation, BlockingQueue queue) {

		boolean result = false;

		switch (orientation) {
		case 1:
			this.pixyFront = queue;
			result = true;
			break;
		case 2:
			this.pixyBack = queue;
			result = true;
			break;
		default:
			if (debug) {
				String s = String.format("VisionServer.queueForPixyN: invalid pixy number %d\n", orientation);
				System.out.println(s);
				SmartDashboard.putString("VisionServerDebug", s);
			}
		}
		return (result);
	}

	public void start() {
		
		listener = new VisionObjectDataPixyListener();
		
		listener.setDebug(debug);
		listener.setPixyIDFront(pixyIDFront);
		listener.setPixyIDBack(pixyIDBack);
		
		listener.setQueueBack(pixyFront);
		listener.setQueueFront(pixyBack);
		
		table.addEntryListener("pixyobjdata", listener, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

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

	private BlockingQueue pixyFront;
	private BlockingQueue pixyBack;

	private boolean pingComplete;
	private VisionPingInfo pInfo;

	private static final String PIXY_TABLE = "pixy";
	private static final String PING_IN_KEY = "pingin";
	private static final String PING_OUT_KEY = "pingout";
	private static final int PING_OUT_DATA = 909;
	private static final int PING_IN_RESPONSE = 0;
	private static final int PING_IN_PI_INFO = 1;
	private static final int PING_IN_NUM_PIXY = 2;
	private static final int PING_IN_PIXY_ID = 3;
	private static final int PING_IN_PIXY_FW_VERSION = 4;
	private static final int PING_IN_PIXY_ID_2 = 5;
	private static final int PING_IN_PIXY_FW_VERSION_2 = 6;

	private NetworkTableInstance inst;
	private NetworkTable table;

	private VisionObjectDataPixyListener listener;

	private boolean debug;

	private int pixyIDFront;
	private int pixyIDBack;
	
}
