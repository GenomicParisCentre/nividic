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
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class implements a filter ExpressionMatrix Object on the number of NA
 * values in columns.
 * @author Laurent Jourdren
 */
public class ExpressionMatrixColumnNAFilter extends
    ExpressionMatrixColumnFilter {

  private double threshold;
  private String dimension = ExpressionMatrix.DIMENSION_M;

  /**
   * Test the values of a Row.
   * @param ba BioAssay to test
   * @return true if the values must be selected
   * @throws ExpressionMatrixRuntimeException if ratioValues is null
   */
  public boolean testColumn(final BioAssay ba)
      throws ExpressionMatrixRuntimeException {

    if (ba == null)
      throw new ExpressionMatrixRuntimeException("BioAssay is null");

    if (!ba.isField(this.dimension))
      throw new ExpressionMatrixRuntimeException("no data to filter");

    if (ba.getFieldType(this.dimension) != BioAssay.DATATYPE_DOUBLE)
      throw new ExpressionMatrixRuntimeException(
          "Invalid type of data to filter");

    final double[] values = ba.getDataFieldDouble(this.dimension);

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

    return dimension;
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

    return "Threshold=" + getThreshold();
  }

  //
  // Constructor
  //

  /**
   * Constructor.
   * @param threshold The rate of the filter
   */
  public ExpressionMatrixColumnNAFilter(final double threshold) {

    if (threshold < 0)
      throw new NividicRuntimeException("Threshold can't be lower than 0.");

    this.threshold = threshold;
  }

  /**
   * Constructor.
   * @param threshold The rate of the filter
   * @param dimension Dimension to filter
   */
  public ExpressionMatrixColumnNAFilter(final String dimension,
      final double threshold) {

    this(threshold);
    this.dimension = dimension;

  }

}
