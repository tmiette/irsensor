package fr.umlv.irsensor.common;

import java.net.InetAddress;

import fr.umlv.irsensor.common.fields.CatchArea;

/**
 * This class represents a sensor configuration.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SensorConfiguration {

  private int autonomy;

  private CatchArea cArea;

  private int clock;

  private int parentId;

  private InetAddress parentAddress;

  private int payload;

  private int quality;

  public SensorConfiguration(CatchArea area, int autonomy, int clock,
      int payload, int quality, int parentId) {
    this.autonomy = autonomy;
    this.cArea = area;
    this.clock = clock;
    this.parentId = parentId;
    this.payload = payload;
    this.quality = quality;
  }

  public SensorConfiguration(CatchArea area, int autonomy, int clock,
      int payload, int quality, InetAddress address) {
    this(area, autonomy, clock, payload, quality, -1);
    this.parentAddress = address;
  }

  /**
   * parentAddress setter.
   * 
   * @param parentAddress to set.
   */
  public void setParentAddress(InetAddress parentAddress) {
    this.parentAddress = parentAddress;
  }

  /**
   * parentId setter.
   * 
   * @param parentId to set
   */
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  /**
   * 
   * @return the sensor parentAddress.
   */
  public InetAddress getParentAddress() {
    return parentAddress;
  }

  /**
   * 
   * @return the sensor parentId.
   */
  public int getParentId() {
    return parentId;
  }

  /**
   * 
   * @return the sensor autonomy.
   */
  public int getAutonomy() {
    return autonomy;
  }

  /**
   * autonomy setter.
   * 
   * @param autonomy to set.
   */
  public void setAutonomy(int autonomy) {
    this.autonomy = autonomy;
  }

  /**
   * 
   * @return sensor's CatchArea.
   */
  public CatchArea getCArea() {
    return cArea;
  }

  /**
   * CatchArea setter.
   * 
   * @param area to set.
   */
  public void setCArea(CatchArea area) {
    cArea = area;
  }

  /**
   * 
   * @return sensor's clock.
   */
  public int getClock() {
    return clock;
  }

  /**
   * set sensor's clock.
   * 
   * @param clock to set.
   */
  public void setClock(int clock) {
    this.clock = clock;
  }

  /**
   * 
   * @return sensor's payload.
   */
  public int getPayload() {
    return payload;
  }

  /**
   * sensor's payload setter.
   * 
   * @param payload
   */
  public void setPayload(int payload) {
    this.payload = payload;
  }

  /**
   * 
   * @return sensor's quality.
   */
  public int getQuality() {
    return quality;
  }

  /**
   * sensor's quality setter.
   * 
   * @param quality to set.
   */
  public void setQuality(int quality) {
    this.quality = quality;
  }

}
