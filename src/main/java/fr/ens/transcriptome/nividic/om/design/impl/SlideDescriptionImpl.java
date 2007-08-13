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

package fr.ens.transcriptome.nividic.om.design.impl;

import java.util.List;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.design.SlideDescription;

/**
 * This class define a slide description.
 * @author Laurent Jourdren
 */
public class SlideDescriptionImpl implements SlideDescription {

  private DesignImpl design;
  private int slideId;

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#getDescription(java.lang.String)
   */
  public String getDescription(final String field) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getDescription(slideName, field);
  }

  /**
   * Get the fields of the descriptions.
   * @return a list of strings with the descriptions fields
   */
  public List<String> getDescriptionFields() {

    return this.design.getDescriptionFieldsNames();
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#getComment()
   */
  public String getComment() {

    return getDescription(COMMENT_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#getDate()
   */
  public String getDate() {

    return getDescription(DATE_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#getOperator()
   */
  public String getOperator() {

    return getDescription(OPERATOR_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#getSerialNumber()
   */
  public String getSerialNumber() {

    return getDescription(SERIAL_NUMBER_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#getSwap()
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

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setDescription(java.lang.String,
   *      java.lang.String)
   */
  public void setDescription(final String field, final String value) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setDescription(slideName, field, value);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setComment(java.lang.String)
   */
  public void setComment(final String comment) {

    setDescription(COMMENT_FIELD, comment);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setDate(java.lang.String)
   */
  public void setDate(final String date) {

    setDescription(DATE_FIELD, date);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setOperator(java.lang.String)
   */
  public void setOperator(final String operator) {

    setDescription(OPERATOR_FIELD, operator);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setSerialNumber(java.lang.String)
   */
  public void setSerialNumber(final String serialNumber) {

    setDescription(SERIAL_NUMBER_FIELD, serialNumber);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setSwap(boolean)
   */
  public void setSwap(final boolean swap) {

    setDescription(SWAP_FIELD, "" + swap);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#setSwap(java.lang.String)
   */
  public void setSwap(final String swap) {

    setSwap(swap == null ? false : swap.equals("true")
        || swap.equals("t") || swap.equals("1"));
  }

  //
  // Fields tester
  //

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#isField(java.lang.String)
   */
  public boolean isField(final String field) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.isDescriptionField(field);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#isCommentField()
   */
  public boolean isCommentField() {

    return isField(COMMENT_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#isDateField()
   */
  public boolean isDateField() {

    return isField(DATE_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#isOperatorField()
   */
  public boolean isOperatorField() {

    return isField(OPERATOR_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#isSerialNumberField()
   */
  public boolean isSerialNumberField() {

    return isField(SERIAL_NUMBER_FIELD);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.SlideDescription#isSwapField()
   */
  public boolean isSwapField() {

    return isField(SWAP_FIELD);
  }

  //
  // Constructor
  //

  SlideDescriptionImpl(final DesignImpl design, final int slideId) {

    this.design = design;
    this.slideId = slideId;
  }

}
