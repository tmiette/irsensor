package fr.umlv.irsensor.common.packets;

public enum PacketFieldLength {

  CATCH_AREA(16), CLOCK(4), AUTONOMY(4), QUALITY(4), PAYLOAD(4), PARENT_ID(4), ID(
      4), OPCODE(OpCode.getOpCodeByteSize()), STATE(2), ERROR_CODE(1);

  private int length;

  private PacketFieldLength(int length) {

    this.length = length;
  }

  public int getLength() {
    return length;
  }

  public static int getLength(PacketFieldLength... fieldLengths) {
    int sum = 0;
    for (PacketFieldLength packetFieldLength : fieldLengths) {
      sum += packetFieldLength.getLength();
    }
    return sum;
  }
}
