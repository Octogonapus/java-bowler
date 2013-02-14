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
package com.neuronrobotics.sdk.common;

// TODO: Auto-generated Javadoc
/**
 * This DatagramFactory Builds a datagram. 
 * 
 * @author robert
 *
 */

public class BowlerDatagramFactory {
	
	/** The Constant REVISION. */
	//public static final byte REVISION = BowlerDatagram.REVISION;
	
	/**
	 * This builds a datagram with the given RPC and MAC address.
	 *
	 * @param addr The MAC address of the DyIO
	 * @param cmd 	The RPC
	 * @return 		A valid Bowler Datagram.
	 */
	private static int failed=0;
	public static BowlerDatagram build(MACAddress addr, BowlerAbstractCommand cmd, int id) {
		ByteList data = new ByteList();
		data.add(BowlerDatagram.REVISION); // revision
		data.add(addr.getBytes()); // mac address
		data.add(cmd.getMethod().getValue()); // method id
		data.add(id); // transaction id
		data.add(cmd.getLength()); // data length
		byte crc = data.genCRC();
		data.add(crc); // crc
		data.add(cmd.getBytes()); // packet
		return new BowlerDatagram(data);
	}
	public static BowlerDatagram build(ByteList buffer ){
		return build(buffer, new BowlerDatagram() );
	}
	public static BowlerDatagram build(ByteList buffer, BowlerDatagram staticMemory){
		if((buffer.size()==0))
			return null;
		byte fb;
		try{
			fb = buffer.get(0);
		}catch (Exception e){
			Log.warning("Datagram builder first byte warning: "+e.getMessage());
			return null;
		}
		
		while(fb!=BowlerDatagram.REVISION) {
			Log.error("First Byte Fail, Junk byte: "+String.format("%02x ", buffer.pop()));
			failed++;
			try{
				if(buffer.size()==0){
					if(failed>0){
						//Log.error("Failed out "+failed+" bytes");
					}
					return null;
				}
				fb = buffer.get(0);
			}catch (Exception e){
				Log.warning("Datagram builder warning: "+e.getMessage());
				if(failed>0){
					//Log.error("Failed out "+failed+" bytes");
				}
				return null;
			}
		}
		
		
		if(buffer.size()<BowlerDatagram.HEADER_SIZE) 
			return null;
		boolean check = false;
		while(check==false) {
			try{
				if( (buffer.get(0) != BowlerDatagram.REVISION)
						|| (!BowlerDatagram.CheckCRC(buffer))){
					if(buffer.get(0) != BowlerDatagram.REVISION)
						Log.error("First Byte Fail (second attempt) Junk byte: "+String.format("%02x ", buffer.pop()));
					else
						Log.error("CRC check Fail (second attempt) Junk byte: "+String.format("%02x ", buffer.pop()));
					failed++;
				}else{
					check=true;
				}
			}catch (Exception e){
				//Log.error("Datagram builder error: "+e.getMessage());
				if(failed>0){
					//Log.error("Failed out "+failed+" bytes");
				}
				e.printStackTrace();
				return null;
			}
			if (buffer.size()<BowlerDatagram.HEADER_SIZE) {
				if(failed>0){
					//Log.error("Failed out "+failed+" bytes");
				}
				return null;//Not enough bytes to even be a header, try back later
			}
		}
		int len =(int) buffer.getByte(9);
		if(len<0){
			len+=256;
		}
		if(len<4){
			Log.error("#*#*Warning, packet has no RPC, size: "+len);
			
		}
		int totalLen = len+BowlerDatagram.HEADER_SIZE;
		// See if all the data has arived for this packet
		if (buffer.size()>=(totalLen) ){
			failed=0;
			ByteList rawContent = new ByteList(buffer.popList(totalLen));
			staticMemory.parse(rawContent);
			return  staticMemory;
		}
//		if(failed>0)
//			Log.error("Failed out "+failed+" bytes");
		return null;
	}
}
