package fr.umlv.irsensor.common.packets;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFields;
import fr.umlv.irsensor.common.exception.MalformedPacketException;

public class RepDataPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.REPDATA;
  private final int id;
  private final byte[] datas;

  private RepDataPacket(int id, byte[] datas) {
    this.id = id;
    this.datas = datas;
  }

  public static RepDataPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();
    int index = 0;

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REPDATA.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();

    // Tests if the state is valid and sets it
    if (packet.capacity() <= PacketFields.getLength(PacketFields.OPCODE, PacketFields.ID)) {
      throw new MalformedPacketException();
      //No data found
    }
    final byte[] datas = new byte[packet.capacity()];
    for (int i = packet.position(); i < packet.limit(); i++) {
      datas[i] = packet.get();
    }

    return new RepDataPacket(id, datas);
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  public byte[] getDatas() {
    return datas;
  }
}
