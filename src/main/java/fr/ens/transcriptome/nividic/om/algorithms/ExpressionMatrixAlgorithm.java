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

package fr.ens.transcriptome.nividic.om.algorithms;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;

/**
 * This interface defines an algorithm for expression matrix.
 * @author Laurent Jourdren
 */
public interface ExpressionMatrixAlgorithm {

  /**
   * Execute the algorithm.
   * @param matrix to handle
   * @return the input matrix
   */
  ExpressionMatrix execute(final ExpressionMatrix matrix);

}
