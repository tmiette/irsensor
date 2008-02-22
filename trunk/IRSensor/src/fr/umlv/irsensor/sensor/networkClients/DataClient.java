package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.data.RepDataPacket;
import fr.umlv.irsensor.sensor.SensorDataListener;

public class DataClient {
  private InetSocketAddress serverAddr;
  private SocketChannel channel;
  private ByteBuffer dataServerRepDataBuffer = ByteBuffer.allocate(300000);
  private final List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
  private final ScheduledExecutorService executor = Executors
      .newSingleThreadScheduledExecutor();

  public DataClient(final int id, final CatchArea catchArea, final int quality,
      final int clock, final String serverAddr) {

    try {
      DataClient.this.serverAddr = new InetSocketAddress(InetAddress
          .getByName(serverAddr), IRSensorConfiguration.DATA_SERVER_PORT);
    } catch (UnknownHostException uhe) {
      throw new AssertionError(uhe.getMessage());
    }

    this.executor.schedule(new Runnable() {
      @Override
      public void run() {
        retrieveData(id, catchArea, quality, clock);
      }
    }, clock, TimeUnit.MILLISECONDS);
  }

  public void retrieveData(final int id, CatchArea catchArea,
      final int quality, final int clock) {
    try {
      channel = SocketChannel.open();
      channel.connect(serverAddr);
      ByteBuffer buffer = PacketFactory.createReqData(id, catchArea, quality,
          clock);
      // TODO beware clock is different from date, clock is the capture interval
      // whereas date is the time when it was taken
      channel.write(buffer);
      channel.read(dataServerRepDataBuffer);
      dataServerRepDataBuffer.flip();
      try {
        RepDataPacket packetReceived = RepDataPacket
            .getPacket(dataServerRepDataBuffer);
        byte[] im = packetReceived.getDatas();

        firedataReceived(System.currentTimeMillis(), im);
        // displayImage(im);
      } catch (MalformedPacketException e) {
        System.err.println("Malformed packet received from data server "
            + e.getMessage());
      }
    } catch (IOException e) {
      System.err.println("connect()");
      close();
    }
  }

  // private void displayImage(byte[] zone){
  // JFrame frame = new JFrame();
  // frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  // frame.setSize(600,600);
  // final ImageIcon imageIcon = new ImageIcon(zone);
  // frame.getContentPane().add(new JLabel(imageIcon));
  // frame.setVisible(true);
  // }

  public void close() {
    try {
      if (channel != null)
        channel.close();
    } catch (IOException e) {
      System.err.println("close()");
      System.exit(1);
    }
  }

  public void addSensorDataListener(SensorDataListener listener) {
    this.listeners.add(listener);
  }

  protected void firedataReceived(long currentDate, byte[] data) {
    for (SensorDataListener l : this.listeners) {
      l.dataReceived(currentDate, data);
    }
  }
}
