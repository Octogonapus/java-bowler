package com.neuronrobotics.test.dyio;

import com.neuronrobotics.sdk.common.Log;
import com.neuronrobotics.sdk.dyio.DyIO;
import com.neuronrobotics.sdk.dyio.peripherals.DigitalInputChannel;
import com.neuronrobotics.sdk.dyio.peripherals.DigitalOutputChannel;
import com.neuronrobotics.sdk.serial.SerialConnection;
import com.neuronrobotics.sdk.ui.ConnectionDialog;
import com.neuronrobotics.sdk.util.ThreadUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class DIgitalOutputTest.
 */
public class DIgitalOutputTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Log.enableWarningPrint();
		DyIO.disableFWCheck();
		SerialConnection.getAvailableSerialPorts();
		ThreadUtil.wait(5000);
		DyIO dyio=new DyIO(new SerialConnection("/dev/ttyACM0", 115200));
		dyio.connect();
//		if (!ConnectionDialog.getBowlerDevice(dyio)){
//			System.exit(1);
//		}
//		
		DigitalOutputChannel doc = new DigitalOutputChannel(dyio.getChannel(13));
		// Blink the LED 5 times
		for(int i = 0; i < 10; i++) {
			System.out.println("Blinking.");
			// Set the value high every other time, exit if unsuccessful
			if(!doc.setHigh(i % 2 == 1)) {
				System.err.println("Could not connect to the device.");
				System.exit(0);
			}
			// pause between cycles so that the changes are visible
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        System.exit(0);
	}

}
