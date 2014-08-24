package junit.test.neuronrobotics.cartesian;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;

import com.neuronrobotics.replicator.driver.BowlerBoardDevice;
import com.neuronrobotics.replicator.driver.NRPrinter;
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR;
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;
import com.neuronrobotics.sdk.common.Log;
import com.neuronrobotics.sdk.serial.SerialConnection;
import com.neuronrobotics.sdk.ui.ConnectionDialog;
import com.neuronrobotics.sdk.util.ThreadUtil;

public class CartesianTestSuit {

	@Test
	public void test() {
		Log.enableDebugPrint();
		BowlerBoardDevice dev = new BowlerBoardDevice();
		dev.setConnection(new SerialConnection("/dev/ttyACM0"));
		dev.getConnection().setSynchronusPacketTimeoutTime(2000);
		assertTrue(dev.connect());
		System.out.println("Connection ok");
		
//		dev.setDesiredTaskSpaceTransform(new TransformNR(), 44);
//		dev.getCurrentTaskSpaceTransform();
//		dev.setDesiredJointSpaceVector(new double [] {150,150,150,0,0},55);
//		dev.setDesiredJointAxisValue(0,100,20);

		NRPrinter printer = new NRPrinter(dev);
		printer.cancelPrint();
		//ThreadUtil.wait(5000);
		try{
			File stl = new File("calibration_angle.stl");
			File gcode = new File(stl.getAbsoluteFile()+".gcode");
			if(!gcode.exists())
				printer.slice(stl, gcode);
			
			//printer.print(CartesianTestSuit.class.getResourceAsStream("test.gcode"));
			if(gcode.exists())
				printer.print(new FileInputStream(gcode));
		}catch(Exception ex){
			ex.printStackTrace();
			fail();
		}
		
		
	}

}
