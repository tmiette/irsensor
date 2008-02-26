package fr.umlv.irsensor.common.fields;

/**
 * Represents the different states that a sensor can take. This enumeration
 * associates a byte to a state name.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public enum SensorState {

  UP("00000000"), DOWN("00000001"), PAUSE("0000001");

  private final byte state;

  private SensorState(String code) {
    this.state = Byte.parseByte(code, 2);
  }

  /**
   * 
   * @return state byte value.
   */
  public byte getState() {
    return this.state;
  }

  /**
   * 
   * @return state byte size.
   */
  public static int getSensorStateByteSize() {
    return 1;
  }

  /**
   * Transcodes a byte array to a {@link SensorState}.
   * @param b byte array to transcode.
   * @return {@link SensorState} corresponding.
   */
  public static SensorState getState(byte[] b) {
    if (b.length == getSensorStateByteSize()) {
      for (SensorState code : SensorState.values()) {
        if (code.getState() == b[0]) { return code; }
      }
    }
    return null;
  }

}
