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

import java.util.Iterator;

import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.BiologicalListFactory;

/**
 * This interface define a filter on a biological list
 * @author Laurent Jourdren
 */
public abstract class BiologicalListTestFilter implements BiologicalListFilter {

  /**
   * Count the number of members of the list that pass the filter.
   * @param list The biological list to filter
   * @return the number of spot that pass the filter
   */
  public int count(final BiologicalList list) {

    if (list == null)
      return 0;

    final Iterator it = list.iterator();

    int count = 0;
    while (it.hasNext()) {

      final String s = (String) it.next();
      if (test(s))
        count++;
    }

    return count;
  }

  /**
   * Filter a biological list object.
   * @param list BiologicalList to filter
   * @return A new filtered list object
   */
  public BiologicalList filter(final BiologicalList list) {

    if (list == null)
      return null;

    final Iterator it = list.iterator();

    final BiologicalList result = BiologicalListFactory.createBiologicalList();
    while (it.hasNext()) {

      final String s = (String) it.next();
      if (test(s))
        result.add(s);
    }

    return result;
  }

  /**
   * Test if a member of the list must be filtered
   * @param memberId member of the list to test
   * @return true if the member must be filtered
   */
  public abstract boolean test(String memberId);

}
