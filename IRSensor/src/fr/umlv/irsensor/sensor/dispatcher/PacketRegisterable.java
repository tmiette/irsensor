package fr.umlv.irsensor.sensor.dispatcher;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * The class defines the main type of an object which can be
 * notified by the <code>PacketDispatcher</code>.
 * A such object must have an ID to be identified by the Dispatcher, then
 * it has to be able to receive a packet in <code>ByteBuffer</code> format.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface PacketRegisterable {
	
	/**
	 * Retrieves the id of the current <code>PacketRegisterable</code> 
	 * 
	 * @return the id
	 */
	public int getId();
	
	/**
	 * Provides a new packet to the current <code>PacketRegisterable</code> 
	 * 
	 * @param packet
	 * @param channel the associated channel, if the client wants to answer
	 */
	public void setPacket(ByteBuffer packet, SocketChannel channel);
}
