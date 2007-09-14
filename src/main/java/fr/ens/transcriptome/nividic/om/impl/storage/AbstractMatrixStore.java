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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMatrixStore implements MatrixStore, Serializable {

  public List<Double> getColumnValuesAsArray(final int[] rowsIndex,
      final String columnName) throws MatrixStoreException {

    if (rowsIndex == null)
      throw new MatrixStoreException("rows Index are null");

    if (!isColumn(columnName))
      return null;

    final int size = getRowCount();

    List<Double> result = new ArrayList<Double>(size);

    for (int i = 0; i < rowsIndex.length; i++)
      result.add(get(rowsIndex[i], columnName));

    return result;
  }

  public void setValues(final int[] rowsIndex, final String columnName,
      final double[] values) throws MatrixStoreException {

    if (rowsIndex == null)
      throw new MatrixStoreException("Index is null");
    if (values == null)
      throw new MatrixStoreException("Values are null");
    if (rowsIndex.length != values.length)
      throw new MatrixStoreException(
          "Arrays of index and values have not the same size");

    for (int i = 0; i < rowsIndex.length; i++)
      set(rowsIndex[i], columnName, values[i]);

  }

}
