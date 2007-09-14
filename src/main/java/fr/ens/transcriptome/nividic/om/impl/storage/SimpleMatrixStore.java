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

package fr.ens.transcriptome.nividic.om.impl.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

public class SimpleMatrixStore extends AbstractMatrixStore {

  private Map<String, List<Double>> columns =
      new LinkedHashMap<String, List<Double>>();
  public int rowCount;

  public boolean isColumn(final String columnName) {

    return this.columns.containsKey(columnName);
  }

  public int getRowCount() {

    return this.rowCount;
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#addColumn(java.lang.String)
   */
  public void addColumn(final String columnName) throws MatrixStoreException {

    if (columnName == null)
      return;

    List<Double> columnToAdd = new ArrayList<Double>(this.rowCount);

    for (int i = 0; i < this.rowCount; i++)
      columnToAdd.add(i, Double.NaN);

    this.columns.put(columnName, columnToAdd);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#removeColumn(java.lang.String)
   */
  public void removeColumn(final String columnName) throws MatrixStoreException {

    if (!this.columns.containsKey(columnName))
      throw new ExpressionMatrixRuntimeException(
          "The name of the column to remove " + columnName + " does not exist");

    this.columns.remove(columnName);
  }

  public void renameColumn(final String oldName, final String newName)
      throws MatrixStoreException {

    if (!this.columns.containsKey(oldName))
      throw new ExpressionMatrixRuntimeException(
          "The name of the column to rename " + oldName + " does not exist");

    List<Double> col = this.columns.get(oldName);

    this.columns.remove(oldName);
    this.columns.put(newName, col);
  }

  public void addRow(int count) throws MatrixStoreException {

    int c = count < 1 ? 1 : count;

    for (Map.Entry<String, List<Double>> entry : this.columns.entrySet())
      for (int i = 0; i < c; i++)
        entry.getValue().add(Double.NaN);

    this.rowCount += c;
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#addRow()
   */
  public void addRow() throws MatrixStoreException {

    addRow(1);
  }

  public int getColumnCount() {

    return this.columns.size();
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#set(int,
   *      java.lang.String, double)
   */
  public void set(final int rowIndex, final String columnName,
      final double value) throws MatrixStoreException {

    if (rowIndex < 0 || rowIndex >= this.rowCount)
      throw new MatrixStoreException("The row doesn't exist ("
          + rowIndex + "\t" + this.rowCount + ")");

    final List<Double> column = this.columns.get(columnName);
    if (column == null)
      throw new MatrixStoreException("The column doesn't exist");

    column.set(rowIndex, value);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#get(int,
   *      java.lang.String, double)
   */
  public double get(final int rowIndex, final String columnName)
      throws MatrixStoreException {

    if (rowIndex < 0 || rowIndex >= this.rowCount)
      throw new MatrixStoreException("The row doesn't exist");

    final List<Double> column = this.columns.get(columnName);
    if (column == null)
      throw new MatrixStoreException("The column doesn't exist");

    return column.get(rowIndex);
  }

  public void fill(final String columnName, final double value)
      throws MatrixStoreException {

    final List<Double> column = this.columns.get(columnName);
    if (column == null)
      throw new MatrixStoreException("The column doesn't exist");

    Collections.fill(column, Double.NaN);
  }

}
