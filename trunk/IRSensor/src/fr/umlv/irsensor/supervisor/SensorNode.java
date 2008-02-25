package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;

/**
 * Define a sensor node It has a state like down, up or pause It could be
 * in configured state or not
 * 
 */
public class SensorNode {

  private final int id;

  private InetAddress ipAddress;

  private SensorConfiguration configuration;

  private SensorState state;

  private boolean isConnected;

  public SensorNode(int id) {
    this.id = id;
    this.state = SensorState.DOWN;
    this.isConnected = true;
  }

  /**
   * Sensor's configuration setter.
   * @param configuration setter.
   */
  public void setConfiguration(SensorConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Sensor's address getter.
   * @return sensor'adress.
   */
  public InetAddress getAddress() {
    return this.ipAddress;
  }

  /**
   * Sensor's autonomy.
   * @return sensor's autonomy.
   */
  public int getAutonomy() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getAutonomy();
  }

  /**
   * Sensor's catch area.
   * @return sensor's catch area.
   */
  public CatchArea getCArea() {
    if (this.configuration == null) {
      return null;
    }
    return this.configuration.getCArea();
  }

  /**
   * Sensor's clock.
   * @return sensor's clock.
   */
  public int getClock() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getClock();
  }

  /**
   * Sensor's id.
   * @return sensor's id.
   */
  public int getId() {
    return id;
  }

  /**
   * Sensor's parent id.
   * @return sensor's parent id.
   */
  public int getParentId() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getParentId();
  }
  /**
   * Sensor's parent address.
   * @return sensor's catch area.
   */
  public InetAddress getParentAddress() {
    if (this.configuration == null) {
      return null;
    }
    return this.configuration.getParentAddress();
  }
  /**
   * Sensor's payload.
   * @return sensor's payload.
   */
  public int getPayload() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getPayload();
  }
  /**
   * Sensor's quality.
   * @return sensor's quality.
   */
  public int getQuality() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getQuality();
  }
  /**
   * Sensor's state.
   * @return sensor's state.
   */
  public SensorState getState() {
    return this.state;
  }
  /**
   * Sensor's state setter.
   */
  public void setState(SensorState state) {
    this.state = state;
  }
  /**
   * Return configurated sensor state.
   * @return if sensor is configurated.
   */
  public boolean isConfigured() {
    return this.configuration != null;
  }
  /**
   * @return if sensor is already connected.
   */
  public boolean isConnected() {
    return isConnected;
  }
  /**
   * Sensor's ip address setter
   * @return sensor's ip address.
   */
  public void setIpAddress(InetAddress ipAddress) {
    this.ipAddress = ipAddress;
  }
}
