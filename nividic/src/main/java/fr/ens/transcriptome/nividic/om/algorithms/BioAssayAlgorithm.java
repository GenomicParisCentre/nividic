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

package fr.ens.transcriptome.nividic.om.algorithms;

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This interface defines an algorithm
 * @author Laurent Jourdren
 */
public interface BioAssayAlgorithm {

  /**
   * Execute the algorithm.
   * @param bioAssay to handle
   * @return the input bioAssay
   */
  BioAssay execute(final BioAssay bioAssay);

}
