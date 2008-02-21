package fr.umlv.irsensor.common.packets.supervisor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

public class RepDataPacket
    implements SupervisorPacket {

  private final OpCode opCode = OpCode.REPDATA;
  private final int id;
  private final byte[] datas;
  private int dataLen;
  private int mimetype;

  public RepDataPacket(int id, int mimeType, int dataLen, byte[] datas) {
    this.id = id;
    this.mimetype = mimeType;
    this.dataLen = dataLen;
    this.datas = datas;
  }

  /**
   * Parses a REPDATA packet and instantiate a new RepData object offering
   * method to retrieve the packet's fields
   * 
   * @param bb the bytebuffer containing the REPDATA packet
   * @return an object representing this packet
   * @throws MalformedPacketException if the expecting REPDATA packet is
   *           malformed
   */
  public static RepDataPacket getPacket(ByteBuffer bb)
      throws MalformedPacketException {
    if (bb == null) throw new IllegalArgumentException("Empty REPDATA packet");
    ByteBuffer packet = bb.duplicate();
    int index = 0;

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REPDATA.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();
    index += PacketFields.getLength(PacketFields.ID);
    int mimetype = packet.getInt(index);
    index += PacketFields.getLength(PacketFields.MIMETYPE);
    int datalen = packet.getInt(index);
    index += PacketFields.getLength(PacketFields.LENGHT);
    // Tests if the state is valid and sets it
    // TODO : test erroné
    if (packet.capacity() <= PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID)) { throw new MalformedPacketException(
        "No datas were found in REPDATA"); }
    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.MIMETYPE, PacketFields.LENGHT));
    final byte[] datas = new byte[datalen];
    for (int i = 0; i < datalen; i++) {
      datas[i] = packet.get();
    }
    return new RepDataPacket(id, mimetype, datalen, datas);
  }

  @Override
  public int getId() {
    return this.id;
  }

  public int getDataLenght() {
    return this.dataLen;
  }

  public int getMimetype() {
    return mimetype;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  public byte[] getDatas() {
    return datas;
  }

  @Override
  public String toString() {
    return "Packet : ID = " + id + " MimeType : " + mimetype + " Data len : "
        + dataLen;
  }
}
