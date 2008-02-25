package fr.umlv.irsensor.common.packets.supervisor;

import fr.umlv.irsensor.common.fields.OpCode;

/**
 * Default SUPERVISOR protocol interface.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public interface SupervisorPacket {
	
  /**
   * 
   * @return SUPERVISOR packet {@link OpCode}.
   */
	public OpCode getOpCode();
	
  /**
   * 
   * @return SUPERVISOR packet sensor's destination id.
   */
	public int getId();
}
