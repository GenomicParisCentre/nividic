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

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class define a filter for double with a threshold and a comparator.
 * @author Laurent Jourdren
 */
public class BioAssayDoubleThresholdFilter extends
    BioAssayGenericDoubleFieldFilter {

  private String field = BioAssay.FIELD_NAME_M;
  private static final Comparator defaultComparator = Comparator.UPPER;
  private double threshold;
  private boolean absolute;

  private Comparator comparator = Comparator.UPPER;

  private enum Comparator {
    LOWER, LOWEREQUALS, EQUALS, NOTEQUALS, UPPEREQUALS, UPPER;

    public String toString() {

      switch (this) {
      case LOWER:
        return "<";
      case LOWEREQUALS:
        return "<=";
      case EQUALS:
        return "=";
      case NOTEQUALS:
        return "!=";
      case UPPEREQUALS:
        return ">=";
      case UPPER:
        return ">";

      default:
        return "";
      }

    }

    public static Comparator getComparator(final String comparatorString) {

      if (comparatorString == null)
        return defaultComparator;

      String s = comparatorString.trim();

      if (s.equals("<"))
        return LOWER;
      if (s.equals("<="))
        return LOWEREQUALS;
      if (s.equals("=") || s.equals("=="))
        return EQUALS;
      if (s.equals("!="))
        return NOTEQUALS;
      if (s.equals(">="))
        return UPPEREQUALS;
      if (s.equals(">"))
        return UPPER;

      return defaultComparator;
    }

  };

  //
  // Getter
  //

  /**
   * Define the field to filter.
   * @return the field to filter
   */
  public String getFieldToFilter() {

    return field;
  }

  /**
   * Get the comparator.
   * @return The comparator
   */
  public String getComparator() {

    return comparator.toString();
  }

  /**
   * Get the threshold.
   * @return The threshold
   */
  public double getThreshold() {

    return this.threshold;
  }

  /**
   * test if the value to test is absolute.
   * @return true if the value to test is absolute
   */
  public boolean isAbsolute() {

    return this.absolute;
  }

  //
  // Setter
  //

  /**
   * Set the field to filter.
   * @param field Field to filter
   */
  public void setFieldToFilter(final String field) {

    this.field = field;
  }

  /**
   * Set the comparator.
   * @param comparator Comparator to set
   */
  public void setComparator(final String comparator) {

    this.comparator = Comparator.getComparator(comparator);
  }

  /**
   * Set the threshold.
   * @param threshold Threshold to set
   */
  public void setThreshold(final double threshold) {

    this.threshold = threshold;
  }

  /**
   * Set if the value to test is absolute.
   * @param absolute true to enable absolute values
   */
  public void setAbsolute(final boolean absolute) {

    this.absolute = absolute;
  }

  //
  // Other methods
  //

  /**
   * Test the value.
   * @param value Value to test
   * @return true if the test if positive
   */
  @Override
  public boolean test(final double value) {

    final double threshold = this.threshold;

    final double v = this.absolute ? Math.abs(value) : value;

    if (Double.isNaN(threshold))
      return false;

    switch (this.comparator) {
    case LOWER:
      return v < threshold;
    case LOWEREQUALS:
      return v <= threshold;
    case EQUALS:
      return v == threshold;
    case NOTEQUALS:
      return v != threshold;
    case UPPEREQUALS:
      return v >= threshold;
    case UPPER:
      return v > threshold;

    default:
      return false;
    }
  }

  /**
   * Get parameter filter information for the history
   * @return a String with information about the parameter of the filter
   */
  public String getParameterInfo() {

    return "Field="
        + getFieldToFilter() + ";Comparator=" + getComparator() + ";Threshold="
        + getThreshold();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param field Field to test
   * @param threshold Threshold of the test
   * @param comparator comparator to use
   */
  public BioAssayDoubleThresholdFilter(final String field,
      final String comparator, final double threshold) {

    this(field, comparator, threshold, false);
  }

  /**
   * Public constructor.
   * @param field Field to test
   * @param threshold Threshold of the test
   * @param comparator comparator to use
   * @param absolute true if the threshold is absolute value
   */
  public BioAssayDoubleThresholdFilter(final String field,
      final String comparator, final double threshold, final boolean absolute) {

    setFieldToFilter(field);
    setThreshold(threshold);
    setComparator(comparator);
    setAbsolute(absolute);
  }

}
