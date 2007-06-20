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

package fr.ens.transcriptome.nividic.sgdb;

import fr.ens.transcriptome.nividic.om.translators.BasicTranslator;

public class SGDBDescriptionTranslator extends BasicTranslator {

  private static final String[] FIELDS = {"Probe", "ORF", "Description"};

  //
  // Method for the Translator
  //

  /**
   * Get an ordered list of the annotations fields
   * @return an ordered list of the annotations fields.
   */
  public String[] getFields() {

    return FIELDS;
  }

  /**
   * Get all the annotation for an feature
   * @param id Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  public String[] translate(final String id) {

    if (id == null)
      return null;

    final String[] values = id.trim().split("\\|");

    final String[] result = new String[FIELDS.length];

    for (int i = 0; i < result.length; i++)
      result[i] = values[i] == null ? null : values[i].trim();

    return result;
  }

  private int getIndexField(final String field) {

    if (field == null)
      return -1;

    String[] fields = getFields();

    if (fields == null)
      return -1;

    for (int i = 0; i < fields.length; i++)
      if (field.equals(fields[i]))
        return i;

    return -1;
  }

  /**
   * Get an annotation for an feature
   * @param id Identifier of the feature
   * @param fieldName Field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id, final String fieldName) {

    final String field;

    if (fieldName == null)
      field = getDefaultField();
    else
      field = fieldName;

    if (field == null)
      return null;

    final int fieldIndex = getIndexField(field);

    if (fieldIndex == -1)
      return null;

    if (id == null)
      return null;

    final String[] values = id.trim().split("\\|");

    if (values == null || values.length <= fieldIndex)
      return null;

    return values[fieldIndex].trim();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param bioAssay BioAssay used for the annotation
   */
  public SGDBDescriptionTranslator() {
    setDefaultField(FIELDS[1]);
  }

}
