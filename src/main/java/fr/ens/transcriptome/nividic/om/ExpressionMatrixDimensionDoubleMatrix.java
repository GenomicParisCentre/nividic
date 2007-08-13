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

package fr.ens.transcriptome.nividic.om;

public class ExpressionMatrixDimensionDoubleMatrix implements DoubleMatrix {

  private ExpressionMatrixDimension dimension;
  private String[] columnNames;
  private String[] rowNames;

  /**
   * Get row dimension.
   * @return m, the number of rows.
   */

  public int getRowCount() {
    return rowNames.length;
  }

  /**
   * Get column dimension.
   * @return n, the number of columns.
   */

  public int getColumnCount() {
    return columnNames.length;
  }

  /**
   * Set a single element.
   * @param i Row index.
   * @param j Column index.
   * @param s A(i,j).
   * @exception ArrayIndexOutOfBoundsException
   */

  public void set(final int i, final int j, final double s) {

    this.dimension.setValue(this.rowNames[i], this.columnNames[j], s);
  }

  /**
   * Get a single element.
   * @param i Row index.
   * @param j Column index.
   * @return A(i,j)
   * @exception ArrayIndexOutOfBoundsException
   */

  public double get(final int i, final int j) {

    return this.dimension.getValue(this.rowNames[i], this.columnNames[j]);
  }

  //
  // Static methods
  //

  ExpressionMatrixDimensionDoubleMatrix(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      throw new NullPointerException("The dimension is null");

    this.dimension = dimension;

    this.columnNames = dimension.getColumnNames();
    this.rowNames = dimension.getRowNames();
  }
  
}
