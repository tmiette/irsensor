package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.swing.JFrame;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.supervisor.listeners.SupervisorListener;
import fr.umlv.irsensor.supervisor.listeners.SupervisorServerClientListener;
import fr.umlv.irsensor.supervisor.listeners.SupervisorServerListener;
import fr.umlv.irsensor.supervisor.network.SupervisorServer;
import fr.umlv.irsensor.supervisor.network.SupervisorServerClient;
import fr.umlv.irsensor.util.IRSensorLogger;
import fr.umlv.irsensor.util.Pair;

/**
 * This class defines a supervisor A Supervisor maintains a list of sensors and
 * their configurations. It can interact with them through its server part <code>SupervisorServer</code> and
 * its client part <code>SupervisorServerClient</code>. 
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class Supervisor {

	private final HashMap<Integer, SensorNode> sensors = new HashMap<Integer, SensorNode>();

	private SensorNode rootNode;

	private final List<Pair<Integer, SensorConfiguration>> sensorConfs;

	private final SupervisorServer server;

	private final SupervisorServerClient client;

	private final ArrayList<SupervisorListener> listeners = new ArrayList<SupervisorListener>();
	
	private final Object hashMaplock = new Object(); 

	public Supervisor(List<Pair<Integer, SensorConfiguration>> sensors) {
		this.client = new SupervisorServerClient();
		this.server = new SupervisorServer();
		
		//listen to the server part
		this.server.addSupervisorServerListener(new SupervisorServerListener() {
			@Override
			public void ackConPacketReceived(int id, InetAddress ipAddress) {
				final SensorNode sNode = new SensorNode(id);
				sNode.setIpAddress(ipAddress);
				addSensorNode(id, sNode);
				fireSensorNodeConnected(sNode, ipAddress);
			}

			@Override
			public void registrationTerminated() {
				//TODO : remove this
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				  // TODO Auto-generated catch block
					e.printStackTrace();
				}
				IRSensorLogger.postMessage(Level.FINE, "All sensor nodes have been registered");
				for (Pair<Integer, SensorConfiguration> pair : sensorConfs) {
					setConf(getSensorNode(pair.getFirstElement()), pair.getSecondElement());
				}
				
				IRSensorLogger.postMessage(Level.FINE, "All sensor nodes have been configured");
				
				
				server.listenRepData();
				
			}
			
			@Override
			public void ErrorCodeReceived(ErrorCode code) {}

			@Override
			public void answerDataReceived(byte[] data) {
				fireAnswerDataReceived(data);
			}
		});
		
		//listen to the client part
		this.client.addSupervisorServerClientListener(new SupervisorServerClientListener() {
			@Override
			public void sensorConfigurationChanged(SensorNode node,
					SensorConfiguration conf) {
				node.setConfiguration(conf);
				fireSensorNodeConfigured(node);
			}

			@Override
			public void sensorStateChanged(SensorNode node, SensorState state) {
				node.setState(state);
				fireSensorStateChanged(node);
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
	
	/**
	 * Set a new state to the given sensor node
	 * 
	 * @param id
	 * @param state
	 */
	public void setState(int id, SensorState state) {
		SensorNode node = this.getSensorNode(id);
		if (node != null) {
			this.client.setState(node, state);
		}
	}
	
	/**
	 * Set a new configuration to the given sensor node
	 * 
	 * @param id
	 * @param conf
	 */
	public void setConf(int id, SensorConfiguration conf) {
		SensorNode node = this.getSensorNode(id);
		if (node != null) {
			this.setConf(node, conf);
		}
	}
	
	/**
	 * Set a new configuration to the given sensor node
	 * 
	 * @param node
	 * @param conf
	 */
	public void setConf(SensorNode node, SensorConfiguration conf) {
		SensorNode parent = this.getSensorNode(conf.getParentId());
		if (parent != null) {
			conf.setParentAddress(parent.getAddress());
		}
		this.client.setConf(node, conf);
	}
	
	/**
	 * Send a data request to the sink <code>SensorNode</code>
	 * 
	 * @param cArea
	 * @param quality
	 * @param clock
	 */
	public void dataRequest(CatchArea cArea, int quality, int clock){
		if(this.rootNode == null){
			for(SensorNode node: this.getSensorNodes()){
				if(node.getParentId() == -1){
					this.rootNode = node;
					break;
				}
			}
		}
		IRSensorLogger.postMessage(Level.FINE, "A data request has been sent");
		this.client.dataRequest(this.rootNode, cArea, clock, quality); 
	}

	/**
	 * Retrieves the list of sensor nodes of the supervisor
	 * 
	 * @return List<SensorNode>
	 */
	public List<SensorNode> getSensorNodes() {
		ArrayList<SensorNode> l = new ArrayList<SensorNode>();
		synchronized(hashMaplock){
			for (Entry<Integer, SensorNode> entry : this.sensors.entrySet()) {
				l.add(entry.getValue());
			}
		}
		return l;
	}
	
	public SensorNode getSensorNode(int id){
		synchronized (hashMaplock) {
			return this.sensors.get(id);
		}
	}

	/**
	 * Add a new sensor node to the supervisor
	 * 
	 * @param sensorNode
	 */
	public void addSensorNode(int key, SensorNode sensorNode) {
		synchronized (hashMaplock) {
			this.sensors.put(key, sensorNode);
		}
	}

	/**
	 * Remove the sensor node, corresponding to the given index, from the
	 * supervisor
	 * 
	 * @param sensorNode
	 */
	public void removeSensorNode(int key) {
		synchronized (hashMaplock) {
			this.sensors.remove(key);
		}
	};
	
	/**
	 * Shutdown the supervisor in closing the server part
	 * 
	 * @throws IOException
	 */
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

	protected void fireSensorStateChanged(SensorNode sensor) {
		for (SupervisorListener listener : this.listeners) {
			listener.sensorNodeStateChanged(sensor);
		}
	}

	protected void fireAnswerDataReceived(byte[] data) {
		for (SupervisorListener listener : this.listeners) {
			listener.answerDataReceived(data);
		}
	}
}
