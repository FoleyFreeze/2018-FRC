package org.usfirst.frc.team910.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.auton.steps.AutonSet;
import org.usfirst.frc.team910.robot.auton.steps.AutonStep;
import org.usfirst.frc.team910.robot.auton.steps.DriveForward;
import org.usfirst.frc.team910.robot.auton.steps.DriveProfile;
import org.usfirst.frc.team910.robot.auton.steps.DriveStraightPath;
import org.usfirst.frc.team910.robot.auton.steps.DriveTurnStep;
import org.usfirst.frc.team910.robot.auton.steps.ElevatorPosition;
import org.usfirst.frc.team910.robot.auton.steps.EndStep;
import org.usfirst.frc.team910.robot.auton.steps.IfInterface;
import org.usfirst.frc.team910.robot.auton.steps.IfSet;
import org.usfirst.frc.team910.robot.auton.steps.SeriesSet;
import org.usfirst.frc.team910.robot.auton.steps.ShootStep;
import org.usfirst.frc.team910.robot.auton.steps.StartStep;
import org.usfirst.frc.team910.robot.auton.steps.WaitStep;
import org.usfirst.frc.team910.robot.components.Elevator;
import org.usfirst.frc.team910.robot.util.Profile;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class AutonMain extends Component {

	//private ArrayList<AutonStep> steps;
	private AutonSet currentAuton;
	
	final String startPosition = "default";
	final String PositionLeft = "Left";
	final String PositionCenter = "Center";
	final String PositionRight = "Right";
	
	private SeriesSet driveForward;
	private SeriesSet onlySwitch;
	private SeriesSet centerSwitch;
	private SeriesSet straightScale;
	private SeriesSet testProfile;
	
	//public int currentStep = 0;
	
	public static final int STRAIGHT_ONLY = 0;
	public static final int LEFT = 1;
	public static final int CENTER = 2;
	public static final int RIGHT = 3;
	public static final int SCALE = 4;
	public static final int SWITCH = 5;
	public static final int EXCHANGE = 6;
	public static final int TEST =7;
	SendableChooser<Integer> startLocation;
	SendableChooser<Integer> priority;
	
	public static class AutonOptions{
		int selectedStart;
		int selectedPriority;
		boolean switchIfLeft;
		boolean switchIfRight;
		boolean exchangeIfSwitch;
		boolean switchBeforeScale;
		
		boolean switchIsLeft;
		boolean scaleIsLeft;
	}
	public static AutonOptions options = new AutonOptions();
	
	public AutonMain() {
		
		startLocation = new SendableChooser<>();
		//startLocation.addDefault("Center", CENTER);//was straight only
		startLocation.addDefault("Straight" , STRAIGHT_ONLY);//straight only
		startLocation.addObject("Left", LEFT);
		//startLocation.addObject("Straight", STRAIGHT_ONLY);//was center 
		startLocation.addObject("Center", CENTER);
		startLocation.addObject("Right", RIGHT);
		startLocation.addObject("TEST", TEST);
		SmartDashboard.putData("AutoStartLocation", startLocation);
		
		priority = new SendableChooser<>();
		priority.addDefault("Switch", SWITCH);
		priority.addObject("Scale", SCALE);
		priority.addObject("Exchange", EXCHANGE);
		SmartDashboard.putData("AutoPriority", priority);
		
		
		//build autons
		driveForward = new SeriesSet();
		AutonBuilder b = new AutonBuilder(driveForward);
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
			b.add(new DriveForward(105, 1.5));
			b.add(new EndStep());
			b.end();
		
		onlySwitch = new SeriesSet();
		b.add(onlySwitch);
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
			b.add(new DriveForward(120, 1.5));
			
				b.add(new IfSet(new IfInterface() {
					public boolean choice() { //if the switch we are in front of is the one we should score on
						return options.selectedStart == LEFT && options.switchIsLeft ||
						      options.selectedStart == RIGHT && !options.switchIsLeft;
					}
				}));
				b.add(new ShootStep());
				b.add(new WaitStep(0));
				b.end();
				
			b.add(new EndStep());
			b.end();
		
		//NOT MP
		/*	
		centerSwitch = new SeriesSet();
		b.add(centerSwitch);
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
			b.add(new DriveForward(10, 0.25));
			
				b.add(new IfSet(new IfInterface() {
					public boolean choice() { //is the switch on the left or right
						return options.switchIsLeft;
					}
				}));
				b.add(new DriveTurnStep(-0.1, 0.45, 0.5)); //turn left
				b.add(new DriveTurnStep(0.4, -0.1, 0.45)); //turn right
				b.end();
				
				b.add(new IfSet(new IfInterface() {
					public boolean choice() { //is the switch on the left or right
						return options.switchIsLeft;
					}
				}));
				b.add(new DriveForward(45, 1.05));//left //85 linc
				b.add(new DriveForward(45, 1.25));//right //85 linc
				b.end();
				
				b.add(new IfSet(new IfInterface() {
					public boolean choice() { //is the switch on the left or right
						return options.switchIsLeft;
					}
				}));
				b.add(new DriveTurnStep(0, -0.3, 0.2)); //turn right
				b.add(new DriveTurnStep(-0.3, 0, 0.2));//turn left
				b.end();
				
			b.add(new DriveForward(5, 0.15));//was 10	
			b.add(new WaitStep(.1));
			b.add(new ShootStep());
			
			
			//wait and then attempt to drive back to pick up front pyramid
			b.add(new WaitStep(4));
			
				b.add(new IfSet(new IfInterface() {
					public boolean choice() { //is the switch on the left or right
						return options.switchIsLeft;
					}
				}));
				b.add(new DriveTurnStep(-0.45, 0.1, 0.5)); //turn left
				b.add(new DriveTurnStep(0.1, -0.4, 0.45)); //turn right
				b.end();
			
			b.add(new EndStep());
			b.end();
			//b.add(new DriveForward());
			*/
			
			
		//NOT MP
		/*
		straightScale = new SeriesSet();
		b.add(straightScale);
			b.add(new StartStep());
			
			//drive to the scale
			//b.add(new DriveStraightPath());
				//if we are on the same side as our scale, attempt to score
				b.add(new IfSet(new IfInterface() {
					public boolean choice() {
						return options.selectedStart == LEFT && options.scaleIsLeft
						    || options.selectedStart == RIGHT && !options.scaleIsLeft;
					}
				}));
					//for the true case, run this series
					b.add(new SeriesSet());
					b.add(new ElevatorPosition(Elevator.liftState.F_SCALE_POSITION));
					b.add(new WaitStep(2));
						b.add(new IfSet(new IfInterface() {
							public boolean choice() {
								return options.selectedStart == LEFT;
							}
						}));
						b.add(new DriveTurnStep(0.5, 0.05, 0.6));
						b.add(new DriveTurnStep(0.05, 0.5, 0.6));
						b.end();
					b.add(new ShootStep());
					b.end();
				//for the false case, wait for now
				b.add(new WaitStep(0));
				b.end();
			
			b.add(new EndStep());
			b.end();
		*/
			
		straightScale = new SeriesSet();
		b.add(straightScale);
			b.add(new StartStep());
			b.add(new DriveProfile(Profile.straightScaleL));
			b.add(new IfSet(new IfInterface() {
				public boolean choice() {
					return options.selectedStart == LEFT && options.scaleIsLeft
					    || options.selectedStart == RIGHT && !options.scaleIsLeft;
				}
			}));
				//for the true case, run this series
				b.add(new SeriesSet());
				b.add(new ElevatorPosition(Elevator.liftState.F_SCALE_POSITION));
				b.add(new ShootStep());
				b.end();
			//for the false case, wait for now
			b.add(new WaitStep(0));
			b.end();
		
		b.add(new EndStep());
		b.end();
		
		
		testProfile = new SeriesSet();
		b.add(testProfile);
			b.add(new StartStep());
			b.add(new DriveProfile(Profile.centerSwitchR));
			b.add(new EndStep());
		b.end();
		
	}
	
	
	public void init() {
		//currentStep = 0;
	}
	
	
	
	
	public void selectAuton() {
		
		//read driver station values
		options.selectedStart = startLocation.getSelected();
		options.selectedPriority = priority.getSelected();
		options.switchIfLeft = SmartDashboard.getBoolean("SwitchIfLeft", false);
		options.switchIfRight = SmartDashboard.getBoolean("SwitchIfRight", false);
		options.exchangeIfSwitch = SmartDashboard.getBoolean("ExchangeIfSwitch", false);
		options.switchBeforeScale = SmartDashboard.getBoolean("SwitchBeforeScale", false);
		
		getGameData();
		
		switch(options.selectedStart) {
		case STRAIGHT_ONLY:
			currentAuton = driveForward;
			break;
			
		case LEFT:
			currentAuton = onlySwitch;
			break;
			
		case CENTER:
			currentAuton = centerSwitch;
			break;
			
		case RIGHT:
			currentAuton = onlySwitch;
			break;
			
		case TEST:
			currentAuton = testProfile;
			break;
		}
		
		//if scale is selected, do that one
		switch(options.selectedPriority) {
		case SCALE:
			currentAuton = straightScale;
			break;
		}
		
	}
	
	public void getGameData() {
		options.switchIsLeft = sense.switchIsLeft;
		options.scaleIsLeft = sense.scaleIsLeft;
	}
	
	public void run() {
		
		currentAuton.run();
		
		/*
		steps.get(currentStep).run(); //run the functions of the step we are on
		//if step has error we move to the next step
		if(steps.get(currentStep).isError()) {
			currentStep = steps.size() - 1;
		}
		//if step finished move to next step in array
		else if(steps.get(currentStep).isDone()) {
			currentStep++;
			steps.get(currentStep).init(); //run initial code for next step
		}
		*/
	}

}
