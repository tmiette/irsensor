package fr.umlv.irsensor.sensor;

public enum SensorState {
  IP("00000000"), DOWN("00000001"), PAUSE("00000001");
  private final byte state;

  private SensorState(String code) {
    this.state = Byte.parseByte(code, 2);
  }

  public byte getState() {
    return this.state;
  }

  public static int getSensorStateByteSize() {
    return 1;
  }
}
