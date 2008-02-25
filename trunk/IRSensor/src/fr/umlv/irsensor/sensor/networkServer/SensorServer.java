package fr.umlv.irsensor.sensor.networkServer;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.sensor.RepDataPacket;
import fr.umlv.irsensor.common.packets.sensor.ReqDataPacket;
import fr.umlv.irsensor.common.packets.sensor.ReqHelloPacket;
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
    if (DecodePacket.getOpCode(packet).equals(OpCode.REQHELLO)) {
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
    } else if (DecodePacket.getOpCode(packet).equals(OpCode.REQDATA)) {
      ReqDataPacket reqDataPacket = null;
      try {
        reqDataPacket = ReqDataPacket.getPacket(packet);
      } catch (MalformedPacketException e) {
        System.out.println(e.getMessage());
        return;
      }
      fireReqDataReceived(reqDataPacket.getCatchArea(), reqDataPacket
          .getClock(), reqDataPacket.getQuality());
    } else if (DecodePacket.getOpCode(packet).equals(OpCode.REPDATA)) {
      try {
        RepDataPacket repDataPacket = RepDataPacket.getPacket(packet);
        fireRepDataReceived(repDataPacket.getDatas(), repDataPacket
            .getMimetype());
      } catch (MalformedPacketException e) {
        // TODO Auto-generated catch block
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

  protected void fireRepDataReceived(byte[] data, int mimeType) {
    for (SensorServerListener listener : this.listeners) {
      listener.repDataReceived(data, mimeType);
    }
  }
}
