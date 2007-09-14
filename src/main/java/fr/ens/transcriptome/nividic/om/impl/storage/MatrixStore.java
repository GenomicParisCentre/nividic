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

import java.util.List;

public interface MatrixStore {

  void addColumn(final String columnName) throws MatrixStoreException;

  void removeColumn(final String columnName) throws MatrixStoreException;

  void renameColumn(final String oldName, final String newName)
      throws MatrixStoreException;

  void addRow() throws MatrixStoreException;

  void addRow(int count) throws MatrixStoreException;

  void set(final int rowIndex, final String columnName, final double value)
      throws MatrixStoreException;

  double get(final int rowIndex, final String columnName)
      throws MatrixStoreException;

  boolean isColumn(final String columnName);

  int getRowCount();

  int getColumnCount();

  List<Double> getColumnValuesAsArray(final int[] rowsIndex,
      final String columnName) throws MatrixStoreException;

  void setValues(final int[] rowsIndex, final String columnName,
      final double[] values) throws MatrixStoreException;

  void fill(final String columnName, final double value)
      throws MatrixStoreException;

}