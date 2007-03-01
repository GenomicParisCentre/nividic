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

package fr.ens.transcriptome.nividic.om.impl;

import java.util.Date;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * This class define a slide description.
 * @author Laurent Jourdren
 */
public class SlideDescription {

  private DesignImpl design;
  private int slideId;

  /** Comment field. */
  public static final String COMMENT_FIELD = "Comment";
  /** Date field. */
  public static final String DATE_FIELD = "Date";
  /** Serial Number field. */
  public static final String SERIAL_NUMBER_FIELD = "Serial number";
  /** Operator field. */
  public static final String OPERATOR_FIELD = "Operator";
  /** Swap field. */
  public static final String SWAP_FIELD = "Swap";

  //
  // Getters
  //

  /**
   * Get a description.
   * @param field Field of the description to get
   * @return a String with the description
   */
  public String getDescription(final String field) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getDescription(slideName, field);
  }

  /**
   * Get the comment about the slide.
   * @return Returns the comment
   */
  public String getComment() {

    return getDescription(COMMENT_FIELD);
  }

  /**
   * Get the date of the hybridation
   * @return Returns the date
   */
  public String getDate() {

    return getDescription(DATE_FIELD);
  }

  /**
   * Get the name of the operator
   * @return Returns the operator
   */
  public String getOperator() {

    return getDescription(OPERATOR_FIELD);
  }

  /**
   * Get the serial number of the slide
   * @return Returns the serialNumber
   */
  public String getSerialNumber() {

    return getDescription(SERIAL_NUMBER_FIELD);
  }

  /**
   * Get the swap field of the slide
   * @return true if the value of the field is (case insensitive) "true", "t" or
   *         "1"
   */
  public boolean getSwap() {

    String s = getDescription(SWAP_FIELD);

    if (s == null)
      return false;

    s = s.trim().toLowerCase();

    return s.equals("true") || s.equals("t") || s.equals("1");
  }

  //
  // Setters
  //

  /**
   * Set a field of the description.
   * @param field Field to set
   * @param value value to set
   */
  public void setDescription(final String field, final String value) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setDescription(slideName, field, value);
  }

  /**
   * Set the comment.
   * @param comment The comment to set
   */
  public void setComment(final String comment) {

    setDescription(COMMENT_FIELD, comment);
  }

  /**
   * Set the hybridation date
   * @param date The date to set
   */
  public void setDate(final String date) {

    setDescription(DATE_FIELD, date.toString());
  }

  /**
   * Set the name of the operator
   * @param operator The operator to set
   */
  public void setOperator(final String operator) {

    setDescription(OPERATOR_FIELD, operator);
  }

  /**
   * Set the serial number of the slide
   * @param serialNumber The serialNumber to set
   */
  public void setSerialNumber(final String serialNumber) {

    setDescription(SERIAL_NUMBER_FIELD, serialNumber);
  }

  /**
   * Set the swap field of the slide
   * @param swap The swap value of the slide
   */
  public void setSwap(final boolean swap) {

    setDescription(SWAP_FIELD, "" + swap);
  }

  /**
   * Set the swap field of the slide
   * @param swap The swap value of the slide
   */
  public void setSwap(final String swap) {

    setSwap(swap == null ? false : swap.equals("true") || swap.equals("t")
        || swap.equals("1"));
  }

  //
  // Fields tester
  //

  /**
   * Test if a field exists.
   * @param field The field to test
   * @return true if the field exists
   */
  public boolean isField(final String field) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.isDescriptionField(field);
  }

  /**
   * Test if the comment field exists.
   * @return true if the field exists
   */
  public boolean isCommentField() {

    return isField(COMMENT_FIELD);
  }

  /**
   * Test if the date field exists.
   * @return true if the field exists
   */
  public boolean isDateField() {

    return isField(DATE_FIELD);
  }

  /**
   * Test if the operator field exists.
   * @return true if the field exists
   */
  public boolean isOperatorField() {

    return isField(OPERATOR_FIELD);
  }

  /**
   * Test if the serial number field exists.
   * @return true if the field exists
   */
  public boolean isSerialNumberField() {

    return isField(SERIAL_NUMBER_FIELD);
  }

  /**
   * Test if the swap field exists.
   * @return true if the field exists
   */
  public boolean isSwapField() {

    return isField(SWAP_FIELD);
  }

  //
  // Constructor
  //

  SlideDescription(final DesignImpl design, final int slideId) {

    this.design = design;
    this.slideId = slideId;
  }

}
