package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;
import fr.umlv.irsensor.supervisor.DecodeOpCode;
import fr.umlv.irsensor.supervisor.OpCode;
import fr.umlv.irsensor.supervisor.PacketFactory;
import fr.umlv.irsensor.supervisor.DecodePacket;

public class SupervisorSensorClient implements PacketRegisterable {

	private static final byte[] serverAddress = new byte[] { (byte) 192,
		(byte) 168, (byte) 1, (byte) 2 };
	private static final int serverPort = 31001;
	private static final int BUFFER_SIZE = 512;
	
	private int id;
	
	public SupervisorSensorClient() {
	}

	public void launch() throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.connect(new InetSocketAddress(InetAddress.getByName("localhost"),
				serverPort));

		ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		
		ByteBuffer reqPacket = PacketFactory.createReqConPacket();
		
		// send REQCON packet to supervision server
		channel.write(reqPacket);

		// wait for REPCON packet
		channel.read(readBuffer);
		readBuffer.flip();

		if(DecodePacket.getOpCode(readBuffer) != OpCode.REPCON) return;

		//extract the id from the REPCON packet
		this.id = DecodePacket.getId(readBuffer);
		System.out.println("id received "+this.id);
		channel.write(PacketFactory.createAck(this.id, ErrorCode.OK));
		
		channel.close();
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setPacket(ByteBuffer packet, SocketChannel channel) {
		//receive a packet from the supervisor
		DecodeOpCode.decodeByteBuffer(packet);
		try {
			channel.write(ByteBuffer.wrap("Hello OpCode received".getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
