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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some static methods to manipulate strings.
 * @author Laurent Jourdren
 */
public final class StringUtils {

  private static final int RANDOM_WINDOW = 1000000;

  /**
   * Remove double quote from a string.
   * @param s The string parameter
   * @return a string without double quotes
   */
  public static String removeDoubleQuotes(final String s) {

    if (s == null)
      return null;

    String result = s;

    if (result.startsWith("\""))
      result = result.substring(1);
    if (result.endsWith("\""))
      result = result.substring(0, result.length() - 1);

    return result;
  }

  /**
   * Remove double quote and trim a string.
   * @param s The string parameter
   * @return a string without space and double quotes
   */
  public static String removeDoubleQuotesAndTrim(final String s) {

    if (s == null)
      return null;

    return StringUtils.removeDoubleQuotes(s.trim());
  }

  /**
   * Get the extension of a filename
   * @param filename The filename
   * @return The extension of the filename if exists, null if filename is null.
   */
  public static String getFileExtension(final String filename) {

    if (filename == null)
      return null;

    String[] words = filename.split("\\.");
    if (words.length == 0)
      return null;

    if (words.length == 1)
      return "";

    return "." + words[words.length - 1];
  }

  /**
   * Get the extension of a filename
   * @param filename The filename
   * @return The extension of the filename if exists, null if filename is null.
   */
  public static String getFilenameWithoutExtension(final String filename) {

    if (filename == null)
      return null;

    String[] words = filename.split("\\.");
    if (words.length == 0)
      return null;

    if (words.length == 1)
      return filename;

    return words[0];
  }

  /**
   * Generate a random identifier.
   * @param prefix Prefix of the identifier
   * @return a random identifier
   */
  public static String getRandomIdentifier(final String prefix) {

    StringBuffer sb = new StringBuffer();
    if (prefix != null && !prefix.equals("")) {
      sb.append(prefix);
      sb.append("-");
    }
    sb.append(System.currentTimeMillis());
    sb.append(Math.rint(Math.abs(Math.random() * RANDOM_WINDOW)));

    return sb.toString();
  }

  /**
   * Find a string in an array of string
   * @param query String to search
   * @param data Array of string
   * @return the position of the first result of the search
   */
  public static int findStringInArrayString(final String query,
      final String[] data) {

    if (query == null || data == null)
      return -1;

    for (int i = 0; i < data.length; i++)
      if (query.equals(data[i]))
        return i;

    return -1;
  }

  /**
   * Generate a random identifier.
   * @return a random identifier
   */
  public static String getRandomIdentifier() {

    return getRandomIdentifier(null);
  }

  /**
   * Prefix all double quotes by a slash.
   * @param s String to protect
   * @return A protected string
   */
  public static String protectDoubleQuotes(final String s) {

    if (s == null)
      return null;

    return s.replaceAll("\"", "\\\"");
  }

  /**
   * Create a String object with all element of an array of String separated by
   * double quotes and commas.
   * @param arrayString Array of string to concat
   * @return a concatened String
   */
  public static String arrayStringtoString(final String[] arrayString) {

    StringBuffer sb = new StringBuffer();

    if (arrayString != null) {

      for (int i = 0; i < arrayString.length; i++) {
        if (i > 0)
          sb.append(",");
        sb.append('\"');
        sb.append(arrayString[i]);
        sb.append('\"');
      }

    }

    return sb.toString();
  }

  /**
   * Create an array of String from a single String object where the substrings
   * are between double quotes and separated by commas.
   * @param s String to split
   * @return an array of the splited string
   */
  public static String[] stringToArrayString(final String s) {

    if (s == null)
      return null;

    String[] as = s.split(",");
    if (as == null)
      return null;

    for (int i = 0; i < as.length; i++)
      as[i] = removeDoubleQuotesAndTrim(as[i]);

    return as;
  }

  /**
   * Convert an array of strings to an array of integers.
   * @param as Array of strings to convert
   * @return An array of integers
   */
  public static int[] arrayStringToArrayInt(final String[] as) {

    if (as == null)
      return null;

    int[] result = new int[as.length];

    int j = 0;
    for (int i = 0; i < as.length; i++) {

      String s = as[i];
      if (s == null)
        continue;

      try {
        result[j++] = Integer.parseInt(s.trim());
      } catch (NumberFormatException e) {
        continue;
      }
    }

    int[] finalResult = new int[j];

    System.arraycopy(result, 0, finalResult, 0, j);

    return finalResult;
  }

  /**
   * Remove an array of Strings from another array of Strings.
   * @param idsToRemove Strings to remove
   * @param ids Initial Strings
   * @return an array of String without removed String
   */
  public static String[] excludeStrings(final String[] idsToRemove,
      final String[] ids) {

    if (idsToRemove == null || ids == null || idsToRemove.length == 0)
      return null;

    List<String> set = new ArrayList<String>(Arrays.asList(ids));

    for (int i = 0; i < idsToRemove.length; i++)
      set.remove(idsToRemove[i]);

    String[] result = new String[set.size()];

    set.toArray(result);

    return result;
  }

  /**
   * Split a String object. There is a bug with some regex in javascript. This
   * method fix it.
   * @param s String to split
   * @param regex Regex
   * @return an array of String
   */
  public static String[] split(final String s, final String regex) {

    if (s == null)
      return null;

    return s.split(regex);
  }

  /**
   * Escape path separators
   * @param s String to use
   * @return a String with escaping path separator
   */
  public static String escapeFileSeparators(final String s) {

    if (s == null)
      return null;

    final String separator = File.separator;

    return s.replaceAll(separator, "_");
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private StringUtils() {
  }

}