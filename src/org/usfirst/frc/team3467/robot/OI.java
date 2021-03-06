package org.usfirst.frc.team3467.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3467.robot.subsystems.Vision.commands.LightSwitch;
import org.usfirst.frc.team3467.robot.commands.CommandBase;
import org.usfirst.frc.team3467.robot.subsystems.Intake.Intake;
import org.usfirst.frc.team3467.robot.subsystems.Intake.commands.IntakeDrive;
import org.usfirst.frc.team3467.robot.subsystems.Intake.commands.Roller_Actuate;
import org.usfirst.frc.team3467.robot.subsystems.Shooter.commands.*;
import org.usfirst.frc.team3467.robot.subsystems.utilitybar.Pnumatic_system;
import org.usfirst.frc.team3467.robot.subsystems.utilitybar.commands.*;
import org.usfirst.frc.team3467.robot.control.Gamepad;
import org.usfirst.frc.team3467.robot.triggers.DPadDown;
import org.usfirst.frc.team3467.robot.triggers.DPadLeft;
import org.usfirst.frc.team3467.robot.triggers.DPadRight;
import org.usfirst.frc.team3467.robot.triggers.DPadUp;


public class OI {
	
	public static Joystick leftDrive;
	public static Joystick rightDrive;
	public static Gamepad operator;
	
	//User numbers for different button layouts
	public static final int Tank = 1;
	public static final int Arcade = 2;
	public int userlogin = 2;
	
/*
 * Joystick Mappings (done elsewhere in code)
 * 
 * Joystick leftDrive - used for Tank Drive (along with rightDrive)
 * Joystick rightDrive - used for Tank or Arcade Drive
 * 
 * Gamepad getRightStickX() - used for manual drive of Intake rollers
 * Gamepad getLeftStickY() - used for manual drive of Catapult reset bar
 * 
 */
	
	public OI(){
		leftDrive = new Joystick(0);
		rightDrive = new Joystick(1);
		operator = new Gamepad(2);
	}
	
	public Gamepad getGamepad() {
		return operator;
	}
	
	//Get method for leftDrive Joystick instance
	public Joystick getleftDrive() {
		return leftDrive;
	}
	
	//Get method for rightDrive Joystick instance
	public Joystick getrightDrive() {
		return rightDrive;
	}
	
	//Joystick Methods that return values for left and right joysticks
	public double getLeftY(){
		return leftDrive.getY();
	}
	
	public double getRightY(){
		return rightDrive.getY();
	}
	
	public double getRightX(){
		return rightDrive.getX();
	}
	
	public double getRightZ() {
		return rightDrive.getZ();
	}
	
	public int getUserlogin() {
		userlogin = (int) SmartDashboard.getNumber("Enter 1 for Tank, 2 for Arcade");
		return userlogin;
	}
	
	//Method that binds certain commands to certain buttons
	public void BindCommands() {
		switch (userlogin) {
		case Tank: 
		default: 
				CommandBase.driveBase.setDriveMode(true);
				break; 
		case Arcade: 
				CommandBase.driveBase.setDriveMode(false);
				break;
		}
		
		
	//Interupts the previous command
		//new JoystickButton(operator, Gamepad.leftBumper);
		
		
	//Intake
		//Eject Slow
		new JoystickButton(operator, Gamepad.xButton)
			.whileHeld(new IntakeDrive(Intake.kEjectSlow));
		
		//Eject Fast
		new JoystickButton(operator, Gamepad.yButton)
			.whileHeld(new IntakeDrive(Intake.kEjectFast));
		
		//Intake Slow
		new JoystickButton(operator, Gamepad.aButton)
			.whileHeld(new IntakeDrive(Intake.kIntakeSlow));
		
		//Intake Fast
		new JoystickButton(operator, Gamepad.bButton)
			.whileHeld(new IntakeDrive(Intake.kIntakeFast));
		
		//Intake Extend
		new JoystickButton(leftDrive, 1)
		.whenPressed(new Roller_Actuate(true));
		
		new JoystickButton(leftDrive, 2)
		.whenPressed(new Roller_Actuate(false));
		
	
	//Catapult
		// Halt Reset Bar PID and switch to manual mode
		new JoystickButton(operator, Gamepad.startButton)
			.whileHeld(new ShooterReset());
		
		//Reload Catapult
		new JoystickButton(operator, Gamepad.leftBumper)
			.whenPressed(new ShooterPrepare());
		
		//Fire Catapult
		new JoystickButton(operator, Gamepad.rightBumper)
			.whenPressed(new Shoot());
	
		// DPad Up
		new DPadUp(operator)
			.whenActive(new ShooterLatch());

		// DPad Right
		new DPadRight(operator)
			.whenActive(new ShooterLatch());

		// DPad Down
		new DPadDown(operator)
			.whenActive(new ShooterClear());
 		
		// DPad Left
		new DPadLeft(operator)
			.whenActive(new ShooterClear());
		
		
	//Camera Commands
		new JoystickButton(operator, Gamepad.startButton)
		.whenPressed(new LightSwitch(true));
		
		new JoystickButton(operator, Gamepad.backButton)
		.whenActive(new LightSwitch(false));
		
		
	//Utility Bar/Finger
			//Extend Using the Right Trigger
		new JoystickButton(rightDrive, 1)
			.whenPressed(new Bar_actuate(Pnumatic_system.kOut));
		
			//Retract Using the Right Shoulder Button
		new JoystickButton(rightDrive, 2)
			.whenPressed(new Bar_actuate(Pnumatic_system.kIn));
		
			//Extend Finger using Top left button
		new JoystickButton(rightDrive, 3)
			.whenPressed(new Finger_actuate(Pnumatic_system.kOut));
		
			//Retract Finger using Top Right Button
		new JoystickButton(rightDrive, 4)
			.whenActive(new Finger_actuate(Pnumatic_system.kIn));
		
		// SmartDashboard Buttons
		SmartDashboard.putData("Shooter Calibrate", new ShooterCalibrate());

		
	}
}

