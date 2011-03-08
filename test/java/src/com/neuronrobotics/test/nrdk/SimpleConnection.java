package com.neuronrobotics.test.nrdk;

import com.neuronrobotics.sdk.common.Log;
import com.neuronrobotics.sdk.dyio.DyIO;
import com.neuronrobotics.sdk.serial.SerialConnection;

public class SimpleConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SerialConnection s = null;
		System.out.println("Connecting and disconnecting");
		
		//Windows
		//s=new SerialConnection("COM5");
		
		//OSX
		s=new SerialConnection("/dev/tty.usbmodemfd13411");
		
		//Linux
		//s=new SerialConnection("/dev/ttyACM0");
		
		DyIO dyio = new DyIO(s);
		//Log.enableDebugPrint(true);
		dyio.connect();
        dyio.ping();
        dyio.disconnect();
		System.exit(0);
	}

}
