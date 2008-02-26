package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;
import fr.umlv.irsensor.common.fields.SensorState;

/**
 * This class represents a REPSTA packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class RepStaPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.REPSTA;
  private final int id;
  private final SensorState state;

  private RepStaPacket(int id, SensorState state) {
    this.id = id;
    this.state = state;
  }

  /**
   * Parses a REPSTA packet and instantiate a new RepSta object offering
   * method to retrieve the packet's fields
   * 
   * @param bb the bytebuffer containing the REPSTA packet
   * @return an object representing this packet
   * @throws MalformedPacketException if the expecting REPSTA packet is
   *           malformed
   */
  public static RepStaPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;
    
    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.STATE)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REPSTA.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();

    // Tests if the state is valid and sets it
    final byte[] sta = new byte[PacketFields.STATE.getLength()];
    packet.position(PacketFields.getLength(PacketFields.OPCODE, PacketFields.ID));
    packet.get(sta, 0, PacketFields.STATE.getLength());
    SensorState state = SensorState.getState(sta);
    if (state == null) { throw new MalformedPacketException(); }

    return new RepStaPacket(id, state);
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  public SensorState getState() {
    return state;
  }
}
