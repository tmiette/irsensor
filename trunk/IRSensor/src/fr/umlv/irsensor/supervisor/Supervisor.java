package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fr.umlv.irsensor.common.ErrorCode;
import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.util.Pair;

/**
 * This class defines a supervisor A Supervisor maintains a list of sensors and
 * their configuration
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class Supervisor {

  private final HashMap<Integer, SensorNode> sensors = new HashMap<Integer, SensorNode>();

  private final List<Pair<Integer, SensorConfiguration>> sensorConfs;

  private final SupervisorServer server;

  private final SupervisorServerClient client;

  private final ArrayList<SupervisorListener> listeners = new ArrayList<SupervisorListener>();

  public Supervisor(List<Pair<Integer, SensorConfiguration>> sensors) {
    this.client = new SupervisorServerClient();
    this.server = new SupervisorServer();
    this.server.addSupervisorServerListener(new SupervisorServerListener() {
      @Override
      public void ErrorCodeReceived(ErrorCode code) {

      }

      @Override
      public void ackConPacketReceived(int id, InetAddress ipAddress) {
        final SensorNode sNode = new SensorNode(id);
        sNode.setIpAddress(ipAddress);
        sNode.setConnected(true);
        Supervisor.this.sensors.put(id, sNode);
        fireSensorNodeConnected(sNode, ipAddress);
      }

      @Override
      public void registrationTerminated() {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        for (Pair<Integer, SensorConfiguration> pair : sensorConfs) {
          setConf(Supervisor.this.sensors.get(pair.getFirstElement()), pair
              .getSecondElement());
        }

      }
    });
    this.client
        .addSupervisorServerClientListener(new SupervisorServerClientListener() {
          @Override
          public void ackConfPacketReceived(SensorNode node,
              SensorConfiguration conf) {
            node.setConfigured(true);
            fireSensorNodeConfigured(node);
          }
        });

    this.sensorConfs = sensors;

    final int[] ids = new int[sensors.size()];
    int i = 0;
    for (Pair<Integer, SensorConfiguration> pair : sensors) {
      ids[i++] = pair.getFirstElement();
    }

    this.server.registerAllNodes(ids);
  }

  public void setConf(SensorNode node, SensorConfiguration conf) {
    this.client.setConf(node, conf);
  }

  /**
   * Retrieves the list of sensor nodes of the supervisor
   * 
   * @return List<SensorNode>
   */
  public List<SensorNode> getSensorNodes() {
    ArrayList<SensorNode> l = new ArrayList<SensorNode>();
    for (Entry<Integer, SensorNode> entry : this.sensors.entrySet()) {
      l.add(entry.getValue());
    }
    return l;
  }

  /**
   * Add a new sensor node to the supervisor
   * 
   * @param sensorNode
   */
  public void addSensorNode(int key, SensorNode sensorNode) {
    this.sensors.put(key, sensorNode);
  }

  /**
   * Remove the sensor node, corresponding to the given index, from the
   * supervisor
   * 
   * @param sensorNode
   */
  public void removeSensorNode(int key) {
    this.sensors.remove(key);
  }

  public void shutdown() throws IOException {
    this.server.shutdown();
  }

  public void addSupervisorListener(SupervisorListener listener) {
    this.listeners.add(listener);
  }

  public void removeSupervisorListener(SupervisorListener listener) {
    this.listeners.remove(listener);
  }

  protected void fireSensorNodeConnected(SensorNode sensor,
      InetAddress ipAddress) {
    for (SupervisorListener listener : this.listeners) {
      listener.sensorNodeConnected(sensor, ipAddress);
    }
  }

  protected void fireSensorNodeConfigured(SensorNode sensor) {
    for (SupervisorListener listener : this.listeners) {
      listener.sensorNodeConfigured(sensor);
    }
  }
}
