// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Targeting extends SubsystemBase {
  //values from 2022 robot, all values are in feet or degrees
  private final double HEIGHT_OF_TARGET = 8.67; 
  private final double HEIGHT_OF_CAMERA = 37.5 / 12;
  private final double CAMERA_MOUNT_ANGLE = 33.1; 

  private NetworkTable limelight;
  private NetworkTableEntry tv;
  private NetworkTableEntry tx;
  private NetworkTableEntry ty;

  private double validTarget;
  private double x;
  private double y;

  /** Creates a new Targeting. */
  public Targeting() {
    limelight = NetworkTableInstance.getDefault().getTable("limelight");
    tv = limelight.getEntry("tv");
    tx = limelight.getEntry("tx");
    ty = limelight.getEntry("ty");

    //the following are out of range indicating no target found
    validTarget = 0;
    x = 100;
    y = 100;

    limelight.getEntry("ledMode").setNumber(1);
    limelight.getEntry("camMode").setNumber(0);
    limelight.getEntry("pipeline").setNumber(0);
  }

  public void changeTag(int tag){//set according to which tag is in which pipeline
    limelight.getEntry("pipeline").setNumber(tag);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    validTarget = tv.getDouble(0);
    x = tx.getDouble(0);
    y = ty.getDouble(0);

    SmartDashboard.putNumber("Limelight Valid Target", validTarget);
    SmartDashboard.putNumber("limelight x", x);
    SmartDashboard.putNumber("limelight y", y);
    SmartDashboard.putNumber("range", calcRange());
  }

  public double getValidTarget(){
    return validTarget;
  }
  
  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }


  public void ledOn(boolean on) {
    NetworkTableEntry led = limelight.getEntry("ledMode");
    if(on) {
      led.setNumber(3);
    } else {
      led.setNumber(1);
    }
  }

  public double calcRange() {//calculate horizontal range to target
    double d = (HEIGHT_OF_TARGET - HEIGHT_OF_CAMERA) / Math.tan(Math.toRadians(CAMERA_MOUNT_ANGLE + y));
    return d;       
  }

}