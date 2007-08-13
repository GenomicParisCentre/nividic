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
 * This translator is only for tests.
 * @author Laurent Jourdren
 */
public class DummyTranslator extends BasicTranslator {

  private int fieldCount;

  public String[] getFields() {

    final String array[] = new String[this.fieldCount];

    for (int i = 0; i < array.length; i++)
      array[i] = "field#" + i;

    return array;
  }

  public String translateField(String id, String field) {

    if (id == null || field == null)
      return null;

    return id + "_" + field;
  }

  //
  // Translator
  //
  
  public DummyTranslator(final int fieldCount) {
    
    this.fieldCount = fieldCount;
  }
  
}
