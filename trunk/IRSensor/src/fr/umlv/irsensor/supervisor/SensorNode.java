package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

import fr.umlv.irsensor.sensor.CatchArea;

/**This defines a sensor node
 * It has a state like down, up or pause
 * It could be in configured sate or not
 *
 */
public class SensorNode {
	
	/**
	 * IP address of the current sensor
	 */
	private InetAddress ipAddress;
	
	/**
	 * the state of the sensor. please refer to <code>State</code> enum
	 */
	private State state;
	
	/**
	 * a flag to know if a sensor is configured or not
	 */
	private boolean isConfigured;
	
	/**
	 * Sensor ID
	 */
	private final int id;
	
	/**
	 * Sensor parent ID
	 */
	private int idParent;
	
	/**
	 * Sensor's catch area
	 */
	private CatchArea cArea;
	
	/**
	 * Autonomy
	 */
	private int autonomy;
	
	/**
	 * Clock
	 */
	private int clock;
	
	/**
	 * Sensor's payload
	 */
	private int payload;
	
	/**
	 * Sensor's capture quality
	 */
	private int quality;
	
	
	public SensorNode(int id) {
		this.id = id;
		
		this.isConfigured = false;
		this.state = State.DOWN;
	}
	
	/**Change the current state of a sensor
	 * 
	 * @param state <code>State</code>
	 */
	public void setState(State state){
		this.state = state;
	}
	
	public InetAddress getAddress(){
		return this.ipAddress;
	}
	/**
	 * Defines the different states of a sensor
	 */
	enum State{
		PAUSE,
		DOWN,
		UP;
	}
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	public int getIdParent() {
		return idParent;
	}

	public void setIdParent(int idParent) {
		this.idParent = idParent;
	}

	public CatchArea getCArea() {
		return cArea;
	}

	public void setCArea(CatchArea area) {
		cArea = area;
	}

	public int getAutonomy() {
		return autonomy;
	}

	public void setAutonomy(int autonomy) {
		this.autonomy = autonomy;
	}

	public int getClock() {
		return clock;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public int getPayload() {
		return payload;
	}

	public void setPayload(int payload) {
		this.payload = payload;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public State getState() {
		return state;
	}

	public int getId() {
		return id;
	}
}
