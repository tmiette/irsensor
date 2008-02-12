package fr.umlv.irsensor.common.packets;


public enum ErrorCode {
  CONNECT_ERR("00010000"), CONF_ERR("00000001"), STATE_ERR("00000010"), IS_DOWN(
      "00000011"), CANNOT_REPLY("00000100"), OK("00000000");
  private final byte code;

  private ErrorCode(String code) {
    this.code = Byte.parseByte(code, 2);
  }

  public byte getCode() {
    return this.code;
  }

  public static int getOpCodeByteSize() {
    return 1;
  }

  public boolean isServerSide(ErrorCode code) {
    return (code.getCode() >> 4) > 1;
  }

  public boolean isSensorSide(ErrorCode code) {
    return !isServerSide(code);
  }
}
