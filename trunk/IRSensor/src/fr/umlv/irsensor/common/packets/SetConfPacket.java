package fr.umlv.irsensor.common.packets;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.sensor.CatchArea;

/**
 * This object represents a SETCONF packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SetConfPacket
    implements SupervisorPacket {

  /*
   * SETCONF packet : | Header | Id | Catch Area | Clock | Autonomy | Quality |
   * Payload | Parent | address | Optional |
   */
  private final int id;
  private final OpCode opCode = OpCode.SETCONF;
  private final CatchArea ca;
  private final int clock;
  private final int autonomy;
  private final int quality;
  private final int payload;
  private final int parentId;

  public SetConfPacket(int id, CatchArea ca, int clock, int autonomy,
      int quality, int payload, int parentId) {
    super();
    this.id = id;
    this.ca = ca;
    this.clock = clock;
    this.autonomy = autonomy;
    this.quality = quality;
    this.payload = payload;
    this.parentId = parentId;
  }

  /**
   * Returns the autonomy contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing autonomy.
   */
  private static int getAutonomy(ByteBuffer packet) {
    int auto = -1;
    packet.position(PacketFields.OPCODE.getLength()
        + PacketFields.ID.getLength() + PacketFields.CATCH_AREA.getLength()
        + PacketFields.CLOCK.getLength() + PacketFields.AUTONOMY.getLength());

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
    if (packet != null) {
      ByteBuffer bb = packet.duplicate();
      bb
          .position(PacketFields.OPCODE.getLength()
              + PacketFields.ID.getLength());
      area = new CatchArea(bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt());
    }
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
    ByteBuffer bb = packet.duplicate();
    bb.position(PacketFields.OPCODE.getLength() + PacketFields.ID.getLength()
        + PacketFields.CATCH_AREA.getLength());

    clock = bb.getInt();
    return clock;
  }

  /**
   * Returns SetConfPacket corresponding to packet. This methods parses the
   * ByteBuffer and create a SetConfPacket object corresponding to it.
   * 
   * @param bb packet to read.
   * @return SetConfPacket to create.
   * @throws MalformedPacketException if the {@link ByteBuffer} isn't valid packet.
   */
  public static SetConfPacket getPacket(ByteBuffer bb)
      throws MalformedPacketException {

    if (bb == null) throw new IllegalArgumentException();
    ByteBuffer packet = bb.duplicate();
    int index = 0;

    // Tests if it's a valid OpCode
    final byte[] code = new byte[OpCode.getOpCodeByteSize()];
    packet.get(code, 0, OpCode.getOpCodeByteSize());
    if (!OpCode.SETCONF.equals(code)) throw new MalformedPacketException();

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int id = packet.getInt(index);
    if (id < 0) throw new MalformedPacketException();

    CatchArea ca = getCatchArea(packet);

    int clock = getClock(packet);

    int autonomy = getAutonomy(packet);

    int quality = getQuality(packet);

    int payload = getPayload(packet);

    int parentId = getParentID(packet);

    // return new SetConfPacket(id);
    return new SetConfPacket(id, ca, clock, autonomy, quality, payload,
        parentId);
  }

  /**
   * Returns the ParentId value contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing the ParentId.
   */
  private static int getParentID(ByteBuffer packet) {
    int parentID = -1;
    packet.position(PacketFields.OPCODE.getLength()
        + PacketFields.ID.getLength() + PacketFields.CATCH_AREA.getLength()
        + PacketFields.CLOCK.getLength() + PacketFields.AUTONOMY.getLength()
        + PacketFields.PAYLOAD.getLength() + PacketFields.QUALITY.getLength());

    parentID = packet.getInt();
    return parentID;
  }

  /**
   * Returns the Payload value contained in the packet.
   * 
   * @param packet packet to read.
   * @return int representing payload.
   */
  private static int getPayload(ByteBuffer packet) {
    int payload = -1;
    packet.position(PacketFields.OPCODE.getLength()
        + PacketFields.ID.getLength() + PacketFields.CATCH_AREA.getLength()
        + PacketFields.CLOCK.getLength() + PacketFields.AUTONOMY.getLength());

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
    packet.position(PacketFields.OPCODE.getLength()
        + PacketFields.ID.getLength() + PacketFields.CATCH_AREA.getLength()
        + PacketFields.CLOCK.getLength() + PacketFields.AUTONOMY.getLength()
        + PacketFields.PAYLOAD.getLength());

    quality = packet.getInt();
    return quality;
  }

  /**
   * @return the ca
   */
  public CatchArea getCa() {
    return ca;
  }

  /**
   * @return the clock
   */
  public int getClock() {
    return clock;
  }

  /**
   * @return the autonomy
   */
  public int getAutonomy() {
    return autonomy;
  }

  /**
   * @return the quality
   */
  public int getQuality() {
    return quality;
  }

  /**
   * @return the payload
   */
  public int getPayload() {
    return payload;
  }

  /**
   * @return the parentId
   */
  public int getParentId() {
    return parentId;
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
