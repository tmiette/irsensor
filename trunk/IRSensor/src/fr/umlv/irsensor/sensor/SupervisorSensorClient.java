package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.supervisor.OpCode;

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
    buffer.put(OpCode.REQCON.getCode());

    channel.write(buffer);
    buffer.clear();

    channel.read(buffer);

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
