package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class SupervisorServer {

  private static final int serverPort = 31000;

  public SupervisorServer() {
  }

  public void launch() throws IOException {
    ServerSocketChannel servChannel = ServerSocketChannel.open();

    servChannel.socket().bind(new InetSocketAddress(serverPort));
  }

}
