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

/**
 * Define a filter based on the start of the names of the member of a list.
 * @author Laurent Jourdren
 */
public class BiologicalListStartsWithFilter extends BiologicalListTestFilter {

  private String prefix;
  private boolean caseInsensible;

  /**
   * Test if a member of the list must be filtered
   * @param memberId member of the list to test
   * @return true if the member must be filtered
   */
  public boolean test(final String memberId) {

    if (memberId == null)
      return false;

    String s = caseInsensible ? memberId.toLowerCase() : memberId;

    return s.startsWith(this.prefix);
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "Prefix=" + this.prefix + ";CaseInsensible=" + this.caseInsensible;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param prefix Prefix to set
   */
  public BiologicalListStartsWithFilter(final String prefix) {

    this(prefix, false);
  }

  /**
   * Public constructor.
   * @param prefix Prefix to set
   * @param caseInsensible true if the match must be case insensible
   */
  public BiologicalListStartsWithFilter(final String prefix,
      final boolean caseInsensible) {

    if (prefix == null)
      throw new NullPointerException("The prefix is null");

    this.prefix = caseInsensible ? prefix.toLowerCase() : prefix;
    this.caseInsensible = caseInsensible;

  }

}
