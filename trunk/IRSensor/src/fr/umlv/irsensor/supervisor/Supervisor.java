package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fr.umlv.irsensor.common.packets.ErrorCode;

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

  private final SupervisorServer server;

  private final SupervisorServerClient client;

  public Supervisor(List<SensorNode> sensors, SupervisorServerClient client,
      SupervisorServer server) {
    this.client = client;
    this.server = server;
    this.server.addSupervisorServerListener(new SupervisorServerListener() {
      @Override
      public void ErrorCodeReceived(ErrorCode code) {

      }

      @Override
      public void ReqConPacketReceived(int id, InetAddress ipAddress) {
        SensorNode sNode = Supervisor.this.sensors.get(id);
        sNode.setIpAddress(ipAddress);
      }

      @Override
      public void registrationTerminated() {
        for (Entry<Integer, SensorNode> node : Supervisor.this.sensors
            .entrySet()) {
          Supervisor.this.client.setConf(node.getKey(), node.getValue()
              .getAddress());
        }
      }
    });

    final int[] ids = new int[sensors.size()];
    for (int i = 0; i < ids.length; i++) {
      final SensorNode sensor = sensors.get(i);
      this.sensors.put(sensor.getId(), sensor);
      ids[i] = sensor.getId();
    }

    this.server.registerAllNodes(ids);
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
}
