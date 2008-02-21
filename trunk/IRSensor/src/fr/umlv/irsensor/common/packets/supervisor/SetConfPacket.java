package fr.umlv.irsensor.common.packets.supervisor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFields;
import fr.umlv.irsensor.common.exception.MalformedPacketException;

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
   * Payload | Parent address | Optional |
   */
  private final OpCode opCode = OpCode.SETCONF;
  private final int id;
  private final CatchArea ca;
  private final int clock;
  private final int autonomy;
  private final int quality;
  private final int payload;
  private final InetAddress parent;

  public SetConfPacket(int id, CatchArea ca, int clock, int autonomy,
      int quality, int payload, InetAddress parent) {
    super();
    this.id = id;
    this.ca = ca;
    this.clock = clock;
    this.autonomy = autonomy;
    this.quality = quality;
    this.payload = payload;
    this.parent = parent;
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

  /**
   * Returns SetConfPacket corresponding to packet. This methods parses the
   * ByteBuffer and create a SetConfPacket object corresponding to it.
   * 
   * @param bb packet to read.
   * @return SetConfPacket to create.
   * @throws MalformedPacketException if the {@link ByteBuffer} isn't valid
   *           packet.
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

    InetAddress parent = getParent(packet);

    // return new SetConfPacket(id);
    return new SetConfPacket(id, ca, clock, autonomy, quality, payload,
        parent);
  }

  /**
   * Returns the Parent address of a sensor contained in the packet.
   * 
   * @param packet packet to read.
   * @return InetAddress parent address.
   * @throws MalformedPacketException 
   */
  private static InetAddress getParent(ByteBuffer packet) throws MalformedPacketException {
    byte[] parent = new byte[4];
    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.CATCH_AREA, PacketFields.CLOCK,
        PacketFields.AUTONOMY, PacketFields.PAYLOAD, PacketFields.QUALITY));

    packet.get(parent);
    InetAddress adr = null;
    try {
      adr = InetAddress.getByAddress(parent);
    } catch (UnknownHostException e) {
      throw new MalformedPacketException();
    }
    return adr;
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
  public InetAddress getParent() {
    return parent;
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
