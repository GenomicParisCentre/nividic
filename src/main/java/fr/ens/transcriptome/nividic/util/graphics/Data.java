/*
 *                      Nividic development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the microarray platform
 * of the École Normale Supérieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.util.graphics;

import java.util.ArrayList;

/**
 * This a wrapper for Plot data.
 * @author Laurent Jourdren
 */
public class Data {

  private ArrayList x = new ArrayList();
  private ArrayList y = new ArrayList();

  /**
   * Add a data
   * @param x X coordinate of the data
   * @param y Y coordinate of the data
   */
  public void add(final double x, final double y) {

    this.x.add(new Double(x));
    this.y.add(new Double(y));

  }

  /**
   * Get a list of the X data.
   * @return An ArrayList of the X data
   */
  public ArrayList getXs() {
    return this.x;
  }

  /**
   * Get a list of the Y data.
   * @return An ArrayList of the Y data
   */
  public ArrayList getYs() {
    return this.y;
  }

  //
  // Other methods
  //

  /**
   * Get the minimal value of an array list of double.
   * @param al Array list to test
   * @return the minimal value of the array list
   */
  private double getMin(final ArrayList al) {
    if (al == null || al.size() == 0)
      return 0;

    double result = Double.MAX_VALUE;

    for (int i = 0; i < al.size(); i++) {
      final double v = ((Double) al.get(i)).doubleValue();
      if (v < result)
        result = v;
    }
    return result;
  }

  /**
   * Get the maximal value of an array list of double.
   * @param al Array list to test
   * @return the maximal value of the array list
   */
  private double getMax(final ArrayList al) {
    if (al == null || al.size() == 0)
      return 0;

    double result = Double.MIN_VALUE;

    for (int i = 0; i < al.size(); i++) {
      final double v = ((Double) al.get(i)).doubleValue();
      if (v > result)
        result = v;
    }
    return result;
  }

  /**
   * Get the minimal value of the X values.
   * @return The minimal value of the X values
   */
  public double getMinX() {
    return getMin(getXs());
  }

  /**
   * Get the minimal value of the Y values.
   * @return The minimal value of the Y values
   */
  public double getMinY() {
    return getMin(getYs());
  }

  /**
   * Get the maximal value of the X values.
   * @return The maximal value of the X values
   */
  public double getMaxX() {
    return getMax(getXs());
  }

  /**
   * Get the maximal value of the Y values.
   * @return The maximal value of the Y values
   */
  public double getMaxY() {
    return getMax(getYs());
  }

  /**
   * Get the number of the values.
   * @return The number of the values
   */
  public int size() {
    return this.x.size();
  }

}