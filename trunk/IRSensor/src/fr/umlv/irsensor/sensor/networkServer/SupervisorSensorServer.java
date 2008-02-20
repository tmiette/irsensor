package fr.umlv.irsensor.sensor.networkServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.DecodeOpCode;
import fr.umlv.irsensor.common.ErrorCode;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.PacketFactory;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.SetConfPacket;
import fr.umlv.irsensor.sensor.SupervisorSensorListener;
import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SupervisorSensorServer implements PacketRegisterable{
	
	private final int id; 
	
	private final List<SupervisorSensorListener> listener = new ArrayList<SupervisorSensorListener>();
	
	public SupervisorSensorServer(int id) {
		this.id = id;
	}
	
	public void addSupervisorSensorListener(SupervisorSensorListener listener){
		this.listener.add(listener);
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setPacket(ByteBuffer packet, SocketChannel channel) {
		//receive a packet from the supervisor
		if(DecodeOpCode.decodeByteBuffer(packet) == OpCode.SETCONF){
			SetConfPacket setConfPacket = null;
			try {
				setConfPacket = SetConfPacket.getPacket(packet);
			} catch (MalformedPacketException e) {
				System.out.println(e.getMessage());
				return;
			}
			fireConfReceived(setConfPacket.getCatchArea(), setConfPacket.getClock(), setConfPacket.getAutonomy(), 
					setConfPacket.getQuality(), setConfPacket.getPayload(), setConfPacket.getParentId());
		}
		
		try {
			channel.write(PacketFactory.createAck(this.id, ErrorCode.OK));
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void fireConfReceived(CatchArea area, int clock, int autonomy, int quality, int payload, int id){
		for(SupervisorSensorListener l: this.listener){
			l.confReceived(area, clock, autonomy, quality, payload, id);
		}
	}
	
}

