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

import java.util.HashMap;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.SpotIterator;
import fr.ens.transcriptome.nividic.om.translators.BasicTranslator;

public class SGDBYeastBioAssayTranslator extends BasicTranslator {

  private static final String[] FIELDS = {"Probe", "ORF", "Protein", "Ontology"};
  private HashMap annotations = new HashMap();

  /**
   * Set the bioAssay.
   * @param bioAssay BioAssay to add
   */
  public void addBioAssay(final BioAssay bioAssay) {
    if (bioAssay == null)
      return;

    SpotIterator si = bioAssay.iterator();

    while (si.hasNext()) {
      si.next();

      final String id = si.getId();
      final String description = si.getDescription();

      this.annotations.put(id == null ? null : id.trim(),
          description == null ? null : description.trim());
    }
  }

  /**
   * Set the bioAssay.
   * @param bioAssays BioAssays to add
   */
  public void addBioAssay(final BioAssay[] bioAssays) {
    if (bioAssays == null)
      return;

    for (int i = 0; i < bioAssays.length; i++) {
      addBioAssay(bioAssays[i]);
    }
  }

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

    final String desc = getDescription(id);
    if (desc == null)
      return null;

    final String[] values = desc.split("\\|");

    final String[] result = new String[FIELDS.length];

    for (int i = 0; i < values.length; i++)
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

    final String description = getDescription(id);

    if (description == null)
      return null;

    final String[] values = description.split("\\|");

    if (values == null || values.length <= fieldIndex)
      return null;

    return values[fieldIndex].trim();
  }

  //
  // Other methods
  //

  /**
   * Get the description of a feature of the bioAssay.
   * @param id Identifier of the feature
   */
  private String getDescription(final String id) {

    if (id == null)
      return null;

    return (String) this.annotations.get(id.trim());
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param bioAssay BioAssay used for the annotation
   */
  public SGDBYeastBioAssayTranslator(final BioAssay bioAssay) {
    setDefaultField(FIELDS[1]);
    addBioAssay(bioAssay);
  }

  /**
   * Public constructor.
   * @param bioAssays BioAssays used for the annotation
   */
  public SGDBYeastBioAssayTranslator(final BioAssay[] bioAssays) {
    setDefaultField(FIELDS[1]);
    addBioAssay(bioAssays);
  }

}
