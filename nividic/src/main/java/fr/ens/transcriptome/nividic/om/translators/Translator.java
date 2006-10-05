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
 * This interface define how retrieve annotation for a feature.
 * @author Laurent Jourdren
 */
public interface Translator {

  /**
   * Get the default field.
   * @return default field
   */
  String getDefaultField();

  /**
   * Set the default field.
   * @param field The field to set
   */
  void setDefaultField(String field);

  /**
   * Get an ordered list of the translator fields
   * @return an ordered list of the translator fields.
   */
  String[] getFields();

  /**
   * Get all the translation for a feature
   * @param id Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  String[] translate(String id);

  /**
   * Get a translation for a feature
   * @param id Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  String translateField(String id, String field);

  /**
   * Get a translation for a feature. The field is the default field.
   * @param id Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  String translateField(String id);

  /**
   * Get all the translations for features
   * @param ids Identifiers of the features
   * @return An array with the annotation of the Feature
   */
  String[][] translate(String[] ids);

  /**
   * Get translations for features
   * @param ids Identifiers of the features
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  String[] translateField(String[] ids, String field);

  /**
   * Get translations for features. The field is the default field.
   * @param ids Identifiers of the features
   * @return An array with the annotation of the Feature
   */
  String[] translateField(String[] ids);

  /**
   * Test if the field exists.
   * @param field Field to test
   * @return true if the field exists
   */
  boolean isField(String field);

}
