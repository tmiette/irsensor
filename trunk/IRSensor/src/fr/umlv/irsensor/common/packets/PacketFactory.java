package fr.umlv.irsensor.common.packets;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.sensor.CatchArea;
import fr.umlv.irsensor.sensor.SensorState;

/**
 * BufferFactory is in charge of creating the supervisor protocol packets A
 * packet is represented by a <code>ByteBuffer</code>
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class PacketFactory {
  /**
   * Create a new REPCON packet according to the given parameters
   * @return a buffered packet
   */
  public static ByteBuffer createRepConPacket(int id) {
    final ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.put(0, OpCode.REPCON.getCode());
    buffer.putInt(OpCode.getOpCodeByteSize(), id);
    return buffer;
  }

  /**
   * Create a new REQCON packet
   * @return a buffered packet
   */
  public static ByteBuffer createReqConPacket() {
    final ByteBuffer buffer = ByteBuffer.allocate(OpCode.getOpCodeByteSize());
    buffer.put(0, OpCode.REQCON.getCode());
    return buffer;
  }

  /**
   * Returns a new SETCONF packet.
   * @param id sensor id
   * @param area an area a sensor can capture
   * @param clock clock's sensor
   * @param autonomy autonomy's sensor
   * @param quality quality's sensor
   * @param payload payload's sensor
   * @param root root address IP of the sensor
   * @return a bytebuffer corresponding to a SETCONF packet
   */
  public static ByteBuffer createSetConfPacket(int id, CatchArea area,
      int clock, int autonomy, int quality, int payload, byte[] root) {
    final ByteBuffer buffer = ByteBuffer.allocate(128);
    // packet : header | area | clock | autonomy | quality | payload
    int index = 0;
    buffer.put(index, OpCode.SETCONF.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, id);
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP1().getX());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP1().getY());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP2().getX());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP2().getY());
    index += Integer.SIZE / 8;
    buffer.putInt(index, clock);
    index += Integer.SIZE / 8;
    buffer.putInt(index, autonomy);
    index += Integer.SIZE / 8;
    buffer.putInt(index, quality);
    index += Integer.SIZE / 8;
    buffer.putInt(index, payload);
    index += Integer.SIZE / 8;
    buffer.put(root, 0, root.length);
    return buffer;
  }

  /**
   * Returns a new GETCONF packet. Id represents the sensor id which want to
   * retrieve configuration.
   * @param id sensor id.
   * @return bytebuffer corresponding to GETCONF packet.
   */
  public static ByteBuffer createGetConf(int id) {
    final ByteBuffer buffer = ByteBuffer.allocate(4);
    buffer.put(0, OpCode.GETCONF.getCode());
    buffer.putInt(OpCode.getOpCodeByteSize(), id);
    return buffer;
  }

  /**
   * Returns a new REPCONF packet.
   * @param id sensor id
   * @param area an area a sensor can capture
   * @param clock clock's sensor
   * @param autonomy autonomy's sensor
   * @param quality quality's sensor
   * @param payload payload's sensor
   * @return a bytebuffer corresponding to a REPCONF packet
   */
  public static ByteBuffer createRepConf(int id, CatchArea area, int clock,
      int autonomy, int quality, int payload) {
    final ByteBuffer buffer = ByteBuffer.allocate(64);
    // packet : header | area | clock | autonomy | quality | payload
    int index = 0;
    buffer.put(index, OpCode.REPCONF.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, area.getP1().getX());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP1().getY());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP2().getX());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP2().getY());
    index += Integer.SIZE / 8;
    buffer.putInt(index, clock);
    index += Integer.SIZE / 8;
    buffer.putInt(index, autonomy);
    index += Integer.SIZE / 8;
    buffer.putInt(index, quality);
    index += Integer.SIZE / 8;
    buffer.putInt(index, payload);
    return buffer;
  }

  /**
   * Returns a new SETSTA packet.
   * @param id sensor id.
   * @param state sensor state to set up.
   * @return bytebuffer corresponding to SETSTA packet.
   */
  public static ByteBuffer createSetSta(int id, SensorState state) {
    final ByteBuffer buffer = ByteBuffer.allocate(8);
    int index = 0;
    buffer.put(index, OpCode.SETSTA.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, id);
    index += Integer.SIZE / 8;
    buffer.put(index, state.getState());
    return buffer;
  }

  /**
   * Returns a new GETSTA packet.
   * @param id sensor id.
   * @return bytebuffer corresponding to GETSTA packet.
   */
  public static ByteBuffer createGetSta(int id) {
    final ByteBuffer buffer = ByteBuffer.allocate(8);
    int index = 0;
    buffer.put(index, OpCode.GETSTA.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, id);
    return buffer;
  }

  /**
   * Returns a new REPSTA packet.
   * @param id sensor id.
   * @param state sensor state to retrieve.
   * @return bytebuffer corresponding to REPSTA packet.
   */
  public static ByteBuffer createRepSta(int id, SensorState state) {
    final ByteBuffer buffer = ByteBuffer.allocate(8);
    int index = 0;
    buffer.put(index, OpCode.REPSTA.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, id);
    index += Integer.SIZE / 8;
    buffer.put(index, state.getState());
    return buffer;
  }

  /**
   * Returns a new REQDATA packet.
   * @param id sensor id.
   * @param area area to request to sensor.
   * @param quality quality to request to sensor.
   * @param date date to request to sensor.
   * @return bytebuffer corresponding to REQDATA packet.
   */
  public static ByteBuffer createReqData(int id, CatchArea area, int quality,
      int date) {
    final ByteBuffer buffer = ByteBuffer.allocate(64);
    int index = 0;
    buffer.put(index, OpCode.REQDATA.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, id);
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP1().getX());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP1().getY());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP2().getX());
    index += Integer.SIZE / 8;
    buffer.putInt(index, area.getP2().getY());
    index += Integer.SIZE / 8;
    buffer.putInt(index, quality);
    index += Integer.SIZE / 8;
    buffer.putInt(index, date);
    return buffer;
  }

  /**
   * Returns a new REPDATA packet.
   * @param id sensor id.
   * @param data data sensor to reply.
   * @return bytebuffer corresponding to REPDATA packet.
   */
  public static ByteBuffer createRepData(int id, byte[] data) {
    final ByteBuffer buffer = ByteBuffer.allocate((Integer.SIZE / 8)
        + data.length);
    int index = 0;
    buffer.put(index, OpCode.REPDATA.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.put(data, index, data.length);
    return buffer;
  }

  /**
   * Returns a new ACK packet that contains a code with the issue of the last
   * request.
   * @param id sensor id.
   * @param code error code.
   * @return bytebuffer corresponding to ACK packet.
   */
  public static ByteBuffer createAck(int id, ErrorCode code) {
    final ByteBuffer buffer = ByteBuffer.allocate(32);
    int index = 0;
    buffer.put(index, OpCode.ACK.getCode());
    index += OpCode.getOpCodeByteSize();
    buffer.putInt(index, id);
    index += Integer.SIZE / 8;
    buffer.put(index, code.getCode());
    return buffer;
  }
}
