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

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * Exceptions thrown when an error occures during the manipulation of
 * ExpressionMatrixDimension objects
 * @author Lory Montout
 */
public class ExpressionMatrixRuntimeException extends NividicRuntimeException {

  /**
   * Create a new ExpressionMatrixRuntimeException with a message.
   * @param message the message
   */
  public ExpressionMatrixRuntimeException(final String message) {
    super(message);
  }

  /**
   * Create a new ExpressionMatrixRuntimeException with a cause and a message.
   * @param type Type of exception message
   * @param causeMessage the cause that caused this NividicRuntimeException
   */
  public ExpressionMatrixRuntimeException(final int type,
      final String causeMessage) {
    super(type, causeMessage);
  }

  /**
   * Create a new ExpressionMatrixRuntimeException.
   */
  public ExpressionMatrixRuntimeException() {
    super();
  }

}