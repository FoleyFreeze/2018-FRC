package org.usfirst.frc.team910.robot.vision;

import java.util.LinkedList;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionObjectDataPixyListener  {

	public VisionObjectDataPixyListener() {
		
		inst = null;
		table = null;

		debug = false;
		
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
    	
		if (flag) {
			debug = true;
		} else {
			debug = false;
		}

    }
 // associate Pixy with a list and start listening for messages from the
 	// associated network table entry for this Pixy
 	public void listForPixyFront(LinkedList list) {

		this.listFront = list;

 	}

 	public void listForPixyBack(LinkedList list) {

		this.listBack = list;

 	}
 	
 	public void start() {
		
		inst = NetworkTableInstance.getDefault();
		table = inst.getTable(PIXY_TABLE);
		table.addEntryListener(PIXY_DATA, (table, key,  entry, value, flags) -> {

			int currentPixy;
			
			LinkedList<VisionData> currentList;
	        
			// Data from Network Table goes here from entry PIXY_DATA
			// It's the most recent object data update
			String msg = value.getString();

			if (debug == true) {
				System.out.println("VisionObjectDataPixyListener message received = " + msg);
			}
			
			// Elements of message sent by Pi area separated by ','
			String[] msg_parsed = msg.split(",");
			
			// Get the pixy in this message
			currentPixy = Integer.parseInt(msg_parsed[0]);
			
			// Set the right queue based on the pixy in this Pi message
			if (currentPixy == pixyFrontId && saveFront == true ) {
				currentList = listFront;
			} else if (currentPixy == pixyBackId && saveBack == true ) {
				currentList = listBack;
			} else {
				return;
			}

			int numBlocks = 0;
			int frameNum = 0;
            int blockIndex = 3;
            
			VisionData newData = null;

				// Check if there are any blocks in this message
				numBlocks = Integer.parseInt(msg_parsed[2]);

				if (numBlocks > 0) {

					frameNum = Integer.parseInt(msg_parsed[1]);

					// Convert message data into VisionData object data
					for (int i = 0; i < numBlocks; i++) {

						newData = new VisionData();
						newData.timestamp = Timer.getFPGATimestamp();
						newData.blockID = Integer.parseInt(msg_parsed[blockIndex + 0]);
						newData.sig = Integer.parseInt(msg_parsed[blockIndex + 1]);
						newData.x = Integer.parseInt(msg_parsed[blockIndex + 2]);
						newData.y = Integer.parseInt(msg_parsed[blockIndex + 3]);
						newData.w = Integer.parseInt(msg_parsed[blockIndex + 4]);
						newData.h = Integer.parseInt(msg_parsed[blockIndex + 5]);
						newData.frame = frameNum;
						blockIndex += BLOCK_SIZE;
						
					}
					// Add new vision data to the list
					currentList.add(newData);
				}
			
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

	}
	
	private boolean saveFront;
	private boolean saveBack;
	private LinkedList<VisionData> listFront;
	private LinkedList<VisionData> listBack;
    private int pixyFrontId;
    private int pixyBackId;
	private static final int BLOCK_SIZE = 6;
    private boolean debug;
	private static final String PIXY_TABLE = "pixy";
	private static final String PIXY_DATA = "pixyobjdata";
	private NetworkTableInstance inst;
	private NetworkTable table;
}
