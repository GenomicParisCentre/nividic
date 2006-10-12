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

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class define a class that filter row of a bioAssay that are in a list.
 * @author Laurent Jourdren
 */
public abstract class BioAssayIdentifiersFilter extends
    BioAssayGenericStringFieldFilter {

  private Set identifiers = new HashSet();
  private boolean selectNull = true;

  /**
   * Add an identifier.
   * @param identifier Identifier to add
   */
  public void addIdentifier(final String identifier) {
    this.identifiers.add(identifier);
  }

  /**
   * Remove an identifer.
   * @param identifier Identifier to remove
   */
  public void removeIdentifier(final String identifier) {
    this.identifiers.remove(identifier);
  }

  /**
   * Test if an identifier is present in the of identifers to remove
   * @param identifier Identifier to test
   * @return true if the identifier is present in the of identifers to remove
   */
  public boolean containtsIdentifier(final String identifier) {
    return this.identifiers.contains(identifier);
  }

  /**
   * Add identifiers.
   * @param identifiers Identifiers to add
   */
  public void addIdentifers(final String[] identifiers) {

    if (identifiers == null)
      return;

    for (int i = 0; i < identifiers.length; i++) {
      addIdentifier(identifiers[i]);
    }
  }

  /**
   * Remove identifers.
   * @param identifiers Identifiers to remove
   */
  public void removeIdentifers(final String[] identifiers) {

    if (identifiers == null)
      return;

    for (int i = 0; i < identifiers.length; i++) {
      removeIdentifier(identifiers[i]);
    }
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

  //
  // Methods from BioAssayGenericStringFieldFilter
  //

  /**
   * Get the field to filer
   * @return The field to filer
   */
  public String getFieldToFilter() {

    return BioAssay.FIELD_NAME_ID;
  }

  /**
   * Test the value.
   * @param value Value to test
   * @return true if the value must be selected
   */
  public boolean testValueofStringField(final String value) {

    if (value == null)
      return this.selectNull;

    return containtsIdentifier(value);
  }

}
