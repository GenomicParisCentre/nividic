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

package fr.ens.transcriptome.nividic.om.io;

import fr.ens.transcriptome.nividic.om.BiologicalObject;

/**
 * This interface define a reader for Biological objects.
 * @author Laurent Jourdren
 */
public interface BiologicalReader {

  /**
   * Read the biological object.
   * @return a new biological object
   * @throws NividicIOException if an error occurs while reading the biological
   *           object
   */
  BiologicalObject read() throws NividicIOException;

  /**
   * Get the source of the data.
   * @return The source of the data
   */
  String getDataSource();

}
