package fr.umlv.irsensor.common.packets.sensor;

import fr.umlv.irsensor.common.fields.OpCode;
/**
 * Default SENSOR protocol interface.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface SensorPacket {

  /**
   * 
   * @return SENSOR packet sensor's destination id.
   */
  int getId();
  
  /**
   * 
   * @return SENSOR packet {@link OpCode}.
   */
  OpCode getOpCode();

}
