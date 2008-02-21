package fr.umlv.irsensor.common.packets.data;

import fr.umlv.irsensor.common.fields.OpCode;

public interface DataPacket {
  public OpCode getOpCode();

  public int getId();
}
