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

package fr.ens.transcriptome.nividic.om.io;

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This interface define a reader for BioAssay objects.
 * @author Laurent Jourdren
 */
public interface BioAssayReader extends BiologicalReader {

  /**
   * Read the BioAssay.
   * @return a new BioAssay object
   * @throws NividicIOException if an error occurs while reading the bioAssay
   */
  BioAssay read() throws NividicIOException;

}
