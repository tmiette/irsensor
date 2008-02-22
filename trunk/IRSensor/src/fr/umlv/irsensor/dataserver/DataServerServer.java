package fr.umlv.irsensor.dataserver;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.data.ReqDataPacket;


public class DataServerServer {

	/**
	 * Image data
	 */
	private final ViewSight completeCapturedZone = new ViewSight("./src/images/code_sm.png");

	/**
	 * Max client that can handle the data server
	 */
	private static final int MAX_CLIENT = 5;

	/**
	 * Server socket address
	 */
	private final InetSocketAddress socketAddress;

	/**
	 * Server socket
	 */
	private ServerSocketChannel channel;

	/**
	 * Executor to handle multiple clients
	 */
	private final ExecutorService executer = Executors.newFixedThreadPool(MAX_CLIENT);

	public DataServerServer() {
		socketAddress = new InetSocketAddress(IRSensorConfiguration.DATA_SERVER_PORT);
		try {
			channel = ServerSocketChannel.open();
			channel.socket().bind(socketAddress);
		} catch (IOException e) {
			System.err.println("DatagramChannel.open()");
		}	
	}

	/**
	 * Starts the data server
	 */
	public void listen(){
		while (true){
			try {
				SocketChannel clientChannel = channel.accept();
				executer.submit(handleClient(clientChannel));
				System.out.println("Client accepted");
			} catch (IOException e) {
				System.err.println("accept()");
				close();
			}
		}
	}

	/**
	 * Creates a runnable used to handle client request
	 * @param clientChannel the client(); channel
	 * @return the runnable
	 */
	private Runnable handleClient(final SocketChannel clientChannel){
		final ByteBuffer dst = ByteBuffer.allocate(16392);
		return new Runnable(){
			@Override
			public void run() {
				try {
				  int i = 0;
				  System.out.println("i'm reading");
					clientChannel.read(dst);
					System.out.println("I read"+ i++);
					dst.flip();
					// Parse request and retrieve catch area
					ReqDataPacket packet = ReqDataPacket.getPacket(dst);
					System.out.println("Received REQDATA from client : "+packet);
					// Create image area
					BufferedImage subImage = completeCapturedZone.getSubImage(packet.getCatchArea());
					System.out.println("I read"+ i++);
					// Prepare response
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					System.out.println("I read"+ i++);
					ImageIO.write(subImage, completeCapturedZone.getFileExtension(), bos);
					System.out.println("I read"+ i++);
					ByteBuffer sendBuffer = PacketFactory.createRepData(packet.getId(), 12, bos.toByteArray().length, bos.toByteArray());
					System.out.println("write not done");
					clientChannel.write(sendBuffer);
					System.out.println("write done");
					dst.clear();
				}
				catch (MalformedPacketException e){
					System.err.println("Malformed packet "+e.getMessage());
					try {
						System.out.println("Client connection closed");
						clientChannel.close();
					} catch (IOException e1) {
						System.err.println("close()");
						close();
					}
				}
				catch (IOException e){
					System.err.println("read()");
					close();
				}
			}
		};
	}

	/**
	 * Closes the data server socket
	 */
	public void close(){
		try {
			if (channel != null)
				channel.close();
		} catch (IOException e) {
			System.err.println("close()");
		}
	}


}
