package fr.umlv.irsensor.common.packets.supervisor;

import fr.umlv.irsensor.common.OpCode;


public interface SupervisorPacket {
	
	public OpCode getOpCode();
	
	public int getId();
}
