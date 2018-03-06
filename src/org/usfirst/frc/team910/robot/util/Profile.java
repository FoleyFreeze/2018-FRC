package org.usfirst.frc.team910.robot.util;


public class Profile {
	private static final double[][] TestRoute = {{ 60, 40 , 120} , //
												{0, -90, 0}} ;
	private static final double MAX_VELOC = 60;
	private static final double MAX_ACCEL = 60;
	private Path[] paths;
	public Profile(double[][] route, double startPosition, double startVelocity, double endVelocity) {
		int numPaths = route[0].length;
		paths = new Path[numPaths];
		
		double[] inVel = new double[numPaths];
		double[] outVel = new double[numPaths];
		//default to max velocity, unless something limits it
		for(int i=0;i<numPaths;i++) {
			inVel[i] = MAX_VELOC;
			outVel[i] = MAX_VELOC;
		}
		
		inVel[0] = startVelocity;
		outVel[numPaths - 1] = endVelocity;
		
		//start at 0, countup
		for (int i=0; i<numPaths; i++ ) {
			//if the path is straight / no angle
			if(route[1][i] == 0) {
				double Vf = Math.sqrt(inVel[i] * inVel[i] + 2 * MAX_ACCEL * route[0][i] );
				outVel[i] = Math.min(outVel[i], Vf);
				
				if(i != numPaths - 1) {
					inVel[i + 1] = outVel[i];
				}
			//if the path is a curve / some angle	
			}else {
				double Vf = Math.sqrt(inVel[i] * inVel[i] + 2 * MAX_ACCEL * route[0][i] );
				
			}
			
		}
		
		//start at size of array, countdown
		for (int i=numPaths - 1; i>=0; i-- ) {
			
			
		}
	
	
	
	
	}
}