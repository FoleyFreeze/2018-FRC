package org.usfirst.frc.team910.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team910.robot.Component;
import org.usfirst.frc.team910.robot.auton.steps.AutoGather;
import org.usfirst.frc.team910.robot.auton.steps.AutonSet;
import org.usfirst.frc.team910.robot.auton.steps.AutonStep;
import org.usfirst.frc.team910.robot.auton.steps.CheckMatchTime;
import org.usfirst.frc.team910.robot.auton.steps.DriveForward;
import org.usfirst.frc.team910.robot.auton.steps.DriveProfile;
import org.usfirst.frc.team910.robot.auton.steps.DriveRecPath;
import org.usfirst.frc.team910.robot.auton.steps.DriveStraightPath;
import org.usfirst.frc.team910.robot.auton.steps.DriveTurnStep;
import org.usfirst.frc.team910.robot.auton.steps.ElevatorPosition;
import org.usfirst.frc.team910.robot.auton.steps.EndStep;
import org.usfirst.frc.team910.robot.auton.steps.ErrorStep;
import org.usfirst.frc.team910.robot.auton.steps.IfInterface;
import org.usfirst.frc.team910.robot.auton.steps.IfSet;
import org.usfirst.frc.team910.robot.auton.steps.ParallelSet;
import org.usfirst.frc.team910.robot.auton.steps.RecordStart;
import org.usfirst.frc.team910.robot.auton.steps.RecordStop;
import org.usfirst.frc.team910.robot.auton.steps.ResetEncoders;
import org.usfirst.frc.team910.robot.auton.steps.ReverseProfile;
import org.usfirst.frc.team910.robot.auton.steps.SeriesSet;
import org.usfirst.frc.team910.robot.auton.steps.ShootStep;
import org.usfirst.frc.team910.robot.auton.steps.StartStep;
import org.usfirst.frc.team910.robot.auton.steps.TurnAngle;
import org.usfirst.frc.team910.robot.auton.steps.NegativeWait;
import org.usfirst.frc.team910.robot.auton.steps.WaitStep;
import org.usfirst.frc.team910.robot.components.Elevator;
import org.usfirst.frc.team910.robot.util.Path;
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
	
	private SeriesSet driveForward; //runs anywhere
	private SeriesSet onlySwitch; //must start infront of switch only shoot if its goalSwitch
	private SeriesSet centerSwitch; //start center priority switch
	private SeriesSet mpCenterSwitch;
	private SeriesSet scale;//no matta wat //start left or right priority scale
	private SeriesSet buddyScale;
	private SeriesSet testProfile; //test
	private SeriesSet straightScale;
	private SeriesSet test2Cube;
	private SeriesSet scaleSwitch;
	
	//public int currentStep = 0;
	
	public static final int STRAIGHT_ONLY = 0;
	public static final int CENTER_SWITCH = 1;
	public static final int CENTER_SWITCH_MP = 2;
	public static final int LEFT_SCALE_ONLY = 3;
	public static final int RIGHT_SCALE_ONLY = 4;
	public static final int LEFT_SCALE_SWITCH = 5;
	public static final int RIGHT_SCALE_SWITCH = 6;
	public static final int LEFT_SWITCH = 7;
	public static final int RIGHT_SWITCH = 8;
	public static final int LEFT_BUDDY_SCALE = 9;
	public static final int RIGHT_BUDDY_SCALE = 10;
	
	public static final int TEST =11;
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
		boolean startedLeft;
	}
	public static AutonOptions options = new AutonOptions();
	
	public AutonMain() {
		
		startLocation = new SendableChooser<>();
		//startLocation.addDefault("Center", CENTER);//was straight only
		startLocation.addDefault("Straight Only" , STRAIGHT_ONLY);//straight only
		startLocation.addObject("Left Switch", LEFT_SWITCH );
		//startLocation.addObject("Straight", STRAIGHT_ONLY);//was center 
		startLocation.addObject("Right Switch", RIGHT_SWITCH);
		startLocation.addObject("Center Switch", CENTER_SWITCH);
		startLocation.addObject("Center Switch MP", CENTER_SWITCH_MP);
		startLocation.addObject("Left Scale", LEFT_SCALE_ONLY);
		startLocation.addObject("Right Scale", RIGHT_SCALE_ONLY);
		startLocation.addObject("Left Scale + Switch Behind ", LEFT_SCALE_SWITCH);
		startLocation.addObject("Right Scale + Switch Behind ", RIGHT_SCALE_SWITCH);
		startLocation.addObject("Left BuddyScale", LEFT_BUDDY_SCALE);
		startLocation.addObject("Right BuddyScale", RIGHT_BUDDY_SCALE);
		startLocation.addObject("TEST", TEST);
		SmartDashboard.putData("AutoStartLocation", startLocation);
		
		/*
		priority = new SendableChooser<>();
		priority.addDefault("Switch", SWITCH);
		priority.addObject("Scale", SCALE);
		priority.addObject("Exchange", EXCHANGE);
		SmartDashboard.putData("AutoPriority", priority);
		*/
		
		//build autons
		driveForward = new SeriesSet();
		AutonBuilder b = new AutonBuilder(driveForward); {
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
			b.add(new DriveForward(105, 1.5));
			b.add(new EndStep());
			b.end(); }
		
		onlySwitch = new SeriesSet();
		b.add(onlySwitch); {
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
			b.add(new DriveForward(120, 1.5));
			
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //if the switch we are in front of is the one we should score on
					return options.startedLeft && options.switchIsLeft ||
					      !options.startedLeft && !options.switchIsLeft;
				}
			})); {
				b.add(new ShootStep());
				b.add(new WaitStep(0));
				b.end(); }
				
			b.add(new EndStep());
			b.end(); }
		
			
		mpCenterSwitch = new SeriesSet();
		b.add(mpCenterSwitch); {
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new SeriesSet()); {
					b.add(new ParallelSet()); {
						b.add(new DriveProfile(Profile.centerSwitchL,false));
						b.add(new SeriesSet()); {
							b.add(new WaitStep(3));  //2.2 msc
							b.add(new ShootStep());
							b.end(); }
					b.end(); }
				b.end(); }
				b.add(new ParallelSet()); {
					b.add(new DriveProfile(Profile.centerSwitchR,false));
					b.add(new SeriesSet()); {
						b.add(new WaitStep(3)); //2.2 msc
						b.add(new ShootStep());
						b.end(); }
					b.end(); }
				b.end(); }
			b.add(new EndStep());
			b.end(); }
			
		//NOT MP
	
		centerSwitch = new SeriesSet();
		b.add(centerSwitch); {
			b.add(new StartStep()); 
			b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
			
			
			b.add(new DriveForward(10, 0.25));
			
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new DriveTurnStep(-0.2, 0.8, 0.4)); //turn left
				b.add(new DriveTurnStep(0.8, -0.2, 0.4)); //turn right
				b.end(); }
				
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new DriveForward(65, 1.8));//left //65 //45 btroy //85 linc
				b.add(new DriveForward(50, 1.30));//right //45 btroy //85 linc
				b.end(); }
				
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new DriveTurnStep(1, -0.3, 0.35)); //left start
				b.add(new DriveTurnStep(-0.3, 1, 0.35)); //right start
				b.end(); }
				
			//b.add(new DriveForward(20, 0.7));//was 10
			b.add(new DriveTurnStep(0.6, 0.6, 0.3)); //1 sec
			b.add(new ParallelSet()); {
				b.add(new SeriesSet()); {
					//b.add(new WaitStep(.5));
					b.add(new ShootStep());
					b.end(); }
				b.add(new DriveTurnStep(0.2,0.2,1));
				b.end(); }
			
			
			//wait and then attempt to drive back
			//b.add(new WaitStep(0.5));
			
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new DriveTurnStep(-0.5, 0.1, 1.1)); //left start
				b.add(new DriveTurnStep(0.1, -0.5, 1.1)); //right start
				b.end(); }
			
			b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
			b.add(new DriveForward(40, 1));
			
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new DriveTurnStep(0.4, -0.05, 1)); //left start
				b.add(new DriveTurnStep(-0.05, 0.4, 1)); //right start
				b.end(); }
			
			b.add(new DriveForward(70, 1.4));//was 60
			
			b.add(new IfSet(new IfInterface() {
				public boolean choice() { //is the switch on the left or right
					return options.switchIsLeft;
				}
			})); {
				b.add(new DriveTurnStep(-0.4, 0.05, 1)); //left start
				b.add(new DriveTurnStep(0.05, -0.4, 1)); //right start
				b.end(); }
				
			b.add(new EndStep());
			b.end(); }
			//b.add(new DriveForward());
			
			
			
		//NOT MP
		
		straightScale = new SeriesSet();
		b.add(straightScale); {
			b.add(new StartStep());
			b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
			
			//drive to the scale
			//b.add(new DriveStraightPath());
			//if we are on the same side as our scale, attempt to score
			b.add(new IfSet(new IfInterface() {
				public boolean choice() {
					return options.startedLeft && options.scaleIsLeft;
				}
			})); {
				//for the true case, run this series
				b.add(new SeriesSet()); {
					b.add(new DriveProfile(Profile.straightScaleLeftFast));
					b.add(new ElevatorPosition(Elevator.liftState.F_SCALE_POSITION));
					b.add(new WaitStep(1));
					b.add(new DriveTurnStep(0.55, 0.05, 0.6)); //kick
					b.add(new ShootStep(-0.35));
					//b.add(new WaitStep(3));
					b.add(new DriveTurnStep(-.25, -.25, 3));
					b.add(new WaitStep(3));
					b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
					b.end(); }
	
				b.add(new SeriesSet()); {
					b.add(new DriveProfile(Profile.straightScaleR));
					b.add(new ElevatorPosition(Elevator.liftState.F_SCALE_POSITION));
					b.add(new WaitStep(1));
					b.add(new DriveTurnStep(0.05, 0.6, 0.6));//left kick
					b.add(new ShootStep(-0.35));
					//b.add(new WaitStep(3));
					b.add(new DriveTurnStep(-.25, -.25, 3));
					b.add(new WaitStep(3));
					b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
					b.end(); }
				b.end(); }
			b.add(new EndStep());
			b.end(); }
		
		
		scale = new SeriesSet();
		b.add(scale); {
			b.add(new StartStep());
			
//------------------------------------STRAIGHT SCALE----------------------------------------------------------------
			//if straight or cross scale
			b.add(new IfSet(new IfInterface() {
				public boolean choice() {
					return options.startedLeft && options.scaleIsLeft
						|| !options.startedLeft && !options.scaleIsLeft;					 
				}
			})); {
				//if left or right straight scale
				b.add(new IfSet(new IfInterface() {
					public boolean choice() {
						return options.startedLeft && options.scaleIsLeft;					 
					}
				})); {
						//start left goal left
					b.add(new SeriesSet()); { 
						
						//FIRST CUBE
						b.add(new ParallelSet()); {
							b.add(new DriveProfile(Profile.straightScaleLeftFast,10));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(2.0));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								b.add(new WaitStep(1.3));
								b.add(new ShootStep(-0.5));
								b.end(); }
							b.end(); }
						
						//PICKUP SECOND CUBE
						b.add(new RecordStart());
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.35, -0.1, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(1.1 + 0.2));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						b.add(new RecordStop());
						
						//SHOOT CUBE 2
						//b.add(new ResetEncoders());
						b.add(new ParallelSet()); {
							//b.add(new DriveProfile(Profile.right1ToScale));
							b.add(new DriveRecPath(20));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.1));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								//b.add(new WaitStep(1.3));
								b.add(new NegativeWait(0.5));
								b.add(new ShootStep(-0.7));
								b.end();}
							b.end();}
						
						//PICKUP CUBE 3
						b.add(new RecordStart());
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.4, -0.1, 2));//-0.45, 0.15 //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(1.0 + 0.3));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						b.add(new RecordStop());
						
						//SHOOT CUBE 3
						//b.add(new ResetEncoders());
						b.add(new ParallelSet()); {
							//b.add(new DriveProfile(Profile.right1ToScale));
							b.add(new DriveRecPath(0));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.1));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								//b.add(new WaitStep(1.3));
								b.add(new NegativeWait(0.3));
								b.add(new ShootStep(-0.7));
								b.end();}
							b.end();}
						
						//PICKUP CUBE 4
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.45, 0.15, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.8));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }

						b.end();}
						
					//RIGHT SIDE
					b.add(new SeriesSet()); { 
						
						//FIRST CUBE
						b.add(new ParallelSet()); {
							b.add(new DriveProfile(Profile.straightScaleRightFast,10));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(2.0));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								b.add(new WaitStep(1.3));
								b.add(new ShootStep(-0.6));
								b.end(); }
							b.end(); }
						
						//PICKUP SECOND CUBE
						b.add(new RecordStart());
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.1, -0.35, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(1.1 + 0.2));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						b.add(new RecordStop());
						
						//SHOOT CUBE 2
						//b.add(new ResetEncoders());
						b.add(new ParallelSet()); {
							//b.add(new DriveProfile(Profile.right1ToScale));
							b.add(new DriveRecPath(20));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.1));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								//b.add(new WaitStep(1.3));
								b.add(new NegativeWait(0.5));
								b.add(new ShootStep(-0.7));
								b.end();}
							b.end();}
						
						//PICKUP CUBE 3
						b.add(new RecordStart());
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.1, -0.4, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(1.0 + 0.3));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						b.add(new RecordStop());
						
						//SHOOT CUBE 3
						//b.add(new ResetEncoders());
						b.add(new ParallelSet()); {
							//b.add(new DriveProfile(Profile.right1ToScale));
							b.add(new DriveRecPath(0));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.1));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								//b.add(new WaitStep(1.3));
								b.add(new NegativeWait(0.3));
								b.add(new ShootStep(-0.7));
								b.end();}
							b.end();}
						
						//PICKUP CUBE 4
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.1, -0.45, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.8));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }

						b.end();}
					b.end(); }
				
	//------------------CROSS SCALE---------------------------------------------------------------------
			
			
				//start right have to cross to left
				b.add(new IfSet(new IfInterface() {
					public boolean choice() {
						return !options.startedLeft && options.scaleIsLeft;
					}
				})); {
					b.add(new SeriesSet()); { 
						b.add(new ParallelSet()); {
							b.add(new DriveProfile(Profile.rightToLeftScale));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(4.4+0.4)); 
								//b.add(new EndStep()); //TODO: remove when we want to complete cross scale auto
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION)); //TODO: re add this
								
								//moved shooting logic into this wait, so we start shooting before finishing driving
								b.add(new WaitStep(1.8 - 0.1));
								b.add(new ShootStep(-0.8));
								b.end(); }
							b.end(); }
						//b.add(new WaitStep(0.5));
						
						//record the driveturn and autogather
						b.add(new RecordStart());
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.35 , -0.1, 2)); //(-0.45 + 0.15, -0.05, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(1.0 +0.2));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						b.add(new RecordStop());
						
						//b.add(new ResetEncoders());
						b.add(new ParallelSet()); {
							//b.add(new DriveProfile(Profile.right1ToScale));
							b.add(new DriveRecPath(20));
							b.add(new SeriesSet()); {
								//b.add(new WaitStep(0.1));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								//b.add(new WaitStep(1.3));
								b.add(new NegativeWait(0.3 + 0.3));
								b.add(new ShootStep(-0.85));
								b.end();}
							b.end();}
						
						//go for cube 3
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.4 , -0.1, 2));//(-0.45, 0.05, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.8));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						
						b.end(); }
					
					b.add(new SeriesSet()); { 
						b.add(new ParallelSet()); {
							b.add(new DriveProfile(Profile.leftToRightScale));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(4.4+0.4)); 
								//b.add(new EndStep()); //TODO: remove when we want to complete cross scale auto
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION)); //TODO: re add this
								
								//moved shooting logic into this wait, so we start shooting before finishing driving
								b.add(new WaitStep(1.8 - 0.1));
								b.add(new ShootStep(-0.8));
								b.end(); }
							b.end(); }
						//b.add(new WaitStep(0.5));
						
						//record the driveturn and autogather
						b.add(new RecordStart());
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.1 , -0.35, 2));//(0.05, -0.45 + 0.15, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(1.0 +0.1));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						b.add(new RecordStop());
						
						//b.add(new ResetEncoders());
						b.add(new ParallelSet()); {
							//b.add(new DriveProfile(Profile.right1ToScale));
							b.add(new DriveRecPath(20));
							b.add(new SeriesSet()); {
								//b.add(new WaitStep(0.1));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								//b.add(new WaitStep(1.3));
								b.add(new NegativeWait(0.3 + 0.3));
								b.add(new ShootStep(-0.85));
								b.end();}
							b.end();}
						
						//go for cube 3
						b.add(new ParallelSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
							b.add(new DriveTurnStep(-0.1 , -0.4, 2));//(0.05, -0.45, 2)); //-0.2,-0.3 MSC
							b.add(new SeriesSet()); {
								b.add(new WaitStep(0.8));
								b.add(new AutoGather());
								b.end(); }
							b.end(); }
						
						b.end(); }
					b.end(); }//ends the left or right cross if
				b.end(); }
			b.add(new EndStep());
			b.end(); }
				/*
				b.add(new AutoGather());
				b.add(new ParallelSet());
					b.add(new DriveProfile(Profile.right1ToScale));
					b.add(new SeriesSet());
						b.add(new WaitStep(0.1));
						b.add(new ElevatorPosition(Elevator.liftState.F_SCALE_POSITION)); //TODO: re add this
						b.end();
					b.end();
				b.add(new ShootStep());
				b.add(new WaitStep(0.5));
				b.add(new DriveTurnStep(-0.2, -0.2, 1));
				b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
				*/
		
		
		scaleSwitch = new SeriesSet();
		b.add(scaleSwitch); {
			b.add(new StartStep());
			
			//where is the scale
			b.add(new IfSet(new IfInterface() {
				public boolean choice() {
					return options.startedLeft && options.scaleIsLeft
						|| !options.startedLeft && !options.scaleIsLeft;					 
				}
			})); {
				//if left or right straight scale (this means scale first then switch)
				b.add(new IfSet(new IfInterface() {
					public boolean choice() {
						return options.startedLeft;					 
					}
				})); {
					
//-----------------------------------------straight-------------------------------------------------------------------------				
						//start left goal left
					b.add(new SeriesSet()); { 
						
						//FIRST CUBE
						b.add(new ParallelSet()); {
							b.add(new DriveProfile(Profile.straightScaleLeftFast,10));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(2.0));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								b.add(new WaitStep(1.3));
								b.add(new ShootStep(-0.6));
								b.end(); }
							b.end(); }
						
						//if switch is left, pickup and plop cube 2 in the switch
						b.add(new IfSet(new IfInterface() {
							public boolean choice() {
								return options.switchIsLeft;
							}
						}));{
							//true case
							b.add(new SeriesSet());{
								//PICKUP SECOND CUBE
								b.add(new RecordStart());
								b.add(new ParallelSet()); {
									b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
									b.add(new DriveTurnStep(-0.35, -0.1, 2));//(-0.45, 0.05, 2)); //-0.2,-0.3 MSC
									b.add(new SeriesSet()); {
										b.add(new WaitStep(1.1 + 0.2));
										b.add(new AutoGather());
										b.end(); }
									b.end(); }
								b.add(new RecordStop());
								
								//put cube on switch
								b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
								b.add(new DriveTurnStep(0.3, 0.3, 0.25));
								b.add(new DriveTurnStep(-0.3, -0.3, 0.35));
								b.add(new ShootStep(-0.5));
								
								//go for cube 3
								b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
								b.add(new DriveTurnStep(0.15,0.55,0.5));
								b.add(new AutoGather());
								
								//put cube 3 on scale
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								b.add(new DriveRecPath(20));
								b.add(new ShootStep(-0.7));
								
								//go for cube 4
								b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
								b.add(new DriveTurnStep(-0.45,0.05,0.5));
								b.add(new AutoGather());
								b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
								
								b.end(); } //end left switch
							
							//false case
							//cross to the right side then pickup and plop the far cube in the switch
							b.add(new SeriesSet()); {
								
								//gather and switch cube 2
								b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
								b.add(new ReverseProfile(Profile.leftScaleRightSwitch, 180));
								b.add(new ParallelSet()); {
									b.add(new DriveTurnStep(0.7, -0.05, 2));
									b.add(new AutoGather());
									b.end();}
								b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
								b.add(new DriveTurnStep(0.3, 0.3, 0.3));
								b.add(new DriveTurnStep(-0.5, -0.5, 0.5));
								b.add(new ShootStep(-1.0));
								
								//go for cube 3 in the switch
								b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
								b.add(new DriveTurnStep(0.9,0.15,0.5));
								b.add(new AutoGather());
								b.add(new CheckMatchTime(13));
								b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
								b.add(new DriveTurnStep(0.3, 0.3, 0.3));
								b.add(new DriveTurnStep(-0.5, -0.5, 0.5));
								b.add(new ShootStep(-1.0));
								
								//go for cube 4 in the switch
								b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
								b.add(new DriveTurnStep(0.9,0.15,0.5));
								b.add(new AutoGather());
								b.add(new CheckMatchTime(13));
								b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
								b.add(new DriveTurnStep(0.3, 0.3, 0.3));
								b.add(new DriveTurnStep(-0.5, -0.5, 0.5));
								b.add(new ShootStep(-1.0));
								
								
								b.end(); }//end right switch
							b.end(); } //end the if for switch location
						b.end(); }//end the left straight scale
					
					//start right scale right
					b.add(new SeriesSet());{
						//FIRST CUBE
						b.add(new ParallelSet()); {
							b.add(new DriveProfile(Profile.straightScaleRightFast,10));
							b.add(new SeriesSet()); {
								b.add(new WaitStep(2.0));
								b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
								b.add(new WaitStep(1.3));
								b.add(new ShootStep(-0.6));
								b.end(); }
							b.end(); }
						
						//if switch is right, pickup and plop cube 2 in the switch
						b.add(new IfSet(new IfInterface() {
							public boolean choice() {
								return !options.switchIsLeft;
							}
						}));{
							//true case
							b.add(new SeriesSet());{
								//PICKUP SECOND CUBE
								b.add(new ParallelSet()); {
									b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
									b.add(new DriveTurnStep(-0.1, -0.35, 2)); //-0.2,-0.3 MSC
									b.add(new SeriesSet()); {
										b.add(new WaitStep(1.1 + 0.2));
										b.add(new AutoGather());
										b.end(); }
									b.end(); }
								b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
								b.add(new DriveTurnStep(0.3, 0.3, 0.2));
								b.add(new DriveTurnStep(-0.3, -0.3, 0.3));
								b.add(new ShootStep(-0.5));
								b.end(); } //end left switch
								
							
							//false case
							//cross to the left side then pickup and plop the far cube in the switch
							b.add(new SeriesSet()); {
								b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
								//b.add(new DriveProfile()); //TODO: make profile
								b.add(new AutoGather());
								b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
								b.add(new DriveTurnStep(0.3, 0.3, 0.2));
								b.add(new DriveTurnStep(-0.3, -0.3, 0.3));
								b.add(new ShootStep(-0.5));
								b.end(); }//end right switch
							b.end(); } //end the if for switch location
						b.end(); }//end start right scale right
					b.end(); }//end straight scale if (left right)
				
				
				b.add(new SeriesSet());{ // cross scale
					b.add(new IfSet(new IfInterface() {
						public boolean choice() {
							return options.startedLeft;
						}
					})); {
						b.add(new SeriesSet());{//if left cross right 
							b.add(new IfSet(new IfInterface() {
								public boolean choice() {
									return !options.switchIsLeft; //if switch is right
								}
							})); {
								b.add(new SeriesSet());{ //switch is right, same as scale
									b.add(new ParallelSet()); {
										b.add(new DriveProfile(Profile.leftToRightScale));
										b.add(new SeriesSet()); {
											b.add(new WaitStep(4.4+0.4)); 
											//b.add(new EndStep()); //TODO: remove when we want to complete cross scale auto
											b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION)); //TODO: re add this
											
											//moved shooting logic into this wait, so we start shooting before finishing driving
											b.add(new WaitStep(1.8 - 0.1));
											b.add(new ShootStep(-0.6));
											b.end(); }
										b.end(); }
									//b.add(new WaitStep(0.5));
									
									b.add(new RecordStart());
									b.add(new ParallelSet()); {
										b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
										b.add(new DriveTurnStep(-0.1, -0.35, 2));//(-0.05 - 0.1, -0.45 + 0.35, 2)); //-0.2,-0.3 MSC
										b.add(new SeriesSet()); {
											b.add(new WaitStep(1.0 +0.2));
											b.add(new AutoGather());
											b.end(); }
										b.end(); }
									b.add(new RecordStop());
										
									//put cube on switch
									b.add(new ElevatorPosition(Elevator.liftState.R_SWITCH_POSITION));
									b.add(new DriveTurnStep(0.3, 0.3, 0.3));
									b.add(new DriveTurnStep(-0.5, -0.5, 0.5));
									b.add(new ShootStep(-1.0));
									
									//go for cube 3
									b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
									b.add(new DriveTurnStep(0.9,0.15,0.5));
									b.add(new AutoGather());
									
									//put cube 3 on scale
									b.add(new CheckMatchTime(13));//if time greater, error out
									b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
									b.add(new DriveRecPath(20));
									b.add(new ShootStep(-0.7));
									
									//go for cube 4
									b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
									b.add(new DriveTurnStep(-0.45,0.05,0.5));
									b.add(new AutoGather());
									b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
									
									b.end(); }//end right switch
								
								b.add(new SeriesSet());{ //switch is left, same as robot, scale right
									b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
									b.add(new DriveProfile(Profile.leftSideSwitch));
									//b.add(new DriveTurnStep(0.4, 0.4, 0.4));
									b.add(new ShootStep(-0.7));
									
									b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
									//b.add(new DriveTurnStep(0.05, -0.7, 3.0));
									b.add(new ReverseProfile(Profile.leftSideSwitchLoop, 76));
									b.add(new AutoGather());
									
									//score 3 in the switch
									b.end(); }//end right switch
								
								b.end(); }//end switch if (right left)
							
							b.end(); }//end left cross right
						
						b.add(new SeriesSet());{//if right cross left
							
							b.end(); }//end right cross left
						
						b.end(); }//end cross if (left right)
					
					b.end(); }//end cross scale
				
				b.end(); }//end if (straight cross)
			
			b.add(new EndStep());
			b.end(); }//end scale switch auton
		
		
		buddyScale = new SeriesSet();
		b.add(buddyScale); {
			b.add(new StartStep());
			
//------------------------------------STRAIGHT SCALE----------------------------------------------------------------
			//if straight or cross scale
			b.add(new IfSet(new IfInterface() {
				public boolean choice() {
					return options.startedLeft && options.scaleIsLeft
						|| !options.startedLeft && !options.scaleIsLeft;					 
				}
			})); {
				//if left or right straight scale
				b.add(new IfSet(new IfInterface() {
					public boolean choice() {
						return options.startedLeft && options.scaleIsLeft;					 
					}
				})); {
						//start left goal left
					b.add(new SeriesSet()); { 
						
						b.add(new DriveProfile(Profile.straightScaleZoom));
						b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
						b.add(new WaitStep(1.2));
						b.add(new TurnAngle(20));
						b.add(new WaitStep(0.1));
						b.add(new ShootStep(-0.9));
						b.add(new DriveTurnStep(-0.3, -0.3, 0.6));
						b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
						b.end();}
						
					//start right goal right
					b.add(new SeriesSet()); { 
						//FIRST CUBE
						b.add(new DriveProfile(Profile.straightScaleZoom));
						b.add(new ElevatorPosition(Elevator.liftState.AUTON_SCALE_POSITION));
						b.add(new WaitStep(1.2));
						b.add(new TurnAngle(160));
						b.add(new ShootStep(-1.0));
						b.add(new DriveTurnStep(-0.3, -0.3, 0.5));
						b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
						b.end();}
					
					b.end(); }
				
	//------------------CROSS SCALE---------------------------------------------------------------------
			
			
				//start right have to cross to left
				b.add(new IfSet(new IfInterface() {
					public boolean choice() {
						return !options.startedLeft && options.scaleIsLeft;
					}
				})); {
					b.add(new IfSet(new IfInterface() {
						public boolean choice() {
							return !options.switchIsLeft;
						}
					})); {
						//start right scale left switch right
						b.add(new SeriesSet()); { 
							b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
							b.add(new DriveProfile(Profile.rightSideSwitch));	
							b.add(new ShootStep());
							//b.add(new WaitStep(0.5));
							b.add(new DriveTurnStep(0.05, -0.3, 2));
							b.end(); }
						//start right scale left switch left
						b.add(new SeriesSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
							b.add(new DriveProfile(Profile.rightToLeftScaleHalf));
							b.end();}
						
						b.end();}
					
					//start left scale left switch right
					b.add(new IfSet(new IfInterface() {
						public boolean choice() {
							return options.switchIsLeft;
						}
					})); {
						b.add(new SeriesSet()); { 
							b.add(new ElevatorPosition(Elevator.liftState.F_SWITCH_POSITION));
							b.add(new DriveProfile(Profile.leftSideSwitch));	
							b.add(new ShootStep());
							//b.add(new WaitStep(0.5));
							b.add(new DriveTurnStep(0.05, -0.3, 2));
							b.end(); }
						//start right scale left switch left
						b.add(new SeriesSet()); {
							b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
							b.add(new DriveProfile(Profile.leftToRightScaleHalf));
							b.end();}
						b.end();}
					b.end();}
				b.end(); }
			b.add(new EndStep());
			b.end(); }
			
					
		//Path.print = true;
		testProfile = new SeriesSet();
		b.add(testProfile); {
			b.add(new StartStep());
			//b.add(new DriveTurnStep(-0.35, -0.1, 1.3));  //0.05 W   //-0.2,-0.3 MSC
			b.add(new ParallelSet()); {
				b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
				b.add(new DriveTurnStep(-0.35, -0.2, 2)); //-0.2,-0.3 MSC
				b.add(new SeriesSet()); {
					b.add(new WaitStep(1.1 + 0.2));
					b.add(new AutoGather());
					b.end(); }
				b.end(); }
			
			
			//b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
			//b.add(new DriveProfile(Profile.straightScaleLeftFast));
			/*
			b.add(new WaitStep(0.5));
			b.add(new AutoGather());
			b.add(new ElevatorPosition(Elevator.liftState.REST_POSITION));
			b.add(new DriveRecPath());
			//b.add(new DriveProfile(Profile.rightToLeftScale));
			//b.add(scale);
			//b.end();
			 */
			b.add(new EndStep());
			b.end(); }
		//Path.print = false;
			
			
		test2Cube = new SeriesSet();
		b.add(test2Cube); {
			b.add(new StartStep());
			b.add(new DriveTurnStep(-0.2, -0.2, 1));
			b.add(new ElevatorPosition(Elevator.liftState.R_FLOOR_POSITION));
			b.add(new AutoGather()); //true means back gather
			b.add(new ParallelSet()); {
				b.add(new DriveProfile(Profile.left1ToScale));
				b.add(new SeriesSet()); {
					b.add(new WaitStep(0.1));
					b.add(new ElevatorPosition(Elevator.liftState.F_SCALE_POSITION)); //TODO: re add this
					b.end(); }
				b.end(); }
				b.add(new ShootStep());
			b.end(); }
	}
	
	
	public void init() {
		//currentStep = 0;
	}
	
	
	
	
	public void selectAuton() {
		
		//read driver station values
		options.selectedStart = startLocation.getSelected();
		options.startedLeft = false;
		//options.selectedPriority = priority.getSelected();
		options.switchIfLeft = SmartDashboard.getBoolean("SwitchIfLeft", false);
		options.switchIfRight = SmartDashboard.getBoolean("SwitchIfRight", false);
		options.exchangeIfSwitch = SmartDashboard.getBoolean("ExchangeIfSwitch", false);
		options.switchBeforeScale = SmartDashboard.getBoolean("SwitchBeforeScale", false);
		
		getGameData();
		
		switch(options.selectedStart) {
		case STRAIGHT_ONLY:
			currentAuton = driveForward;
			SmartDashboard.putString("CurrAuton","driveForward");
			break;
			
		case LEFT_SWITCH:
			currentAuton = onlySwitch;
			SmartDashboard.putString("CurrAuton","onlySwitchLeft");
			options.startedLeft = true;
			break;
			
		case RIGHT_SWITCH:
			currentAuton = onlySwitch;
			SmartDashboard.putString("CurrAuton","onlySwitchRight");
			options.startedLeft = false;
			break;
			
		case CENTER_SWITCH_MP:
			currentAuton = mpCenterSwitch;
			SmartDashboard.putString("CurrAuton","centerSwitchMP");
			break;
		
		case CENTER_SWITCH:
			currentAuton = centerSwitch;
			SmartDashboard.putString("CurrAuton","centerSwitch");
			break;
		
		case LEFT_SCALE_ONLY:
			currentAuton = scale;
			SmartDashboard.putString("CurrAuton","scaleLeft");
			options.startedLeft = true;
			break;
			
		case RIGHT_SCALE_ONLY:
			currentAuton = scale;
			SmartDashboard.putString("CurrAuton","scaleRight");
			options.startedLeft = false;
			break;
			
		case LEFT_SCALE_SWITCH:
			currentAuton = scaleSwitch;
			SmartDashboard.putString("CurrAuton","scaleSwitchLeft");
			options.startedLeft = true;
			break;
			
		case RIGHT_SCALE_SWITCH:
			currentAuton = scaleSwitch;
			SmartDashboard.putString("CurrAuton","scaleSwitchRight");
			options.startedLeft = false;
			break;
			
		case LEFT_BUDDY_SCALE:
			currentAuton = buddyScale;
			SmartDashboard.putString("CurrAuton","buddyLeft");
			options.startedLeft = true;
			break;
		
		case RIGHT_BUDDY_SCALE:
			currentAuton = buddyScale;
			SmartDashboard.putString("CurrAuton","buddyRight");
			options.startedLeft = false;
			break;
			
		case TEST:
			currentAuton = testProfile;
			//currentAuton = test2Cube;
			SmartDashboard.putString("CurrAuton","testProfile");
			break;
		}
		/*
		//if scale is selected, do that one
		switch(options.selectedPriority) {
		case SCALE:
			//currentAuton = straightScale;
			currentAuton = scale;
			SmartDashboard.putString("CurrAuton","scale");
			break;
		}
		*/
	}
	
	public void getGameData() {
		options.switchIsLeft = sense.switchIsLeft;
		options.scaleIsLeft = sense.scaleIsLeft;
	}
	
	ErrorStep errStep = new ErrorStep();
	public void run() {
		
		//if there is an error stop everything
		if(currentAuton.isError()) {
			errStep.run();
		} else {
			//otherwise, run the selected auton
			currentAuton.run();
		}
		
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
