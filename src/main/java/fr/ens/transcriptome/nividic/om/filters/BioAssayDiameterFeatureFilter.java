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

/**
 * This class define a filter for BioAssay based on the diameter of the spot. It
 * use the value provided by Axon Genepix GPR files. With this class a range of
 * value must be defined.
 * @author Laurent Jourdren
 */
public class BioAssayDiameterFeatureFilter extends
    BioAssayGenericIntegerFieldFilter {

  private int min;
  private int max;

  /**
   * Get the field to filer
   * @return The field to filer
   */
  public String getFieldToFilter() {

    return "Dia.";
  }

  /**
   * Test the flag value.
   * @param dia Value to test
   * @return true if the value must be selected
   */
  public final boolean test(final int dia) {

    return dia <= min || dia >= max;
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "min: " + this.min + " max: " + this.max;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param min minimal value of the range of value to keep
   * @param max maximal value of the range of value to keep
   */
  public BioAssayDiameterFeatureFilter(final int min, final int max) {

    this.min = Math.min(min, max);
    this.max = Math.max(min, max);
  }

}
