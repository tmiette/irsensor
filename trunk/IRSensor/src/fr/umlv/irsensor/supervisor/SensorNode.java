package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;

/**
 * Define a sensor node It has a state like down, up or pause It could be
 * in configured sate or not
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

  public void setConfiguration(SensorConfiguration configuration) {
    this.configuration = configuration;
  }

  public InetAddress getAddress() {
    return this.ipAddress;
  }

  public int getAutonomy() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getAutonomy();
  }

  public CatchArea getCArea() {
    if (this.configuration == null) {
      return null;
    }
    return this.configuration.getCArea();
  }

  public int getClock() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getClock();
  }

  public int getId() {
    return id;
  }

  public int getParentId() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getParentId();
  }

  public InetAddress getParentAddress() {
    if (this.configuration == null) {
      return null;
    }
    return this.configuration.getParentAddress();
  }

  public int getPayload() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getPayload();
  }

  public int getQuality() {
    if (this.configuration == null) {
      return -1;
    }
    return this.configuration.getQuality();
  }

  public SensorState getState() {
    return this.state;
  }

  public void setState(SensorState state) {
    this.state = state;
  }

  public boolean isConfigured() {
    return this.configuration != null;
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void setIpAddress(InetAddress ipAddress) {
    this.ipAddress = ipAddress;
  }
}
