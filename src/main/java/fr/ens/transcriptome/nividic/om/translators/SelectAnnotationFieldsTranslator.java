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

package fr.ens.transcriptome.nividic.om.translators;

/**
 * This class define a translator with only few of field of another translator.
 * @author Laurent Jourdren
 */
public class SelectAnnotationFieldsTranslator extends BasicTranslator {

  private Translator translator;
  private String[] fields;

  /**
   * Get an ordered list of the translator fields
   * @return an ordered list of the translator fields.
   */
  public String[] getFields() {

    return this.fields;
  }

  /**
   * Get a translation for a feature
   * @param id Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id, final String field) {

    return this.translator.translateField(id, field);
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param fields fields to set for the translator
   */
  public SelectAnnotationFieldsTranslator(final String[] fields) {

    if (fields == null)
      throw new NullPointerException("Fields can't be null");

    this.fields = fields;
  }

}
