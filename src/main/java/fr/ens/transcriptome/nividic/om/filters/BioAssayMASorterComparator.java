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

import fr.ens.transcriptome.nividic.om.Spot;

public class BioAssayMASorterComparator implements SpotComparator {

  private boolean descending;

  public int compare(Spot a, Spot b) {

    return this.descending ? -internalCompare(a, b) : internalCompare(a, b);
  }

  private int internalCompare(Spot a, Spot b) {

    if (a == null && b == null)
      return 0;
    if (a == null)
      return -1;
    if (b == null)
      return 1;

    int comp = Double.compare(a.getM(), b.getM());

    if (comp == 0.0)
      comp = Double.compare(a.getA(), b.getA());

    return comp;
  }

  //
  // Constructor
  //

  public BioAssayMASorterComparator() {

    this(false);
  }

  public BioAssayMASorterComparator(final boolean descending) {

    this.descending = descending;
  }

}
