package fr.umlv.irsensor.common.packets;

import java.nio.ByteBuffer;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.ErrorCode;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFields;
import fr.umlv.irsensor.common.SensorState;

/**
 * BufferFactory is in charge of creating the supervisor protocol packets A
 * packet is represented by a <code>ByteBuffer</code>
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class PacketFactory {
	/**
	 * Create a new REPCON packet according to the given parameters | opcode | id |
	 * @return a buffered packet
	 */
	public static ByteBuffer createRepConPacket(int id) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;

		buffer.put(index, OpCode.REPCON.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		return buffer;
	}

	/**
	 * Create a new REQCON packet | opcode |
	 * @return a buffered packet
	 */
	public static ByteBuffer createReqConPacket() {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.REQCON.getCode());
		return buffer;
	}

	/**
	 * Returns a new SETCONF packet. | Opcode | Id | Catch Area | Clock | Autonomy |
	 * Quality | Payload | ParentID | Optional |
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
			int clock, int autonomy, int quality, int payload, byte[] root, int rootId) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID, PacketFields.CATCH_AREA,
				PacketFields.CLOCK, PacketFields.AUTONOMY,
				PacketFields.QUALITY, PacketFields.PAYLOAD,
				PacketFields.PARENT_ID);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		// packet : header | area | clock | autonomy | quality | payload
		int index = 0;
		buffer.put(index, OpCode.SETCONF.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		index += PacketFields.ID.getLength();
		buffer.putInt(index, area.getP1().getX());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, area.getP1().getY());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, area.getP2().getX());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, area.getP2().getY());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, clock);
		index += PacketFields.CLOCK.getLength();
		buffer.putInt(index, autonomy);
		index += PacketFields.AUTONOMY.getLength();
		buffer.putInt(index, quality);
		index += PacketFields.QUALITY.getLength();
		buffer.putInt(index, payload);
		index += PacketFields.PAYLOAD.getLength();
		buffer.position(index);
		buffer.put(root, 0, root.length);
		index += root.length;
		buffer.position(index);
		buffer.putInt(index, rootId);
		buffer.rewind();

		return buffer;
	}

	/**
	 * Returns a new GETCONF packet. Id represents the sensor id which want to
	 * retrieve configuration. | Opcode | Id |
	 * @param id sensor id.
	 * @return bytebuffer corresponding to GETCONF packet.
	 */
	public static ByteBuffer createGetConf(int id) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;

		buffer.put(index, OpCode.GETCONF.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		return buffer;
	}

	/**
	 * Returns a new SETSTA packet. | Opcode | Id | State |
	 * @param id sensor id.
	 * @param state sensor state to set up.
	 * @return bytebuffer corresponding to SETSTA packet.
	 */
	public static ByteBuffer createSetSta(int id, SensorState state) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID, PacketFields.STATE);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.SETSTA.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		index += PacketFields.ID.getLength();
		buffer.put(index, state.getState());
		return buffer;
	}

	/**
	 * Returns a new GETSTA packet. | Opcode | Id |
	 * @param id sensor id.
	 * @return bytebuffer corresponding to GETSTA packet.
	 */
	public static ByteBuffer createGetSta(int id) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.GETSTA.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		return buffer;
	}

	/**
	 * Returns a new REPSTA packet. | Opcode | Id | State |
	 * @param id sensor id.
	 * @param state sensor state to retrieve.
	 * @return bytebuffer corresponding to REPSTA packet.
	 */
	public static ByteBuffer createRepSta(int id, SensorState state) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID, PacketFields.STATE);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.REPSTA.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		index += PacketFields.ID.getLength();
		buffer.put(index, state.getState());
		return buffer;
	}

	/**
	 * Returns a new REQDATA packet. | Opcode | Id | Catch area | min quality
	 * requested |
	 * @param id sensor id.
	 * @param area area to request to sensor.
	 * @param quality quality to request to sensor.
	 * @param date date to request to sensor.
	 * @return bytebuffer corresponding to REQDATA packet.
	 */
	public static ByteBuffer createReqData(int id, CatchArea area, int quality,
			int date) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID, PacketFields.CATCH_AREA,
				PacketFields.QUALITY, PacketFields.DATE);
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.REQDATA.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		index += PacketFields.ID.getLength();
		buffer.putInt(index, area.getP1().getX());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, area.getP1().getY());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, area.getP2().getX());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, area.getP2().getY());
		index += PacketFields.CATCH_AREA.getLength() / 4;
		buffer.putInt(index, quality);
		index += PacketFields.QUALITY.getLength();
		buffer.putInt(index, date);
		return buffer;
	}

	/**
	 * Returns a new REPDATA packet.
	 * | Opcode | Id | Datas |
	 * @param id sensor id.
	 * @param data data sensor to reply.
	 * @return bytebuffer corresponding to REPDATA packet.
	 */
	public static ByteBuffer createRepData(int id, int mimetype, int len, byte[] data) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE, PacketFields.ID, PacketFields.MIMETYPE, PacketFields.LENGHT) + len;
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.REPDATA.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		index += PacketFields.getLength(PacketFields.ID);
		buffer.putInt(index, mimetype);
		index += PacketFields.getLength(PacketFields.MIMETYPE);
		buffer.putInt(index, len);
		index += PacketFields.getLength(PacketFields.LENGHT);
		buffer.position(index);
		buffer.put(data);
		buffer.rewind();
		return buffer;
	}

	/**
	 * Returns a new ACK packet that contains a code with the issue of the last
	 * request.
	 * | Header | Id | Error code |
	 * @param id sensor id.
	 * @param code error code.
	 * @return bytebuffer corresponding to ACK packet.
	 */
	public static ByteBuffer createAck(int id, ErrorCode code) {
		int bufSize = PacketFields.getLength(PacketFields.OPCODE,
				PacketFields.ID) + ErrorCode.getOpCodeByteSize();
		final ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		int index = 0;
		buffer.put(index, OpCode.ACK.getCode());
		index += PacketFields.OPCODE.getLength();
		buffer.putInt(index, id);
		index += PacketFields.ID.getLength();
		buffer.put(index, code.getCode());
		return buffer;
	}
}
