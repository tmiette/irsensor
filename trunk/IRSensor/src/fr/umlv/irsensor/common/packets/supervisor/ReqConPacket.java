package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

public class ReqConPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.REQCON;

  public static ReqConPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException();

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
