package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors extends Component {

	private AHRS navx;

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

	

	public Sensors() {
		navx = new AHRS(SPI.Port.kMXP);
		navx.zeroYaw();
		robotAngle = new Angle(0);
		pdp = new PowerDistributionPanel();
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

		if(gameData != null && gameData.length() > 3) {
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
	}

	public void reset() {
		// zero encoders
		//out.resetEncoders(); //FIXME: undo this before real competitions
		out.readEncoders();

		// zero navx
		navx.zeroYaw();
	}

}