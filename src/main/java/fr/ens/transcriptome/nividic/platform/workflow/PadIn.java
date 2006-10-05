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

package fr.ens.transcriptome.nividic.platform.workflow;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.UnboundedFifoBuffer;

import fr.ens.transcriptome.nividic.platform.PlatformException;

/**
 * Input pad
 * @author Laurent Jourdren
 */
public abstract class PadIn {

  private Buffer buffer = new UnboundedFifoBuffer();;

  /**
   * Get the buffer of the Pad.
   * @return The buffer of the pad
   */
  Buffer getBuffer() {
    return this.buffer;
  }

  /**
   * Handle an event.
   * @param c Container to compute
   * @throws PlatformException if an error occurs while computing the
   *                 container
   */
  void handleEvent(final Container c) throws PlatformException {

    this.buffer.add(c);
    start();
  }

  protected abstract void start() throws PlatformException;

}