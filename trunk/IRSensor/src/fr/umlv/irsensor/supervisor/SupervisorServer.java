package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.umlv.irsensor.sensor.CatchArea;
import fr.umlv.irsensor.sensor.ErrorCode;
import fr.umlv.irsensor.sensor.CatchArea.Point;

public class SupervisorServer {

	private static int MAX_THREADS = 2;
	private static final int BUFFER_SIZE = 512;
	private static final int serverPort = 31001;

	private static final Object lock = new Object();
	private final ExecutorService executor;

	private final HashMap<Integer, SensorNode> sensors;
	private int sensorAlreadyConnected;

	private static final int nbrSensor = 2;

	public SupervisorServer() {
		this.executor = Executors.newFixedThreadPool(MAX_THREADS);
		this.sensors = new HashMap<Integer, SensorNode>();
		this.sensorAlreadyConnected = 0;
	}

	public void launch() throws IOException {
		final ServerSocketChannel servChannel = ServerSocketChannel.open();
		servChannel.socket().bind(new InetSocketAddress(serverPort));
		new Thread(new Runnable(){
			@Override
			public void run() {
				for (;;) {
					//all the sensors have already been configured
					if(sensorAlreadyConnected == nbrSensor) break;
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
								.createRepConPacket(sensorAlreadyConnected));


						// wait for ACK packet
						sensorChannel.read(readBuffer);
						readBuffer.flip();
						
						 

						// store it in the list of sensors
						final SensorNode node = new SensorNode(sensorChannel);
						sensors.put(sensorAlreadyConnected, node);

						sensorAlreadyConnected++;


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
				try {
					// Just an Hack cause of the dispatcher is not established at the moment, waiting for it
					Thread.sleep(1000); 
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				//set a new configuration
				
				try {
										
					for(int i=0; i<nbrSensor; i++){
						SocketChannel socketClient;
						socketClient = SocketChannel.open();
						System.out.println("Server is trying to reach a client...");
						
						socketClient.connect(new InetSocketAddress("localhost", 31000));

						socketClient.write(PacketFactory.createSetConfPacket(i, new CatchArea(new Point(0,0), new Point(0,0)), 
								0, 0, 0, 0, new byte[3]));
						
						final ByteBuffer buffer = ByteBuffer.allocate(64);
						socketClient.read(buffer);
						buffer.flip();
						System.out.println(new String(buffer.array()));
						buffer.clear();
						socketClient.close();
					}
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}
}
