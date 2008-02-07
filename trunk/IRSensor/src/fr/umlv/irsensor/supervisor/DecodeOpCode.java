package fr.umlv.irsensor.supervisor;

import java.nio.ByteBuffer;

public class DecodeOpCode {
  
  public static OpCode decodeByteBuffer(ByteBuffer bb) {
    byte opcod = bb.get(0);
    for (OpCode opcode : OpCode.values()) {
      if (opcode.getCode().equals(opcod)) {
        return opcode;
      }
    }
    return null;
  }
  
}
