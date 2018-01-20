package org.usfirst.frc.team910.robot.io;

import org.usfirst.frc.team910.robot.Component;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

public class Sensors extends Component {

	private AHRS navx;

	Encoder leftEncoder;
	Encoder rightEncoder;

	public double leftDist;
	public double rightDist;
	public Angle robotAngle;

	public Sensors() {
		navx = new AHRS(SPI.Port.kMXP);
		navx.zeroYaw();
		robotAngle = new Angle(0);
		leftEncoder = new Encoder(3, 4);
		rightEncoder = new Encoder(6, 5);
		leftEncoder.setDistancePerPulse(1);
		rightEncoder.setDistancePerPulse(1);

	}

	public void read() {
		leftDist = leftEncoder.getDistance();
		rightDist = rightEncoder.getDistance();
		robotAngle.set(navx.getYaw());

	}
}
