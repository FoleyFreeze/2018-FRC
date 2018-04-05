package org.usfirst.frc.team910.robot.util;

import java.util.LinkedList;

public class LimitedStack<T> extends LinkedList<T>{
	
	private static final long serialVersionUID = 1L;
	private int limitedSize;
	
	public LimitedStack(int size) {
		super();
		limitedSize = size;
	}
	
	@Override
	public boolean add(T object) {
		if(this.size() >= limitedSize) {
			this.removeFirst();
		}
		return super.add(object);
	}
	
}
