package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import java.lang.reflect.InvocationTargetException;
import com.zephyr.pixy.*;
import frc.robot.Toggle;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.links.SPILink;
import frc.robot.PairOfMotors;
import java.util.List;
import java.util.ArrayList;

public class Robot extends TimedRobot {
	private XboxController xbox;
	private Joystick driverLeft;
	private Joystick driverRight;
	private DriverStation driverStation;
	private SpeedController frontLeftSpeed;
	private SpeedController frontRightSpeed;
	private SpeedController backLeftSpeed;
	private SpeedController backRightSpeed;
	private SpeedControllerGroup leftSpeed;
	private SpeedControllerGroup rightSpeed;
	private DifferentialDrive drive;
	private List<PairOfMotors> motorPairList;
	int autoLoopCounter;

	@Override
	public void robotInit() {
		System.out.println("System initialized...");
		
		driverStation = DriverStation.getInstance();

		driverLeft = new Joystick(0);
		driverRight = new Joystick(1);
		xbox = new XboxController(2);	

		DriverInput.nameInput("Driver-Left");
		DriverInput.nameInput("Driver-Right");
		frontLeftSpeed		= new WPI_TalonSRX(15);
		backLeftSpeed		= new WPI_TalonSRX(9);
		frontRightSpeed		= new WPI_TalonSRX(14);
		backRightSpeed		= new WPI_TalonSRX(8);

		leftSpeed = new SpeedControllerGroup(frontLeftSpeed, backLeftSpeed);
		rightSpeed = new SpeedControllerGroup(frontRightSpeed, backRightSpeed);
		drive = new DifferentialDrive(leftSpeed, rightSpeed);
	}
	@Override
	public void teleopPeriodic() {
		
		try {
			actions.input(new DriverInput()
				.withInput("Operator-Start-Button",	xbox.getRawButton(8)) 		//  boost
				.withInput("Operator-Back-Button",	xbox.getRawButton(7))
				.withInput("Operator-DPad",			xbox.getPOV()) 				// used
				.withInput("Driver-Left", 			driverLeft.getRawAxis(1)) 	// used - drives left side
				.withInput("Driver-Right", 			driverRight.getRawAxis(1)) 	// used - drives right side
				.withInput("Driver-Left-Trigger", 	driverLeft.getRawButton(1))
				.withInput("Driver-Right-Trigger", 	driverRight.getRawButton(1))
				.withInput("Driver-Left-8", 		driverLeft.getRawButton(8)) // used - enables backwards
				.withInput("Driver-Left-7", 		driverLeft.getRawButton(7)) // used - speed changer one
				.withInput("Driver-Left-6", 		driverLeft.getRawButton(6)) // used - speed changer two
				.withInput("Operator-Right-Stick",	xbox.getRawButton(10))
			);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public void robotOperation(DriverInput input) {
		double leftAxis = Math.pow(input.getAxis("Driver-Left"), 3);
		double rightAxis = Math.pow(input.getAxis("Driver-Right"), 3);
		drive.tankDrive(rightAxis, leftAxis, false);
	}
	public void robotOperation(DriverInput input) {
		
		RobotStopWatch watch = new RobotStopWatch("robotOperation");
		
		double leftAxis = SpeedVariable * input.getAxis("Driver-Left");
		double rightAxis = SpeedVariable * input.getAxis("Driver-Right");
		leftAxis = Math.abs(Math.pow(leftAxis, 3)) * leftAxis/Math.abs(leftAxis);
		rightAxis = Math.abs(Math.pow(rightAxis, 3)) * rightAxis/Math.abs(rightAxis);

		backwards.input(input.getButton("Driver-Left-8"));
		SmartDashboard.putBoolean("Backwards State", backwards.getState());

		if (!backwards.getState()) drive.tankDrive(-1 * leftAxis, -1 * rightAxis, false);
		else drive.tankDrive(rightAxis, leftAxis, false);
	}
}