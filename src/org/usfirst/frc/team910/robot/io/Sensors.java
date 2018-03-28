package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.components.Elevator;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors extends Component {

	private AHRS navx;
	private AnalogInput frontCenter;
	private AnalogInput frontRight;
	private AnalogInput frontLeft;

	public boolean robotDisabled = true;
	
	public Angle robotAngle;
	public double angleOffset = 0;

	public double leftDist = 0;
	public double rightDist = 0;
	public double liftPos = 0;
	public double armPosL= 0;
	public double armPosR= 0;
	// public double gatherLeftPos = 0;
	// public double gatherRightPos = 0;
	public PowerDistributionPanel pdp;
	
	
	String gameData;

	public boolean goalsLeft = false;
	public boolean goalsRight = false;
	public boolean goalsZig = false;
	public boolean goalsZag = false;
	
	public boolean switchIsLeft;
	public boolean scaleIsLeft;

	//infrared distance sensors
	public double distFC;
	public double distFR;
	public double distFL;
	
	//public static final double[] DIST_POINTS = {1.57, 2.36, 3.15, 3.94,  4.72, 5.51, 6.3,  7.09, 7.87, 8.66,  9.45, 10.24, 11.02, 11.81};
	//public static final double[] VOLT_POINTS = { 2.7,    2,  1.5,  1.3,   1.1,  0.9, 0.8,  0.75, 0.65,  0.6,  0.55,   0.5,  0.45,   0.4};
	public static final double[] VOLT_POINTS = { 0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.75, 0.8, 0.9, 1.1, 1.3, 1.5, 2, 2.7};
	public static final double[] DIST_POINTS = {11.81,11.02,10.24,9.45,8.66,7.87,7.09,6.3,5.51,4.72,3.94,3.15,2.36,1.57};
	
	public Sensors() {
		Component.sense = this;
		
		navx = new AHRS(SPI.Port.kMXP);
		navx.zeroYaw();
		angleOffset = -90;
		robotAngle = new Angle(0);
		pdp = new PowerDistributionPanel(0);
		frontCenter = new AnalogInput(ElectroBach.F_CNT_DIST);
		frontCenter.setOversampleBits(1);
		frontCenter.setAverageBits(1);
		frontRight = new AnalogInput(ElectroBach.F_RGT_DIST);
		frontRight.setOversampleBits(1);
		frontRight.setAverageBits(1);
		frontLeft = new AnalogInput(ElectroBach.F_LFT_DIST);
		frontLeft.setOversampleBits(1);//6
		frontLeft.setAverageBits(1);//3
		
	}
	
	public double prevLeftPos;
	public double prevRightPos;
	public double leftVel;
	public double rightVel;
	public double prevLeftVel;
	public double prevRightVel;
	public double leftAccel;
	public double rightAccel;
	public double dt;
	public double oldTime;

	
	private int numNavxTries = 0;
	
	public void read() {
		double time = Timer.getFPGATimestamp();
		dt = time - oldTime;
		oldTime = time;
		
		//if the navx is acting up, try to reconnect
		/*if(!navx.isConnected() && numNavxTries < 10) {
			navx = new AHRS(SPI.Port.kMXP);
			numNavxTries++;
		}*/
		
		if(in.resetEnc) {
			out.resetEncoders();
			//navx.zeroYaw();
			angleOffset = -navx.getYaw() - 90;
		}
		
		out.readEncoders();
		
		leftVel = (leftDist - prevLeftPos) / dt;
		rightVel = (rightDist - prevRightPos) / dt;
		leftAccel = (leftVel - prevLeftVel) / dt;
		rightAccel = (rightVel - prevRightVel) / dt;
		
		prevLeftPos = leftDist;
		prevRightPos = rightDist;
		prevLeftVel = leftVel;
		prevRightVel = rightVel;
		
		SmartDashboard.putNumber("DT", dt);
		SmartDashboard.putNumber("L Drive Enc", leftDist);
		SmartDashboard.putNumber("R Drive Enc", rightDist);
		SmartDashboard.putNumber("L Drive Vel", leftVel);
		SmartDashboard.putNumber("R Drive Vel", rightVel);
		SmartDashboard.putNumber("L Drive Accel", leftAccel);
		SmartDashboard.putNumber("R Drive Accel", rightAccel);
		SmartDashboard.putNumber("Lift Enc", liftPos);
		SmartDashboard.putNumber("L Arm Enc", armPosL);
		SmartDashboard.putNumber("R Arm Enc", armPosR);
		// SmartDashboard.putNumber("Left Gather Enc", gatherLeftPos);
		// SmartDashboard.putNumber("Right Gather Enc", gatherRightPos);

		gameData = DriverStation.getInstance().getGameSpecificMessage();

		if(gameData != null && gameData.length() == 3) {
			if(gameData.charAt(0) == 'L') switchIsLeft = true;
			else switchIsLeft = false;
			if(gameData.charAt(1) == 'L') scaleIsLeft = true;
			else scaleIsLeft = false;
			
			if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
				goalsLeft = true;
			} else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
				goalsRight = true;
			} else if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
				goalsZig = true;
			} else {
				goalsZag = true;
			}
		}
		SmartDashboard.putString("gameData", gameData);

		robotAngle.set(-navx.getYaw() - angleOffset);
		SmartDashboard.putNumber("Yaw", robotAngle.get());
		
		distFC = Elevator.interp(VOLT_POINTS, DIST_POINTS, frontCenter.getAverageVoltage());
		distFR = Elevator.interp(VOLT_POINTS, DIST_POINTS, frontRight.getAverageVoltage());
		distFL = Elevator.interp(VOLT_POINTS, DIST_POINTS, frontLeft.getAverageVoltage());
		
		SmartDashboard.putNumber("distFC", ((int) (distFC * 10))/10.0);
		SmartDashboard.putNumber("distFR", ((int) (distFR * 10))/10.0);
		SmartDashboard.putNumber("distFL", ((int) (distFL * 10))/10.0);
	}

	public void reset() {
		// zero encoders
		//out.resetEncoders(); //FIXME: undo this before real competitions
		out.readEncoders();

		// zero navx
		navx.zeroYaw();
	}

}