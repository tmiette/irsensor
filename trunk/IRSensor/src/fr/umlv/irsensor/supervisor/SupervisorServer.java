package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.SupervisorConfiguration;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.ErrorCode;
import fr.umlv.irsensor.common.packets.OpCode;
import fr.umlv.irsensor.common.packets.PacketFactory;

public class SupervisorServer {

	private static final int BUFFER_SIZE = 512;
	
	private static final int serverPort = SupervisorConfiguration.SERVER_PORT;

	private final List<SupervisorServerListener> listeners = new ArrayList<SupervisorServerListener>();
	
	private ServerSocketChannel servChannel;
	
	public SupervisorServer() {
		try {
			this.servChannel = ServerSocketChannel.open();
			servChannel.socket().bind(new InetSocketAddress(serverPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void registerAllNodes(final int[] ids){
		final int nbrNodes = ids.length;
		new Thread(new Runnable(){
			@Override
			public void run() {
				int nbrOfNodeRegistered = 0;
				for (;;) {
					//all the sensors have already been configured
					if(nbrOfNodeRegistered == nbrNodes) break;
					SocketChannel sensorChannel = null;
					try {
						sensorChannel = servChannel.accept();

						final ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

						// wait for REQCON packet
						sensorChannel.read(readBuffer);
						readBuffer.flip();

						if(DecodePacket.getOpCode(readBuffer) != OpCode.REQCON) {
							sensorChannel.close();
							return;
						}
						readBuffer.clear();


						// send REPC dispatcher.startDispatcher();ON packet to the sensor
						sensorChannel.write(PacketFactory
								.createRepConPacket(ids[nbrOfNodeRegistered]));


						// wait for ACK packet
						sensorChannel.read(readBuffer);
						readBuffer.flip();

						fireReqConPacketReceived(nbrOfNodeRegistered, (Inet4Address)sensorChannel.socket().getInetAddress());
						nbrOfNodeRegistered++;


					} catch (IOException e) {
						e.printStackTrace();
					}
					finally{
						try {
							if(sensorChannel.isConnected()) sensorChannel.close();
						} catch (IOException e) {
							//TODO launch runtime exception
						}
					}
				}
				fireRegistrationTerminated();
			}
		}).start();
	}
	
	public void addSupervisorServerListener(SupervisorServerListener listener){
		this.listeners.add(listener);
	}
	
	protected void fireReqConPacketReceived(int id, InetAddress ipAddress){
		for(SupervisorServerListener l : this.listeners){
			l.ReqConPacketReceived(id, ipAddress);
		}
	}
	
	protected void fireErrorCodeReceived(ErrorCode code){
		for(SupervisorServerListener l : this.listeners){
			l.ErrorCodeReceived(code);
		}
	}
	
	protected void fireRegistrationTerminated(){
		for(SupervisorServerListener l : this.listeners){
			l.registrationTerminated();
		}
	}
}
