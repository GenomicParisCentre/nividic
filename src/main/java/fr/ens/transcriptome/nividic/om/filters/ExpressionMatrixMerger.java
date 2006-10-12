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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.rank.Median;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class defined a class used to merge rows, columns or dimensions inside
 * an ExpressionMatrix Object.
 * @author Laurent Jourdren
 */
public class ExpressionMatrixMerger {

  private boolean medianMode = true;
  private ExpressionMatrix matrix;

  //
  // Getters
  //

  /**
   * Test if the median mode is enable.
   * @return true if the median mode is enable
   */
  public boolean isMedianMode() {

    return medianMode;
  }

  /**
   * Get the matrix.
   * @return Returns the matrix
   */
  public ExpressionMatrix getMatrix() {
    return matrix;
  }

  //
  // Setters
  //

  /**
   * Set enable or not the median mode.
   * @param medianMode The median mode
   */
  public void setMedianMode(final boolean medianMode) {
    this.medianMode = medianMode;
  }

  /**
   * Set the matrix
   * @param matrix The matrix to set
   */
  public void setMatrix(final ExpressionMatrix matrix) {
    this.matrix = matrix;
  }

  //
  // Other methods
  //

  /**
   * Merge columns of an ExpressionMatrix.
   * @param columnNames Columns to merge. The result merged column will be the
   *          first column of the array of columns names.
   * @return The merger object.
   */
  public ExpressionMatrixMerger mergeColumns(final String[] columnNames) {

    return mergeColumns(columnNames, null);
  }

  /**
   * Merge columns of an ExpressionMatrix.
   * @param columnNames Columns to merge. The result merged column will be the
   *          first column of the array of columns names.
   *  @param newColumnName The name of the new column
   * @return The merger object.
   */
  public ExpressionMatrixMerger mergeColumns(final String[] columnNames,
      final String newColumnName) {

    if (columnNames == null)
      throw new ExpressionMatrixRuntimeException("columnNames is null");

    if (columnNames.length == 1)
      return this;

    final ExpressionMatrix matrix = getMatrix();

    for (int i = 0; i < columnNames.length; i++) {
      if (!matrix.containsColumn(columnNames[i]))
        throw new ExpressionMatrixRuntimeException("Unknown column name: "
            + columnNames[i]);
    }

    // Define the algorithm
    final AbstractUnivariateStatistic algo;

    if (isMedianMode())
      algo = new Median();
    else
      algo = new Mean();

    final String[] dimensionNames = matrix.getDimensionNames();
    final String[] rowIds = matrix.getRowIds();
    final int nCols = columnNames.length;

    for (int i = 0; i < dimensionNames.length; i++) {

      final ExpressionMatrixDimension dimension = matrix
          .getDimension(dimensionNames[i]);

      for (int j = 0; j < rowIds.length; j++) {

        final String rowId = rowIds[j];

        final double[] values = new double[nCols];

        for (int k = 0; k < nCols; k++) {
          values[k] = dimension.getValue(rowId, columnNames[k]);
        }

        dimension.setValue(rowId, columnNames[0], algo.evaluate(values));
      }

    }

    Set set = new HashSet();

    String[] matrixColumnNames = matrix.getColumnNames();
    for (int i = 0; i < matrixColumnNames.length; i++)
      set.add(matrixColumnNames[i]);

    for (int i = 1; i < columnNames.length; i++)
      set.remove(columnNames[i]);

    String[] columnsToremove = new String[set.size()];
    set.toArray(columnsToremove);

    System.out.println("Col not filtered: ");
    for (int i = 0; i < columnsToremove.length; i++) {
      System.out.println(columnsToremove[i] + "\t");
    }

    setMatrix(matrix.subMatrixColumns(columnsToremove));

    /*
     * if (newColumnName != null) { this.matrix.renameColumn(columnNames[0],
     * newColumnName); }
     */

    return this;
  }

  /**
   * Merge rows of an ExpressionMatrix.
   * @param RowIds Row to merge. The result merged row will be the
   *          first row of the array of row names.
   * @return The merger object.
   */
  public ExpressionMatrixMerger mergeRows(String[] RowIds) {

    return this;
  }

  /**
   * Merge dimensions of an ExpressionMatrix.
   * @param dimensionNames Dimensions to merge. The result merged row will be the
   *          first dimension of the array of dimensions names.
   * @return The merger object.
   */
  public ExpressionMatrixMerger mergeDimension(String[] dimensionNames) {

    return this;
  }

}
