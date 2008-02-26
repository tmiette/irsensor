package fr.umlv.irsensor.common.fields;

import java.nio.ByteBuffer;

/**
 * this enumeration associates a byte value to an Opcode. An {@link OpCode}
 * specify a packet type.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public enum OpCode {

  REQCON("00000000") {

  },
  REPCON("00000001") {

  },
  SETCONF("00000010") {

  },
  GETCONF("00000011") {

  },
  REPCONF("00000100") {

  },
  SETSTA("00000101") {

  },
  GETSTA("00000110") {

  },
  REPSTA("00000111") {

  },
  ACK("00001000") {

  },
  REQDATA("00001001") {

  },
  REPDATA("00001010") {

  },
  REPHELLO("00001011") {

  },
  REQHELLO("00001100") {

  },
  INFOSPACKET("00001101") {

  };

  private final Byte code;

  private OpCode(String code) {
    this.code = Byte.parseByte(code, 2);
  }

  /**
   * 
   * @return byte {@link OpCode} value.
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
   * Transcodes a bytebuffer to a {@link OpCode}.
   * @param bb bytebuffer to transcode.
   * @return {@link OpCode} corresponding.
   */
  public static OpCode getOpcode(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (OpCode opcode : OpCode.values()) {
      if (opcode.getCode().equals(opcod)) { return opcode; }
    }
    return null;
  }

  /**
   * Tests if a byte array is a valid {@link OpCode}.
   * @param opcode byte array to test.
   * @return if the opcode is a valid {@link OpCode}.
   */
  public boolean equals(byte[] opcode) {
    boolean flag = false;
    for (int i = 0; i < OpCode.getOpCodeByteSize(); i++) {
      if (this.code == (opcode[i])) flag = true;
      else flag = false;
    }

    return flag;
  }
}
