package org.usfirst.frc.team910.robot.vision;

import java.util.concurrent.BlockingQueue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;

public class VisionObjectDataPixyListener implements TableEntryListener {

	public VisionObjectDataPixyListener() {

		debug = false;
		
	}

	public void setQueueFront(BlockingQueue queue) {
		
		queueFront = queue;
	}
	
    public void setQueueBack(BlockingQueue queue) {
	
    	queueBack = queue;
		
	}
    public void saveValuesFront(boolean save) {
    
    	if(save == true) {
    		saveFront = true;
    	} else {
    		saveFront = false;
    	}
    }
    public void saveValuesBack(boolean save) {
        
    	if(save == true) {
    		saveBack = true;
    	} else {
    		saveBack = false;
    	}
    }
    
    public void setPixyIDFront(int Id) {
    	
        pixyFrontId = Id;
		
	}
    public void setPixyIDBack(int Id) {
    	
    	pixyBackId = Id;
    	
	}
    
    public void setDebug (boolean flag) {
    	
        debug = flag;
    }
    
	public void valueChanged(NetworkTable table, java.lang.String key, NetworkTableEntry entry, NetworkTableValue value,
			int flags) {

		int currentPixy;
		BlockingQueue<VisionData> currentQueue;
        
		// Data from Network Table goes here from entry "pixy1objdata"
		// It's the most recent object data update
		String msg = value.getString();

		if (debug == true) {
			System.out.println("VisionObjectDataPixyListener message received = " + msg);
			SmartDashboard.putString("VisionServerDebug", msg);
		}
		
		// Elements of message sent by Pi area separated by ','
		String[] msg_parsed = msg.split(",");
		
		// Get the pixy in this message
		currentPixy = Integer.parseInt(msg_parsed[0]);
		
		// Set the right queue based on the pixy in this Pi message
		if (currentPixy == pixyFrontId && saveFront == true ) {
			currentQueue = queueFront;
		} else if (currentPixy == pixyBackId && saveBack == true ) {
			currentQueue = queueBack;
		} else {
			return;
		}

		int numBlocks = 0;
		int frameNum = 0;

		VisionData newData = null;

		try { // BlockingQueue can raise exceptions

			// Check if there are any blocks in this message
			numBlocks = Integer.parseInt(msg_parsed[2]);

			if (numBlocks > 0) {

				frameNum = Integer.parseInt(msg_parsed[1]);

				// Convert message data into VisionData object data
				for (int i = 0; i < numBlocks; i++) {

					newData = new VisionData();
					newData.timestamp = Timer.getFPGATimestamp();
					newData.blockID = Integer.parseInt(msg_parsed[(i * (BLOCK_SIZE + 0)) + 2]); // block ID
					newData.sig = Integer.parseInt(msg_parsed[(i * (BLOCK_SIZE + 1)) + 2]); // signature
					newData.x = Integer.parseInt(msg_parsed[(i * (BLOCK_SIZE + 2)) + 2]); // x
					newData.y = Integer.parseInt(msg_parsed[(i * (BLOCK_SIZE + 3)) + 2]); // y
					newData.w = Integer.parseInt(msg_parsed[(i * (BLOCK_SIZE + 4)) + 2]); // width
					newData.h = Integer.parseInt(msg_parsed[(i * (BLOCK_SIZE + 5)) + 2]); // height
					newData.frame = frameNum;
				}
				// Add this vision data to the queue
				currentQueue.put(newData);
			}
		}
		 catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // set interrupt flag
		}
	}

	private boolean saveFront;
	private boolean saveBack;
	private BlockingQueue<VisionData> queueFront;
	private BlockingQueue<VisionData> queueBack;
    private int pixyFrontId;
    private int pixyBackId;
	private static final int BLOCK_SIZE = 6;
    private boolean debug;
    
}
