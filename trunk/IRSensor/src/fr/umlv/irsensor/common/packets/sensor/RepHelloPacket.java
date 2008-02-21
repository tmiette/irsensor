package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFields;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.supervisor.GetConfPacket;

public class RepHelloPacket
    implements SensorPacket {
  // | Opcode | idD | idS |
  private final OpCode opCode;
  private final int sourceId;
  private final int destId;

  public static GetConfPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.GETCONF.equals(code)) throw new MalformedPacketException("Illegal opcode");

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int sourceId = packet.getInt(index);
    if (sourceId < 0) throw new MalformedPacketException("Illegal sourceId");
    
 // Tests if the id is valid and sets it
    index += PacketFields.ID.getLength();
    int sourceId = packet.getInt(index);
    if (sourceId < 0) throw new MalformedPacketException("Illegal ");
    
    int destId

    return new GetConfPacket(sourceId, );
  }

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
