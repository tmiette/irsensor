package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

import fr.umlv.irsensor.sensor.CatchArea;
import fr.umlv.irsensor.sensor.SensorState;

/**
 * This defines a sensor node It has a state like down, up or pause It could be
 * in configured sate or not
 * 
 */
public class SensorNode {

  /**
   * Autonomy
   */
  private int autonomy;

  /**
   * Sensor's catch area
   */
  private CatchArea cArea;

  /**
   * Clock
   */
  private int clock;

  /**
   * Sensor ID
   */
  private final int id;

  /**
   * Sensor parent ID
   */
  private int idParent;

  /**
   * IP address of the current sensor
   */
  private InetAddress ipAddress;

  /**
   * a flag to know if a sensor is configured or not
   */
  private boolean isConfigured;

  private boolean isConnected;

  /**
   * Sensor's payload
   */
  private int payload;

  /**
   * Sensor's capture quality
   */
  private int quality;

  /**
   * the state of the sensor. please refer to <code>State</code> enum
   */
  private SensorState state;

  public SensorNode(int id) {
    this.id = id;

    this.isConfigured = false;
    this.state = SensorState.DOWN;
  }

  public InetAddress getAddress() {
    return this.ipAddress;
  }

  public int getAutonomy() {
    return autonomy;
  }

  public CatchArea getCArea() {
    return cArea;
  }

  public int getClock() {
    return clock;
  }

  public int getId() {
    return id;
  }

  public int getIdParent() {
    return idParent;
  }

  public int getPayload() {
    return payload;
  }

  public int getQuality() {
    return quality;
  }

  public SensorState getState() {
    return state;
  }

  public boolean isConfigured() {
    return isConfigured;
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void setAutonomy(int autonomy) {
    this.autonomy = autonomy;
  }

  public void setCArea(CatchArea area) {
    cArea = area;
  }

  public void setClock(int clock) {
    this.clock = clock;
  }

  public void setConfigured(boolean isConfigured) {
    this.isConfigured = isConfigured;
  }

  public void setConnected(boolean isConnected) {
    this.isConnected = isConnected;
  }

  public void setIdParent(int idParent) {
    this.idParent = idParent;
  }

  public void setIpAddress(InetAddress ipAddress) {
    this.ipAddress = ipAddress;
  }

  public void setPayload(int payload) {
    this.payload = payload;
  }

  public void setQuality(int quality) {
    this.quality = quality;
  }

  /**
   * Change the current state of a sensor
   * 
   * @param state
   *            <code>State</code>
   */
  public void setState(SensorState state) {
    this.state = state;
  }
}
