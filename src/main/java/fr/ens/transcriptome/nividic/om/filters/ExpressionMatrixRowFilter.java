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

import java.util.ArrayList;
import java.util.List;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class implements a generic filter for filtering using the value of a
 * double row of the matrix
 * @author Lory Montout
 * @author Laurent Jourdren
 */
public abstract class ExpressionMatrixRowFilter implements
    ExpressionMatrixFilter {

  private List<String> columnNamesToFilter;
  private int[] columnToFilter;

  /**
   * Add a column to filter.
   * @param columnName Name of the column to filter
   */
  public void addColumnToFilter(final String columnName) {

    if (columnName == null)
      return;

    if (columnNamesToFilter == null)
      this.columnNamesToFilter = new ArrayList<String>();
    this.columnNamesToFilter.add(columnName);
  }

  /**
   * Add columns to filter.
   * @param columnNames Names of the column to filter
   */
  public void addColumnToFilter(final String[] columnNames) {

    if (columnNames == null)
      return;

    for (int i = 0; i < columnNames.length; i++)
      addColumnToFilter(columnNames[i]);

  }

  private void defineColumnToFilter(final ExpressionMatrix em) {

    if (columnNamesToFilter == null)
      return;

    List<Integer> idToFilter = new ArrayList<Integer>();

    for (int i = 0; i < columnNamesToFilter.size(); i++) {
      int pos = em.getColumnIndex(this.columnNamesToFilter.get(i));
      if (pos != -1)
        idToFilter.add(pos);
    }

    this.columnToFilter = new int[idToFilter.size()];
    for (int j = 0; j < this.columnToFilter.length; j++)
      this.columnToFilter[j] = idToFilter.get(j);
  }

  private double[] getValuesToTest(final double[] values) {

    if (columnToFilter == null)
      return values;

    if (columnToFilter.length == values.length)
      return values;

    double[] result = new double[columnToFilter.length];

    for (int i = 0; i < columnToFilter.length; i++)
      result[i] = values[columnToFilter[i]];

    return result;
  }

  /**
   * Filter a ExpressionMatrixDimension object.
   * @param em ExpressionMatrixDimension to filter
   * @return A new filtered ExpressionMatrixDimension object
   * @throws ExpressionMatrixRuntimeException if an error occurs while filtering
   *           data
   */
  public ExpressionMatrix filter(final ExpressionMatrix em)
      throws ExpressionMatrixRuntimeException {

    if (em == null)
      return null;

    defineColumnToFilter(em);

    ExpressionMatrixDimension d = em.getDimension(getDimensionToFilter());

    String[] rowNames = d.getRowNames();

    final int size = d.getRowCount();
    List<String> al = new ArrayList<String>();
    String[] positiveRows = new String[al.size()];

    for (int i = 0; i < size; i++)
      if (testRow(getValuesToTest(d.getRowToArray(rowNames[i]))))
        al.add(new String(rowNames[i]));

    positiveRows = al.toArray(positiveRows);

    if (isRemovePositiveRows())
      return em.subMatrixRowsExclude(positiveRows);

    return em.subMatrixRows(positiveRows);
  }

  /**
   * Count the number of the row that pass the filter
   * @param em The matrix to filter
   * @return the number of rows that pass the filter
   */
  public int count(final ExpressionMatrix em) {

    if (em == null)
      return -1;

    ExpressionMatrixDimension d = em.getDimension(getDimensionToFilter());

    String[] rowIds = d.getRowNames();

    final int size = d.getRowCount();
    int count = 0;

    for (int i = 0; i < size; i++)
      if (testRow(getValuesToTest(d.getRowToArray(rowIds[i]))))
        count++;

    return count;
  }

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  public abstract String getDimensionToFilter();

  /**
   * Test if filtered identifiers must be removed.
   * @return true if filtered row must be removed
   */
  public boolean isRemovePositiveRows() {

    return true;
  }

  /**
   * Test the values of a Row.
   * @param values Values of M to test
   * @return true if the values must be selected
   */
  public abstract boolean testRow(final double[] values);

}