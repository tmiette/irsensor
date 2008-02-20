package fr.umlv.irsensor.common;

import java.net.InetAddress;

public class SensorConfiguration {

  private int autonomy;

  private CatchArea cArea;

  private int clock;

  private int parentId;
  
  private InetAddress parentAddress;

  private int payload;

  private int quality;

  private SensorState state;

  public SensorConfiguration(SensorState state, CatchArea area, int autonomy,
      int clock, int payload, int quality, int parentId) {
    this.autonomy = autonomy;
    this.cArea = area;
    this.clock = clock;
    this.parentId = parentId;
    this.payload = payload;
    this.quality = quality;
    this.state = state;
  }
  
  public SensorConfiguration(SensorState state, CatchArea area, int autonomy,
	      int clock, int payload, int quality, InetAddress address) {
	    this(state, area, autonomy, clock, payload, quality, -1);
	    this.parentAddress = address;
  }
  
  public void setParentAddress(InetAddress parentAddress) {
    this.parentAddress = parentAddress;
  }
  
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
  
  public InetAddress getParentAddress() {
    return parentAddress;
  }
  
  public int getParentId() {
    return parentId;
  }

  public int getAutonomy() {
    return autonomy;
  }

  public void setAutonomy(int autonomy) {
    this.autonomy = autonomy;
  }

  public CatchArea getCArea() {
    return cArea;
  }

  public void setCArea(CatchArea area) {
    cArea = area;
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

  public SensorState getState() {
    return state;
  }

  public void setState(SensorState state) {
    this.state = state;
  }

}
