package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.umlv.irsensor.common.SupervisorConfiguration;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.ErrorCode;
import fr.umlv.irsensor.common.packets.PacketFactory;

/**
 * SupervisorClient is a network client which is used to retrieve informations
 * from sensors's server
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SupervisorServerClient {

  private final ArrayList<SupervisorServerClientListener> listeners = new ArrayList<SupervisorServerClientListener>();

  public void setConf(SensorNode sensor) {
    // set a new configuration
    try {
      SocketChannel socketClient;
      socketClient = SocketChannel.open();
      socketClient.connect(new InetSocketAddress(sensor.getAddress(),
          SupervisorConfiguration.SERVER_PORT_LOCAL));
 
      ByteBuffer b = PacketFactory.createSetConfPacket(sensor.getId(), sensor
          .getCArea(), sensor.getClock(), sensor.getAutonomy(), sensor
          .getQuality(), sensor.getPayload(), new byte[3]);

      socketClient.write(b);

      final ByteBuffer buffer = ByteBuffer.allocate(64);
      socketClient.read(buffer);
      buffer.flip();
      if(DecodePacket.getErrorCode(buffer) == ErrorCode.OK){
    	  System.out.println("Sensor configured, ui fired");
    	  fireAckConfPacketReceived(sensor);
      }else{
    	  //TODO what can we do here?
      }
      socketClient.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void addSupervisorServerClientListener(
      SupervisorServerClientListener listener) {
    this.listeners.add(listener);
  }

  public void removeSupervisorServerClientListener(
      SupervisorServerClientListener listener) {
    this.listeners.remove(listener);
  }

  protected void fireAckConfPacketReceived(SensorNode sensor) {
    for (SupervisorServerClientListener listener : this.listeners) {
      listener.ackConfPacketReceived(sensor);
    }
  }

}
