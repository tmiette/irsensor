package fr.umlv.irsensor.common.fields;
/**
 * This enumeration represents all packet fields that can be in protocols.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public enum PacketFields {

  CATCH_AREA(16), CLOCK(4), AUTONOMY(4), QUALITY(4), PAYLOAD(4), PARENT_ID(4), ID(
      4), OPCODE(OpCode.getOpCodeByteSize()), STATE(SensorState
      .getSensorStateByteSize()), ERROR_CODE(1), IP(4), MIMETYPE(4), LENGHT(4), DATE(4);

  private int length;

  private PacketFields(int length) {

    this.length = length;
  }

  /**
   * 
   * @return packet field length.
   */
  public int getLength() {
    return length;
  }

  /**
   * Adds given {@link PacketFields} length.
   * @param fieldLengths.
   * @return total length.
   */
  public static int getLength(PacketFields... fieldLengths) {
    int sum = 0;
    for (PacketFields packetFieldLength : fieldLengths) {
      sum += packetFieldLength.getLength();
    }
    return sum;
  }
}
