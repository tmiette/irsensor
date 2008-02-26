package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.sensor.SensorClientListener;

public class SensorClient {

  private final List<SensorClientListener> listeners = new ArrayList<SensorClientListener>();

  public void sendHelloRequest(int idD, int idS, InetAddress ipAddressD) {
    if (ipAddressD != null) {
      SocketChannel socketClient = null;
      try {
        socketClient = SocketChannel.open();
        socketClient.connect(new InetSocketAddress(ipAddressD,
            IRSensorConfiguration.SENSOR_SERVER_PORT));
        ByteBuffer b = PacketFactory.createReqHello(idS, idD, ErrorCode.OK);
        socketClient.write(b);

        final ByteBuffer buffer = ByteBuffer.allocate(64);
        socketClient.read(buffer);
        buffer.flip();
        if (DecodePacket.getOpCode(buffer) == OpCode.REPHELLO) {
          fireHelloReplyReceived();
        } else {
          // TODO what can we do here?
        }
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

  public void sendReqData(InetAddress address, int id, CatchArea area,
      int clock, int quality) {
    SocketChannel socketClient = null;
    try {
      socketClient = SocketChannel.open();
      socketClient.connect(new InetSocketAddress(address,
          IRSensorConfiguration.SENSOR_SERVER_PORT));
      ByteBuffer b = PacketFactory.createReqData(id, area, quality, clock);
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

  public void sendRepData(InetAddress address, int id, int mimeType,
      int dataLen, byte[] data) {
    SocketChannel socketClient = null;
    try {
      socketClient = SocketChannel.open();
      socketClient.connect(new InetSocketAddress(address,
          IRSensorConfiguration.SENSOR_SERVER_PORT));
      ByteBuffer b = PacketFactory.createRepData(id, mimeType, dataLen, data);
      System.out.println("I send data " + b);
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

  public void addSensorClientListener(SensorClientListener listener) {
    this.listeners.add(listener);
  }

  protected void fireHelloReplyReceived() {
    for (SensorClientListener l : this.listeners) {
      l.helloReplyReceived();
    }
  }

}
