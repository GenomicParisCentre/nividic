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

package fr.ens.transcriptome.nividic.js;

/**
 * Utility methods for javascript.
 * @author Laurent Jourdren
 */
public final class Util {

  /**
   * Split a String object. There is a bug with some regex in javascript. This
   * method fix it.
   * @param s String to split
   * @param regex Regex
   * @return an array of String
   */
  public static String[] split(final String s, final String regex) {

    if (s == null)
      return null;

    return s.split(regex);
  }

  //
  // Constructor
  //

  private Util() {
  }

}
