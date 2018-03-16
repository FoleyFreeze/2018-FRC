package org.usfirst.frc.team910.robot.components;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.vision.*;

public class Vision extends Component {

	ArrayList<VisionData> blocks = new ArrayList<VisionData>();

	public Vision() {

		VisionObjectDataPixyListener listener = new VisionObjectDataPixyListener();
		listener.saveValuesFront(true);

		// PIXY ID #8
		listener.setPixyIDFront(-1225198266);

		listener.listForPixyFront(blocks);

		listener.setDebug(true);
		listener.start(); // vision data going into <blocks> now

	}

	public void run() {

	}
}
