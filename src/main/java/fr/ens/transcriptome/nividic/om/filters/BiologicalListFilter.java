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

/**
 * This interface define a filter on a biological list
 * @author Laurent Jourdren
 */
public interface BiologicalListFilter {

  /**
   * Filter a biological list object.
   * @param list BiologicalList to filter
   * @return A new filtered list object
   */
  BiologicalList filter(BiologicalList list);

  /**
   * Count the number of members of the list that pass the filter.
   * @param list The biological list to filter
   * @return the number of spot that pass the filter
   */
  int count(BiologicalList list);

}
