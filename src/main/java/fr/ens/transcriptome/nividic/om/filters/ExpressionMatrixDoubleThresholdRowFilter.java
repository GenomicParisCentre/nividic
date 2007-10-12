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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class define a common filter for row data values of ExpressionMatrix
 * @author Laurent Jourdren
 */
public class ExpressionMatrixDoubleThresholdRowFilter extends
    ExpressionMatrixRowFilter {

  private ExpressionMatrixRowFilter filter;
  private double rowThreshold;
  private String dimension;

  /**
   * Count the number of the row that pass the filter
   * @param em The matrix to filter
   * @return the number of rows that pass the filter
   */
  public int count(final ExpressionMatrix em) {

    return filter.count(em);
  }

  /**
   * Test the values of a Row.
   * @param em The matrix to filter
   * @return true if the values must be selected
   * @throws ExpressionMatrixRuntimeException if an error occurs while executing
   *             the filter
   */
  public ExpressionMatrix filter(final ExpressionMatrix em)
      throws ExpressionMatrixRuntimeException {

    return filter.filter(em);
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return filter.getParameterInfo() + " rowThreshold=" + this.rowThreshold;
  }

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  @Override
  public String getDimensionToFilter() {

    return this.dimension;
  }

  /**
   * Test the values of a Row.
   * @param values Values of M to test
   * @return true if the values must be selected
   */
  @Override
  public boolean testRow(final double[] values) {

    return this.filter.testRow(values);
  }

  //
  // Constructors
  //

  private ExpressionMatrixDoubleThresholdRowFilter(final double rowThreshold,
      final String dimension, final ExpressionMatrixRowFilter filter) {

    this.rowThreshold = rowThreshold;
    this.dimension = dimension;
    this.filter = filter;
  }

  /**
   * Public constructor.
   * @param dimension Dimension to test
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdRowFilter(final String dimension,
      final String comparator, final double valueThreshold,
      final double rowThreshold) {

    this(rowThreshold, dimension,
        new ExpressionMatrixRowFilterBioAssayFilterAdapter(
            new BioAssayDoubleThresholdFilter(dimension, comparator,
                valueThreshold), rowThreshold));
  }

  /**
   * Public constructor. The field used by this constructor is M.
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param absolute true if the threshold is absolute value
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdRowFilter(final double valueThreshold,
      final String comparator, final boolean absolute, final double rowThreshold) {

    this(BioAssay.FIELD_NAME_M, valueThreshold, comparator, absolute,
        rowThreshold);
  }

  /**
   * Public constructor. The field used by this constructor is M.
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdRowFilter(final String comparator,
      final double valueThreshold, final double rowThreshold) {

    this(BioAssay.FIELD_NAME_M, comparator, valueThreshold, rowThreshold);
  }

  /**
   * Public constructor.
   * @param dimension Dimension to test
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param absolute true if the threshold is absolute value
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdRowFilter(final String dimension,
      final double valueThreshold, final String comparator,
      final boolean absolute, final double rowThreshold) {

    this(rowThreshold, dimension,
        new ExpressionMatrixRowFilterBioAssayFilterAdapter(
            new BioAssayDoubleThresholdFilter(dimension, comparator,
                valueThreshold, absolute), rowThreshold));

  }

}
