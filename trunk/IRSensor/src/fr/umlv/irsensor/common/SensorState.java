package fr.umlv.irsensor.common;

public enum SensorState {

  UP("00000000"), DOWN("00000001"), PAUSE("0000001");

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

  public static SensorState getState(byte[] b) {
    if (b.length == getSensorStateByteSize()) {
      for (SensorState code : SensorState.values()) {
        if (code.getState() == b[0]) { return code; }
      }
    }
    return null;
  }

}
