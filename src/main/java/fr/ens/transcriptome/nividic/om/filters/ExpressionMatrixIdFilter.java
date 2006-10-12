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

package fr.ens.transcriptome.nividic.om.filters;

import java.util.ArrayList;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This class implements an abstract class to filter expressionMatrix based on
 * the rows identifiers.
 * @author Laurent Jourdren
 */
public abstract class ExpressionMatrixIdFilter implements
    ExpressionMatrixFilter {

  private Translator translator;
  private String translatorField;

  //
  // Getters
  //

  /**
   * Get the translator.
   * @return The translator of the filter
   */
  public Translator getTranslator() {

    return this.translator;
  }

  /**
   * Get the field of the translator to use during the translation.
   * @return a translator field
   */
  public String getTranslatorField() {

    return this.translatorField;
  }

  //
  // Setters
  //

  /**
   * Set the translator of the filter.
   * @param translator The transloator to set
   */
  public void setTranslator(final Translator translator) {

    this.translator = translator;
  }

  /**
   * Set the field of the translator to use during the translation.
   * @param translatorField the translator field. If null is set, the default
   *          translator field will be used
   */
  public void setTranslatorField(final String translatorField) {

    this.translatorField = translatorField;
  }

  //
  // Other methods
  //

  /**
   * Filter a ExpressionMatrixDimension object.
   * @param em ExpressionMatrixDimension to filter
   * @return A new filtered ExpressionMatrixDimension object
   * @throws ExpressionMatrixRuntimeException if an error occurs while filtering
   *           data
   */
  public ExpressionMatrix filter(final ExpressionMatrix em)
      throws ExpressionMatrixRuntimeException {

    if (em == null)
      return null;

    final String[] rowIds = em.getRowIds();

    final int size = rowIds.length;

    ArrayList al = new ArrayList();

    final Translator translator = getTranslator();
    final boolean translate = translator != null;
    String translatorField = getTranslatorField();

    if (translate && translatorField == null)
      translatorField = translator.getDefaultField();

    for (int i = 0; i < size; i++) {

      final String id = rowIds[i];

      if (testId(translate ? translator.translateField(id, translatorField)
          : id))
        al.add(id);
    }

    String[] foundIds = new String[al.size()];
    al.toArray(foundIds);

    if (removeFoundId())
      return em.subMatrixRows(StringUtils.excludeStrings(foundIds, rowIds));

    return em.subMatrixRows(foundIds);
  }

  /**
   * Test the id of a Row.
   * @param id Identifier to test
   * @return true if the values must be selected
   */
  public abstract boolean testId(final String id);

  /**
   * Test if found identifiers must be removed.
   * @return true if found identifiers must be removed
   */
  public abstract boolean removeFoundId();

}
