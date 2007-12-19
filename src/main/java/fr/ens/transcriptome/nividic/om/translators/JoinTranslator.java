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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class define a translator that concat two translator. To use the field
 * of the second translator, id must be translated with the first translator.
 * translator and a array of identifers.
 * @author Laurent Jourdren
 */
public class JoinTranslator extends BasicTranslator implements Serializable {

  private Translator translator1;
  private Translator translator2;
  private String joinField;
  private String[] fields;
  private Map<String, Translator> mapTranslator =
      new HashMap<String, Translator>();
  private boolean returnTranslation1IfNoTranslation;

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

    final Translator t = this.mapTranslator.get(field);

    if (t == null)
      return null;
    if (t == translator1)
      return this.translator1.translateField(id, field);

    final String result1 = this.translator1.translateField(id, this.joinField);

    final String result = this.translator2.translateField(result1, field);

    if (result == null && this.returnTranslation1IfNoTranslation)
      return result1;

    return result;
  }

  /**
   * Test if the link information is available for the field
   * @param field Field to test
   * @return true if link information is available
   */
  public boolean isLinkInfo(final String field) {

    final Translator t = this.mapTranslator.get(field);

    if (t == null)
      return false;
    if (t == translator1)
      return this.translator1.isLinkInfo(field);

    return this.translator2.isLinkInfo(field);
  }

  /**
   * Get link information.
   * @param translatedId Translated id
   * @param field field of the id
   * @return a link for the translated id
   */
  public String getLinkInfo(final String translatedId, final String field) {

    System.out.println(this.getClass().getName()
        + "\ttranslateId=" + translatedId + "\tfield=" + field);

    final Translator t = this.mapTranslator.get(field);

    if (t == null)
      return null;

    if (t == translator1)
      return this.translator1.getLinkInfo(translatedId, field);

    return this.translator2.getLinkInfo(translatedId, field);
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param translator1 First translator
   * @param translator2 Second translator
   * @param joinField The field of the join
   */
  public JoinTranslator(final Translator translator1, final String joinField,
      final Translator translator2) {

    this(translator1, joinField, translator2, false);
  }

  /**
   * Public constructor.
   * @param translator1 First translator
   * @param translator2 Second translator
   * @param joinField The field of the join
   * @param returnTranslation1IfNoTranslation true if the result must the result
   *            of translator1 if there is no result for translator2
   */
  public JoinTranslator(final Translator translator1, final String joinField,
      final Translator translator2,
      final boolean returnTranslation1IfNoTranslation) {

    if (translator1 == null)
      throw new NullPointerException("Translator1  can't be null");
    if (translator2 == null)
      throw new NullPointerException("Translator1  can't be null");
    if (joinField == null)
      throw new NullPointerException("Join field  can't be null");
    if (!translator1.isField(joinField))
      throw new NullPointerException("The join field isn't in translator 1");

    this.translator1 = translator1;
    this.translator2 = translator2;
    this.returnTranslation1IfNoTranslation = returnTranslation1IfNoTranslation;

    final ArrayList<String> fieldList = new ArrayList<String>();

    for (final String f : translator1.getFields()) {
      this.mapTranslator.put(f, translator1);
      fieldList.add(f);
    }

    for (final String f : translator2.getFields())
      if (!this.mapTranslator.containsKey(f)) {
        this.mapTranslator.put(f, translator2);
        fieldList.add(f);
      }

    this.fields = NividicUtils.toArray(fieldList);

  }

}
