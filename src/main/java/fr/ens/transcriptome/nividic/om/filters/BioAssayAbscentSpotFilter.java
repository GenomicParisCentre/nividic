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

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class implements a filter which remove all rows of a bioarray with a
 * flag is equals to Genepix abscent flag.
 * @author Laurent Jourdren
 */
public class BioAssayAbscentSpotFilter extends BioAssayFlagsFilter {

  /**
   * Test the flag value.
   * @param flag Value to test
   * @return true if the value must be selected
   */
  public boolean test(final int flag) {
    return flag != BioAssay.FLAG_ABSCENT;
  }

}