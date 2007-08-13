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

/**
 * This class defines physical constants for microarrays.
 * @author Laurent Jourdren
 */
public final class PhysicalConstants {

  /** CY5_COLOR wavelength. */
  public static final int CY5_WAVELENGTH = 635;
  /** CY3_COLOR wavelength. */
  public static final int CY3_WAVELENGTH = 532;

  /** Green field value. */
  public static final String GREEN_FIELD = "green";
  
  /** Red field value. */
  public static final String RED_FIELD = "red";
  
  /** Red wavelength. */
  public static final int RED_WAVELENGTH = CY5_WAVELENGTH;
  /** Green wavelength. */
  public static final int GREEN_WAVELENGTH = CY3_WAVELENGTH;

  /** Color of CY3_COLOR dye. */
  public static final String CY3_COLOR = GREEN_FIELD;

  /** Color of CY5_COLOR dye. */
  public static final String CY5_COLOR = RED_FIELD;

  /** Default dye for green. */
  public static final String GREEN_DEFAULT_DYE = "Cy3";

  /** Default dye for red. */
  public static final String RED_DEFAULT_DYE = "Cy5";

  /**
   * Get the name of the color for a value of wavelength.
   * @param wavelength Wavelength in nanometers
   * @return the name of the color for the wavelength or null if there is no
   *         known color for the wavelength
   */
  public static String getColorOfWaveLength(final int wavelength) {

    if (wavelength == CY5_WAVELENGTH)
      return CY5_COLOR;
    if (wavelength == CY3_WAVELENGTH)
      return CY3_COLOR;

    return null;
  }

  /**
   * Get the wavelength for a color name.
   * @param colorName Name of the color
   * @return the wavelength of the color or 0 if unknown
   */
  public static int getWavelengthOfColor(final String colorName) {

    if (colorName == null)
      return 0;

    if (GREEN_DEFAULT_DYE.equals(colorName))
      return GREEN_WAVELENGTH;

    if (RED_DEFAULT_DYE.equals(colorName))
      return RED_WAVELENGTH;

    return 0;
  }

  /**
   * Get the common dye for the color..
   * @param colorName Name of the color
   * @return the common dye of the color or null if unknown
   */
  public static String getDyeForColor(final String colorName) {

    if (colorName == null)
      return null;

    if (CY3_COLOR.toLowerCase().equals(colorName.trim().toLowerCase()))
      return GREEN_DEFAULT_DYE;

    if (CY5_COLOR.toLowerCase().equals(colorName.trim().toLowerCase()))
      return RED_DEFAULT_DYE;

    return null;
  }

  /**
   * Get the color of a dye.
   * @param dye Name of the dye
   * @return the color for the dye or null if unknown
   */
  public static String getColorForDye(final String dye) {

    if (dye == null)
      return null;

    if (GREEN_DEFAULT_DYE.toLowerCase().equals(dye.trim().toLowerCase()))
      return CY3_COLOR;

    if (RED_DEFAULT_DYE.toLowerCase().equals(dye.trim().toLowerCase()))
      return CY5_COLOR;

    return null;
  }

  //
  // Constructor
  //

  private PhysicalConstants() {
  }

}
