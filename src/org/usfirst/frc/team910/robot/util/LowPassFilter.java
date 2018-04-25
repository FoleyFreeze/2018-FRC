package org.usfirst.frc.team910.robot.util;

public class LowPassFilter {

	double filter;
	double value;
	
	public LowPassFilter(double filter) {
		this.filter = filter;
		value = 0;
	}
	
	public void reset(double initVal) {
		value = initVal;
	}
	
	public double filt(double newVal) {
		value += (newVal - value) * filter;
		return value;
	}
	
}
