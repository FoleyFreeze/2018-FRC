package org.usfirst.frc.team910.robot.util;
import java.util.ArrayList;

public class PathRunner {
/*
    public static final double SCRUB_FACTOR = 2;
	public static final double TURNING_WIDTH = 23.2375 * SCRUB_FACTOR;
	public static final double ROBOT_WIDTH = 26;
	public static final double ROBOT_HEIGHT = 36;
	
	static final double ACCEL = 300;
	static final double MAX_VEL = 80;
	
	public static final double STRAIGHT = 1.0;
	public static final double TURN = 2.0;
	
	public static final double[][] testProfile =
	    // type    dist velTrgt endVel 
	    {{STRAIGHT, 120,  120,  120},
	    //type distL distR velTrgt endVel
	    {TURN,  120,  90,    120,   0}
	    };
	
	public static void buildTurn(Path pL, Path pR, double distL, double distR, double targetVel, double endVel) {
		if(distL > distR) {
			pL.buildPath(ACCEL, targetVel, distL, endPosL, endVelL, endVel);
			double targetTime = pL.positions.size() * Path.DT;
			
			double maxVel = targetVel;
			double minVel = 1;
			
			//TODO: Have Steven explain how this works
			for(int i = 0; i<10; i++) {
				double testVel = (maxVel + minVel) / 2;
				
				pR.buildPath(ACCEL, testVel, distR, endPosR, endVelR, endVel);
				
				double time = pR.positions.size() * Path.DT;
				if(time == targetTime) break;
				if(time < targetTime) maxVel = testVel;
				else minVel = testVel;	
			}
			
		} else {
			pR.buildPath(ACCEL, targetVel, distR, endPosR, endVelR, endVel);
			double targetTime = pR.positions.size() * Path.DT;
			
			double maxVel = targetVel;
			double minVel = 1;
			
			for(int i = 0; i<10; i++) {
				double testVel = (maxVel + minVel) / 2;
				
				pL.buildPath(ACCEL, testVel, distL, endPosL, endVelL, endVel);
				
				double time = pL.positions.size() * Path.DT;
				if(time == targetTime) break;
				if(time < targetTime) maxVel = testVel;
				else minVel = testVel;
				
			}
		}
		
		
 	}
	
	static ArrayList<Path> pathsL = new ArrayList<>();
    static ArrayList<Path> pathsR = new ArrayList<>();
    static double totalTime = 0;
    
    static double endPosL = 0;
    static double endPosR = 0;
    static double endVelL = 0;
    static double endVelR = 0;
	
	private static void parseArray(double[] array){
	    Path pathL = new Path();
	    Path pathR = new Path();
	    
	    if(array[0] == STRAIGHT){
	        pathL.buildPath(ACCEL, array[2], array[1], endPosL, endVelL, array[3]);
	        pathR.buildPath(ACCEL, array[2], array[1], endPosR, endVelR, array[3]);
	        
	    } else if(array[0] == TURN){
	        buildTurn(pathL, pathR, array[1], array[2], array[3], array[4]);
	        
	    } else {
	        System.out.println("ERROR: Incorrect profile array format");
	        System.exit(-1);
	    }
	    
	    int lastIdxL = pathL.positions.size()-1;
        endPosL = pathL.positions.get(lastIdxL);
        endVelL = pathL.velocities.get(lastIdxL);
        int lastIdxR = pathR.positions.size()-1;
        endPosR = pathR.positions.get(lastIdxR);
        endVelR = pathR.velocities.get(lastIdxR);
        totalTime += pathL.positions.size() * Path.DT;
        
        if(pathL.positions.size() != pathR.positions.size()){
            System.out.println("ERROR: Paths have different lengths");
            System.exit(-1);
        }
        
        pathsL.add(pathL);
        pathsR.add(pathR);
	}
	
	static ArrayList<Double> x = new ArrayList<>();
    static ArrayList<Double> y = new ArrayList<>();
    static ArrayList<Double> theta = new ArrayList<>();
    
    static ArrayList<Double> cornersX = new ArrayList<>();
    static ArrayList<Double> cornersY = new ArrayList<>();
    
    static ArrayList<Double> cornersLFX = new ArrayList<>();
    static ArrayList<Double> cornersLFY = new ArrayList<>();
    
    static ArrayList<Double> cornersRFX = new ArrayList<>();
    static ArrayList<Double> cornersRFY = new ArrayList<>();
    
    static ArrayList<Double> cornersLRX = new ArrayList<>();
    static ArrayList<Double> cornersLRY = new ArrayList<>();
    
    static ArrayList<Double> cornersRRX = new ArrayList<>();
    static ArrayList<Double> cornersRRY = new ArrayList<>();
    
    static ArrayList<Double> FW = new ArrayList<>();
    static ArrayList<Double> LH = new ArrayList<>();
    static ArrayList<Double> RW = new ArrayList<>();
    static ArrayList<Double> RH = new ArrayList<>();
	
	private static void simulateDrive(){
	    
        // start of robot
        x.add(29.0 + ROBOT_WIDTH / 2);
        y.add(ROBOT_HEIGHT / 2);
        theta.add(Math.PI / 2);

        double prevPosL = 0;
        double prevPosR = 0;

        
        for (int i = 0; i < pathsL.size() && i < pathsR.size(); i++) {
            //
            for (int j = 0; j < pathsL.get(i).positions.size() && j < pathsR.get(i).positions.size(); j++) {
                // find where we were
                double prevX = x.get(x.size() - 1);
                double prevY = y.get(y.size() - 1);
                double prevTheta = theta.get(theta.size() - 1);

                double deltaL = pathsL.get(i).positions.get(j) - prevPosL;
                double deltaR = pathsR.get(i).positions.get(j) - prevPosR;

                prevPosL = pathsL.get(i).positions.get(j);
                prevPosR = pathsR.get(i).positions.get(j);

                // find deltaTheta

                double deltaTheta = Math.atan2(deltaR - deltaL, TURNING_WIDTH / 2);
                double averageTheta = prevTheta + deltaTheta / 2;
                
                double cT = prevTheta + deltaTheta;
                theta.add(cT);
                
                //find delta x and y
                
                double deltaX = (deltaL + deltaR)/2 * Math.cos(averageTheta);
                double deltaY = (deltaL + deltaR)/2 * Math.sin(averageTheta);
                
                double cX = prevX + deltaX;
                double cY = prevY + deltaY;
                x.add(cX);
                y.add(cY);
                
                
                //calculate where the 4 corners of the robot are
                double sinT = Math.sin(cT);
                double cosT = Math.cos(cT);
                
                double w2 = ROBOT_WIDTH/2;
                double h2 = ROBOT_HEIGHT/2;
                
                //LF
                double lfx = cX - w2*sinT + h2*cosT;
                double lfy = cY + h2*sinT + w2*cosT;
                
                //RF
                double rfx = cX + w2*sinT + h2*cosT;
                double rfy = cY + h2*sinT - w2*cosT;
                
                //LR
                double lrx = cX - w2*sinT - h2*cosT;
                double lry = cY - h2*sinT + w2*cosT;
                
                //RR
                double rrx = cX + w2*sinT - h2*cosT;
                double rry = cY - h2*sinT - w2*cosT;
                
                //add lists
                cornersLFX.add(lfx);
                cornersLFY.add(lfy);
                cornersRFX.add(rfx);
                cornersRFY.add(rfy);
                cornersLRX.add(lrx);
                cornersLRY.add(lry);
                cornersRRX.add(rrx);
                cornersRRY.add(rry);
                
                cornersX.add(lfx);
                cornersY.add(lfy);
                cornersX.add(rfx);
                cornersY.add(rfy);
                cornersX.add(rrx);
                cornersY.add(rry);
                cornersX.add(lrx);
                cornersY.add(lry);
                //go back if you want to draw squares
                //cornersX.add(lfx);
                //cornersY.add(lfy);
                
                FW.add(dist(lfx,rfx,lfy,rfy));
                LH.add(dist(lfx,lrx,lfy,lry));
                RW.add(dist(lrx,rrx,lry,rry));
                RH.add(dist(rfx,rrx,rfy,rry));
                
            }
        }
	}
	
	public static double dist(double x1, double x2, double y1, double y2) {
	    return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	
	public static void main(String[] args) {

	    double[][] activeProf = testProfile;
	    
	    for(int i=0; i<activeProf.length; i++){
	        parseArray(activeProf[i]);
	    }
	    
	    simulateDrive();

	    //print paths
	    Path tempPath;
	    String s = "{";
	    for(int i=0; i<pathsL.size(); i++){
	        if(i==0) s += "{";
	        else s += ",\n{";
	        
	        //left path
	        tempPath = pathsL.get(i);
	        s += tempPath.accelMax + ",";
	        s += tempPath.velocMax + ",";
	        s += tempPath.dist + ",";
	        s += tempPath.startPos + ",";
	        s += tempPath.startVel + ",";
	        s += tempPath.endVel + "},\n{";
	        
	        //right path
	        tempPath = pathsR.get(i);
	        s += tempPath.accelMax + ",";
            s += tempPath.velocMax + ",";
            s += tempPath.dist + ",";
            s += tempPath.startPos + ",";
            s += tempPath.startVel + ",";
            s += tempPath.endVel + "}";
	    }
	    s += "};";
	    System.out.println(s);
	    
	    //print simulated drive
	    for(int i=0; i<x.size(); i++) {
            //System.out.println(x.get(i) + "\t" + y.get(i) + "\t" + theta.get(i));
	        System.out.format("%.1f\t%.1f\t%.1f\n", x.get(i), y.get(i), theta.get(i)*180/Math.PI);
        }
	    
	    //print out robot corner loctations
	    /*
	    for(int i=0; i<cornersX.size(); i++) {
            //System.out.println(x.get(i) + "\t" + y.get(i) + "\t" + theta.get(i));
            System.out.format("%.1f\t%.1f\n", cornersX.get(i), cornersY.get(i));
        }
        */
	    
	    //print distances to check corners
	    /*
	    for(int i=0; i<FW.size(); i++) {
	        System.out.format("%.3f\t%.3f\t%.3f\t%.3f\n", FW.get(i), RW.get(i), LH.get(i), RH.get(i));
	    }
        */
	    
//	}

}
