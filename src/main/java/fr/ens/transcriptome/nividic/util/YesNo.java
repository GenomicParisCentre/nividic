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

package fr.ens.transcriptome.nividic.util;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This class defines a yes/no object based on boolean type.
 * @author Laurent Jourdren
 */
public class YesNo {

  private static final int HASHCODE_ODD_NUMBER_1 = 4032205;
  private static final int HASHCODE_ODD_NUMBER_2 = 1122;

  /** String representation of YES_STRING value. */
  public static final String YES_STRING = "yes";

  /** String representation of NO_STRING value. */
  public static final String NO_STRING = "no";

  /** The yes value. */
  public static final YesNo YES = new YesNo(true);

  /** The no value. */
  public static final YesNo NO = new YesNo(false);

  private boolean value;

  //
  // Getters
  //

  /**
   * Test if the value of this object is yes. Return true if the value of this
   * object is yes
   * @return true if the value of the object is true
   */
  public boolean isYes() {
    return value;
  }

  /**
   * Test if the value of this object is yes. Return true if the value of this
   * object is yes
   * @return true if the value of the object is true
   */
  public boolean isNo() {
    return !value;
  }

  //
  // Setters
  //

  /**
   * Set the value of the object with a string value.
   * @param value String representation of the value to set
   */
  public void setValue(final String value) {

    if (value == null)
      throw new NullPointerException();

    if (value.toLowerCase().trim().equals(YES_STRING))
      setValue(true);
    else if (value.toLowerCase().trim().equals(NO_STRING))
      setValue(false);
    else
      throw new NumberFormatException(value);

  }

  /**
   * Set the value of the object with a boolean value.
   * @param value boolean representation of the value to set
   */
  public void setValue(final boolean value) {
    this.value = value;
  }

  //
  // Other methods
  //

  /**
   * Convert this object in a boolean value.
   * @return the boolean value of this object
   */
  public boolean toBoolean() {
    return value;
  }

  /**
   * Test if two YesNo object are equals.
   * @param o Object to test
   * @return true
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof YesNo))
      return false;

    return this.value == ((YesNo) o).toBoolean();
  }

  /**
   * Returns a hash code value for the list.
   * @return a hash code value for this list
   */
  public int hashCode() {

    HashCodeBuilder hcb = new HashCodeBuilder(HASHCODE_ODD_NUMBER_1,
        HASHCODE_ODD_NUMBER_2);

    hcb.append(this.value);

    return hcb.toHashCode();
  }

  /**
   * Return a string representation of the object.
   * @return a string representation of the object
   */
  public String toString() {
    if (this.value)
      return YES_STRING;

    return NO_STRING;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public YesNo() {
  }

  /**
   * Public constructor
   * @param value Value to set
   */
  public YesNo(final String value) {
    setValue(value);
  }

  /**
   * Public constructor
   * @param value Value to set
   */
  public YesNo(final boolean value) {
    setValue(value);
  }
}