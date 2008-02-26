package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.sensor.Sensor;

public class SupervisorClient {

  private static final byte[] serverAddress = new byte[] { (byte) 192,
      (byte) 168, (byte) 1, (byte) 5 };

  private static final int BUFFER_SIZE = 512;

  private final Sensor sensor;
  
  private InetSocketAddress serverAddr;

  public SupervisorClient(Sensor sensor, String ipAddress) {
    this.sensor = sensor;
    try {
		this.serverAddr = new InetSocketAddress(InetAddress.getByName(ipAddress), 
				IRSensorConfiguration.SUPERVISOR_SERVER_PORT);
	} catch (UnknownHostException e) {
		System.err.println("IO Error : " + e.getMessage());
	    System.exit(1); // the application cannot continue
	}
  }

  public void registrySensor() throws IOException, MalformedPacketException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(this.serverAddr);

    ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    ByteBuffer reqPacket = PacketFactory.createReqConPacket();

    // send REQCON packet to supervision server
    channel.write(reqPacket);

    // wait for REPCON packet
    channel.read(readBuffer);
    readBuffer.flip();

    if (DecodePacket.getOpCode(readBuffer) != OpCode.REPCON)
      throw new MalformedPacketException();
    int id = DecodePacket.getId(readBuffer);
    this.sensor.setId(id);
    channel.write(PacketFactory.createAck(id, ErrorCode.OK));
  }

  public void sendRepData(int id, int mimeType, int dataLen, byte[] data) {
    SocketChannel socketClient = null;
    try {
      socketClient = SocketChannel.open();
      socketClient.connect(this.serverAddr);
      ByteBuffer b = PacketFactory.createRepData(id, mimeType, data.length,
          data);
      socketClient.write(b);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        socketClient.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
