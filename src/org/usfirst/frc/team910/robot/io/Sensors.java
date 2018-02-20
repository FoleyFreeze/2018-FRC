package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors extends Component {

	private AHRS navx;

	
	public Angle robotAngle;

	public double leftDist = 0;
	public double rightDist = 0;
	public double liftPos = 0;
	public double armPos = 0;
	
	public Sensors() {
		navx = new AHRS(SPI.Port.kMXP);
		navx.zeroYaw();
		robotAngle = new Angle(0);

	}

	public void read() {
		out.readEncoders();
		SmartDashboard.putNumber("Left Drive Enc", leftDist);
		SmartDashboard.putNumber("Right Drive Enc", rightDist);
		SmartDashboard.putNumber("Lift Position Enc", liftPos);
		SmartDashboard.putNumber("Arm Position Enc", armPos);
		
		robotAngle.set(-navx.getYaw());
	}
	
	public void reset() {
		//zero encoders
		out.resetEncoders();
		out.readEncoders();
		
		//zero navx
		navx.zeroYaw();
	}
	
}