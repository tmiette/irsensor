package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.data.RepDataPacket;
import fr.umlv.irsensor.sensor.SensorDataListener;

public class DataClient {
  private InetSocketAddress serverAddr;
  private SocketChannel channel;
  private ByteBuffer dataServerRepDataBuffer = ByteBuffer.allocate(IRSensorConfiguration.PACKET_MAX_SIZE);
  private final List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
  private final ScheduledExecutorService executor = Executors
      .newSingleThreadScheduledExecutor();
  private final int INITIAL_DEMAND = 0;

  public DataClient(final int id, final CatchArea catchArea, final int quality,
      final int clock, final String serverAddr) {

    try {
      DataClient.this.serverAddr = new InetSocketAddress(InetAddress
          .getByName(serverAddr), IRSensorConfiguration.DATA_SERVER_PORT);
    } catch (UnknownHostException uhe) {
      throw new AssertionError(uhe.getMessage());
    }

    this.executor.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
    	retrieveData(id, catchArea, quality, clock);
      }
    }, INITIAL_DEMAND, clock, TimeUnit.SECONDS);
  }

  public void retrieveData(final int id, CatchArea catchArea,
      final int quality, final int clock) {
    try {
      channel = SocketChannel.open();
      channel.connect(serverAddr);
      ByteBuffer buffer = PacketFactory.createReqData(id, catchArea, quality,
          clock);
      channel.write(buffer);
      while (channel.read(dataServerRepDataBuffer) != -1) {
      }
      dataServerRepDataBuffer.flip();
      try {

        OpCode code = DecodePacket.getOpCode(dataServerRepDataBuffer);

        switch (code) {
        case ACK:
          System.err.println(ErrorCode.getErrorMessage(DecodePacket
              .getErrorCode(dataServerRepDataBuffer)));
          break;
        case REPDATA:
          RepDataPacket packetReceived = RepDataPacket
              .getPacket(dataServerRepDataBuffer);
          byte[] im = packetReceived.getDatas();
          firedataReceived(System.currentTimeMillis(), packetReceived
              .getMimetype(), im);
          break;
        default:
          break;
        }

        close();
      } catch (MalformedPacketException e) {
        System.err.println("Malformed packet received from data server "
            + e.getMessage());
      }
    } catch (IOException e) {
      close();
    }
  }

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

  protected void firedataReceived(long currentDate, int mimeType, byte[] data) {
    for (SensorDataListener l : this.listeners) {
      l.dataReceived(currentDate, mimeType, data);
    }
  }
}
