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

import org.apache.commons.collections.primitives.ArrayIntList;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.SpotIterator;

/**
 * This class implements a filter of empty spots using the EmptySpotTester of
 * the bioAssay.
 * @author Laurent Jourdren
 */
public class BioAssayEmptySpotFilter implements BioAssayFilter {

  /**
   * Filter a bioAssay object.
   * @param bioAssay BioAssay to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  public BioAssay filter(final BioAssay bioAssay)
      throws BioAssayRuntimeException {

    if (bioAssay == null)
      return null;

    final ArrayIntList al = new ArrayIntList();

    final SpotIterator si = bioAssay.iterator();

    while (si.hasNext()) {

      si.next();

      if (si.isEmpty())
        al.add(si.getIndex());
    }

    final int[] toRemove = new int[al.size()];
    al.toArray(toRemove);

    return BioAssayUtils.removeRowsFromBioAssay(bioAssay, toRemove);
  }

  /**
   * Count the number of spots that pass the filter.
   * @param bioAssay The bioAssay to filter
   * @return the number of spot that pass the filter
   */
  public int count(final BioAssay bioAssay) {

    if (bioAssay == null)
      return 0;

    final SpotIterator si = bioAssay.iterator();

    int count = 0;

    while (si.hasNext()) {

      si.next();

      if (si.isEmpty())
        count++;
    }

    return count;
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "";
  }

}
