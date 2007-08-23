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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.translators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.SpotIterator;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.Slide;

/**
 * This class implement a feature annotation for BioAssay
 * @author Laurent Jourdren
 */
public class DescriptionBioAssayTranslator extends BasicTranslator implements
    Serializable {

  private static final String DESCRIPTION_FIELD =
      BioAssay.FIELD_NAME_DESCRIPTION;
  private Map<String, String> annotations = new HashMap<String, String>();

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
      this.annotations.put(si.getId(), si.getDescription());
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

  /**
   * Set the bioAssay from the design..
   * @param design Design to add
   */
  public void addDesign(final Design design) {

    if (design == null)
      return;

    List<Slide> slides = design.getSlides();

    for (Slide s : slides)
      addBioAssay(s.getBioAssay());

  }

  //
  // Method for the Translator
  //

  /**
   * Get an ordered list of the annotations fields
   * @return an ordered list of the annotations fields.
   */
  public String[] getFields() {

    return new String[] {DESCRIPTION_FIELD};
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

    final String[] result = new String[1];
    result[0] = desc;
    return result;
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

    return getDescription(id);
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

    return this.annotations.get(id);
  }

  /**
   * Clear the descriptions of the features.
   */
  public void clear() {

    this.annotations.clear();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public DescriptionBioAssayTranslator() {

    setDefaultField(DESCRIPTION_FIELD);
  }

  /**
   * Public constructor.
   * @param bioAssay BioAssay used for the annotation
   */
  public DescriptionBioAssayTranslator(final BioAssay bioAssay) {

    this();
    addBioAssay(bioAssay);
  }

  /**
   * Public constructor.
   * @param bioAssays BioAssays used for the annotation
   */
  public DescriptionBioAssayTranslator(final BioAssay[] bioAssays) {

    this();
    addBioAssay(bioAssays);
  }

  /**
   * Public constructor.
   * @param design Design used for the annotation
   */
  public DescriptionBioAssayTranslator(final Design design) {

    this();
    addDesign(design);
  }

}
