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

/**
 * Math utility class
 * @author Laurent Jourdren
 */
public final class MathUtils {

  private static final int BASE_10 = 10;
  private static final int BASE_2 = 2;

  /**
   * Calculates the standard deviation of an array of numbers. see
   * http://davidmlane.com/hyperstat/A16252.html
   * @param data Numbers to compute the standard deviation of. Array must
   *                 contain two or more numbers.
   * @return standard deviation estimate of population ( to get estimate of
   *               sample, use n instead of n-1 in last line )
   */
  public static double sdFast(final double[] data) {
    // sd is sqrt of sum of (values-mean) squared divided by n - 1
    // Calculate the mean
    double mean = 0;
    final int n = data.length;
    if (n < 2)
      return Double.NaN;
    for (int i = 0; i < n; i++) {
      mean += data[i];
    }
    mean /= n;
    // calculate the sum of squares
    double sum = 0;
    for (int i = 0; i < n; i++) {
      final double v = data[i] - mean;
      sum += v * v;
    }
    return java.lang.Math.sqrt(sum / (n - 1));
  }

  /**
   * Return the log 10 of a double.
   * @param d a double
   * @return the log 10 of a double
   */
  public static double log10(final double d) {
    return java.lang.Math.log(d) / java.lang.Math.log(BASE_10);
  }

  /**
   * Return the log 2 of a double.
   * @param d a double
   * @return the log 2 of a double
   */
  public static double log2(final double d) {
    return Math.log(d) / Math.log(BASE_2);
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private MathUtils() {
  }

}