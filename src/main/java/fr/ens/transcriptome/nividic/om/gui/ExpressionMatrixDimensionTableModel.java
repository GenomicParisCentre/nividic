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

package fr.ens.transcriptome.nividic.om.gui;

import javax.swing.table.AbstractTableModel;

import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;

public class ExpressionMatrixDimensionTableModel extends AbstractTableModel {

  private ExpressionMatrixDimension dimension;
  // private Map<Integer, String> columnIndexes = new HashMap<Integer,
  // String>();
  // private Map<Integer, Integer> columnTypes = new HashMap<Integer,
  // Integer>();

  private String[] columnNames;
  private String[] rowNames;

  //
  // Getter
  //

  /**
   * @return Returns the bioAssay
   */
  public ExpressionMatrixDimension getDimenssion() {
    return dimension;
  }

  //
  // Setter
  //

  /**
   * @param bioAssay The bioAssay to set
   */
  public void setDimension(final ExpressionMatrixDimension dimension) {

    this.dimension = dimension;

    if (dimension != null) {

      this.columnNames = dimension.getColumnNames();
      this.rowNames = dimension.getRowNames();
      fireTableDataChanged();
    }

  }

  public String getColumnName(int column) {

    if (column == 0)
      return "RowName";

    return this.columnNames[column - 1];
  }

  public int getColumnCount() {

    return this.dimension == null ? 0 : this.columnNames.length + 1;
  }

  public int getRowCount() {

    return this.dimension == null ? 0 : this.rowNames.length;
  }

  public Object getValueAt(final int rowIndex, final int columnIndex) {

    if (this.dimension == null)
      return null;

    if (columnIndex == 0)
      return rowNames[rowIndex];

    return Double.valueOf(this.dimension.get(rowIndex, columnIndex - 1));
  }

}
