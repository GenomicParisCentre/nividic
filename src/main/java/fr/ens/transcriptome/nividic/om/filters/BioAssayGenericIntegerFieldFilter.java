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

import java.util.ArrayList;
import java.util.List;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

/**
 * This class implements a generic filter for filtering using the value of an
 * integer field.
 * @author Laurent Jourdren
 */
public abstract class BioAssayGenericIntegerFieldFilter implements
    BioAssayFilter {

  /**
   * Filter a bioAssay object using value of the integer field.
   * @param bioAssay BioAssay to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  public BioAssay filter(final BioAssay bioAssay)
      throws BioAssayRuntimeException {

    if (bioAssay == null)
      return null;

    final String field = getFieldToFilter();

    if (!bioAssay.isField(field))
      throw new NividicRuntimeException("Unknown field ("
          + field + ") in bioAssay");

    final int[] data = bioAssay.getDataFieldInt(field);

    int size = bioAssay.size();
    List<Integer> al = new ArrayList<Integer>();

    for (int i = 0; i < size; i++)
      if (!test(data[i]))
        al.add(i);

    int[] toRemove = new int[al.size()];
    for (int i = 0; i < toRemove.length; i++)
      toRemove[i] = al.get(i);

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

    final int[] data = bioAssay.getDataFieldInt(getFieldToFilter());

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
  public abstract boolean test(final int value);

}