package fr.umlv.irsensor.common.packets.data;

import fr.umlv.irsensor.common.fields.OpCode;

/**
 * Default DATA protocol interface.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface DataPacket {
  /**
   * 
   * @return DATA packet {@link OpCode}.
   */
  public OpCode getOpCode();

  /**
   * 
   * @return DATA packet sensor's destination id.
   */
  public int getId();
}
