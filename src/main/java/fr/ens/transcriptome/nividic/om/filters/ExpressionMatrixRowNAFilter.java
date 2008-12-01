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
 * of the �cole Normale Sup�rieure and the individual authors.
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
 * This class implements a filter ExpressionMatrix Object on the number of NA
 * values in rows.
 * @author Lory Montout
 * @author Laurent Jourdren
 */
public class ExpressionMatrixRowNAFilter extends ExpressionMatrixRowFilter {

  private double threshold;
  private String dimension = ExpressionMatrix.DIMENSION_M;

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

    final double ratio = 1.0 - ((double) count / (double) size);

    return ratio >= this.threshold;
  }

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  public String getDimensionToFilter() {

    return dimension;
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

    return "Threshold="
        + getThreshold() + " RemovePositiveRows=" + isRemovePositiveRows();
  }

  //
  // Constructor
  //

  /**
   * Constructor.
   * @param threshold The rate of the filter
   */
  public ExpressionMatrixRowNAFilter(final double threshold) {

    if (threshold < 0)
      throw new NividicRuntimeException("Threshold can't be lower than 0.");

    if (threshold > 1)
      throw new NividicRuntimeException("Threshold can't be upper than 1.");

    this.threshold = threshold;
  }

  /**
   * Constructor.
   * @param threshold The rate of the filter
   * @param dimension Dimension to filter
   */
  public ExpressionMatrixRowNAFilter(final String dimension,
      final double threshold) {

    this(threshold);
    this.dimension = dimension;

  }

}