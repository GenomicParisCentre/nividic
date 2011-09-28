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

package fr.ens.transcriptome.nividic.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Discribe a version of a software.
 * @author Laurent Jourdren
 */
public final class Version implements Comparable {

  private static final int HASHCODE_ODD_NUMBER_1 = 912204;
  private static final int HASHCODE_ODD_NUMBER_2 = 1524;

  private int major;
  private int minor;
  private int revision;

  //
  // Getters
  //

  /**
   * Get the number of major version of the Version.
   * @return The major version
   */
  public int getMajor() {
    return major;
  }

  /**
   * Get the number of minor version of the Version.
   * @return The minor version
   */
  public int getMinor() {
    return minor;
  }

  /**
   * Get the number of revision of the Version
   * @return the number of revsion of the version
   */
  public int getRevision() {
    return revision;
  }

  //
  // Setters
  //

  /**
   * Set the number of major version of the Version.
   * @param major The major version of the version
   */
  public void setMajor(final int major) {
    if (major >= 0)
      this.major = major;
    else
      this.major = 0;
  }

  /**
   * Set the number of minor version of the Version.
   * @param minor The minor version of the version
   */
  public void setMinor(final int minor) {
    if (minor >= 0)
      this.minor = minor;
    else
      this.minor = 0;
  }

  /**
   * Set the number of revision of the Version
   * @param revision The number of revision of the version
   */
  public void setRevision(final int revision) {
    if (revision >= 0)
      this.revision = revision;
    else
      this.revision = 0;
  }

  //
  // Other methods
  //

  /**
   * Get the version in a string format.
   * @return The version in a strinf format
   */
  public String toString() {
    return getMajor() + "." + getMinor() + "." + getRevision();
  }

  /**
   * Set the version.
   * @param major The major version of the version
   * @param minor The minor version of the version
   * @param revision The number of revision of the version
   */
  public void setVersion(final int major, final int minor, final int revision) {
    setMajor(major);
    setMinor(minor);
    setRevision(revision);
  }

  /**
   * Set the version.
   * @param version The version to set
   */
  public void setVersion(final String version) {
    if (version == null)
      return;
    if (version.matches("^\\s*\\d+\\s*\\.\\s*\\d+\\s*\\.\\s*\\d+\\s*$")) {
      String[] ch = version.split("\\.");
      setVersion(Integer.parseInt(ch[0].trim()),
          Integer.parseInt(ch[1].trim()), Integer.parseInt(ch[2].trim()));
    }
  }

  /**
   * Compare 2 Version object.
   * @param version Version to compare.
   * @return a negative integer, zero, or a positive integer as this object is
   *         less than, equal to, or greater than the specified object.
   */
  public int compareTo(final Object version) {

    if (version == null)
      return 1;

    final Version v = (Version) version;

    final int compMajor = Integer.valueOf(getMajor()).compareTo(v.getMajor());
    if (compMajor != 0)
      return compMajor;

    final int compMinor = Integer.valueOf(getMinor()).compareTo(v.getMinor());
    if (compMinor != 0)
      return compMinor;

    return Integer.valueOf(getRevision()).compareTo(v.getRevision());
  }

  /**
   * Test if two version object are equals.
   * @param o Object version to test
   * @return true if the two version are equals
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof Version))
      return false;

    Version v = (Version) o;

    return new EqualsBuilder().append(major, v.major).append(minor, v.minor)
        .append(revision, v.revision).isEquals();
  }

  /**
   * Returns a hash code value for the list.
   * @return a hash code value for this list
   */
  public int hashCode() {

    HashCodeBuilder hcb =
        new HashCodeBuilder(HASHCODE_ODD_NUMBER_1, HASHCODE_ODD_NUMBER_2);

    hcb.append(major).append(minor).append(revision);

    return hcb.toHashCode();
  }

  /**
   * Get the minimal version from an array of versions.
   * @param versions The array of versions
   * @return The minimal version
   */
  public static Version getMinimalVersion(final Version[] versions) {

    if (versions == null || versions.length == 0)
      return null;

    Version min = versions[0];

    for (int i = 1; i < versions.length; i++)
      if (min.compareTo(versions[i]) > 0)
        min = versions[i];

    return min;
  }

  /**
   * Get the maximal version from an array of versions.
   * @param versions The array of versions
   * @return The maximal version
   */
  public static Version getMaximalVersion(final Version[] versions) {

    if (versions == null || versions.length == 0)
      return null;

    Version max = versions[0];

    for (int i = 1; i < versions.length; i++)
      if (max.compareTo(versions[i]) < 0)
        max = versions[i];

    return max;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public Version() {
  }

  /**
   * Public constructor.
   * @param major The major version of the version
   * @param minor The minor version of the version
   * @param revision The number of revision of the version
   */
  public Version(final int major, final int minor, final int revision) {
    setVersion(major, minor, revision);
  }

  /**
   * Public constructor.
   * @param version The version to set
   */
  public Version(final String version) {
    setVersion(version);
  }

}