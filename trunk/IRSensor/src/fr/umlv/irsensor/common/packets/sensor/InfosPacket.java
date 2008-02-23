package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

public class InfosPacket
    implements SensorPacket {

  // | Opcode | IdD | IdS | Catch Area | Clock | Autonomy | Quality | Payload |
  private final OpCode opCode;
  private final int destId;
  private final int sourceId;
  private final CatchArea catchArea;
  private final int clock;
  private final int autonomy;
  private final int quality;
  private final int payload;

  public InfosPacket(OpCode opCode, int destId, int sourceId,
      CatchArea catchArea, int clock, int autonomy, int quality, int payload) {
    super();
    this.opCode = opCode;
    this.destId = destId;
    this.sourceId = sourceId;
    this.catchArea = catchArea;
    this.clock = clock;
    this.autonomy = autonomy;
    this.quality = quality;
    this.payload = payload;
  }

  /**
   * Returns the autonomy contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing autonomy.
   */
  private static int getAutonomy(ByteBuffer packet) {
    int auto = -1;
    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.CATCH_AREA, PacketFields.CLOCK));

    auto = packet.getInt();
    return auto;
  }

  /**
   * Returns the area contained in the packet.�
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
        PacketFields.ID, PacketFields.CATCH_AREA));

    clock = packet.getInt();
    return clock;
  }

  public static InfosPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException("Illegal packet");
    int index = 0;

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.ID, PacketFields.CATCH_AREA,
        PacketFields.CLOCK, PacketFields.AUTONOMY, PacketFields.QUALITY,
        PacketFields.PAYLOAD)) { throw new MalformedPacketException(
        "Packet too short"); }

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.INFOSPACKET.equals(code))
      throw new MalformedPacketException("Illegal opcode");

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int dId = packet.getInt(index);
    if (dId < 0) throw new MalformedPacketException("Illegal destination id");

    // Tests if the id is valid and sets it
    index += PacketFields.ID.getLength();
    int sId = packet.getInt(index);
    if (sId < 0) throw new MalformedPacketException("Illegal source id");

    CatchArea ca = getCatchArea(packet);

    int clock = getClock(packet);

    int autonomy = getAutonomy(packet);

    int quality = getQuality(packet);

    int payload = getPayload(packet);

    return new InfosPacket(OpCode.REPHELLO, sId, dId, ca, clock, autonomy,
        quality, payload);
  }

  /**
   * Returns the Payload value contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing payload.
   */
  private static int getPayload(ByteBuffer packet) {
    int payload = -1;
    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.CATCH_AREA, PacketFields.CLOCK,
        PacketFields.AUTONOMY, PacketFields.QUALITY));

    payload = packet.getInt();
    return payload;
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
        PacketFields.ID, PacketFields.CATCH_AREA, PacketFields.CLOCK,
        PacketFields.AUTONOMY));

    quality = packet.getInt();
    return quality;
  }

  /**
   * @return the autonomy
   */
  public int getAutonomy() {
    return autonomy;
  }

  /**
   * @return the catchArea
   */
  public CatchArea getCatchArea() {
    return catchArea;
  }

  /**
   * @return the clock
   */
  public int getClock() {
    return clock;
  }

  /**
   * @return the destId
   */
  public int getDestId() {
    return destId;
  }

  @Override
  public int getId() {
    return this.destId;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  /**
   * @return the payload
   */
  public int getPayload() {
    return payload;
  }

  /**
   * @return the quality
   */
  public int getQuality() {
    return quality;
  }

  /**
   * @return the sourceId
   */
  public int getSourceId() {
    return sourceId;
  }

}
