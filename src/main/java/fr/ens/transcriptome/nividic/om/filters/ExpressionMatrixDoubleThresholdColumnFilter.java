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
 * This class define a common filter for column data values of ExpressionMatrix
 * @author Laurent Jourdren
 */
public class ExpressionMatrixDoubleThresholdColumnFilter implements
    ExpressionMatrixFilter {

  private ExpressionMatrixFilter filter;
  private double columnThreshold;

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

    return filter.getParameterInfo()
        + " columnThreshold=" + this.columnThreshold;
  }

  //
  // Constructors
  //

  /**
   * Public constructor. The field used by this constructor is M.
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param absolute true if the threshold is absolute value
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdColumnFilter(final String comparator,
      final double valueThreshold, final boolean absolute,
      final double rowThreshold) {

    this.filter =
        new ExpressionMatrixColumnFilterBioAssayFilterAdapter(
            new BioAssayDoubleThresholdFilter(BioAssay.FIELD_NAME_M,
                comparator, valueThreshold, absolute), rowThreshold);

    this.columnThreshold = rowThreshold;
  }

  /**
   * Public constructor. The field used by this constructor is M.
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdColumnFilter(final String comparator,
      final double valueThreshold, final double rowThreshold) {

    this.filter =
        new ExpressionMatrixColumnFilterBioAssayFilterAdapter(
            new BioAssayDoubleThresholdFilter(BioAssay.FIELD_NAME_M,
                comparator, valueThreshold), rowThreshold);

    this.columnThreshold = rowThreshold;
  }

  /**
   * Public constructor.
   * @param field Field to test
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdColumnFilter(final String field,
      final String comparator, final double valueThreshold,
      final double rowThreshold) {

    this.filter =
        new ExpressionMatrixColumnFilterBioAssayFilterAdapter(
            new BioAssayDoubleThresholdFilter(field, comparator, valueThreshold),
            rowThreshold);

    this.columnThreshold = rowThreshold;
  }

  /**
   * Public constructor.
   * @param field Field to test
   * @param valueThreshold Threshold of the test of values
   * @param comparator comparator to use
   * @param absolute true if the threshold is absolute value
   * @param rowThreshold Threshold of values that pass the test in a row
   */
  public ExpressionMatrixDoubleThresholdColumnFilter(final String field,
      final String comparator, final double valueThreshold,
      final boolean absolute, final double rowThreshold) {

    this.filter =
        new ExpressionMatrixColumnFilterBioAssayFilterAdapter(
            new BioAssayDoubleThresholdFilter(field, comparator,
                valueThreshold, absolute), rowThreshold);

    this.columnThreshold = rowThreshold;
  }

}
