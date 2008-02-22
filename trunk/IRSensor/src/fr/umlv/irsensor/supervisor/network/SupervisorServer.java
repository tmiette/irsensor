package fr.umlv.irsensor.supervisor.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.supervisor.listeners.SupervisorServerListener;
import fr.umlv.irsensor.util.IRSensorLogger;

/**
 * Define the server part of the <code>Supervisor</code>	 
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SupervisorServer {

	private static final int BUFFER_SIZE = 512;

	private static final int serverPort = IRSensorConfiguration.SUPERVISOR_SERVER_PORT;

	private final List<SupervisorServerListener> listeners = new ArrayList<SupervisorServerListener>();

	private ServerSocketChannel servChannel;

	public SupervisorServer() {
		try {
			this.servChannel = ServerSocketChannel.open();
			servChannel.socket().bind(new InetSocketAddress(serverPort));
			IRSensorLogger.postMessage(Level.FINE, "Supervisor Server is listening on "+serverPort);
		} catch (IOException e) {
			System.err.println("IO Error : "+e.getMessage());
			System.exit(1); //the application cannot continue
		}
	}
	
	/**
	 * Wait for the registration of each <code>SensorNode</code>
	 * 
	 * @param ids
	 */
	public void registerAllNodes(final int[] ids) {
		final int nbrNodes = ids.length;
		new Thread(new Runnable() {
			@Override
			public void run() {
				int nbrOfNodeRegistered = 0;
				for (;;) {
					// all the sensors have already been configured
					if (nbrOfNodeRegistered == nbrNodes)
						break;
					SocketChannel sensorChannel = null;
					try {
						sensorChannel = servChannel.accept();

						final ByteBuffer readBuffer = ByteBuffer
						.allocateDirect(BUFFER_SIZE);

						// wait for REQCON packet
						sensorChannel.read(readBuffer);
						readBuffer.flip();

						if (DecodePacket.getOpCode(readBuffer) != OpCode.REQCON) {
							sensorChannel.close();
							continue;
						}

						// send REPC dispatcher.startDispatcher();ON packet to the sensor
						sensorChannel.write(PacketFactory
								.createRepConPacket(ids[nbrOfNodeRegistered]));

						// wait for ACK packet
						sensorChannel.read(readBuffer);
						readBuffer.flip();
						if(DecodePacket.getErrorCode(readBuffer) != ErrorCode.OK){
							sensorChannel.close();
							continue;
						}
						fireReqConPacketReceived(ids[nbrOfNodeRegistered],
								(Inet4Address) sensorChannel.socket().getInetAddress());
						nbrOfNodeRegistered++;

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						close(sensorChannel);
					}
				}
				fireRegistrationTerminated();
				try {
					shutdown();
				} catch (IOException e) {
					System.err.println("IO erro : "+e.getMessage());
				}
			}
		}).start();
	}

	
	/**
	 * Shutdown the server socket
	 * 
	 * @throws IOException
	 */
	public void shutdown() throws IOException {
		this.servChannel.close();
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
	
	public void addSupervisorServerListener(SupervisorServerListener listener) {
		this.listeners.add(listener);
	}
	
	protected void fireReqConPacketReceived(int id, InetAddress ipAddress) {
		for (SupervisorServerListener l : this.listeners) {
			l.ackConPacketReceived(id, ipAddress);
		}
	}

	protected void fireErrorCodeReceived(ErrorCode code) {
		for (SupervisorServerListener l : this.listeners) {
			l.ErrorCodeReceived(code);
		}
	}

	protected void fireRegistrationTerminated() {
		for (SupervisorServerListener l : this.listeners) {
			l.registrationTerminated();
		}
	}
}
