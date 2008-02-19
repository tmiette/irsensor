package fr.umlv.irsensor.common.packets;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;

public class RepConPacket implements SupervisorPacket {
	
	private final int id;
	
	private final OpCode opCode = OpCode.REQCON;
	
	public static RepConPacket getPacket(ByteBuffer packet) throws MalformedPacketException{
		if(packet == null) throw new IllegalArgumentException();
		int index = 0;
		
		//Tests if it's a valid OpCode
		final byte[] code = new byte[PacketFieldLength.OPCODE.getLength()];
		packet.get(code, 0, PacketFieldLength.OPCODE.getLength());
		if(!OpCode.REQCON.equals(code)) throw new MalformedPacketException();
		
		//Tests if the id is valid and sets it
		index+= PacketFieldLength.OPCODE.getLength();
		int id = packet.getInt(index);
		if(id<0) throw new MalformedPacketException();
		
		return new RepConPacket(id);
	}
	
	private RepConPacket(int id){
		this.id = id;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public OpCode getOpCode() {
		return this.opCode;
	}
}
