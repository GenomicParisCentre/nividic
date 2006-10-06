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

package fr.ens.transcriptome.nividic.om.filters;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class implements a filter for filtering using the value of a double row
 * of the matrix
 * @author Lory Montout
 * @author Laurent Jourdren
 */
public class ExpressionMatrixMFloorRowFilter extends ExpressionMatrixRowFilter {

  private static final double RATE_DEFAULT = 2.0 / 3.0;
  private static final double THRESHOLD_DEFAULT = 1.0;

  private double threshold = THRESHOLD_DEFAULT;
  private double rate = RATE_DEFAULT;
  private boolean abs = true;

  /**
   * Test the values of a Row.
   * @param values Values to test
   * @return true if the values must be selected
   * @throws ExpressionMatrixRuntimeException if ratioValues is null
   */
  public boolean testRow(final double[] values)
      throws ExpressionMatrixRuntimeException {

    if (values == null)
      throw new ExpressionMatrixRuntimeException("no values to test");

    final int size = values.length;

    int count = 0;
    final boolean abs = isAbs();
    final double threshold = getThreshold();
    final double rate = getRate();

    for (int i = 0; i < size; i++) {

      final double value = abs ? Math.abs(values[i]) : values[i];

      if (value <= threshold)
        count++;
    }

    final double ratio = (double) count / (double) size;

    if (ratio >= rate)
      return true;

    return false;
  }

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  public String getDimensionToFilter() {

    return ExpressionMatrix.DIMENSION_M;
  }

  /**
   * Test if filtered identifiers must be removed.
   */
  public boolean removeFilteredRows() {

    return false;
  }

  /**
   * Get the rate of the filter.
   * @return Returns the rate
   */
  public double getRate() {

    return rate;
  }

  /**
   * Set the rate of the filter.
   * @param rate The rate to set
   */
  public void setRate(final double rate) {

    this.rate = rate;
  }

  /**
   * Get the threshold.
   * @return Returns the threshold
   */
  public double getThreshold() {
    return threshold;
  }

  /**
   * Set the threshold
   * @param threshold The threshold to set
   */
  public void setThreshold(final double threshold) {
    this.threshold = threshold;
  }

  /**
   * Test if the absolute value of the data must be used.
   * @return Returns if the absolute value of the data must be used
   */
  public boolean isAbs() {
    return abs;
  }

  /**
   * Set if the absolute value of the data must be used.
   * @param abs The abs to set
   */
  public void setAbs(final boolean abs) {
    this.abs = abs;
  }

}