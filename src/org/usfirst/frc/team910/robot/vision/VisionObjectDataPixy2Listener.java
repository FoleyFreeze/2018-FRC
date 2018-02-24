package org.usfirst.frc.team910.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;

public class VisionObjectDataPixy2Listener implements TableEntryListener {

	public void valueChanged(NetworkTable table, java.lang.String key, NetworkTableEntry entry, NetworkTableValue value, int flags) {
	
		// TODO: Need to parse pixy1objdata data and put in queue
	}
	
}
