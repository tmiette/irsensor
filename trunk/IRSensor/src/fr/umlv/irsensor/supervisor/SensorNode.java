package fr.umlv.irsensor.supervisor;

import java.nio.channels.SocketChannel;

/**This defines a sensor node
 * It has a state like down, up or pause
 * It could be in configured sate or not
 *
 */
public class SensorNode {
	
	//just an hack to host many sensors on a same machine
	private final SocketChannel channel;
	
	/**
	 * the state of the sensor. please refer to <code>State</code> enum
	 */
	private State state;
	
	/**
	 * a flag to know if a sensor is configured or not
	 */
	private boolean isConfigured;
	
	public SensorNode(SocketChannel socketChannel) {
		this.channel = socketChannel;
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
