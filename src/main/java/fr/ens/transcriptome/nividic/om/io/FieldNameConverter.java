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

package fr.ens.transcriptome.nividic.om.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Convert field name from BioAssay to another represation and from the other
 * representation to BioAssay.
 * @author Laurent Jourdren
 */
public class FieldNameConverter {

  // private BidiMap table = new TreeBidiMap();
  private Map<String, String> table = new HashMap<String, String>();
  private Map<String, String> reverseTable = new HashMap<String, String>();

  /**
   * Add an entry.
   * @param bioAssayFieldName BioAssay field name
   * @param otherFieldName Other name for the field
   */
  public void add(final String bioAssayFieldName, final String otherFieldName) {
    this.table.put(bioAssayFieldName, otherFieldName);
    this.reverseTable.put(otherFieldName, bioAssayFieldName);
  }

  /**
   * Remove the bioAssay field name.
   * @param bioAssayName Field name to remove
   */
  public void remove(final String bioAssayName) {
    this.table.remove(bioAssayName);
  }

  /**
   * clear entries.
   */
  public void clear() {
    this.table.clear();
  }

  /**
   * Get the other name of the bioAssay field name.
   * @param bioAssayFieldName BioAssay field name
   * @return the other name of the bioAssay fieldname
   */
  public String getOtherFieldName(final String bioAssayFieldName) {

    String result = (String) this.table.get(bioAssayFieldName);

    if (result == null)
      return bioAssayFieldName;

    return result;
  }

  /**
   * Get the bioAssay field name from the other name.
   * @param otherFieldName The other name of the field
   * @return The bioAssay name of the field
   */
  public String getBioAssayFieldName(final String otherFieldName) {

    String result = this.reverseTable.get(otherFieldName);

    if (result == null)
      return otherFieldName;

    return result;
  }

}