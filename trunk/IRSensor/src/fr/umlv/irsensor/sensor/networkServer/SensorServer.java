package fr.umlv.irsensor.sensor.networkServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.DecodeOpCode;
import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.OpCode;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.sensor.ReqHelloPacket;
import fr.umlv.irsensor.sensor.SensorServerListener;
import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SensorServer implements PacketRegisterable {

  private final int id;

  private final ArrayList<SensorServerListener> listeners;

  private ServerSocketChannel servChannel;

  public SensorServer(int id) {
    this.id = id;
    this.listeners = new ArrayList<SensorServerListener>();
    try {
      this.servChannel = ServerSocketChannel.open();
      servChannel.socket().bind(
          new InetSocketAddress(IRSensorConfiguration.SENSOR_SERVER_PORT));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setPacket(ByteBuffer packet, SocketChannel channel) {
    if (DecodeOpCode.decodeByteBuffer(packet) == OpCode.REQHELLO) {
      ReqHelloPacket reqHelloPacket = null;
      try {
        reqHelloPacket = ReqHelloPacket.getPacket(packet);
      } catch (MalformedPacketException e) {
        System.out.println(e.getMessage());
        return;
      }
      fireHelloRequestReceived(reqHelloPacket.getSourceId(), channel.socket()
          .getInetAddress());

      try {
        channel.write(PacketFactory.createRepHello(id, reqHelloPacket
            .getSourceId()));
        channel.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void addSensorServerListener(SensorServerListener listener) {
    this.listeners.add(listener);
  }

  public void removeSensorServerListener(SensorServerListener listener) {
    this.listeners.remove(listener);
  }

  protected void fireHelloRequestReceived(int id, InetAddress address) {
    for (SensorServerListener listener : this.listeners) {
      listener.helloRequestReceived(id, address);
    }
  }

  protected void fireReqDataReceived(CatchArea cArea, int clock, int quality) {
    for (SensorServerListener listener : this.listeners) {
      listener.reqDataReceived(cArea, clock, quality);
    }
  }

}
