package fr.umlv.irsensor.common.fields;

import java.nio.ByteBuffer;
/**
 * This enumeration represents different values that can take a packet lead. The
 * packet lead represents a packet type for sensor protocol packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public enum SensorProtocolOpCode {

  ACK("00000000") {

  },
  REQDATA("00000001") {

  },
  REPDATA("00000010") {

  },
  REPHELLO("00000011") {

  },
  REQHELLO("00000100") {

  },
  INFOSPACKET("00000101") {

  };

  private final Byte code;

  private SensorProtocolOpCode(String code) {
    this.code = Byte.parseByte(code, 2);
  }

  /**
   * 
   * @return byte corresponding to {@link OpCode}.
   */
  public Byte getCode() {
    return this.code;
  }

  /**
   * 
   * @return {@link OpCode} size.
   */
  public static int getOpCodeByteSize() {
    return 1;
  }

  /**
   * Transcode a bytebuffer to {@link SensorProtocolOpCode}.
   * @param bb bytebuffer to transcode.
   * @return {@link SensorProtocolOpCode} corresponding.
   */
  public static SensorProtocolOpCode getOpcode(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (SensorProtocolOpCode opcode : SensorProtocolOpCode.values()) {
      if (opcode.getCode().equals(opcod)) { return opcode; }
    }
    return null;
  }

  /**
   * Tests if the given byte array corresponds to a valid {@link OpCode}.
   * @param opcode to test.
   * @return if given byte array corresponds to an {@link OpCode}.
   */
  public boolean equals(byte[] opcode) {
    boolean flag = false;
    for (int i = 0; i < SensorProtocolOpCode.getOpCodeByteSize(); i++) {
      if (this.code == (opcode[i])) flag = true;
      else flag = false;
    }

    return flag;
  }
}
