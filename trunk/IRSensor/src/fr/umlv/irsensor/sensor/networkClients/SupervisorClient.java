package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.common.SupervisorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.DecodeOpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.ErrorCode;
import fr.umlv.irsensor.common.packets.OpCode;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.sensor.Sensor;
import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SupervisorClient implements PacketRegisterable {

	private static final byte[] serverAddress = new byte[] { (byte) 127,
		(byte) 0, (byte) 0, (byte) 1 };
	
	private static final int SERVER_PORT = SupervisorConfiguration.SERVER_PORT;
	
	private static final int BUFFER_SIZE = 512;
	
	private int id;
	
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

		//extract the id from the REPCON packet
		this.id = DecodePacket.getId(readBuffer);
		System.out.println("id received "+this.id);
		channel.write(PacketFactory.createAck(this.id, ErrorCode.OK));
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setPacket(ByteBuffer packet, SocketChannel channel) {
		//receive a packet from the supervisor
		DecodeOpCode.decodeByteBuffer(packet);
		//do something
		System.out.println("Received configuration "+this.id);
		
		try {
			channel.write(PacketFactory.createAck(this.id, ErrorCode.OK));
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
