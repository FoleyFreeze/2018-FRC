package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

public class Sensors extends Component {

	private AHRS navx;

	public double leftDist;
	public double rightDist;
	public Angle robotAngle;

	public Sensors() {
		navx = new AHRS(SPI.Port.kMXP);
		navx.zeroYaw();
		robotAngle = new Angle(0);

	}

	public void read() {
		out.readEncoders();
		robotAngle.set(-navx.getYaw());

	}
}
