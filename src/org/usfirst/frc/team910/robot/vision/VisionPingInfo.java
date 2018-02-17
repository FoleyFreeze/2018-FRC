package org.usfirst.frc.team910.robot.vision;

public class VisionPingInfo {

	int result; // 0 = ping responded within timeout time; other than 0 = ping didn't respond
	String piInfo;
	int numCameras;
    int[] cameraIDs;
    String[] cameraFWVersions;

    public VisionPingInfo () {
    
    	cameraIDs = new int[2]; // Only up to 2 cameras are supported 
    	cameraFWVersions = new String[2]; // Only up to 2 cameras are supported, FW version info as string looks like this. XX.XX.XX

    }
    
}
