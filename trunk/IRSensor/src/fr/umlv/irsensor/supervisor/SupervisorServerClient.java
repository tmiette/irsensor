package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.DecodePacket;
import fr.umlv.irsensor.common.ErrorCode;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.SensorState;
import fr.umlv.irsensor.common.SupervisorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.supervisor.RepDataPacket;

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

	public void setState(SensorNode node, SensorState state) {
		// set a new configuration
		try {
			SocketChannel socketClient;
			socketClient = SocketChannel.open();
			socketClient.connect(new InetSocketAddress(node.getAddress(),
					SupervisorConfiguration.SERVER_PORT_LOCAL));

			ByteBuffer b = PacketFactory.createSetSta(node.getId(), state);
			socketClient.write(b);

			final ByteBuffer buffer = ByteBuffer.allocate(64);
			socketClient.read(buffer);
			buffer.flip();
			if (DecodePacket.getErrorCode(buffer) == ErrorCode.OK) {
				System.out.println("Sensor state changed, ui fired");
				fireSensorStateChanged(node, state);
			} else {
				// TODO what can we do here?
			}
			socketClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setConf(SensorNode node, SensorConfiguration conf) {
		// set a new configuration
		SocketChannel socketClient = null;
		try {
			socketClient = SocketChannel.open();
			socketClient.connect(new InetSocketAddress(node.getAddress(),
					SupervisorConfiguration.SERVER_PORT_LOCAL));

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
				System.out.println("Sensor configured, ui fired");
				fireSensorConfigurationChanged(node, conf);
			} else {
				// TODO what can we do here?
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				socketClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void dataRequest(SensorNode node, CatchArea cArea, int clock, int quality){
		// request
		SocketChannel socketClient = null;
		try {
			socketClient = SocketChannel.open();
			socketClient.connect(new InetSocketAddress(node.getAddress(),
					SupervisorConfiguration.SERVER_PORT_LOCAL));

			ByteBuffer b = PacketFactory.createReqData(node.getId(), cArea, quality, clock);

			socketClient.write(b);
			//wait for the answer
			final ByteBuffer buffer = ByteBuffer.allocate(64);
			socketClient.read(buffer);
			buffer.flip();
			
			if (DecodePacket.getOpCode(buffer) == OpCode.REPDATA) {
				System.out.println("Received a data request answer");
				try {
					RepDataPacket packet = RepDataPacket.getPacket(buffer);
					fireAnswerDataReceived(packet.getDatas());
				} catch (MalformedPacketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// TODO what can we do here?
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				socketClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addSupervisorServerClientListener(
			SupervisorServerClientListener listener) {
		this.listeners.add(listener);
	}

	public void removeSupervisorServerClientListener(
			SupervisorServerClientListener listener) {
		this.listeners.remove(listener);
	}

	protected void fireSensorConfigurationChanged(SensorNode node,
			SensorConfiguration conf) {
		for (SupervisorServerClientListener listener : this.listeners) {
			listener.sensorConfigurationChanged(node, conf);
		}
	}

	protected void fireSensorStateChanged(SensorNode node, SensorState state) {
		for (SupervisorServerClientListener listener : this.listeners) {
			listener.sensorStateChanged(node, state);
		}
	}
	
	protected void fireAnswerDataReceived(byte[] data) {
		for (SupervisorServerClientListener listener : this.listeners) {
			listener.answerDataReceived(data);
		}
	}

}
