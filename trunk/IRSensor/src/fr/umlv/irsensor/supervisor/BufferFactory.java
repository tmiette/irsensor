package fr.umlv.irsensor.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.sensor.CatchArea;

/**BufferFactory is in charge of creating the supervisor protocol packets
 * A packet is represented by a <code>ByteBuffer</code>
 * 
 * @author Miette Tom
 * @author Moreau Alan
 * @author Mouret Sebastien
 * @author Pons Julien
 */
public class BufferFactory {
	
	/**Create a new REPCON packet according to the given parameters 
	 * 
	 * @return a buffered packet
	 */
	public static ByteBuffer createRepConPacket(int id){
		final ByteBuffer buffer = ByteBuffer.allocate(OpCode.getOpCodeByteSize()+4);
		buffer.put(0,OpCode.REPCON.getCode());
		buffer.putInt(OpCode.getOpCodeByteSize(),id);
		
		return buffer;
	}
	
	/**Create a new REQCON packet 
	 * 
	 * @return a buffered packet
	 */
	public static ByteBuffer createReqConPacket(){
		final ByteBuffer buffer = ByteBuffer.allocate(OpCode.getOpCodeByteSize());
		buffer.put(0, OpCode.REQCON.getCode());
		
		return buffer;
	}

	/**
	 * Returns a new SETCONF packet.
	 * @param id sensor id
	 * @param area an area a sensor can capture
	 * @param clock clock's sensor
	 * @param autonomy autonomy's sensor
	 * @param quality quality's sensor
	 * @param payload payload's sensor
	 * @return a bytebuffer corresponding to a SETCONF packet 
	 */
	public static ByteBuffer createSetConfPacket(int id, CatchArea area, int clock, int autonomy, int quality, int payload) {
    final ByteBuffer buffer = ByteBuffer.allocate(64);
    // packet : header | area | clock | autonomy | quality | payload
    buffer.put(BufferFactory.createRepConPacket(id));
    buffer.putInt(area.getP1().getX()); 
    buffer.putInt(area.getP1().getY()); 
    buffer.putInt(area.getP2().getX());
    buffer.putInt(area.getP2().getY()); 
    buffer.putInt(clock);
    buffer.putInt(autonomy);
    buffer.putInt(quality);
    buffer.putInt(payload);
    return buffer;
  }
	
	
	
	
	
	
	
}
