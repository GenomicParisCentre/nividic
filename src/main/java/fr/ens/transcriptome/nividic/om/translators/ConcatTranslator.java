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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class define a translator composed of several translators.
 * @author Laurent Jourdren
 */
public class ConcatTranslator extends BasicTranslator implements Serializable {

  private Map<String, Translator> translators =
      new LinkedHashMap<String, Translator>();

  /**
   * Get an ordered list of the translator fields
   * @return an ordered list of the translator fields.
   */
  public String[] getFields() {

    return NividicUtils.toArray(translators.keySet());
  }

  /**
   * Get a translation for a feature
   * @param id Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id, final String field) {

    Translator t = this.translators.get(field);

    if (t == null)
      return null;

    return t.translateField(id, field);
  }

  /**
   * Test if the link information is available for the field
   * @param field Field to test
   * @return true if link information is available
   */
  public boolean isLinkInfo(final String field) {

    Translator t = this.translators.get(field);

    if (t == null)
      return false;

    return t.isLinkInfo(field);
  }

  /**
   * Get link information.
   * @param translatedId Translated id
   * @param field field of the id
   * @return a link for the translated id
   */
  public String getLinkInfo(final String translatedId, final String field) {

    Translator t = this.translators.get(field);

    if (t == null)
      return null;

    return t.getLinkInfo(translatedId, field);
  }

  /**
   * Add a translator to the concat translator.
   * @param translator Translator to add
   */
  public void addTranslator(final Translator translator) {

    if (translator == null)
      return;

    final String[] fields = translator.getFields();

    if (fields == null)
      return;

    for (int i = 0; i < fields.length; i++) {

      final String key = fields[i];

      if (!this.translators.containsKey(key))
        this.translators.put(key, translator);
    }
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public ConcatTranslator() {
  }

  /**
   * Public constructor.
   * @param translator1 first translator to add
   * @param translator2 first translator to add
   */
  public ConcatTranslator(final Translator translator1,
      final Translator translator2) {

    addTranslator(translator1);
    addTranslator(translator2);
  }

}
