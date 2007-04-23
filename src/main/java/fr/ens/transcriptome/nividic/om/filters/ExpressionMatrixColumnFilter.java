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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This class implements a generic filter for filtering using the value of a
 * double Column of the matrix
 * @author Lory Montout
 */
public abstract class ExpressionMatrixColumnFilter implements
    ExpressionMatrixFilter {

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

    String[] columnNames = em.getColumnNames();

    int size = em.getColumnCount();
    List<String> al = new ArrayList<String>();

    ExpressionMatrixDimension d = em.getDimension(getDimensionToFilter());

    for (int i = 0; i < size; i++)
      if (testColumn(d.getColumn(columnNames[i])))
        al.add(new String(columnNames[i]));

    String[] positiveColumns = new String[al.size()];
    positiveColumns = al.toArray(positiveColumns);

    System.out.println(positiveColumns.length);

    if (removePositiveColumns())
      return em.subMatrixColumnsExclude(positiveColumns);

    return em.subMatrixColumns(positiveColumns);
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

    final String[] ColumnNames = d.getColumnNames();

    final int size = ColumnNames.length;
    int count = 0;

    for (int i = 0; i < size; i++)
      if (testColumn(d.getColumn(ColumnNames[i])))
        count++;

    return count;
  }

  /**
   * Test if filtered identifiers must be removed.
   * @return true if filtered column must be removed
   */
  public boolean removePositiveColumns() {

    return false;
  }

  /**
   * Get the dimension to filter.
   * @return The dimension to filter
   */
  public abstract String getDimensionToFilter();

  /**
   * Test the values of BioAssay object.
   * @param bioAssay BioAssay object where are the values to test
   * @return true if the values must be selected
   */
  public abstract boolean testColumn(final BioAssay bioAssay);

}