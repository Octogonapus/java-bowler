package com.neuronrobotics.imageprovider;

import java.net.MalformedURLException;
import java.net.URL;

public class VirtualCameraFactory {
	private IVirtualCameraFactory factory = new IVirtualCameraFactory() {
		
		@Override
		public AbstractImageProvider getVirtualCamera() {
			// TODO Auto-generated method stub
			try {
				return new URLImageProvider(new URL("http://neuronrobotics.com/img/AndrewHarrington/2014-09-15-86.jpg"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);			
			}
		}
	}; 
	public AbstractImageProvider getVirtualCamera(){
		return getFactory().getVirtualCamera();
	}
	public IVirtualCameraFactory getFactory() {
		return factory;
	}
	public void setFactory(IVirtualCameraFactory factory) {
		this.factory = factory;
	}
}
