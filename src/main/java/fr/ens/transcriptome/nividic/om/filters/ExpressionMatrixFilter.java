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

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;

/**
 * This interface defines how to filter ExpressionMatrixDimension Objects
 * @author Lory Montout
 */
public interface ExpressionMatrixFilter extends
    BiologicalFilter<ExpressionMatrix> {

  /**
   * Filter a ExpressionMatrix object.
   * @param em ExpressionMatrix to filter
   * @return A new filtered ExpressionMatrix object
   * @throws ExpressionMatrixRuntimeException if an error occurs while filtering
   *           data
   */
  ExpressionMatrix filter(ExpressionMatrix em)
      throws ExpressionMatrixRuntimeException;

  /**
   * Count the number of the row that pass the filter
   * @param em The matrix to filter
   * @return the number of rows that pass the filter
   */
  int count(ExpressionMatrix em);

}