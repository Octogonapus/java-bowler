package com.neuronrobotics.test.kinematics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import com.neuronrobotics.replicator.driver.BowlerBoardDevice;
import com.neuronrobotics.sdk.addons.kinematics.AbstractKinematicsNR;
import com.neuronrobotics.sdk.addons.kinematics.DHParameterKinematics;
import com.neuronrobotics.sdk.addons.kinematics.ITaskSpaceUpdateListenerNR;
import com.neuronrobotics.sdk.addons.kinematics.gui.DHKinematicsViewer;
import com.neuronrobotics.sdk.addons.kinematics.gui.SampleGuiNR;
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR;
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;
import com.neuronrobotics.sdk.common.BowlerAbstractConnection;
//import com.neuronrobotics.sdk.common.Log;
import com.neuronrobotics.sdk.dyio.DyIO;
import com.neuronrobotics.sdk.dyio.DyIOChannel;
import com.neuronrobotics.sdk.dyio.peripherals.DigitalInputChannel;
import com.neuronrobotics.sdk.dyio.peripherals.IDigitalInputListener;
import com.neuronrobotics.sdk.serial.SerialConnection;
import com.neuronrobotics.sdk.ui.ConnectionDialog;
import com.neuronrobotics.sdk.util.ThreadUtil;
public class TrobotDisplayOnly implements ITaskSpaceUpdateListenerNR, IDigitalInputListener {
	DHParameterKinematics model;
	//DeltaForgeDevice deltaRobot;
	TransformNR current = new TransformNR();
	double scale=.2;
	double [] startVect = new double [] { 0,0,0,0,0,0};
	private boolean button=false;
	private boolean lastButton=false;
	private int open = 20;
	private int closed = 100;
	
	public TrobotDisplayOnly() {
		DyIO.disableFWCheck();
		
		//Create the references for my known DyIOs
		DyIO master = new DyIO(ConnectionDialog.promptConnection());
		
		master.connect();
		
		model = new DHParameterKinematics(master,"TrobotMaster.xml");
	
		DigitalInputChannel di = new DigitalInputChannel(master.getChannel(0));
		di.addDigitalInputListener(this);
		button=di.isHigh();
		lastButton = button;

		try{
			final SampleGuiNR gui = new SampleGuiNR();
			final JFrame frame = new JFrame();
			final JTabbedPane tabs = new JTabbedPane();
			JPanel starter = new JPanel(new MigLayout());
			gui.setKinematicsModel(model);
			try{
				tabs.add("Display",new DHKinematicsViewer(model));
			}catch(Error ex){
				JPanel error = new JPanel(new MigLayout());
				error.add(new JLabel("Error while loading Java3d library:"),"wrap");
				error.add(new JLabel(ex.getMessage()),"wrap");
				error.add(new JLabel(System.getProperty("java.library.path")),"wrap");
				tabs.add("Display [ERROR]",error);
				ex.printStackTrace();
				System.out.println("Lib path searched");
			}
			
			frame.setLocationRelativeTo(null);
			//zero();
			
			gui.add(starter);
			tabs.add("Control",gui);
			//Add scroller here
			frame.getContentPane().add(tabs);
			//frame.setSize(640, 480);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(1);
		}
		model.addPoseUpdateListener(this);
		//Log.enableWarningPrint();
		int loopTime=50;
		master.getConnection().setSynchronusPacketTimeoutTime(2000);
		//delt.getConnection().setSynchronusPacketTimeoutTime(2000);
		int x=0,y=0,z=0;
		//Log.enableInfoPrint();
//		for (DyIOChannel c: master.getChannels()){
//			c.setAsync(false);
//		}
		while ( true) {
			long time = System.currentTimeMillis();
			try {				
				//master.getAllChannelValues();
				
				ThreadUtil.wait(10,1);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//throw new RuntimeException("Main exited!");
	}
	
	private void zero(){
		try {
			model.setDesiredJointSpaceVector(startVect, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TrobotDisplayOnly();
	}
	public void onTaskSpaceUpdate(AbstractKinematicsNR source, TransformNR pose) {
		//System.err.println("Got:"+pose);
		double ws=50;
		current = new TransformNR(	((pose.getY())*scale+25),
									((pose.getX()+180)*scale),
									((pose.getZ())+100),
				new RotationNR());
		//System.out.println("Current = "+current);
	}
	public void onTargetTaskSpaceUpdate(AbstractKinematicsNR source,TransformNR pose) {}

	@Override
	public void onDigitalValueChange(DigitalInputChannel source, boolean isHigh) {
		button=isHigh;
		System.out.println("Hand is "+isHigh);
	}
}