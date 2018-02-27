package org.usfirst.frc.team910.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;

public class VisionObjectDataPixy1Listener implements TableEntryListener {

	public void valueChanged(NetworkTable table, java.lang.String key, NetworkTableEntry entry, NetworkTableValue value,
			int flags) {
		if (saveValue1 == true) {
			// TODO: Need to parse the pixy2objdata data and put in queue

		}
	}

	// TODO: Need to parse the pixy1objdata data and put in queue
	// Set the object save mode pixy1
	// flag == true, save object data for pixy2; flag == false, ignore the info.
	public void saveValues(boolean flag) {

		if (flag) {
			saveValue1 = true;
		} else {
			saveValue1 = false;
		}

	}

	private boolean saveValue1;
}
