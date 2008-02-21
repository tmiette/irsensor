package fr.umlv.irsensor.common.packets.sensor;

import fr.umlv.irsensor.common.OpCode;

public class RepHelloPacket
    implements SensorPacket {
  //| Opcode | idD | idS |
  private final OpCode opCode;
  private final 

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public OpCode getOpCode() {
    // TODO Auto-generated method stub
    return null;
  }

}
