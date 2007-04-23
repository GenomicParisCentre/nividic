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

package fr.ens.transcriptome.nividic.om;

import fr.ens.transcriptome.nividic.om.filters.BiologicalFilter;

/**
 * This interface define a biological Object.
 * @author Laurent Jourdren
 */
public interface BiologicalObject extends Annotable {

  /**
   * Get the name of the Biological object.
   * @return The name of the biological object
   */
  String getName();

  /**
   * Set the name of the biological object.
   * @param name The name of the biological object
   */
  void setName(String name);

  /**
   * Get the history of the biological object.
   * @return The history object of the object
   */
  History getHistory();

  /**
   * Copy the biological Object.
   * @return a copy of the biological object
   */
  BiologicalObject copy();

  /**
   * Filter the object with a biological filter
   * @param filter Filter to apply
   * @return the biological object filtered
   */
  BiologicalObject filter(BiologicalFilter filter);

  /**
   * Count the number that the object pass the filter.
   * @param filter Filter to apply
   * @return the count the number that the object pass the filter
   */
  int count(BiologicalFilter filter);

  /**
   * Clear the biological object.
   */
  void clear();

  /**
   * Get the size of the biological object.
   * @return The size of the biological object
   */
  int size();

}
