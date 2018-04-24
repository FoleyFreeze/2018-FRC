package org.usfirst.frc.team910.robot.auton.steps;

import org.usfirst.frc.team910.robot.util.Path;

import edu.wpi.first.wpilibj.Timer;

public class AutoGather extends AutonStep {
	
	public AutoGather() {
		
	}

	@Override
	public void init() {
		//Path.startRecord();
		//in.recordPath = true;
	}

	public void run() {
		in.liftFlip = true;
		in.autoGather = true;
		in.gather = true;
	}

	double startTime = 0;
	boolean startedCount = false;
	
	@Override
	public boolean isDone() {
		if(sense.hasCube && !startedCount) {			
			startTime = Timer.getFPGATimestamp();
			startedCount = true;
			return false;
		} else if(startedCount && startTime + 0 < Timer.getFPGATimestamp()) {
			in.liftFlip = false;
			in.autoGather = false;
			in.gather = false;
			//in.recordPath = false;
			return true;	
		}
		return false;
	}

}
