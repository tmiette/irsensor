package fr.umlv.irsensor.common.packets.sensor;

import fr.umlv.irsensor.common.OpCode;

public interface SensorPacket {

  int getId();
  OpCode getOpCode();

}
