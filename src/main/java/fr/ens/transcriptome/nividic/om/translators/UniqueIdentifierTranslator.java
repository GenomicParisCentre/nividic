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

import java.util.HashMap;
import java.util.Map;

/**
 * This class define a translator with create unique identifer from another
 * translator and a array of identifers.
 * @author Laurent Jourdren
 */
public class UniqueIdentifierTranslator extends BasicTranslator {

  private static final String DEFAULT_FIELD = "UniqueId";

  private Map<String, String> mapUniqueId = new HashMap<String, String>();
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

    final String translatedId = this.mapUniqueId.get(id);

    if (newFieldName.equals(field))
      return translatedId;

    return this.translator.translateField(translatedId, field);
  }

  //
  // Other methods
  //

  private void translateIds(final String[] ids, final String field) {

    final Translator translator = this.translator;

    Map<String, Integer> translationCount = new HashMap<String, Integer>();

    final String fieldName;

    if (field == null)
      fieldName = translator.getDefaultField();
    else
      fieldName = field;

    for (int i = 0; i < ids.length; i++) {

      final String row = ids[i];
      String t = translator.translateField(row, fieldName);

      if (t == null || "".equals(t))
        t = row;

      if (translationCount.containsKey(t)) {
        int count = translationCount.get(t);
        translationCount.put(t, ++count);
        // System.out.println(t + "#" + count + "\t" + row);
        mapUniqueId.put(t + "#" + count, row);
      } else {
        translationCount.put(t, 1);
        // System.out.println(t + "\t" + row);
        mapUniqueId.put(t, row);
      }

    }

  }

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

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param ids Identifier to set unique
   * @param translator Translator to use
   */
  public UniqueIdentifierTranslator(final String[] ids,
      final Translator translator) {

    this(ids, translator, translator != null
        ? translator.getDefaultField() : null, null);
  }

  /**
   * Public constructor.
   * @param ids Identifier to set unique
   * @param translator Translator to use
   * @param translatorField field of the translator to use
   */
  public UniqueIdentifierTranslator(final String[] ids,
      final Translator translator, final String translatorField) {

    this(ids, translator, translatorField, null);
  }

  /**
   * Public constructor.
   * @param ids Identifier to set unique
   * @param translator Translator to use
   * @param translatorField field of the translator to use
   * @param newFieldName the name of new field
   */
  public UniqueIdentifierTranslator(final String[] ids,
      final Translator translator, final String translatorField,
      final String newFieldName) {

    if (ids == null)
      throw new NullPointerException("Identifiers can't be null");

    if (translator == null)
      throw new NullPointerException("Translator can't be null");

    this.translator = translator;

    translateIds(ids.clone(), translatorField);
    setNewFieldName(newFieldName);
    updateFields();
  }

}
