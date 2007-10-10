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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

/**
 * This class implements a filter which remove all rows of a bioarray with a
 * intensity (in red or green) under a threshold.
 * @author Laurent Jourdren
 */
public class BioAssayMaximalIntensityFilter  implements BioAssayFilter {

  private int maxIntensity;

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

    final int[] greens = bioAssay.getGreens();
    final int[] reds = bioAssay.getReds();

    final int size = bioAssay.size();
    final List<Integer> al = new ArrayList<Integer>(greens.length);

    for (int i = 0; i < size; i++)
      if (!test(greens[i], reds[i]))
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

    final int[] greens = bioAssay.getGreens();
    final int[] reds = bioAssay.getReds();

    int size = bioAssay.size();

    for (int i = 0; i < size; i++)
      if (!test(greens[i], reds[i]))
        count++;

    return count;
  }

  /**
   * Test the value.
   * @param value Value to test
   * @return true if the value must be selected
   */
  private boolean test(final double green, final double red) {

    return green < this.maxIntensity && red < this.maxIntensity;
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "min: " + this.maxIntensity;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param maxIntensity value of the threshold
   */
  public BioAssayMaximalIntensityFilter(final int maxIntensity) {

    this.maxIntensity = maxIntensity;
  }


}