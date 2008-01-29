package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.supervisor.BufferFactory;

public class SupervisorSensorClient {

  private static final byte[] serverAddress = new byte[] { (byte) 192,
      (byte) 168, (byte) 1, (byte) 2 };
  private static final int serverPort = 31000;
  private static final int BUFFER_SIZE = 512;

  public SupervisorSensorClient() {
  }

  public void launch() throws IOException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(new InetSocketAddress(InetAddress.getByName("localhost"),
        serverPort));

    ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    // send REQCON packet to supervision server
    channel.write(BufferFactory.createReqConPacket());

    // wait for REPCON packet
    channel.read(readBuffer);
    readBuffer.flip();
    System.out.println(readBuffer.getInt());

    channel.close();
  }

  public static void main(String[] args) {
    try {
      new SupervisorSensorClient().launch();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
