package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.data.RepDataPacket;
import fr.umlv.irsensor.sensor.SensorDataListener;

public class DataClient {
	private InetSocketAddress serverAddr;
	private SocketChannel channel;
	private ByteBuffer dataServerRepDataBuffer = ByteBuffer.allocate(300000);
	private final List<SensorDataListener> listeners =  new ArrayList<SensorDataListener>();
	
	public DataClient(String serverAddr) {
		try {
			this.serverAddr = new InetSocketAddress(InetAddress.getByName(serverAddr), IRSensorConfiguration.DATA_SERVER_PORT);	
		} catch (UnknownHostException e) {
			throw new AssertionError(e.getMessage());
		}
	}

	public void retrieveData(){
		try {
			channel = SocketChannel.open();
			channel.connect(serverAddr);
			final CatchArea catchArea = new CatchArea(0,0,10,10);
			ByteBuffer buffer = PacketFactory.createReqData(0, catchArea, 0, 0);
			channel.write(buffer);
			channel.read(dataServerRepDataBuffer);
			dataServerRepDataBuffer.flip();
			try {
				RepDataPacket packetReceived = RepDataPacket.getPacket(dataServerRepDataBuffer);
				byte[] im = packetReceived.getDatas();
				firedataReceived(im);
				//displayImage(im);
			} catch (MalformedPacketException e) {
				System.err.println("Malformed packet received from data server "+e.getMessage());
			}
		} catch (IOException e) {
			System.err.println("connect()");
			close();
		}
	}
	
//	private void displayImage(byte[] zone){
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.setSize(600,600);
//		final ImageIcon imageIcon = new ImageIcon(zone);
//		frame.getContentPane().add(new JLabel(imageIcon));
//		frame.setVisible(true);
//	}
	
	public void close(){
		try {
			if (channel != null)
				channel.close();
		} catch (IOException e) {
			System.err.println("close()");
			System.exit(1);
		}
	}
	
	public void addSensorDataListener(SensorDataListener listener){
		this.listeners.add(listener);
	}
	
	protected void firedataReceived(byte[] data){
		for(SensorDataListener l: this.listeners){
			l.dataReceived(data);
		}
	}
}
