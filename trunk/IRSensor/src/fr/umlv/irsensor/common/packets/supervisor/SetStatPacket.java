package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFields;
import fr.umlv.irsensor.common.SensorState;
import fr.umlv.irsensor.common.exception.MalformedPacketException;

public class SetStatPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.SETSTA;
  private final int id;
  private final SensorState state;

  private SetStatPacket(int id, SensorState sensorState) {
    this.id = id;
    this.state = sensorState;
  }

  public static SetStatPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;

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
