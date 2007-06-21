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
 * This class define a filter that remove the number of the identifiers of a
 * biological list.
 * @author Laurent Jourdren
 */
public class BiologicalListIdentifierNumberRemoverFilter extends
    BiologicalListTransformFilter {

  /**
   * Transform a member identifier of a list to new identifier.
   * @param memberId member to transform
   * @return a new name for the identifier
   */
  public String transform(final String memberId) {

    if (memberId == null)
      return null;

    int index = memberId.indexOf("#");

    if (index == -1)
      return memberId;

    return memberId.substring(0, index);
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "";
  }

}
