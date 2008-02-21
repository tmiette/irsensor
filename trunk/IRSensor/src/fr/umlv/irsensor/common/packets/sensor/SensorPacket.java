package fr.umlv.irsensor.common.packets.sensor;

import fr.umlv.irsensor.common.fields.OpCode;

public interface SensorPacket {

  int getId();
  OpCode getOpCode();

}
