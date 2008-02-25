package fr.umlv.irsensor.util;

/**
 * 
 * This class associates two elements.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 * 
 * @param <E1>
 *            first element.
 * @param <E2>
 *            second element.
 */
public class Pair<E1, E2> {

  private final E1 firstElement;

  private final E2 secondElement;

  /**
   * Constructor of a pair.
   * 
   * @param firstElement
   *            the first element.
   * @param secondElement
   *            the second element.s
   */
  public Pair(E1 firstElement, E2 secondElement) {
    this.firstElement = firstElement;
    this.secondElement = secondElement;
  }

  /**
   * Returns the first element.
   * 
   * @return the first element.
   */
  public E1 getFirstElement() {
    return this.firstElement;
  }

  /**
   * Returns the second element.
   * 
   * @return the second element.
   */
  public E2 getSecondElement() {
    return this.secondElement;
  }

  @Override
  public String toString() {
    return "{" + this.firstElement + ", " + this.secondElement + "}";
  }

}
