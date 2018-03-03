package org.usfirst.frc.team910.robot.vision;

import java.util.concurrent.BlockingQueue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;

public class VisionObjectDataPixy1Listener implements TableEntryListener {

	public VisionObjectDataPixy1Listener(BlockingQueue<VisionData> queue) {

		this.queue = queue;

	}

	public void valueChanged(NetworkTable table, java.lang.String key, NetworkTableEntry entry, NetworkTableValue value,
			int flags) {

		if (saveValue) { // save object data if enabled
			// Data from Network Table goes here from entry "pixy1objdata"
			// It's the most recent object data update
			String msg = value.getString();

			// Elements of message sent by Pi area separated by ','
			String[] msg_parsed = msg.split(",");
			int numBlocks = 0;
			int BLOCK_SIZE = 6;
			int frameNum = 0;

			VisionData newData = null;

			try { // BlockQueue can raise exceptions

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
					queue.put(newData);
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // set interrupt flag
			}
		}
	}

	// Set the object save mode pixy1
	// flag == true, save object data for pixy1; flag == false, ignore the info.
	public void saveValues(boolean flag) {

		if (flag) {
			saveValue = true;
		} else {
			saveValue = false;
		}

	}

	private boolean saveValue;
	private BlockingQueue<VisionData> queue;

}
