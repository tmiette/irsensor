package fr.umlv.irsensor.supervisor.listeners;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.supervisor.SensorNode;

/**
 * Define a listener to the <code>SupervisorServerClient</code>. It is based on Pattern Observer
 * This interface can be used by a object which wants to be notified of incoming changes
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface SupervisorServerClientListener {

	/**
	 * Send a notification when a <code>SensorNode</code> configuration has been changed
	 * 
	 * @param node
	 * @param conf
	 */
	public void sensorConfigurationChanged(SensorNode node, SensorConfiguration conf);
	
	/**
	 * Send a notification when the state of a <code>SensorNode</code> has been changed
	 * 
	 * @param node
	 * @param state
	 */
	public void sensorStateChanged(SensorNode node, SensorState state);
	
	/**
	 * Send a notification when the answer of a request is incoming
	 * 
	 * @param data
	 */
	public void answerDataReceived(byte[] data);

}
