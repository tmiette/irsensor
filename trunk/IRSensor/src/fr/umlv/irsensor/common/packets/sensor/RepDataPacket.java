package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

/**
 * This class represents a REPDATA packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class RepDataPacket
    implements SensorPacket {

  private final OpCode opCode = OpCode.REPDATA;
  private final int id;
  private int mimetype;
  private int dataLen;
  private final byte[] datas;

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
    packet.clear();
    int index = 0;

    if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.LENGHT, PacketFields.MIMETYPE)) { throw new MalformedPacketException(
        "Packet too " +
        "short"); }

/*    
    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REPDATA.equals(code)) throw new MalformedPacketException();
*/

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
    /*
     * if (packet.capacity() < PacketFields.getLength(PacketFields.OPCODE,
     * PacketFields.ID, PacketFields.MIMETYPE, PacketFields.LENGHT) + datalen) {
     * throw new MalformedPacketException( "No datas were found in REPDATA"); }
     */
    System.out.println(id + " recu " + datalen);

    packet.position(PacketFields.getLength(PacketFields.OPCODE,
        PacketFields.ID, PacketFields.MIMETYPE, PacketFields.LENGHT));

    final byte[] datas = new byte[datalen];
    packet.get(datas, 0, datalen);
    /*
     * for (int i = 0; i < datalen; i++) { datas[i] = packet.get(); }
     */
    return new RepDataPacket(id, mimetype, datalen, datas);
  }

  /**
   * 
   * @return data length.
   */
  public int getDataLenght() {
    return this.dataLen;
  }

  /**
   * 
   * @return the mimetype.
   */
  public int getMimetype() {
    return mimetype;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  /**
   * 
   * @return the data.
   */
  public byte[] getDatas() {
    return datas;
  }

  @Override
  public String toString() {
    return "Packet : ID = " + id + " MimeType : " + mimetype + " Data len : "
        + dataLen;
  }

  @Override
  public int getId() {
    return this.id;
  }
}
