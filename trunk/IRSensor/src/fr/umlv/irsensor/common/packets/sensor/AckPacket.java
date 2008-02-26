package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;
import fr.umlv.irsensor.common.packets.supervisor.SupervisorPacket;

/**
 * This class represents a ACK packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class AckPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.ACK;
  private final int id;
  private final ErrorCode errorCode;

  private AckPacket(int id, ErrorCode errorCode) {
    this.id = id;
    this.errorCode = errorCode;
  }

  /**
   * Decodes a bytebuffer packet to a {@link AckPacket}.
   * 
   * @param packet to decode.
   * @return corresponding {@link AckPacket}.
   * @throws MalformedPacketException if packet contains bad data.
   */
  public static AckPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {

    if (packet == null) throw new IllegalArgumentException("Illegal packet");
    int index = 0;

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.ERROR_CODE)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.ACK.equals(code))
      throw new MalformedPacketException("Illegal Opcode");

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException("Illegal Id");

    // Tests if the ErrorCode is valid and sets it
    final byte[] b = new byte[PacketFields.ERROR_CODE.getLength()];
    packet.get(b, 0, PacketFields.ERROR_CODE.getLength());
    ErrorCode errorCode = ErrorCode.getErrorCode(b);
    if (errorCode == null) { throw new MalformedPacketException(
        "Illegal ErrorCode"); }

    return new AckPacket(id, errorCode);
  }

  /**
   * 
   * @return the {@link ErrorCode}.
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }
}
