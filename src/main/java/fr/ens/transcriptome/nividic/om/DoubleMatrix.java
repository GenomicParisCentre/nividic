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
 * of the �cole Normale Sup�rieure and the individual authors.
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

/**
 * Define a useful interface to manipulate Double Matrix for Math methods.
 * @author Laurent Jourdren
 */
public interface DoubleMatrix {

  /**
   * Get row dimension.
   * @return m, the number of rows.
   */
  int getRowCount();

  /**
   * Get column dimension.
   * @return n, the number of columns.
   */
  int getColumnCount();

  /**
   * Set a single element.
   * @param i Row index.
   * @param j Column index.
   * @param s A(i,j).
   * @exception ArrayIndexOutOfBoundsException
   */
  void set(final int i, final int j, final double s);

  /**
   * Get a single element.
   * @param i Row index.
   * @param j Column index.
   * @return A(i,j)
   * @exception ArrayIndexOutOfBoundsException
   */
  double get(final int i, final int j);

}
