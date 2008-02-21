package fr.umlv.irsensor.sensor.networkServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.supervisor.ReqDataPacket;
import fr.umlv.irsensor.common.packets.supervisor.SetConfPacket;
import fr.umlv.irsensor.common.packets.supervisor.SetStatPacket;
import fr.umlv.irsensor.sensor.SupervisorSensorListener;
import fr.umlv.irsensor.sensor.dispatcher.PacketRegisterable;

public class SupervisorSensorServer implements PacketRegisterable {

  private final int id;

  private final List<SupervisorSensorListener> listener = new ArrayList<SupervisorSensorListener>();

  private boolean isWaitingForAnswer;

  private SocketChannel channel;

  public SupervisorSensorServer(int id) {
    this.id = id;
  }

  public void addSupervisorSensorListener(SupervisorSensorListener listener) {
    this.listener.add(listener);
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setPacket(ByteBuffer packet, SocketChannel channel) {
    // receive a packet from the supervisor
    if (OpCode.getOpcode(packet) == OpCode.SETCONF) {
      SetConfPacket setConfPacket = null;
      try {
        setConfPacket = SetConfPacket.getPacket(packet);
      } catch (MalformedPacketException e) {
        System.out.println(e.getMessage());
        return;
      }
      SensorConfiguration conf = new SensorConfiguration(setConfPacket
          .getCatchArea(), setConfPacket.getAutonomy(), setConfPacket
          .getClock(), setConfPacket.getPayload(), setConfPacket.getQuality(),
          setConfPacket.getParent());
      fireConfReceived(conf);
      this.isWaitingForAnswer = false;
    } else if (OpCode.getOpcode(packet) == OpCode.REQDATA) {
      ReqDataPacket reqDataPacket = null;
      try {
        reqDataPacket = ReqDataPacket.getPacket(packet);
      } catch (MalformedPacketException e) {
        System.out.println(e.getMessage());
        return;
      }
      System.out.println("Request data received");

      fireReqDataReceived(reqDataPacket.getCatchArea(), reqDataPacket
          .getClock(), reqDataPacket.getQuality());
      this.isWaitingForAnswer = true;
      this.channel = channel;
    } else if (OpCode.getOpcode(packet) == OpCode.SETSTA) {
      SetStatPacket setStatPacket = null;
      try {
        setStatPacket = SetStatPacket.getPacket(packet);
      } catch (MalformedPacketException e) {
        System.out.println(e.getMessage());
        return;
      }
      fireSetStateChanged(setStatPacket.getState());
      this.isWaitingForAnswer = false;

    }
    if (!this.isWaitingForAnswer) {
      try {
        channel.write(PacketFactory.createAck(this.id, ErrorCode.OK));
        channel.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void answerReqData(byte[] datas) {
    if (this.channel != null) {
      try {
        channel.write(PacketFactory.createRepData(id, mimetype, len, data));
      } catch (IOException e) {
        System.out.println(e.getMessage());
        return;
      }
    }
  }

  protected void fireConfReceived(SensorConfiguration conf) {
    for (SupervisorSensorListener l : this.listener) {
      l.confReceived(conf);
    }
  }

  protected void fireReqDataReceived(CatchArea area, int clock, int quality) {
    for (SupervisorSensorListener l : this.listener) {
      l.reqDataReceived(area, clock, quality);
    }
  }

  protected void fireSetStateChanged(SensorState state) {
    for (SupervisorSensorListener l : this.listener) {
      l.stateChanged(state);
    }
  }

}
