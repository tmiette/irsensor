package fr.umlv.irsensor.common.fields;

import java.nio.ByteBuffer;

/**
 * This enumeration represents different values that can take a packet lead. The
 * packet lead represents a packet type for data protocol packet.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public enum DataProtocolOpCode {

  ACK("00000000") {

  },
  REQDATA("00000001") {

  },
  REPDATA("00000010") {

  };

  private final Byte code;

  private DataProtocolOpCode(String code) {
    this.code = Byte.parseByte(code, 2);
  }

  /**
   * @return byte corresponding to opcode.
   */
  public Byte getCode() {
    return this.code;
  }

  /**
   * @return opcode size.
   */
  public static int getOpCodeByteSize() {
    return 1;
  }

  /**
   * Decode a bytebuffer to {@link DataProtocolOpCode}.
   * @param bb bytebuffer to decode.
   * @return DataProtocolOpCode corresponding to the bytebuffer.
   */
  public static DataProtocolOpCode getOpcode(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (DataProtocolOpCode opcode : DataProtocolOpCode.values()) {
      if (opcode.getCode().equals(opcod)) { return opcode; }
    }
    return null;
  }

  /**
   * Test if a byte array is a correct {@link DataProtocolOpCode}.
   * @param opcode opcode to test.
   * @return if {@link OpCode} equals to a {@link DataProtocolOpCode}.
   */
  public boolean equals(byte[] opcode) {
    boolean flag = false;
    for (int i = 0; i < DataProtocolOpCode.getOpCodeByteSize(); i++) {
      if (this.code == (opcode[i])) flag = true;
      else flag = false;
    }

    return flag;
  }
}
