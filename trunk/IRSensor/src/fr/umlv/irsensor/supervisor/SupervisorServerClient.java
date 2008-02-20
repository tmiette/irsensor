package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.sun.swing.internal.plaf.basic.resources.basic;

import fr.umlv.irsensor.common.DecodePacket;
import fr.umlv.irsensor.common.ErrorCode;
import fr.umlv.irsensor.common.PacketFactory;
import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.SupervisorConfiguration;

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

  public void setConf(SensorNode node, SensorConfiguration conf) {
    // set a new configuration
    try {
      SocketChannel socketClient;
      socketClient = SocketChannel.open();
      socketClient.connect(new InetSocketAddress(node.getAddress(),
          SupervisorConfiguration.SERVER_PORT_LOCAL));

      byte[] parentAddress = new byte[4];
      if (conf.getParentAddress() != null) {
        parentAddress = conf.getParentAddress().getAddress();
      }

      ByteBuffer b = PacketFactory.createSetConfPacket(node.getId(), conf
          .getCArea(), conf.getClock(), conf.getAutonomy(), conf.getQuality(),
          conf.getPayload(), parentAddress);

      socketClient.write(b);

      final ByteBuffer buffer = ByteBuffer.allocate(64);
      socketClient.read(buffer);
      buffer.flip();
      if (DecodePacket.getErrorCode(buffer) == ErrorCode.OK) {
        System.out.println("Sensor configured, ui fired");
        fireAckConfPacketReceived(node, conf);
      } else {
        // TODO what can we do here?
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

  protected void fireAckConfPacketReceived(SensorNode node,
      SensorConfiguration conf) {
    for (SupervisorServerClientListener listener : this.listeners) {
      listener.ackConfPacketReceived(node, conf);
    }
  }

}
