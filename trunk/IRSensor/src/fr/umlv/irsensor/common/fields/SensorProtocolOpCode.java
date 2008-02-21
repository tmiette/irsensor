package fr.umlv.irsensor.common.fields;

import java.nio.ByteBuffer;

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

  public Byte getCode() {
    return this.code;
  }

  public static int getOpCodeByteSize() {
    return 1;
  }

  public static SensorProtocolOpCode getOpcode(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (SensorProtocolOpCode opcode : SensorProtocolOpCode.values()) {
      if (opcode.getCode().equals(opcod)) { return opcode; }
    }
    return null;
  }

  public boolean equals(byte[] opcode) {
    boolean flag = false;
    for (int i = 0; i < SensorProtocolOpCode.getOpCodeByteSize(); i++) {
      if (this.code == (opcode[i])) flag = true;
      else flag = false;
    }

    return flag;
  }
}
