package com.neuronrobotics.addons.driving;

import java.util.ArrayList;

public class LineTrack implements IRobotDriveEventListener,ISensorListener{
	AbstractRobotDrive mainRobot;
	AbstractSensor line;
	public void runTrack(AbstractRobotDrive m,AbstractSensor l) {
		mainRobot=m;
		line=l;
		mainRobot.addIRobotDriveEventListener(this);
		line.addSensorListener(this);
		mainRobot.DriveVelocityStraight(100);
	}
	@Override
	public void onRangeSensorEvent(AbstractSensor source,ArrayList<DataPoint> data, long timeStamp) {
		// Never gets called...
	}
	@Override
	public void onLineSensorEvent(AbstractSensor source, Integer left,Integer middle, Integer right, long timeStamp) {
		//System.out.println("Sensor Event left="+left+" middle="+middle+" right="+right);
		double diff = (double)(left-right);
		///System.out.println("Steer value ="+diff);
		if(diff<100 && diff>-100) {
			//System.out.println("Drive straight");
			mainRobot.DriveVelocityStraight(20);
		}else {
			//System.out.println("turn");
			mainRobot.DriveVelocityArc((diff)/30, 10*((diff>0)?1:-1));
		}
	}
	@Override
	public void onDriveEvent(AbstractRobotDrive source, double x, double y,double orentation) {
		//System.out.println("Drive Event: x="+x+" y="+y);
		
	}
}
