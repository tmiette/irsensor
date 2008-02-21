package fr.umlv.irsensor.supervisor.listeners;

import java.net.InetAddress;

import fr.umlv.irsensor.supervisor.SensorNode;

/**
 * Define a listener to the supervisor. It is based on Pattern Observer
 * This interface can be used by a graphic model to handle graphic changes
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface SupervisorListener {
	/**
	 * Send a notification when a <code>SensorNode</code> is connected 
	 * 
	 * @param sensor
	 * @param inetAddress the IP address of the given node
	 */
	public void sensorNodeConnected(SensorNode sensor, InetAddress inetAddress);
	
	/**
	 * Send a notification when a <code>SensorNode</code> has just been configured  
	 * 
	 * @param sensor
	 */
	public void sensorNodeConfigured(SensorNode sensor);

	/**
	 * Send a notification when the state of a <code>SensorNode</code> has just been changed
	 * 
	 * @param sensor
	 */
	public void sensorNodeStateChanged(SensorNode sensor);
	
	/**
	 * Send a notification when the answer of a request is incoming
	 * 
	 * @param data
	 */
	public void answerDataReceived(byte[] data);

}
