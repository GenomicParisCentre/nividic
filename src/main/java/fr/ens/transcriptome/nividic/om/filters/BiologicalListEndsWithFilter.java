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
 * Define a filter based on the end of the names of the member of a list.
 * @author Laurent Jourdren
 */
public class BiologicalListEndsWithFilter extends BiologicalListTestFilter {

  private String suffix;
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

    return s.endsWith(this.suffix);
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param suffix Prefix to set
   */
  public BiologicalListEndsWithFilter(final String suffix) {

    this(suffix, false);
  }

  /**
   * Public constructor.
   * @param suffix Prefix to set
   * @param caseInsensible true if the match must be case insensible
   */
  public BiologicalListEndsWithFilter(final String suffix,
      final boolean caseInsensible) {

    if (suffix == null)
      throw new NullPointerException("The suffix is null");

    this.suffix = caseInsensible ? suffix.toLowerCase() : suffix;
    this.caseInsensible = caseInsensible;

  }

}
