package fr.umlv.irsensor.sensor.networkServer;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.sensor.SensorServerListener;
import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SensorServer implements PacketRegisterable {

  private final int id;

  private final ArrayList<SensorServerListener> listeners;

  public SensorServer(int id) {
    this.id = id;
    this.listeners = new ArrayList<SensorServerListener>();
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setPacket(ByteBuffer packet, SocketChannel channel) {

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
