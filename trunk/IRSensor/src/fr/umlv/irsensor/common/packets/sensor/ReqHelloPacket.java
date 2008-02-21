package fr.umlv.irsensor.common.packets.sensor;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.PacketFields;

public class ReqHelloPacket
    implements SensorPacket {

  // | Opcode | idD | idS | error code
  private final OpCode opCode;
  private final int destId;
  private final int sourceId;
  private final ErrorCode errorCode;

  /**
   * @return the errorCode
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public ReqHelloPacket(OpCode opCode, int destId, int sourceId,
      ErrorCode errorCode) {
    super();
    this.opCode = opCode;
    this.destId = destId;
    this.sourceId = sourceId;
    this.errorCode = errorCode;
  }

  public static ReqHelloPacket getPacket(ByteBuffer packet)
      throws MalformedPacketException {
    if (packet == null) throw new IllegalArgumentException("Illegal packet");
    int index = 0;

    // Tests if it's a valid OpCode
    final byte[] code = new byte[PacketFields.OPCODE.getLength()];
    packet.get(code, 0, PacketFields.OPCODE.getLength());
    if (!OpCode.REQHELLO.equals(code))
      throw new MalformedPacketException("Illegal opcode");

    // Tests if the id is valid and sets it
    index += PacketFields.OPCODE.getLength();
    int dId = packet.getInt(index);
    if (dId < 0) throw new MalformedPacketException("Illegal detination id");

    // Tests if the id is valid and sets it
    index += PacketFields.ID.getLength();
    int sId = packet.getInt(index);
    if (sId < 0) throw new MalformedPacketException("Illegal source id");

    // Tests if the ErrorCode is valid and sets it
    index += PacketFields.ID.getLength();
    final byte[] b = new byte[PacketFields.ERROR_CODE.getLength()];
    packet.get(b, 0, PacketFields.ERROR_CODE.getLength());
    ErrorCode errorCode = ErrorCode.getErrorCode(b);
    if (errorCode == null) { throw new MalformedPacketException(
        "Illegal errorCode"); }

    return new ReqHelloPacket(OpCode.REQHELLO, dId, sId, errorCode);
  }

  /**
   * @return the sourceId
   */
  public int getSourceId() {
    return sourceId;
  }

  /**
   * @return the destId
   */
  public int getDestId() {
    return destId;
  }

  @Override
  public OpCode getOpCode() {
    return this.opCode;
  }

  @Override
  public int getId() {
    return this.destId;
  }

}
