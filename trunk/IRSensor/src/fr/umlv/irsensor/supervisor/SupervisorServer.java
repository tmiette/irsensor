package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.umlv.irsensor.sensor.ErrorCode;

public class SupervisorServer {

	private static int MAX_THREADS = 2;
	private static final int BUFFER_SIZE = 512;
	private static final int serverPort = 31000;

	private static final Object lock = new Object();
	private final ExecutorService executor;

	private final HashMap<Integer, SensorNode> sensors;
	private int sensorAlreadyConnected;
	
	private static final int nbrSensor = 3;
	
	public SupervisorServer() {
		this.executor = Executors.newFixedThreadPool(MAX_THREADS);
		this.sensors = new HashMap<Integer, SensorNode>();
		this.sensorAlreadyConnected = 0;
	}

	public void launch() throws IOException {
		ServerSocketChannel servChannel = ServerSocketChannel.open();
		servChannel.socket().bind(new InetSocketAddress(serverPort));

		for (;;) {
			//all the sensors have already been configured
			if(this.sensorAlreadyConnected == nbrSensor) break;
			
			final SocketChannel sensorChannel = servChannel.accept();
			
			// new connection
			this.executor.execute(new Runnable() {
				@Override
				public void run() {
					final ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

					try {

						// wait for REQCON packet
						sensorChannel.read(readBuffer);
						readBuffer.flip();
						
						if(DecodePacket.getOpCode(readBuffer) != OpCode.REQCON) return;
						readBuffer.clear();
						
						synchronized (lock) {
							// send REPCON packet to the sensor
							sensorChannel.write(PacketFactory
									.createRepConPacket(sensorAlreadyConnected));
							
							
							// wait for ACK packet
							sensorChannel.read(readBuffer);
							readBuffer.flip();
							
							if(DecodePacket.getErrorCode(readBuffer) != ErrorCode.OK) return;
							
							// store it in the list of sensors
							sensors.put(sensorAlreadyConnected, new SensorNode(sensorChannel));
							
							sensorAlreadyConnected++;
						}

						sensorChannel.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}

	}

	public static void main(String[] args) {

		try {
			new SupervisorServer().launch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
