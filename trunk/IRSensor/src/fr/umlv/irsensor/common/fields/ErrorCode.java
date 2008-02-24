package fr.umlv.irsensor.common.fields;

/**
 * This enumeration makes translation between a {@link ErrorCode} string and the
 * its byte value. An {@link ErrorCode} represents an error that can occurs
 * during a server-client communication.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public enum ErrorCode {
  CONNECT_ERR("00010000"), CONF_ERR("00000001"), STATE_ERR("00000010"), IS_DOWN(
      "00000011"), CANNOT_REPLY("00000100"), OK("00000000"), INVALID_PACKET(
      "00010001");
  private final byte code;

  private ErrorCode(String code) {
    this.code = Byte.parseByte(code, 2);
  }

  /**
   * 
   * @return the {@link OpCode} in byte.
   */
  public byte getCode() {
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
   * Tests if a given {@link OpCode} error is sent by server.
   * 
   * @param code to test.
   * @return if {@link ErrorCode} belongs to server.
   */
  public boolean isServerSide(ErrorCode code) {
    return (code.getCode() >> 4) > 1;
  }

  /**
   * Tests if a given {@link OpCode} error is sent by client.
   * 
   * @param code to test.
   * @return if {@link ErrorCode} belongs to client.
   */
  public boolean isSensorSide(ErrorCode code) {
    return !isServerSide(code);
  }

  /**
   * Transcode a byte array to an {@link ErrorCode}.
   * @param b
   * @return
   */
  public static ErrorCode getErrorCode(byte[] b) {
    if (b.length == getOpCodeByteSize()) {
      for (ErrorCode code : ErrorCode.values()) {
        if (code.getCode() == b[0]) { return code; }
      }
    }
    return null;

  }

  /**
   * Displays a message corresponding to an {@link ErrorCode}.
   * @param code code to display.
   * @return code message.
   */
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
