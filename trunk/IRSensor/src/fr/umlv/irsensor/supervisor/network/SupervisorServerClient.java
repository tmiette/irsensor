package fr.umlv.irsensor.supervisor.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.supervisor.SensorNode;
import fr.umlv.irsensor.supervisor.listeners.SupervisorServerClientListener;

/**
 * SupervisorClient is a network client which is used to retrieve informations
 * from sensors's server
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SupervisorServerClient {

	private final ArrayList<SupervisorServerClientListener> listeners = new ArrayList<SupervisorServerClientListener>();
	
	public SupervisorServerClient() {
	}
	
	/**
	 * Switch the node state with the given one
	 * 
	 * @param node
	 * @param state
	 */
	public void setState(SensorNode node, SensorState state) {
		// set a new configuration
		SocketChannel socketClient = null;
		try {
			socketClient = SocketChannel.open();
			socketClient.connect(new InetSocketAddress(node.getAddress(),
					IRSensorConfiguration.SERVER_PORT_LOCAL));
			System.out.println("want to change state of "+node.getId()+" this state "+state);
			ByteBuffer b = PacketFactory.createSetSta(node.getId(), state);
			socketClient.write(b);

			final ByteBuffer buffer = ByteBuffer.allocate(64);
			socketClient.read(buffer);
			buffer.flip();
			
			if (DecodePacket.getErrorCode(buffer) == ErrorCode.OK) {
				fireSensorStateChanged(node, state);
			}
			else {
				socketClient.write(PacketFactory.createAck(node.getId(), ErrorCode.INVALID_PACKET));
			}
		} catch (IOException e) {
			System.err.println("IO error "+e.getMessage());
		}
		finally{
			close(socketClient);
		}
	}

	/**
	 * Set a new configuration to the given <code>SensorNode</code>
	 * 
	 * @param node
	 * @param conf
	 */
	public void setConf(SensorNode node, SensorConfiguration conf) {
		// set a new configuration
		SocketChannel socketClient = null;
		try {
			socketClient = SocketChannel.open();
			socketClient.connect(new InetSocketAddress(node.getAddress(),
					IRSensorConfiguration.SERVER_PORT_LOCAL));

			byte[] parentAddress = new byte[4];
			if (conf.getParentAddress() != null) {
				parentAddress = conf.getParentAddress().getAddress();
			}

			ByteBuffer b = PacketFactory.createSetConfPacket(node.getId(), conf
					.getCArea(), conf.getClock(), conf.getAutonomy(), conf.getQuality(),
					conf.getPayload(), parentAddress, conf.getParentId());

			socketClient.write(b);

			final ByteBuffer buffer = ByteBuffer.allocate(64);
			socketClient.read(buffer);
			buffer.flip();
			if (DecodePacket.getErrorCode(buffer) == ErrorCode.OK) {
				fireSensorConfigurationChanged(node, conf);
			} else {
				socketClient.write(PacketFactory.createAck(node.getId(), ErrorCode.INVALID_PACKET));
			}
		} catch (IOException e) {
			System.err.println("IO error "+e.getMessage());
		}
		finally{
			close(socketClient);
		}
	}
	
	/**
	 * Send a request to the sink SensorNode and wait for an answer
	 * Then it notifies the <code>Supervisor</code> when the answer has been received 
	 * 
	 * @param node
	 * @param cArea
	 * @param clock
	 * @param quality
	 */
	public void dataRequest(SensorNode node, CatchArea cArea, int clock, int quality){
		// request
		SocketChannel socketClient = null;
		try {
			socketClient = SocketChannel.open();
			socketClient.connect(new InetSocketAddress(node.getAddress(),
					IRSensorConfiguration.SERVER_PORT_LOCAL));

			socketClient.write(PacketFactory.createReqData(node.getId(), cArea, quality, clock));
		} catch (IOException e) {
			System.err.println("IO error "+e.getMessage());
		}
		finally{
			close(socketClient);
		}
	}
	
	/**
	 * Close the given channel
	 * 
	 * @param channel
	 */
	private void close(SocketChannel channel){
		try {
			if(channel.isConnected())channel.close();
		} catch (IOException e) {
			System.err.println("An error has occured during closing channel");
		}
	}

	public void addSupervisorServerClientListener(SupervisorServerClientListener listener) {
		this.listeners.add(listener);
	}

	public void removeSupervisorServerClientListener(SupervisorServerClientListener listener) {
		this.listeners.remove(listener);
	}

	protected void fireSensorConfigurationChanged(SensorNode node, SensorConfiguration conf) {
		for (SupervisorServerClientListener listener : this.listeners) {
			listener.sensorConfigurationChanged(node, conf);
		}
	}

	protected void fireSensorStateChanged(SensorNode node, SensorState state) {
		for (SupervisorServerClientListener listener : this.listeners) {
			listener.sensorStateChanged(node, state);
		}
	}
}
