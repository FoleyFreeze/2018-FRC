package org.usfirst.frc.team910.robot.vision;

// message received format:
// camera number, frame number, number of blocks, <block id, signature, x, y, width, height>
// each message can have more than one <block id, signature, x, y, width, height>

public class VisionData {
	public double timestamp;
	public int frame;
	public int blockID;
	public int sig;
	public int x;
	public int y;
	public int w;
	public int h;
	public double robotAngle;
}