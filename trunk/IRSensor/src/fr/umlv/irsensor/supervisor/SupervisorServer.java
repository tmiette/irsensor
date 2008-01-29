package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SupervisorServer {

  private static int MAX_THREADS = 2;
  private static final int BUFFER_SIZE = 512;
  private static final int serverPort = 31000;
  private static final Object lock = new Object();
  private final ExecutorService executor;
  private final HashMap<Integer, SocketChannel> sensors;
  private int sensorAlreadyConnected;

  public SupervisorServer() {
    this.executor = Executors.newFixedThreadPool(MAX_THREADS);
    this.sensors = new HashMap<Integer, SocketChannel>();
    this.sensorAlreadyConnected = 0;
  }

  public void launch() throws IOException {
    ServerSocketChannel servChannel = ServerSocketChannel.open();
    servChannel.socket().bind(new InetSocketAddress(serverPort));

    for (;;) {
      final SocketChannel sensorChannel = servChannel.accept();

      // new connection
      this.executor.execute(new Runnable() {
        @Override
        public void run() {
          final ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

          try {

            // wait for REQCON packet
            sensorChannel.read(readBuffer);

            synchronized (lock) {
              // send REPCON packet to the sensor
              sensorChannel.write(BufferFactory
                  .createRepConPacket(sensorAlreadyConnected));
              // store it in the list of sensors
              sensors.put(sensorAlreadyConnected, sensorChannel);
              sensorAlreadyConnected++;
            }

            sensorChannel.close();

          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      });

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
