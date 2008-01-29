package fr.umlv.irsensor.sensor;
public class Sensor {

  private final int id;
  private CatchArea area;
  private int quality;
  private int clock;
  private int autonomy;
  private int payload;

  public Sensor(int id) {
    this.id = id;
  }

  public CatchArea getArea() {
    return this.area;
  }

  public void setArea(CatchArea area) {
    this.area = area;
  }

  public int getQuality() {
    return this.quality;
  }

  public void setQuality(int quality) {
    this.quality = quality;
  }

  public int getClock() {
    return this.clock;
  }

  public void setClock(int clock) {
    this.clock = clock;
  }

  public int getAutonomy() {
    return this.autonomy;
  }

  public void setAutonomy(int autonomy) {
    this.autonomy = autonomy;
  }

  public int getPayload() {
    return this.payload;
  }

  public void setPayload(int payload) {
    this.payload = payload;
  }

  public int getId() {
    return this.id;
  }

}
