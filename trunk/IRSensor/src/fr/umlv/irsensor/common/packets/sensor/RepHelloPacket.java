package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFields;
import fr.umlv.irsensor.common.exception.MalformedPacketException;

public class RepHelloPacket implements SensorPacket {
  // | Opcode | idD | idS |
  private final OpCode opCode;
  private final int destId;
  private final int sourceId;

  public static RepHelloPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REPHELLO.equals(code))
      throw new MalformedPacketException("Illegal opcode");

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int sourceId = packet.getInt(index);
    if (sourceId < 0) throw new MalformedPacketException("Illegal sourceId");

    // Tests if the id is valid and sets it
    index += PacketFields.ID.getLength();
    int dId = packet.getInt(index);
    if (dId < 0) throw new MalformedPacketException("Illegal detination id");
    
    index += PacketFields.ID.getLength();
    int sId = packet.getInt(index);
    if (sId < 0) throw new MalformedPacketException("Illegal source id");

    return new RepHelloPacket(OpCode.REPHELLO, sId, dId);
  }

  public RepHelloPacket(OpCode opCode, int sourceId, int destId) {
    super();
    this.opCode = opCode;
    this.sourceId = sourceId;
    this.destId = destId;
  }

  /**
   * @return the sourceId
   */
  public int getSourceId() {
    return sourceId;
  }

  /**
   * @return the destId
   */
  public int getDestId() {
    return destId;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }
  
  @Override
  public int getId() {
    return this.destId;
  }

}
