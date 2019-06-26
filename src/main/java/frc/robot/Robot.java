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
// import edu.wpi.first.cameraserver.CameraServer;
import java.lang.reflect.InvocationTargetException;
// import edu.wpi.first.cameraserver.*;
import com.zephyr.pixy.*;
import frc.robot.Toggle;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.links.SPILink;
//import sun.tools.jconsole.inspector.Utils;
import frc.robot.PairOfMotors;
import java.util.List;

// import javax.lang.model.util.ElementScanner6;

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
	int autoLoopCounter;
	private List<PairOfMotors> motorPairList;

	@Override
	public void robotInit() {
	
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
		
	}

	public void robotOperation(DriverInput input) {
		double leftAxis = Math.pow(input.getAxis("Driver-Left"), 3);
		double rightAxis = Math.pow(input.getAxis("Driver-Right"), 3);
		drive.tankDrive(rightAxis, leftAxis, false);
		}
	}