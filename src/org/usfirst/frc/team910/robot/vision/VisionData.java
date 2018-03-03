package org.usfirst.frc.team910.robot.vision;

// message received format:
// camera number, frame number, number of blocks, <block id, signature, x, y, width, height>
// each message can have more than one <block id, signature, x, y, width, height>

public class VisionData {
	double timestamp;
	int frame;
	int blockID;
    int sig;
    int x;
    int y;
    int w;
    int h;
}