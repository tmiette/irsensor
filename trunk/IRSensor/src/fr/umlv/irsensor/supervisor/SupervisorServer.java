package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SupervisorServer {

  private static final int serverPort = 31000;

  public SupervisorServer() {
  }

  public void launch() throws IOException {
    ServerSocketChannel servChannel = ServerSocketChannel.open();
    servChannel.socket().bind(new InetSocketAddress(serverPort));

    for (;;) {
      final SocketChannel socket = servChannel.accept();
      ByteBuffer readBuffer = ByteBuffer.allocateDirect(512);

      while (socket.read(readBuffer) != -1) {
        readBuffer.flip();
        System.err.println(readBuffer.array());
        readBuffer.clear();
      }

      socket.close();
    }

  }

  public static void main(String[] args) {

    try {
      new SupervisorServer().launch();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
