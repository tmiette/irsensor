package fr.umlv.irsensor.common.packets;

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
   * valid Id found at current position.
   * @param bb bytebuffer which contains the id.
   * @return int which represents the id.
   */
  public static int getId(ByteBuffer packet) {
    int id = -1;
    if (packet != null) {
      System.out.println(packet);
      ByteBuffer bb = packet.duplicate();
      bb.position(0);
      id = bb.getInt(OpCode.getOpCodeByteSize());
      System.out.println(bb);
    }
    return id;
  }

  /**
   * Returns the error code contained in the given packet or null if no
   * ErrorCode is present.
   * @param packet packet to read.
   * @return ErrorCode contained in the packet.
   */
  public static ErrorCode getErrorCode(ByteBuffer packet) {
    if (packet != null) {
      ByteBuffer bb = packet.duplicate();
      bb.rewind();
      byte[] errorCode = new byte[ErrorCode.getOpCodeByteSize()];
      // Opcode | id | ErrorCode
      bb.position(OpCode.getOpCodeByteSize() + (Integer.SIZE / 8));
      bb.get(errorCode, 0, ErrorCode.getOpCodeByteSize());
      for (int i = 0; i < ErrorCode.getOpCodeByteSize(); i++) {
        for (ErrorCode code : ErrorCode.values()) {
          if (code.getCode() == (errorCode[i])) {
            return code;
          }
        }
      }
    }
    return null;
  }

  /**
   * Returns the opcode of the packet. Returns null if no opcode is present.
   * @param packet packet to read.
   * @return OpCode of the packet
   */
  public static OpCode getOpCode(ByteBuffer packet) {
    if (packet != null) {
      ByteBuffer bb = packet.duplicate();
      bb.rewind();
      byte[] code = new byte[OpCode.getOpCodeByteSize()];
      // Opcode
      bb.position(0);
      bb.get(code, 0, OpCode.getOpCodeByteSize());
      for (int i = 0; i < OpCode.getOpCodeByteSize(); i++) {
        for (OpCode opCode : OpCode.values()) {
          if (opCode.getCode() == (code[i])) {
            return opCode;
          }
        }
      }
    }
    return null;
  }
  
  
}















