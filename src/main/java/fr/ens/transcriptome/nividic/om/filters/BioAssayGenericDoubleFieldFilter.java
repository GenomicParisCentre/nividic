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

import java.util.ArrayList;
import java.util.List;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

/**
 * This class implements a generic filter for filtering using the value of a
 * double field.
 * @author Laurent Jourdren
 */
public abstract class BioAssayGenericDoubleFieldFilter implements
    BioAssayFilter {

  /**
   * Filter a bioAssay object using value of the double field.
   * @param bioAssay BioAssay to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  public BioAssay filter(final BioAssay bioAssay)
      throws BioAssayRuntimeException {

    if (bioAssay == null)
      return null;

    final double[] data = bioAssay.getDataFieldDouble(getFieldToFilter());

    final int size = bioAssay.size();
    final List<Integer> al = new ArrayList<Integer>(data.length);

    for (int i = 0; i < size; i++)
      if (!test(data[i]))
        al.add(i);

    final int n = al.size();
    final int[] toRemove = new int[n];

    for (int i = 0; i < n; i++)
      toRemove[i] = al.get(i).intValue();

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

    int count = 0;

    final double[] data = bioAssay.getDataFieldDouble(getFieldToFilter());

    int size = bioAssay.size();

    for (int i = 0; i < size; i++)
      if (!test(data[i]))
        count++;

    return count;
  }

  /**
   * Get the field to filer
   * @return The field to filer
   */
  public abstract String getFieldToFilter();

  /**
   * Test the value.
   * @param value Value to test
   * @return true if the value must be selected
   */
  public abstract boolean test(final double value);

}