package fr.umlv.irsensor.dataserver;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.RepDataPacket;
import fr.umlv.irsensor.common.packets.ReqDataPacket;


public class DataServerServer {
	
	/**
	 * Image data
	 */
	private final ViewSight completeCapturedZone = new ViewSight("src/images/ig2k.gif");
	
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
	
	public DataServerServer(int port) {
		socketAddress = new InetSocketAddress(port);
		try {
			channel = ServerSocketChannel.open();
			channel.socket().bind(socketAddress);
		} catch (IOException e) {
			System.err.println("DatagramChannel.open()");
			// TODO :
		}	
	}	// TODO Auto-generated constructor stub
	
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
		final ByteBuffer dst = ByteBuffer.allocate(300000);
		return new Runnable(){
			@Override
			public void run() {
				try {
					clientChannel.read(dst);
					dst.flip();
					// Parse request and retrieve catch area
					ReqDataPacket packet = ReqDataPacket.getPacket(dst);
					// Create image area
					ImageArea subImage = completeCapturedZone.getImageArea(packet.getCatchArea());
					// Prepare response
					byte[] imBytes = ((DataBufferByte)subImage.getImage().getRaster().getDataBuffer()).getData();
					System.out.println("Image bytes len "+imBytes.length);
					ByteBuffer sendBuffer = PacketFactory.createRepData(packet.getId(), imBytes);
					clientChannel.write(sendBuffer);
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
