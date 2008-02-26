package fr.umlv.irsensor.supervisor.listeners;

import java.net.InetAddress;

import fr.umlv.irsensor.common.fields.ErrorCode;

/**
 * Define a listener to the <code>SupervisorServer</code>. It is based on Pattern Observer
 * This interface can be used by a object which wants to be notified of incoming packets or connections
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface SupervisorServerListener {
	
	/**
	 * Send a notification when an <code>ErrorCode</code> is received
	 * 
	 * @param code
	 */
	public void ErrorCodeReceived(ErrorCode code);
	
	/**
	 * Send a notification when a sensor has been registered
	 * 
	 * @param id the id of the SensorNode
	 * @param ipAddress the Ip address of the SensorNode
	 */
	public void ackConPacketReceived(int id, InetAddress ipAddress);
	
	/**
	 * Send a notification when all the SensorNode have been registered
	 */
	public void registrationTerminated();
	
	/**
	 * Send a notification when the answer of a request is incoming
	 * 
	 * @param data
	 */
	public void answerDataReceived(byte[] data);
}
