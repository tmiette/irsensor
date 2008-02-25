package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

public class ReqDataPacket
    implements SensorPacket {

  /*
   * | Header | id | catch area | min quality requested | min date requested |
   */
  private final OpCode opCode = OpCode.REQDATA;
  private final int id;
  private final CatchArea ca;
  private final int quality;
  private final int clock;

  public ReqDataPacket(int id, CatchArea ca, int clock, int quality) {
    super();
    this.id = id;
    this.ca = ca;
    this.clock = clock;
    this.quality = quality;
  }

  /**
   * Returns the area contained in the packet.
   * 
   * @param packet packet to read.
   * @return CatchArea contained in the packet.
   */
  private static CatchArea getCatchArea(ByteBuffer packet) {

    CatchArea area = null;
    packet.position(PacketFields
        .getLength(PacketFields.OPCODE, PacketFields.ID));

    area = new CatchArea(packet.getInt(), packet.getInt(), packet.getInt(),
        packet.getInt());
    return area;
  }

  /**
   * Returns the clock value contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing clock.
   */
  private static int getClock(ByteBuffer packet) {
    int clock = -1;
    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.CATCH_AREA, PacketFields.QUALITY));

    clock = packet.getInt();
    return clock;
  }

  /**
   * Returns SetConfPacket corresponding to packet. This methods parses the
   * ByteBuffer and create a SetConfPacket object corresponding to it.
   * 
   * @param bb packet to read.
   * @return SetConfPacket to create.
   * @throws MalformedPacketException if the {@link ByteBuffer} isn't valid
   *           packet.
   */
  public static ReqDataPacket getPacket(ByteBuffer bb)
      throws MalformedPacketException {

    if (bb == null) throw new IllegalArgumentException();
    ByteBuffer packet = bb.duplicate();
    packet.clear();
    int index = 0;

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.CATCH_AREA, PacketFields.QUALITY,
        PacketFields.CLOCK)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[OpCode.getOpCodeByteSize()];
    packet.get(code, 0, OpCode.getOpCodeByteSize());
    if (!OpCode.REQDATA.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();

    CatchArea ca = getCatchArea(packet);

    int clock = getClock(packet);

    int quality = getQuality(packet);

    // return new SetConfPacket(id);
    return new ReqDataPacket(id, ca, clock, quality);
  }

  /**
   * Returns the quality value contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing quality.
   */
  private static int getQuality(ByteBuffer packet) {
    int quality = -1;
    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.CATCH_AREA));

    quality = packet.getInt();
    return quality;
  }

  /**
   * @return the catchArea
   */
  public CatchArea getCatchArea() {
    return ca;
  }

  /**
   * @return the clock
   */
  public int getClock() {
    return clock;
  }

  /**
   * @return the quality
   */
  public int getQuality() {
    return quality;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  @Override
  public String toString() {
    return "REQDATA : " + " id : " + id + " Quality : " + quality + " Clock : "
        + clock + " Area " + ca;
  }

  @Override
  public int getId() {
    return this.id;
  }

}
