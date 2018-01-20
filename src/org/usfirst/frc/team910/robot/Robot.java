/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team910.robot;

import org.usfirst.frc.team910.robot.components.Arm;
import org.usfirst.frc.team910.robot.components.Climber;
import org.usfirst.frc.team910.robot.components.DriveTrain;
import org.usfirst.frc.team910.robot.components.Elevator;
import org.usfirst.frc.team910.robot.components.Gatherer;
import org.usfirst.frc.team910.robot.components.Vision;
import org.usfirst.frc.team910.robot.io.Inputs;
import org.usfirst.frc.team910.robot.io.Outputs;
import org.usfirst.frc.team910.robot.io.Sensors;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	
	DriveTrain drive;
	Climber climb;
	Elevator elevate;
	Gatherer gather;
	Vision view;
	Inputs input;
	Outputs output;
	Sensors sensor;
	Arm arm;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		output = new Outputs();
		drive = new DriveTrain(output);
		climb = new Climber();
		elevate = new Elevator();
		gather = new Gatherer();
		view = new Vision();
		input = new Inputs();
		sensor = new Sensors();
		arm = new Arm();
		Component.set(input, output, sensor, arm, climb, drive, elevate, gather, view);
	}

	/**
	 * This function is called once when autonomous is first active.
	 */
	@Override
	public void autonomousInit() {
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
	}
	
	/**
	 * This function is called once when teleop is activiated.
	 */
	@Override
	public void teleopInit() {
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		input.read();
		sensor.read();
		drive.run(input,sensor);
		climb.run();
		elevate.run();
		gather.run();
		view.run();
		arm.run();
	}
	
	/**
	 * This function is called periodically when the robot is disabled.
	 */
	@Override
	public void disabledPeriodic() {
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}
