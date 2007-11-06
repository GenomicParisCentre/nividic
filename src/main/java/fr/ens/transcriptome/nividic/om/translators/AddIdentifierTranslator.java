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

/**
 * This class define a translator that add the identifier to translations.
 * @author Laurent Jourdren
 */
public class AddIdentifierTranslator extends BasicTranslator implements
    Serializable {

  static final long serialVersionUID = -6780143822648820975L;

  private static final String DEFAULT_FIELD = "OrignalId";

  private String[] fields;
  private String newFieldName = DEFAULT_FIELD;

  private Translator translator;

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

    if (newFieldName.equals(field))
      return id;

    return this.translator.translateField(id, field);
  }

  /**
   * Test if the link information is available for the field
   * @param field Field to test
   * @return true if link information is available
   */
  public boolean isLinkInfo(final String field) {

    return this.translator.isLinkInfo(field);
  }

  /**
   * Get link information.
   * @param translatedId Translated id
   * @param field field of the id
   * @return a link for the translated id
   */
  public String getLinkInfo(final String translatedId, final String field) {

    return this.translator.getLinkInfo(translatedId, field);
  }

  //
  // Other methods
  //

  /**
   * Update the fields from the input translator.
   */
  public void updateFields() {

    String[] tFields = this.translator.getFields();

    if (tFields == null)
      this.fields = new String[] {newFieldName};
    else {

      this.fields = new String[tFields.length + 1];
      this.fields[0] = newFieldName;
      System.arraycopy(tFields, 0, this.fields, 1, tFields.length);
    }

  }

  /**
   * Set the name of the new field of the translator.
   * @param newFieldName the name of new field
   */
  public void setNewFieldName(final String newFieldName) {

    if (newFieldName == null)
      this.newFieldName = DEFAULT_FIELD;

    else
      this.newFieldName = newFieldName;
  }

  /**
   * Get the available identfiers by the translator if possible.
   * @return a array of string with the identifiers
   */
  public String[] getIds() {

    return this.translator.getIds();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param translator Translator to use
   */
  public AddIdentifierTranslator(final Translator translator) {

    this(translator, null);
  }

  /**
   * Public constructor.
   * @param translator Translator to use
   * @param newFieldName the name of new field
   */
  public AddIdentifierTranslator(final Translator translator,
      final String newFieldName) {

    if (translator == null)
      throw new NullPointerException("Translator can't be null");

    this.translator = translator;

    setNewFieldName(newFieldName);
    updateFields();
  }

}
