package fr.umlv.irsensor.common.packets;

import fr.umlv.irsensor.common.OpCode;


public interface SupervisorPacket {
	
	public OpCode getOpCode();
	
	public int getId();
}
