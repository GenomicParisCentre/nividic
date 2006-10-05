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
 * BioAssayException should be thrown to indicate that there was a problem with
 * parsing sequence information.
 * @author Laurent Jourdren
 */
public class BioAssayRuntimeException extends NividicRuntimeException {

  /**
   * Create a new BioAssayException with a message.
   * @param message the message
   */
  public BioAssayRuntimeException(final String message) {
    super(message);
  }

  /**
   * Create a new NividicRuntimeException with a cause and a message.
   * @param type Type of exception message
   * @param causeMessage the cause that caused this NividicRuntimeException
   */
  public BioAssayRuntimeException(final int type, final String causeMessage) {
    super(type, causeMessage);
  }

  /**
   * Create a new BioAssayRuntimeException.
   */
  public BioAssayRuntimeException() {
    super();
  }

}