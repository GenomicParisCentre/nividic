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

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class implements a filter for filtering using the value of a double row
 * of the matrix
 * @author Lory Montout
 * @author Laurent Jourdren
 */
public class ExpressionMatrixRowNAFilter extends ExpressionMatrixRowFilter {

  private double threshold;

  /**
   * Test the values of a Row.
   * @param values Values to test
   * @return true if the values must be selected
   * @throws ExpressionMatrixRuntimeException if ratioValues is null
   */
  public boolean testRow(final double[] values)
      throws ExpressionMatrixRuntimeException {

    if (values == null)
      throw new ExpressionMatrixRuntimeException("values is null");

    final int size = values.length;

    int count = 0;

    for (int i = 0; i < size; i++)
      if (Double.isNaN(values[i]))
        count++;

    final double ratio = (double) count / (double) size;

    return ratio < this.threshold;
  }

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  public String getDimensionToFilter() {

    return ExpressionMatrix.DIMENSION_M;
  }

  /**
   * Test if filtered identifiers must be removed. *
   * @return true if filtered row must be removed
   */
  public boolean isRemovePositiveRows() {

    return false;
  }

  /**
   * Get the threshold of the filter.
   * @return Returns the threshold
   */
  public double getThreshold() {

    return threshold;
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "Rate="
        + getThreshold() + " RemovePositiveRows=" + isRemovePositiveRows();
  }

  //
  // Constructor
  //

  /**
   * Default constructor.
   */
  public ExpressionMatrixRowNAFilter() {
  }

  /**
   * Constructor.
   * @param threshold The rate of the filter
   */
  public ExpressionMatrixRowNAFilter(final double threshold) {

    if (threshold < 0)
      throw new NividicRuntimeException("Threshold can't be lower than 0.");

    this.threshold = threshold;
  }

}