package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

/**
 * This class represents a REQCON packet like an object instance.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class ReqConPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.REQCON;

  /**
   * Decodes a bytebuffer representing a packet to a {@link ReqConPacket}
   * instance.
   * 
   * @param packet to transcode.
   * @return {@link ReqConPacket} instance corresponding.
   * @throws MalformedPacketException if packet contains bad data.
   */
  public static ReqConPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REQCON.equals(code)) throw new MalformedPacketException();

    return new ReqConPacket();
  }

  private ReqConPacket() {
  }

  @Override
  public int getId() {
    return -1;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

}
