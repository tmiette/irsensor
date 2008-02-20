package fr.umlv.irsensor.common.packets;

import fr.umlv.irsensor.common.OpCode;

public enum PacketFields {

  CATCH_AREA(16), CLOCK(4), AUTONOMY(4), QUALITY(4), PAYLOAD(4), PARENT_ID(4), ID(
      4), OPCODE(OpCode.getOpCodeByteSize()), STATE(2), ERROR_CODE(1), DATE(4), LENGHT(4), MIMETYPE(4);

  private int length;

  private PacketFields(int length) {

    this.length = length;
  }

  public int getLength() {
    return length;
  }

  public static int getLength(PacketFields... fieldLengths) {
    int sum = 0;
    for (PacketFields packetFieldLength : fieldLengths) {
      sum += packetFieldLength.getLength();
    }
    return sum;
  }
}
