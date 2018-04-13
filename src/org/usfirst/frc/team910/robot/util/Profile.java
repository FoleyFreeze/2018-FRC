package org.usfirst.frc.team910.robot.util;


public class Profile {							
/*	OLD NO WORKO											//maxAccel, maxVeloc, dist, startPos, startVeloc, endVeloc	
	public static final double [][] centerSwitchR = {{500, 80, 24, 0, 0, 80},//L
												    {500, 80, 24, 0, 0, 80},//R
												    {500, 80, 81, 25.5, 70, 80},//L
												    {500, 68.891, 69.5, 25.5, 70,80},//R
												    {500, 62.718, 45, 106.9, 80, 0},//L
												    {500, 80, 55, 96.079, 86.482, 0}//R
												                                   };
*/	
	public static final double [][] test = {{100.0,100.0,24.0,0.0,0.0,100.0},
			{100.0,100.0,24.0,0.0,0.0,100.0},
			{100.0,100.0,123.0,24.49999999999999,70.0,0.0},
			{100.0,52.046875,80.0,24.49999999999999,70.0,0.0}};
	
	public static final double[][] testStraight =
		{{175.0,90.0,120.0,0.0,0.0,0.0},
		 {175.0,90.0,120.0,0.0,0.0,0.0}
		};
	
	public static final double [][] centerSwitchL ={{200.0,120.0,5.0,0.0,0.0,120.0},
			{200.0,120.0,5.0,0.0,0.0,120.0},
			{100.0,28.1953125,45.0,5.76,48.0,60.0},
			{100.0,60.0,80.0,5.76,48.0,60.0},
			{100.0,60.0,80.0,51.54389529243757,61.292569357855314,30.0},
			{100.0,28.1953125,45.0,86.64000000000011,60.0,30.0},
			{200.0,120.0,5.0,131.89110317390055,28.819410537125854,0.0},
			{200.0,120.0,5.0,131.7592235135388,30.39481374688341,0.0}};
	
	
	public static final double[][] centerSwitchR ={{200.0,120.0,5.0,0.0,0.0,120.0},
			{200.0,120.0,5.0,0.0,0.0,120.0},
			{100.0,60.0,75.0,5.76,48.0,60.0},
			{100.0,24.046875,40.0,5.76,48.0,60.0},
			{100.0,21.28125,37.0,81.8400000000001,60.0,30.0},
			{100.0,60.0,78.0,46.04027977801291,60.46532854126052,30.0},
			{200.0,120.0,10.0,119.1904147169256,31.14615455212891,0.0},
			{200.0,120.0,10.0,124.18956206625114,29.49819557790549,0.0}};
	
	
	public static final double [][] straightScaleL = {{300.0,120.0,227.0,0.0,0.0,60.0},
			{300.0,120.0,227.0,0.0,0.0,60.0},
			{300.0,36.0,38.0,228.0495833333335,54.49999999999919,0.0},
			{300.0,16.5859375,20.0,228.0495833333335,54.49999999999919,0.0}};
	
	public static final double curvedStraightScaleL[][] = {{200.0,120.0,190.0,0.0,0.0,40.0},
													      {200.0,120.0,190.0,0.0,0.0,40.0},
													      {100.0,40.0,53.0,190.13222222222245,39.333333333333044,40.0},
													      {100.0,22.6328125,33.0,190.13222222222245,39.333333333333044,40.0},
													      {100.0,22.0234375,33.0,243.73000000000098,40.0,0.0},
													      {100.0,40.0,53.0,223.5449657810379,41.01888238071675,0.0}};
	
	public static final double [][] straightScaleR = {{200.0,120.0,232.0,0.0,0.0,60.0},
			{200.0,120.0,232.0,0.0,0.0,60.0},
			{100.0,5.1015625,20.0,232.68638888888913,57.66666666666619,0.0},
			{100.0,36.0,40.0,232.68638888888913,57.66666666666619,0.0}};
	
	public static final double [][] curvedStraightScaleR ={{200.0,100.0,183.0,0.0,0.0,70.0},
			{200.0,100.0,183.0,0.0,0.0,70.0},
			{100.0,25.890625,38.0,184.19437500000004,66.5,40.0},
			{100.0,60.0,65.0,184.19437500000004,66.5,40.0},
			{100.0,40.0,65.0,222.56705784840435,40.9211017652366,0.0},
			{100.0,21.109375,38.0,249.46619268663156,39.31458333333413,0.0}};
	public static final double [][] leftToRightScale = {{200.0,100.0,168.0,0.0,0.0,80.0},
														{200.0,100.0,168.0,0.0,0.0,80.0},
														{100.0,80.0,103.0,169.56000000000003,76.0,80.0},
														{100.0,23.21875,60.0,169.56000000000003,76.0,80.0},
														{200.0,80.0,120.0,273.48,80.0,60.0},
														{200.0,80.0,120.0,230.37212059160166,81.00879037684906,60.0},
														{100.0,22.203125,46.0,393.92437500000113,58.49999999999602,0.0},
														{100.0,60.0,95.0,350.8183558965427,58.49363963734446,0.0}};
	
	public static final double [][] rightToLeftScale = {{200.0,100.0,162.0,0.0,0.0,80.0},
			{200.0,100.0,162.0,0.0,0.0,80.0},
			{100.0,18.28125,60.0,163.56000000000003,76.0,80.0},
			{100.0,80.0,106.0,163.56000000000003,76.0,80.0},
			{200.0,80.0,114.0,224.44193447705678,81.09492521367342,60.0},
			{200.0,80.0,114.0,270.67999999999995,80.0,60.0},
			{100.0,60.0,97.0,338.5935384692866,59.4925071173506,0.0},
			{100.0,21.51171875,46.0,384.829375000001,59.49999999999625,0.0}};
	
	public static final double [][] straightSwitchR ={{300.0,120.0,77.0,0.0,0.0,80.0},
			{300.0,120.0,77.0,0.0,0.0,80.0},
			{300.0,22.203125,30.0,78.24995370370368,75.16666666666671,0.0},
			{300.0,60.0,65.0,78.24995370370368,75.16666666666671,0.0}};
	
	public static final double [][] straightSwitchL = {{300.0,120.0,147.0,0.0,0.0,80.0},
													  {300.0,120.0,147.0,0.0,0.0,80.0},
													  {300.0,60.0,40.0,147.99773148148157,76.16666666666629,0.0},
													  {300.0,20.8203125,20.0,147.99773148148157,76.16666666666629,0.0}};
	
	public static final double [][] left1ToScale = {{100.0,40.0,60.0,0.0,0.0,0.0},
												   {100.0,31.46875,50.0,0.0,0.0,0.0}};
			
	public static final double [][] right1ToScale = {{100.0,31.46875,50.0,0.0,0.0,0.0},
													{100.0,40.0,60.0,0.0,0.0,0.0}};			
}