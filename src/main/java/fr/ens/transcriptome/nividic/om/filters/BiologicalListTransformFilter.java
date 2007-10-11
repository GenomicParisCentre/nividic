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

import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.BiologicalListFactory;

/**
 * This abstract class define how to easly transform the members of a biological
 * list.
 * @author Laurent Jourdren
 */
public abstract class BiologicalListTransformFilter implements
    BiologicalListFilter {

  /**
   * Count the number of members of the list that pass the filter.
   * @param list The biological list to filter
   * @return the number of spot that pass the filter
   */
  public int count(final BiologicalList list) {

    return list == null ? 0 : list.size();
  }

  /**
   * Filter a biological list object.
   * @param list BiologicalList to filter
   * @return A new filtered list object
   */
  public BiologicalList filter(final BiologicalList list) {

    if (list == null)
      return null;

    BiologicalList result = BiologicalListFactory.createBiologicalList();

    for (final String s : list)
      result.add(transform(s));

    return result;
  }

  /**
   * Transform a member identifier of a list to new identifier.
   * @param memberId member to transform
   * @return a new name for the identifier
   */
  public abstract String transform(final String memberId);

}
