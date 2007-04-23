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
 * This abstract class implements basic methods to get several field or several
 * annotations.
 * @author Laurent Jourdren
 */
public abstract class BasicTranslator implements Translator {

  private String defaultField;

  /**
   * Get the default field.
   * @return default field
   */
  public String getDefaultField() {

    return this.defaultField;
  }

  /**
   * Set the default field.
   * @param field The field to set
   */
  public void setDefaultField(final String field) {

    if (!isField(field))
      throw new RuntimeException("The field doesn't exists");

    this.defaultField = field;
  }

  /**
   * Get a translation for a feature
   * @param id Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  public String[] translate(final String id) {

    String[] fields = getFields();

    if (fields == null)
      return null;

    String[] result = new String[fields.length];

    for (int i = 0; i < fields.length; i++)
      result[i] = translateField(id, fields[i]);

    return result;
  }

  /**
   * Get all the annotations for features
   * @param ids Identifiers of the features
   * @return An array with the annotation of the Feature
   */
  public String[][] translate(final String[] ids) {

    if (ids == null)
      return null;

    String[][] result = new String[ids.length][];

    for (int i = 0; i < ids.length; i++) {
      result[i] = translate(ids[i]);
    }

    return result;
  }

  /**
   * Get translations for features
   * @param ids Identifiers of the features
   * @return An array with the annotation of the Feature
   */
  public String[] translateField(final String[] ids) {

    return translateField(ids, getDefaultField());
  }

  /**
   * Get translations for features
   * @param ids Identifiers of the features
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String[] translateField(final String[] ids, final String field) {

    if (ids == null)
      return null;

    final String lField;
    if (field == null)
      lField = getDefaultField();
    else
      lField = field;

    if (lField == null || !isField(lField))
      return null;

    String[] result = new String[ids.length];

    for (int i = 0; i < ids.length; i++) {
      result[i] = translateField(ids[i], lField);
    }

    return result;
  }

  /**
   * Get a translation for a feature. The field is the default field.
   * @param id Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id) {

    return translateField(id, getDefaultField());
  }

  /**
   * Test if the field exists.
   * @param field Field to test
   * @return true if the field exists
   */
  public boolean isField(final String field) {

    if (field == null)
      return false;

    String[] fields = getFields();

    if (fields == null)
      return false;

    for (int i = 0; i < fields.length; i++)
      if (field.equals(fields[i]))
        return true;

    return false;
  }

}
