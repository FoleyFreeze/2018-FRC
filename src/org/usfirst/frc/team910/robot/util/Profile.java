package org.usfirst.frc.team910.robot.util;


public class Profile {
	private static final double[][] TestRoute = {{ 60, 40 , 120} , //
												{0, -90, 0}} ;
	private Path[] paths;
	public Profile(double[][] route, double startPosition, double startVelocity, double endVelocity) {
		int numPaths = route[0].length;
		paths = new Path[numPaths];
		
		double[] inVel = new double[numPaths];
		double[] outVel = new double[numPaths];
		inVel[0] = startVelocity;
		outVel[0] = endVelocity;
		
		for (int i=1; i<numPaths; i++ ) {
			for (int x=1; x<numPaths; x++ ) {

			}
		}
	}
}