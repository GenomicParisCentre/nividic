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


/**
 * This class implements a filter which remove all rows of a bioarray with a
 * flag value lesser than 0.
 * @author Laurent Jourdren
 */
public class BioAssayBadFlagsFilter extends BioAssayFlagsFilter {

  /**
   * Test the flag value.
   * @param flag Value to test
   * @return true if the value must be selected
   */
  public boolean testValueofIntegerField(final int flag) {
    return flag >= 0;
  }

}