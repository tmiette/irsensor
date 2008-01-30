package fr.umlv.irsensor.supervisor;

import java.nio.ByteBuffer;

/**
 * This class contains methods that permit to decode a Supervisor Server
 * Protocol packet.
 * @author MIETTE Tom
 * @author MOREAU Alan
 * @author MOURET Sebastien
 * @author PONS Julien
 */
public class DecodePacket {
  /**
   * Returns the id contained in the bytebuffer. The bytebuffer represents a SSP
   * (Supervisor Server Protocol) packet. This method returns -1 if there is no
   * valid Id found.
   * @param bb bytebuffer which contains the id.
   * @return int which represents the id.
   */
  public static int getId(ByteBuffer bb) {
    int id = -1;
    try {
      id = bb.getInt(OpCode.getOpCodeByteSize());
    } catch (IndexOutOfBoundsException e) {
      return id;
    }
    return id;
  }
  
  
}
