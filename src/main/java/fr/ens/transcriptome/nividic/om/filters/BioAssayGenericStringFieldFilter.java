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

package fr.ens.transcriptome.nividic.om.filters;

import java.util.ArrayList;
import java.util.Arrays;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This class implements a generic filter for filtering using the value of a
 * string field.
 * @author Laurent Jourdren
 */
public abstract class BioAssayGenericStringFieldFilter implements
    BioAssayFilter {

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

  /**
   * Filter a bioAssay object using value of the string field.
   * @param bioAssay BioAssay to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  public BioAssay filter(final BioAssay bioAssay)
      throws BioAssayRuntimeException {

    if (bioAssay == null)
      return null;

    String[] data = bioAssay.getDataFieldString(getFieldToFilter());

    int size = bioAssay.size();
    ArrayList al = new ArrayList();

    final Translator translator = getTranslator();
    final boolean translate = translator != null;
    String translatorField = getTranslatorField();

    for (int i = 0; i < size; i++) {

      final String id = translate ? translator.translateField(data[i],
          translatorField) : data[i];

      if (!testValueofStringField(id))
        al.add(new Integer(i));

    }

    int[] toRemove = new int[al.size()];
    for (int i = 0; i < toRemove.length; i++)
      toRemove[i] = ((Integer) al.get(i)).intValue();

    if (removeFoundId())
      return BioAssayUtils.removeRowsFromBioAssay(bioAssay, inverseIntArray(
          toRemove, size));

    return BioAssayUtils.removeRowsFromBioAssay(bioAssay, toRemove);
  }

  private static int[] inverseIntArray(final int[] pos, final int len) {

    int[] result = new int[len - pos.length];
    Arrays.sort(pos);

    int j = 0;
    int k = 0;
    for (int i = 0; i < len; i++) {
      if (i == pos[j])
        j++;
      else
        result[k++] = i;
    }

    return result;
  }

  /**
   * Get the field to filer
   * @return The field to filer
   */
  public abstract String getFieldToFilter();

  /**
   * Test the value.
   * @param value Value to test
   * @return true if the value must be selected
   */
  public abstract boolean testValueofStringField(final String value);

  /**
   * Test if found identifiers must be removed.
   * @return true if found identifiers must be removed
   */
  public abstract boolean removeFoundId();

}