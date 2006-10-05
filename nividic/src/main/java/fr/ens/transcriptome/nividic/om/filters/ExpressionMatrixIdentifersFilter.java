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

import java.util.HashSet;
import java.util.Set;

public abstract class ExpressionMatrixIdentifersFilter extends
    ExpressionMatrixIdFilter {

  private Set idsToFilter = new HashSet();
  private boolean selectNull = false;

  /**
   * Add an identifier to the filtering list
   * @param id Identifier to add
   */
  public void add(final String id) {

    this.idsToFilter.add(id);
  }

  /**
   * Remove an identifier to the filtering list
   * @param id Identifier to remove
   */
  public void remove(final String id) {

    this.idsToFilter.remove(id);
  }

  /**
   * Add an identifier to the filtering list
   * @param ids Identifier to add
   */
  public void add(final String[] ids) {

    if (ids == null)
      return;

    for (int i = 0; i < ids.length; i++)
      this.idsToFilter.add(ids[i]);
  }

  /**
   * Remove an identifier to the filtering list
   * @param ids Identifier to remove
   */
  public void remove(final String[] ids) {

    if (ids == null)
      return;

    for (int i = 0; i < ids.length; i++)
      this.idsToFilter.remove(ids[i]);
  }

  /**
   * Clear the list of identifier to filter.
   */
  public void clear() {

    this.idsToFilter.clear();
  }

  /**
   * Get the identifiers of the filtering list.
   * @return An array of String with the identifiers to filter
   */
  public String[] getIds() {

    String[] result = new String[this.idsToFilter.size()];

    this.idsToFilter.toArray(result);

    return result;
  }

  /**
   * Test if null id must be filtered.
   * @return true if null id must be filtered
   */
  public boolean isFilterNull() {
    return this.selectNull;
  }

  /**
   * Set if if null id must be filtered.
   * @param filterNull The value
   */
  public void setFilterNull(final boolean filterNull) {
    
    this.selectNull = filterNull;
  }
  
  /**
   * Test if an identifier must be filtered.
   */
  public boolean testId(final String id) {

    if (id == null)
      return this.selectNull;

    return this.idsToFilter.contains(id);
  }

}
