package fr.umlv.irsensor.common;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.supervisor.OpCode;

public interface SupervisorPacket {
	
	public OpCode getOpCode();
	
	public int getId();
}
