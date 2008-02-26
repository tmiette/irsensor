package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

/**
 * This class represents a REPHELLO packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class RepHelloPacket
    implements SensorPacket {
  // | Opcode | idD | idS |
  private final OpCode opCode;
  private final int destId;
  private final int sourceId;

  /**
   * Decodes a bytebuffer to a {@link RepHelloPacket}.
   * 
   * @param packet to transcode.
   * @return {@link RepHelloPacket} instance.
   * @throws MalformedPacketException if a packet field is invalid.
   */
  public static RepHelloPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.ID)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REPHELLO.equals(code))
      throw new MalformedPacketException("Illegal opcode");

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int dId = packet.getInt(index);
    if (dId < 0) throw new MalformedPacketException("Illegal detination id");

    // Tests if the id is valid and sets it
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
