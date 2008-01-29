package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SupervisorSensorClient {

  private static final String serverName = "localhost";
  private static final int serverPort = 31000;
  private static final int BUFFER_SIZE = 1024;

  public SupervisorSensorClient() {
  }

  public void launch() throws IOException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(new InetSocketAddress(serverName, serverPort));

    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    buffer.put(new Byte("00000000"));

    channel.write(buffer);
    buffer.clear();
    
    channel.read(buffer);

    channel.close();
  }

}
