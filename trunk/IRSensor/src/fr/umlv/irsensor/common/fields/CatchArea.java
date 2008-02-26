package fr.umlv.irsensor.common.fields;

/**
 * This class represents a data area that a sensor can capture. This area is an
 * abstract of an oblong and it is composed of two points.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class CatchArea {

  private final Point p1;

  private final Point p2;

  public CatchArea(Point p1, Point p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  public CatchArea(int x1, int y1, int x2, int y2) {
    this(new Point(x1, y1), new Point(x2, y2));
  }

  /**
   * @return the first point.
   */
  public Point getP1() {
    return this.p1;
  }

  /**
   * @return the second point.
   */
  public Point getP2() {
    return this.p2;
  }

  /**
   * @return the area width.
   */
  public int getAreaWidth() {
    return p2.x - p1.x;
  }

  /**
   * @return the area height.
   */
  public int getAreaHeight() {
    return p2.y - p1.y;
  }

  @Override
  public String toString() {
    return p1.toString() + ", " + p2.toString();
  }

  public static class Point {

    private final int x;

    private final int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return this.x;
    }

    public int getY() {
      return this.y;
    }

    @Override
    public String toString() {
      return "[" + x + ":" + y + "]";
    }

  }

}
