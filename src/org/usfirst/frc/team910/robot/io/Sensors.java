package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.components.Elevator;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors extends Component {

	private AHRS navx;
	private AnalogInput frontCenter;
	private AnalogInput frontRight;
	private AnalogInput frontLeft;
	private AnalogInput rearCenter;
	private AnalogInput rearRight;
	private AnalogInput rearLeft;
	
	

	public Angle robotAngle;

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
	public double distRC;
	public double distRR;
	public double distFL;
	public double distRL;
	
	public static final double[] DIST_POINTS = {1.57, 2.36, 3.15, 3.94,  4.72, 5.51, 6.3,  7.09, 7.87, 8.66,  9.45, 10.24, 11.02, 11.81};
	public static final double[] VOLT_POINTS = { 2.7,    2,  1.5,  1.3,   1.1,  0.9, 0.8,  0.75, 0.65,  0.6,  0.55,   0.5,  0.45,   0.4};

	public Sensors() {
		navx = new AHRS(SPI.Port.kMXP);
		navx.zeroYaw();
		robotAngle = new Angle(0);
		pdp = new PowerDistributionPanel();
		frontCenter = new AnalogInput(ElectroBach.F_CNT_DIST);
		frontRight = new AnalogInput(ElectroBach.F_RGT_DIST);
		frontLeft = new AnalogInput(ElectroBach.F_LFT_DIST);
		rearCenter = new AnalogInput(ElectroBach.R_CNT_DIST);
		rearRight = new AnalogInput(ElectroBach.R_RGT_DIST);
		rearLeft = new AnalogInput(ElectroBach.R_LEFT_DIST);
		
	}

	public void read() {
		if(in.resetEnc) {
			out.resetEncoders();
		}
		
		out.readEncoders();
		SmartDashboard.putNumber("L Drive Enc", leftDist);
		SmartDashboard.putNumber("R Drive Enc", rightDist);
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

		robotAngle.set(-navx.getYaw());
		
		distFC = Elevator.interp(VOLT_POINTS, DIST_POINTS, frontCenter.getVoltage());
		distFR = Elevator.interp(VOLT_POINTS, DIST_POINTS, frontRight.getVoltage());
		distFL = Elevator.interp(VOLT_POINTS, DIST_POINTS, frontLeft.getVoltage());
		distRC = Elevator.interp(VOLT_POINTS, DIST_POINTS, rearCenter.getVoltage());
		distRR = Elevator.interp(VOLT_POINTS, DIST_POINTS, rearRight.getVoltage());
		distRL = Elevator.interp(VOLT_POINTS, DIST_POINTS, rearLeft.getVoltage());
		
		SmartDashboard.putNumber("distFC", distFC);
		SmartDashboard.putNumber("distFR", distFR);
		SmartDashboard.putNumber("distFL", distFL);
		SmartDashboard.putNumber("distRC", distRC);
		SmartDashboard.putNumber("distRR", distRR);
		SmartDashboard.putNumber("distRL", distRL);
	}

	public void reset() {
		// zero encoders
		//out.resetEncoders(); //FIXME: undo this before real competitions
		out.readEncoders();

		// zero navx
		navx.zeroYaw();
	}

}