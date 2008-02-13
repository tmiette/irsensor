package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

import fr.umlv.irsensor.common.packets.ErrorCode;

/**
 * This class defines a supervisor
 * A Supervisor maintains a list of sensors and their configuration
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class Supervisor {
	
	private int nbrOfSensors;
	
	private final HashMap<Integer, SensorNode> sensors = new HashMap<Integer, SensorNode>();
	
	private final SupervisorServer server;
	
	private final SupervisorClient client;
	
	public Supervisor(int nbrOfSensors, SupervisorClient client, SupervisorServer server) {
		this.nbrOfSensors = nbrOfSensors;
		this.client = client;
		this.server = server;
		this.server.addSupervisorServerListener(new SupervisorServerListener(){
			@Override
			public void ErrorCodeReceived(ErrorCode code) {
				
			}

			@Override
			public void ReqConPacketReceived(int id, InetAddress ipAddress) {
				addSensorNode(id, new SensorNode(ipAddress));
			}
		});
		this.server.registerAllNodes(this.nbrOfSensors);
	}
	
	/**
	 * Retrieves the list of sensor nodes of the supervisor
	 * 
	 * @return List<SensorNode>
	 */
	public List<SensorNode> getSensorNodes(){
		return null;
	}
	
	/**
	 * Add a new sensor node to the supervisor
	 * 
	 * @param sensorNode
	 */
	public void addSensorNode(int key, SensorNode sensorNode) {
		this.sensors.put(key,sensorNode);
	}
	
	/**
	 * Remove the sensor node, corresponding to the given index, from the supervisor
	 * 
	 * @param sensorNode
	 */
	public void removeSensorNode(int key) {
		this.sensors.remove(key);
	}
}
