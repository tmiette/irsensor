package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

/**This defines a sensor node
 * It has a state like down, up or pause
 * It could be in configured sate or not
 *
 */
public class SensorNode {
	
	private final InetAddress ipAddress;
	
	/**
	 * the state of the sensor. please refer to <code>State</code> enum
	 */
	private State state;
	
	/**
	 * a flag to know if a sensor is configured or not
	 */
	private boolean isConfigured;
	
	public SensorNode(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
		this.state = State.DOWN;
	}
	
	/**Change the current state of a sensor
	 * 
	 * @param state <code>State</code>
	 */
	public void setState(State state){
		this.state = state;
	}
	
	/**
	 * Defines the different states of a sensor
	 */
	enum State{
		PAUSE,
		DOWN,
		UP;
	}
}
