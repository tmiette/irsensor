package fr.umlv.irsensor.sensor.networkClients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.DecodePacket;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.SupervisorConfiguration;
import fr.umlv.irsensor.sensor.SensorClientListener;

public class SensorClient {

  private final List<SensorClientListener> listeners = new ArrayList<SensorClientListener>();

  public void sendHelloRequest(int idD, int idS, InetAddress ipAddressD) {
    SocketChannel socketClient = null;
    try {
      socketClient = SocketChannel.open();
      socketClient.connect(new InetSocketAddress(ipAddressD,
          SupervisorConfiguration.SERVER_PORT_LOCAL));

      ByteBuffer b = null;

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

  public void addSensorClientListener(SensorClientListener listener) {
    this.listeners.add(listener);
  }

  protected void fireRepDataReceived(byte[] data) {
    for (SensorClientListener l : this.listeners) {
      l.repDataReceived(data);
    }
  }

  protected void fireHelloReplyReceived() {
    for (SensorClientListener l : this.listeners) {
      l.helloReplyReceived();
    }
  }

}
