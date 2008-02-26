package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

/**
 * This class represents a GETCONF packet like an object instance.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class GetConfPacket
    implements SupervisorPacket {

  private final int id;

  private final OpCode opCode = OpCode.GETCONF;

  /**
   * Decodes a bytebuffer representing a packet to a {@link GetConfPacket}
   * instance.
   * 
   * @param packet to transcode.
   * @return {@link GetConfPacket} instance corresponding.
   * @throws MalformedPacketException if packet contains bad data.
   */
  public static GetConfPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.GETCONF.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();

    return new GetConfPacket(id);
  }

  /**
   * 
   * @param return id 
   */
  private GetConfPacket(int id) {
    this.id = id;
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
