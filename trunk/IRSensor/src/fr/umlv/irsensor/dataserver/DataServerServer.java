package fr.umlv.irsensor.dataserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataServerServer {
	
	
	
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
			} catch (IOException e) {
				System.err.println("accept()");
			}
		}
	}
	
	/**
	 * Creates a runnable used to handle client request
	 * @param clientChannel the client(); channel
	 * @return the runnable
	 */
	private Runnable handleClient(final SocketChannel clientChannel){
		final ByteBuffer dst = ByteBuffer.allocate(8196);
		return new Runnable(){
			@Override
			public void run() {
				try {
					clientChannel.read(dst);
					// Parse request and retrieve catch area
					// Create image area
					// Send back to client the new image 
					dst.flip();
					
					dst.clear();
				}
				catch (IOException e){
					System.err.println("read()");
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
