package fr.umlv.irsensor.sensor.networkServer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SensorServer implements PacketRegisterable {
private final int id; 
	
	public SensorServer(int id) {
		this.id = id;
	}
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setPacket(ByteBuffer packet, SocketChannel channel) {
		
	}
}
