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

/**
 * This class define an adapter of BioAssayFilter for ExpressionMatrixRowFilter
 * @author Laurent Jourdren
 */
public class ExpressionMatrixRowFilterBioAssayFilterAdapter extends
    ExpressionMatrixRowFilter {

  private BioAssayGenericDoubleFieldFilter doubleFilter;
  private BioAssayGenericIntegerFieldFilter integerFilter;

  private double threshold;

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  @Override
  public String getDimensionToFilter() {

    if (doubleFilter != null)
      return doubleFilter.getFieldToFilter();

    return integerFilter.getFieldToFilter();
  }

  /**
   * Test if filtered identifiers must be removed.
   * @return true if filtered row must be removed
   */
  @Override
  public boolean isRemovePositiveRows() {
    // TODO Auto-generated method stub
    return true;
  }

  /**
   * Test the values of a Row.
   * @param values Values of M to test
   * @return true if the values must be selected
   */
  @Override
  public boolean testRow(final double[] values) {

    if (values == null)
      return false;

    int count = 0;

    if (doubleFilter != null) {
      for (int i = 0; i < values.length; i++)
        if (doubleFilter.test(values[i]))
          count++;
    } else {
      for (int i = 0; i < values.length; i++)
        if (integerFilter.test((int) values[i]))
          count++;
    }

    return ((double) count / (double) values.length) > this.threshold;
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    String s = "Threshold=" + this.threshold;

    if (doubleFilter != null)

      return s
          + ";Adapter=" + this.doubleFilter.getClass().getSimpleName() + ";"
          + this.doubleFilter.getParameterInfo();

    return s
        + ";Adapter=" + this.integerFilter.getClass().getSimpleName() + ";"
        + this.integerFilter.getParameterInfo();
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param doubleFilter Filter to set
   * @param threshold Threshold of the row filter
   */
  public ExpressionMatrixRowFilterBioAssayFilterAdapter(
      final BioAssayGenericDoubleFieldFilter doubleFilter,
      final double threshold) {

    if (doubleFilter == null)
      throw new NullPointerException("The filter is null");

    this.doubleFilter = doubleFilter;
    this.threshold = threshold;
  }

  /**
   * Public constructor.
   * @param integerFilter Filter to set
   * @param threshold Threshold of the row filter
   */
  public ExpressionMatrixRowFilterBioAssayFilterAdapter(
      final BioAssayGenericIntegerFieldFilter integerFilter,
      final double threshold) {

    if (integerFilter == null)
      throw new NullPointerException("The filter is null");

    this.integerFilter = integerFilter;
    this.threshold = threshold;
  }

}
