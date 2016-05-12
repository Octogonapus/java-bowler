package junit.test.neuronrobotics.utilities;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neuronrobotics.sdk.addons.kinematics.gcodebridge.GcodeDevice;
import com.neuronrobotics.sdk.common.DeviceManager;

import gnu.io.NRSerialPort;

public class GCODETest {

	private static final String GCODE = "GCODE";

	@Test
	public void test() {
		boolean hasPort=false;
		String portname = "/dev/ttyUSB0";
				
		for (String s:NRSerialPort.getAvailableSerialPorts()){
			if(s.contentEquals(portname))
				hasPort=true;
		}
		if(hasPort){
			GcodeDevice device;
			NRSerialPort  port = new NRSerialPort(portname, 115200);
			device = new GcodeDevice(port);
			device.connect();
				
			String response = device.runLine("M105");
			
			device.disconnect();
			if (response.length()>0)
				System.out.println("Gcode line run: "+response);
			else
				fail("No response");
		}
	}

}