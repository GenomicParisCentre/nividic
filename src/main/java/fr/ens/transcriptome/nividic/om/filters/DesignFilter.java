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

package fr.ens.transcriptome.nividic.om.filters;

import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.design.Design;

/**
 * This interface defines how to filter Design Objects
 * @author Laurent Jourdren
 */
public interface DesignFilter extends BiologicalFilter<Design> {

  /**
   * Filter a bioAssay object.
   * @param design Design to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  Design filter(Design design) throws BioAssayRuntimeException;

  /**
   * Count the number of spots that pass the filter.
   * @param design The design to filter
   * @return the number of spot that pass the filter
   */
  int count(Design design);

}
