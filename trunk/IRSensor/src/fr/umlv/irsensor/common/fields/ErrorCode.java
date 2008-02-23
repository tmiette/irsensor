package fr.umlv.irsensor.common.fields;

public enum ErrorCode {
  CONNECT_ERR("00010000"),
  CONF_ERR("00000001"),
  STATE_ERR("00000010"),
  IS_DOWN("00000011"),
  CANNOT_REPLY("00000100"),
  OK("00000000"),
  INVALID_PACKET("00010001");
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

  public static ErrorCode getErrorCode(byte[] b) {
    if (b.length == getOpCodeByteSize()) {
      for (ErrorCode code : ErrorCode.values()) {
        if (code.getCode() == b[0]) {
          return code;
        }
      }
    }
    return null;

  }

  public static String getErrorMessage(ErrorCode code) {
    StringBuilder sb = new StringBuilder();
    sb.append("Error code ");
    sb.append(code.name());
    sb.append(" received : ");

    switch (code) {
    case CONF_ERR:
      sb.append(" the configuration of the sensor is invalid.");
      break;
    default:
      break;
    }

    return sb.toString();
  }
}
