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

/**
 * This class implements a filter for flags
 * @author Laurent Jourdren
 */
public abstract class BioAssayFlagsFilter extends
    BioAssayGenericIntegerFieldFilter {

  /**
   * Get the field to filer
   * @return The field to filer
   */
  public String getFieldToFilter() {

    return BioAssay.FIELD_NAME_FLAG;
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "";
  }

}