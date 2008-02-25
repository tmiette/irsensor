package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;
import fr.umlv.irsensor.common.fields.SensorState;
/**
 * This class represents a SETSTA packet like an object instance.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SetStatPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.SETSTA;
  private final int id;
  private final SensorState state;

  private SetStatPacket(int id, SensorState sensorState) {
    this.id = id;
    this.state = sensorState;
  }

  /**
   * Returns {@link SetStatPacket} corresponding to packet. This methods parses the
   * ByteBuffer and create a SetStatPacket object corresponding to it.
   * 
   * @param bb packet to read.
   * @return SetStatPacket to create.
   * @throws MalformedPacketException if the {@link ByteBuffer} isn't valid
   *           packet.
   */
  public static SetStatPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;
    
    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.STATE)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.SETSTA.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();

    // Tests if the state is valid and sets it
    final byte[] sta = new byte[PacketFields.STATE.getLength()];
    packet.position(PacketFields.getLength(PacketFields.OPCODE, PacketFields.ID));
    packet.get(sta, 0, PacketFields.STATE.getLength());
    SensorState state = SensorState.getState(sta);
    if (state == null) { throw new MalformedPacketException("SETSTA packet : STATE field unreadable"); }

    return new SetStatPacket(id, state);
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
