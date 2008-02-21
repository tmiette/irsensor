package fr.umlv.irsensor.common.fields;

import java.nio.ByteBuffer;

public enum SupervisorProtocolOpCode {

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

  };

  private final Byte code;

  private SupervisorProtocolOpCode(String code) {
    this.code = Byte.parseByte(code, 2);
  }

  public Byte getCode() {
    return this.code;
  }

  public static int getOpCodeByteSize() {
    return 1;
  }

  public static SupervisorProtocolOpCode getOpcode(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (SupervisorProtocolOpCode opcode : SupervisorProtocolOpCode.values()) {
      if (opcode.getCode().equals(opcod)) { return opcode; }
    }
    return null;
  }

  public boolean equals(byte[] opcode) {
    boolean flag = false;
    for (int i = 0; i < SupervisorProtocolOpCode.getOpCodeByteSize(); i++) {
      if (this.code == (opcode[i])) flag = true;
      else flag = false;
    }

    return flag;
  }
}
