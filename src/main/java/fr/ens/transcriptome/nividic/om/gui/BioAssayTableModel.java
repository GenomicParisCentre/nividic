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

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayBase;

public class BioAssayTableModel extends AbstractTableModel {

  private BioAssay bioAssay;
  private Map<Integer, String> columnIndexes = new HashMap<Integer, String>();
  private Map<Integer, Integer> columnTypes = new HashMap<Integer, Integer>();

  //
  // Getter
  //

  /**
   * @return Returns the bioAssay
   */
  public BioAssay getBioAssay() {
    return bioAssay;
  }

  //
  // Setter
  //

  /**
   * @param bioAssay The bioAssay to set
   */
  public void setBioAssay(final BioAssay bioAssay) {

    this.bioAssay = bioAssay;
    this.columnIndexes.clear();
    this.columnTypes.clear();

    if (bioAssay != null) {

      final String[] columnNames = this.bioAssay.getFields();

      int intCount = 0;
      int doublecount = 0;

      for (int i = 0; i < columnNames.length; i++) {

        final String colName = columnNames[i];
        this.columnIndexes.put(i, colName);
        this.columnTypes.put(i, this.bioAssay.getFieldType(colName));

        if (this.bioAssay.getFieldType(colName) == BioAssayBase.DATATYPE_STRING)
          System.out.println(colName + "\tString field");
        if (this.bioAssay.getFieldType(colName) == BioAssayBase.DATATYPE_INTEGER)
          intCount++;

        if (this.bioAssay.getFieldType(colName) == BioAssayBase.DATATYPE_DOUBLE)
          doublecount++;

      }

    }

  }

  public String getColumnName(int column) {

    return this.columnIndexes.get(column);
  }

  public int getColumnCount() {

    return this.bioAssay == null ? 0 : this.bioAssay.getFieldCount();
  }

  public int getRowCount() {

    return this.bioAssay == null ? 0 : this.bioAssay.size();
  }

  public Object getValueAt(final int rowIndex, final int columnIndex) {

    if (this.bioAssay == null)
      return null;

    final String columnName = this.columnIndexes.get(columnIndex);

    switch (this.columnTypes.get(columnIndex)) {

    case BioAssayBase.DATATYPE_INTEGER:

      final int[] intValues = this.bioAssay.getDataFieldInt(columnName);
      return Integer.valueOf(intValues[rowIndex]);

    case BioAssayBase.DATATYPE_DOUBLE:

      final double[] doubleValues =
          this.bioAssay.getDataFieldDouble(columnName);
      return Double.valueOf(doubleValues[rowIndex]);

    case BioAssayBase.DATATYPE_STRING:

      final String[] stringValues =
          this.bioAssay.getDataFieldString(columnName);
      return stringValues[rowIndex];

    default:
      return null;
    }

  }

}
