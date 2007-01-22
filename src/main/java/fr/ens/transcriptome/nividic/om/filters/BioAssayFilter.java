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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;

/**
 * This interface defines how to filter BioAssay Objects
 * @author Laurent Jourdren
 */
public interface BioAssayFilter {

  /**
   * Filter a bioAssay object.
   * @param bioAssay BioAssay to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  BioAssay filter(BioAssay bioAssay) throws BioAssayRuntimeException;

  /**
   * Count the number of spots that pass the filter.
   * @param bioAssay The bioAssay to filter
   * @return the number of spot that pass the filter
   */
  int count(BioAssay bioAssay);

}