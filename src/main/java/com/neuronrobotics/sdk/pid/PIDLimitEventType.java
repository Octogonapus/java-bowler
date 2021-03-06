/*******************************************************************************
 * Copyright 2010 Neuron Robotics, LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.neuronrobotics.sdk.pid;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc

/**
 * The Enum PIDLimitEventType.
 */
public enum PIDLimitEventType {
	
	/**
	 * 
	 */
	NO_LIMIT(0x00),
	/** The lowerlimit. */
	LOWERLIMIT(0x01),
	
	/** The indexevent. */
	INDEXEVENT(0x02),
	
	/** The upperlimit. */
	UPPERLIMIT(0x04),
	
	/** The overcurrent. */
	OVERCURRENT(0x08),
	/**
	 * 
	 */
	CONTROLLER_ERROR(0x10),
	/**
	 * a home event
	 */
	HOME_EVENT(0x20)
	;
	;
	
	/** The Constant lookup. */
	private static final Map<Byte,PIDLimitEventType> lookup = new HashMap<Byte,PIDLimitEventType>();
	
	static {
		for(PIDLimitEventType cm : EnumSet.allOf(PIDLimitEventType.class)) {
			lookup.put(cm.getValue(), cm);
		}
	}
	
	/** The value. */
	private byte value;
	
	/**
	 * Instantiates a new bowler method.
	 *
	 * @param val the val
	 */
	private PIDLimitEventType(int val) {
		value = (byte) val;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public byte getValue() {
		return value; 
	}

    /**
     * Gets the.
     *
     * @param code the code
     * @return the bowler method
     */
    public static PIDLimitEventType get(byte code) { 
    	
    	return lookup.get(code); 
    }
    
    /**
     * Gets the.
     *
     * @param code the code
     * @return the bowler method
     */
    public static List<PIDLimitEventType> getAllLimitMasks(byte code) { 
    	ArrayList<PIDLimitEventType> ret = new ArrayList<PIDLimitEventType>();
    	
    	for(PIDLimitEventType s:PIDLimitEventType.values()){
    		if((s.value&code)>0){
    			ret.add(s);
    		}
    	}
    	return ret; 
    }

	/**
	 * Gets the bytes.
	 *
	 * @return the bytes
	 */
	/* (non-Javadoc)
	 * @see com.neuronrobotics.sdk.common.ISendable#getBytes()
	 */
	public byte[] getBytes() {
		byte [] b = {getValue()};
		return b;
	}
	
}
