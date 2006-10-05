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

package fr.ens.transcriptome.nividic.om;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A wrapper for a BioAssay object which provides methods for an easy access to
 * the object properties for real Imagene result data.
 * @author Laurent Jourdren
 */
public class ImaGeneResult {

  /** The wrapped object. */
  private BioAssay bioAssay;

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
      "E MMM d hh:mm:ss zzz yyyy");

  /** Imagene results magic string. */
  public static final String IMAGENE_RESULT_MAGIC_STRING = "ImaGene Results";
  private static final String IMAGENE_RESULT_HEADER_TYPE = "Type";
  private static final String IMAGENE_RESULT_HEADER_DATE = "Date";

  //
  // Method for access to the wrapped object
  //

  /**
   * Return the wrapped object.
   * @return The BioAssay object wrapped in this object.
   */
  public BioAssay getBioAssay() {
    return bioAssay;
  }

  /**
   * Set the wrapped object.
   * @param bioAssay The object to be wrapped
   */
  private void setBioAssay(final BioAssay bioAssay) {
    this.bioAssay = bioAssay;
  }

  //
  // Getters
  //

  /**
   * Get the type of the file.
   * @return The type of the file
   */
  public String getType() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(IMAGENE_RESULT_HEADER_TYPE);
  }

  /**
   * Test if data is data from an Imagene result
   * @return true if data cames from an Imagene result
   */
  public boolean isGPRData() {

    String type = getType();

    if (type == null)
      return false;

    return type.startsWith(IMAGENE_RESULT_MAGIC_STRING);
  }

  /**
   * Return the date of the creation of the data.
   * @return The creation date of the data
   */
  public Date getDateTime() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    String date = annotation.getProperty(IMAGENE_RESULT_HEADER_DATE);

    try {
      return DATE_FORMAT.parse(date);
    } catch (ParseException e) {
      return null;
    }

  }

  //
  // Setters
  //

  /**
   * Set the type of Imagene result file.
   * @param type The type of Imagene result file to set
   */
  public void setType(final String type) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(IMAGENE_RESULT_HEADER_TYPE, type);
  }

  /**
   * Test if data is data from a GPR
   * @return true if data cames from a GPR
   */
  public boolean isImageneResultData() {

    String type = getType();

    if (type == null)
      return false;

    return type.startsWith(IMAGENE_RESULT_MAGIC_STRING);
  }

  /**
   * Set the date of the gpr.
   * @param date The date to set
   */
  public void setDateTime(final Date date) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    final String value;

    if (date == null)
      value = "";
    else

      value = DATE_FORMAT.format(date);

    annotation.setProperty(IMAGENE_RESULT_HEADER_DATE, value);
  }

  //
  // Constructor
  //

  /**
   * Standard constructor.
   * @param bioAssay BioAssay object to be wrapped, must be not null
   * @throws NullPointerException if parameter is null
   */
  public ImaGeneResult(final BioAssay bioAssay) throws NullPointerException {

    if (bioAssay == null)
      throw new NullPointerException("BioAssay parameter is null");
    setBioAssay(bioAssay);
  }

}
