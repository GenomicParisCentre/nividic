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
public class ExpressionMatrixMThresholdRowFilter extends
    ExpressionMatrixRowFilter {

  private static final double THRESHOLD_DEFAULT = 5.0;
  private static final double RATE_DEFAULT = 2.0 / 3.0;

  private double threshold = THRESHOLD_DEFAULT;
  private double rate = RATE_DEFAULT;

  /**
   * Test the values of a Row.
   * @param values Values to test
   * @return true if the values must be selected
   * @throws ExpressionMatrixRuntimeException if ratioValues is null
   */
  public boolean testRow(final double[] values)
      throws ExpressionMatrixRuntimeException {

    if (values == null)
      throw new ExpressionMatrixRuntimeException("ratioValue is null");

    final int ratioLenght = values.length;

    int isLower = 0;

    for (int i = 0; i < ratioLenght; i++) {
      if (values[i] <= getThreshold())
        isLower++;
    }

    if (((double) isLower / (double) ratioLenght) >= getRate())
      return false;

    return true;
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
   * Set the threshold.
   * @param threshold The threshold to set
   */
  public void setThreshold(final double threshold) {
    this.threshold = threshold;
  }

}
