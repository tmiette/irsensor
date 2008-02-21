package fr.umlv.irsensor.common.packets.supervisor;

import fr.umlv.irsensor.common.fields.OpCode;


public interface SupervisorPacket {
	
	public OpCode getOpCode();
	
	public int getId();
}
