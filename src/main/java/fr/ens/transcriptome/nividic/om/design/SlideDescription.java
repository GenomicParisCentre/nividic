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

package fr.ens.transcriptome.nividic.om.design;

import java.util.List;

/**
 * This interface define the description of a slide
 * @author Laurent Jourdren
 */
public interface SlideDescription {

  /** Comment field. */
  String COMMENT_FIELD = "Comment";
  /** Date field. */
  String DATE_FIELD = "Date";
  /** Slide Number field. */
  String SLIDE_NUMBER_FIELD = "SildeNumber";
  /** Serial Number field. */
  String SERIAL_NUMBER_FIELD = "Serial number";
  /** Operator field. */
  String OPERATOR_FIELD = "Operator";
  /** Swap field. */
  String SWAP_FIELD = "Swap";
  /** Design ref field. */
  String DESIGN_REF_FIELD = "DesignRef";
  /** Tiff bits field. */
  String TIFF_BITS_FIELD = "TiffBits";
  /** File type field. */
  String FILE_TYPE_FIELD = "FileType";

  /**
   * Get a description.
   * @param field Field of the description to get
   * @return a String with the description
   */
  String getDescription(final String field);

  /**
   * Get the fields of the descriptions.
   * @return a list of strings with the descriptions fields
   */
  List<String> getDescriptionFields();

  /**
   * Get the comment about the slide.
   * @return Returns the comment
   */
  String getComment();

  /**
   * Get the date of the hybridation
   * @return Returns the date
   */
  String getDate();

  /**
   * Get the name of the operator
   * @return Returns the operator
   */
  String getOperator();

  /**
   * Get the slide number of the slide
   * @return Returns the slide number
   */
  String getSlideNumber();

  /**
   * Get the serial number of the slide
   * @return Returns the serialNumber
   */
  String getSerialNumber();

  /**
   * Get the swap field of the slide
   * @return true if the value of the field is (case insensitive) "true", "t" or
   *         "1"
   */
  boolean getSwap();

  /**
   * Get the reference of the design of the microarray.
   * @return The reference of the design of the microarray
   */
  String getDesignRef();

  /**
   * Get the number of bit by pixel in the original tiff image
   * @return the number of bit of the tiff image
   */
  int getTiffBits();

  /**
   * Get the file type of the data.
   * @return the file type of the data
   */
  String getFileType();

  /**
   * Set a field of the description.
   * @param field Field to set
   * @param value value to set
   */
  void setDescription(final String field, final String value);

  /**
   * Set the comment.
   * @param comment The comment to set
   */
  void setComment(final String comment);

  /**
   * Set the hybridation date
   * @param date The date to set
   */
  void setDate(final String date);

  /**
   * Set the name of the operator
   * @param operator The operator to set
   */
  void setOperator(final String operator);

  /**
   * Set the slide number of the slide
   * @param slideNumber The slideNumber to set
   */
  void setSlideNumber(final int slideNumber);

  /**
   * Set the serial number of the slide
   * @param serialNumber The serialNumber to set
   */
  void setSerialNumber(final String serialNumber);

  /**
   * Set the swap field of the slide
   * @param swap The swap value of the slide
   */
  void setSwap(final boolean swap);

  /**
   * Set the swap field of the slide
   * @param swap The swap value of the slide
   */
  void setSwap(final String swap);

  /**
   * Set the reference of the design of the microarray.
   * @param designRef The reference of the design of the microarray
   */
  void setDesignRef(final String designRef);

  /**
   * Set the number of bit by pixel in the original tiff image
   * @param tiffBits the number of bit of the tiff image
   */
  void setTiffBits(final int tiffBits);

  /**
   * Get the file type of the data.
   * @param fileType the file type of the data
   */
  void setFileType(final String fileType);

  /**
   * Test if a field exists.
   * @param field The field to test
   * @return true if the field exists
   */
  boolean isField(final String field);

  /**
   * Test if the comment field exists.
   * @return true if the field exists
   */
  boolean isCommentField();

  /**
   * Test if the date field exists.
   * @return true if the field exists
   */
  boolean isDateField();

  /**
   * Test if the operator field exists.
   * @return true if the field exists
   */
  boolean isOperatorField();

  /**
   * Test if the serial number field exists.
   * @return true if the field exists
   */
  boolean isSerialNumberField();

  /**
   * Test if the swap field exists.
   * @return true if the field exists
   */
  boolean isSwapField();

  /**
   * Test if the design ref field exists.
   * @return true if the field exists
   */
  boolean isDesignRefField();

  /**
   * Test if the tiff bits field exists.
   * @return true if the field exists
   */
  boolean isTiffBitsField();

  /**
   * Test if the file type field exists.
   * @return true if the field exists
   */
  boolean isFileType();

}