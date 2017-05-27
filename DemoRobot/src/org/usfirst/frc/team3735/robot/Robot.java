package org.usfirst.frc.team3735.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team3735.robot.commands.drive.RecordAverageRate;
import org.usfirst.frc.team3735.robot.commands.drive.RecordTrapTurnData;
import org.usfirst.frc.team3735.robot.commands.recorder.RecordSmartDashboardFile;
import org.usfirst.frc.team3735.robot.commands.recorder.SendSmartDashboardFile;
import org.usfirst.frc.team3735.robot.pipelines.GearPipeline;
import org.usfirst.frc.team3735.robot.pipelines.StickyNotePipeline;
import org.usfirst.frc.team3735.robot.subsystems.Drive;
import org.usfirst.frc.team3735.robot.subsystems.Navigation;
import org.usfirst.frc.team3735.robot.subsystems.Shooter;
import org.usfirst.frc.team3735.robot.subsystems.Vision;
import org.usfirst.frc.team3735.robot.util.DriveOI;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	SendableChooser<Command> autonomousChooser;
	Command autonomousCommand;
	
	public static Drive drive;
	public static Navigation navigation;
	public static Vision vision;
	public static Shooter shooter;

	public static GTAOI oi;
	public RobotMap robotmap;
	
	boolean rightSide = false;


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		robotmap = new RobotMap();

		drive = new Drive();
		navigation = new Navigation();
		vision = new Vision();
		shooter = new Shooter();
		
		oi = new GTAOI(); //MUST be instantiated after the subsystems
			
		autonomousChooser = new SendableChooser<Command>();
		
		SmartDashboard.putData("AUTONOMOUS SELECTION", autonomousChooser);
		
		//SmartDashboard.putData("Start Sending Turn Voltages", new RecordTrapTurnData());
		//SmartDashboard.putData("Start Sending Turn Voltages", new RecordAverageRate());

//		SmartDashboard.putData("Record Data", new RecordSmartDashboardFile());
//		SmartDashboard.putData("Send Data", new SendSmartDashboardFile());

		
		SmartDashboard.putData("Resume Thread", new InstantCommand(){
			@Override
			public void initialize(){
				Robot.vision.resume();
			}
		});
		
		SmartDashboard.putData("Pause Thread", new InstantCommand(){
			@Override
			public void initialize(){
				Robot.vision.pause();
			}
		});
		

		SmartDashboard.putData("Zero Yaw", new InstantCommand(){
			@Override
			public void initialize(){
				Robot.navigation.zeroYaw();
			}
		});
		
		
		log();
	}
	
	@Override
	public void robotPeriodic() {
	}
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		navigation.zeroYaw();
		navigation.zeroYaw();
        autonomousCommand = autonomousChooser.getSelected();
        if (autonomousCommand != null) autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		log();
	}

	
    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }
    
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
        Scheduler.getInstance().run();
        log();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	public void log(){
		oi.log();
		drive.log();
		navigation.log();
		vision.log();
	}
	
	
	/**
	 * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
	 */
	@Override
	public void disabledInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();

	}
}

