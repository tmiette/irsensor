package fr.umlv.irsensor.common.fields;

import java.nio.ByteBuffer;

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

  public Byte getCode() {
    return this.code;
  }

  public static int getOpCodeByteSize() {
    return 1;
  }

  public static DataProtocolOpCode getOpcode(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (DataProtocolOpCode opcode : DataProtocolOpCode.values()) {
      if (opcode.getCode().equals(opcod)) { return opcode; }
    }
    return null;
  }

  public boolean equals(byte[] opcode) {
    boolean flag = false;
    for (int i = 0; i < DataProtocolOpCode.getOpCodeByteSize(); i++) {
      if (this.code == (opcode[i])) flag = true;
      else flag = false;
    }

    return flag;
  }
}
