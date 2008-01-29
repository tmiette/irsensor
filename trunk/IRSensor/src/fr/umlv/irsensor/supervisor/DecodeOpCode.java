package fr.umlv.irsensor.supervisor;

import java.nio.ByteBuffer;

public class DecodeOpCode {
  
  public static OpCode decodeByteBuffer(ByteBuffer bb) {
    bb.flip();
    byte opcod = bb.get();
    for (OpCode opcode : OpCode.values()) {
      if (opcode.getCode().equals(opcod)) {
        return opcode;
      }
    }
    return null;
  }
  
}
