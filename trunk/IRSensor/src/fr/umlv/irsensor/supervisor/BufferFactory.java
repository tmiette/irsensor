package fr.umlv.irsensor.supervisor;

import java.nio.ByteBuffer;

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
}
