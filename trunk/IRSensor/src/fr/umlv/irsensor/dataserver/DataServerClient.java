package fr.umlv.irsensor.dataserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.data.RepDataPacket;

public class DataServerClient {

	private InetSocketAddress serverAddr;
	private SocketChannel channel;
	private ByteBuffer dataServerRepDataBuffer = ByteBuffer.allocate(300000);
	
	public DataServerClient(String serverAddr, int dstPort) {
		try {
			this.serverAddr = new InetSocketAddress(InetAddress.getByName(serverAddr), dstPort);
			channel = SocketChannel.open();
		} catch (UnknownHostException e) {
			throw new AssertionError(e.getMessage());
		} catch (IOException e) {
			System.err.println("Io exception()");
			close();
		}
	}

	public void connect(){
		try {
			System.out.println("Connecting to server");
			channel.connect(serverAddr);
			final CatchArea catchArea = new CatchArea(0,0,10,10);
			ByteBuffer buffer = PacketFactory.createReqData(0, catchArea, 0, 0);
			System.out.println("Sending request data");
			channel.write(buffer);
			System.out.println("Sent ok");
			System.out.println("Reading data server response");
			channel.read(dataServerRepDataBuffer);
			dataServerRepDataBuffer.flip();
			try {
				RepDataPacket packetReceived = RepDataPacket.getPacket(dataServerRepDataBuffer);
				System.out.println("Data received correctly "+packetReceived);
				byte[] im = packetReceived.getDatas();
				
				displayImage(im);
			} catch (MalformedPacketException e) {
				System.err.println("Malformed packet received from data server "+e.getMessage());
			}
		} catch (IOException e) {
			System.err.println("connect()");
			close();
		}
	}

	private void displayImage(byte[] zone){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(600,600);
		final ImageIcon imageIcon = new ImageIcon(zone);
		frame.getContentPane().add(new JLabel(imageIcon));
		frame.setVisible(true);
	}
	
	public void close(){
		try {
			if (channel != null)
				channel.close();
		} catch (IOException e) {
			System.err.println("close()");
			System.exit(1);
		}
	}
}
