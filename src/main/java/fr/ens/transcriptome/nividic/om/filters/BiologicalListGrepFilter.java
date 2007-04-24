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

import java.util.regex.Pattern;

/**
 * This class define a grep equivalent for biological lists.
 * @author Laurent Jourdren
 */
public class BiologicalListGrepFilter extends BiologicalListTestFilter {

  private Pattern pattern;

  /**
   * Test if a member of the list must be filtered
   * @param memberId member of the list to test
   * @return true if the member must be filtered
   */
  public boolean test(final String memberId) {

    if (memberId == null)
      return false;

    return pattern.matcher(memberId).matches();
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "Pattern=" + pattern.pattern();
  }

  //
  // Constructor
  //

  /**
   * Constructor with a regex in string.
   * @param regex Regex of the filter
   */
  public BiologicalListGrepFilter(final String regex) {

    this(Pattern.compile(regex));

  }

  /**
   * Constructor with a regex in a pattern object.
   * @param pattern Compiled regex
   */
  public BiologicalListGrepFilter(final Pattern pattern) {

    if (pattern == null)
      throw new NullPointerException("Patter is null");

    this.pattern = pattern;
  }

}
