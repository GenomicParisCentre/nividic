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

/**
 * This class define an adapter of BioAssayFilter for ExpressionMatrixRowFilter
 * @author Laurent Jourdren
 */
public class ExpressionMatrixColumnFilterBioAssayFilterAdapter extends
    ExpressionMatrixColumnFilter {

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
   * Test the values of BioAssay object.
   * @param bioAssay BioAssay object where are the values to test
   * @return true if the values must be selected
   */
  @Override
  public boolean testColumn(final BioAssay bioAssay) {

    if (bioAssay == null)
      return false;

    final int size = bioAssay.size();
    int count = 0;

    if (doubleFilter != null) {

      final double[] values = bioAssay
          .getDataFieldDouble(getDimensionToFilter());

      for (int i = 0; i < size; i++)
        if (doubleFilter.test(values[i]))
          count++;
    } else {

      final int[] values = bioAssay.getDataFieldInt(getDimensionToFilter());

      for (int j = 0; j < size; j++)
        if (doubleFilter.test(values[j]))
          count++;
    }

    return ((double) count / (double) size) > this.threshold;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param doubleFilter Filter to set
   * @param threshold Threshold of the row filter
   */
  public ExpressionMatrixColumnFilterBioAssayFilterAdapter(
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
  public ExpressionMatrixColumnFilterBioAssayFilterAdapter(
      final BioAssayGenericIntegerFieldFilter integerFilter,
      final double threshold) {

    if (integerFilter == null)
      throw new NullPointerException("The filter is null");

    this.integerFilter = integerFilter;
    this.threshold = threshold;
  }

}
