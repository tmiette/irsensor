package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.common.DecodeOpCode;
import fr.umlv.irsensor.common.DecodePacket;
import fr.umlv.irsensor.common.ErrorCode;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFactory;
import fr.umlv.irsensor.common.SupervisorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.sensor.Sensor;
import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SupervisorClient{

	private static final byte[] serverAddress = new byte[] { (byte) 127,
		(byte) 0, (byte) 0, (byte) 1 };
	
	private static final int SERVER_PORT = SupervisorConfiguration.SERVER_PORT;
	
	private static final int BUFFER_SIZE = 512;
	
	private final Sensor sensor;
	
	public SupervisorClient(Sensor sensor) {
		this.sensor = sensor;
	}

	public void registrySensor() throws IOException, MalformedPacketException {
		SocketChannel channel = SocketChannel.open();
		channel.connect(new InetSocketAddress(InetAddress.getByAddress(serverAddress),
				SERVER_PORT));

		ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		
		ByteBuffer reqPacket = PacketFactory.createReqConPacket();
		
		// send REQCON packet to supervision server
		channel.write(reqPacket);

		// wait for REPCON packet
		channel.read(readBuffer);
		readBuffer.flip();

		if(DecodePacket.getOpCode(readBuffer) != OpCode.REPCON) throw new MalformedPacketException();
		int id = DecodePacket.getId(readBuffer);
		this.sensor.setId(id);
		channel.write(PacketFactory.createAck(id, ErrorCode.OK));
	}
}
