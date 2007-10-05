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
 * Utility class for handling Annotations
 * @author Laurent Jourdren
 */
public final class AnnotationUtils {

  /**
   * Print an annotation.
   * @param annot Annotation to print
   */
  public static void printAnnotation(final Annotation annot) {

    if (annot == null)
      return;

    final String[] keys = annot.getPropertiesKeys();

    for (int i = 0; i < keys.length; i++)
      System.out.println(keys[i] + "\t" + annot.getProperty(keys[i]));

  }

  /**
   * Private constructor.
   */
  private AnnotationUtils() {
  }

}
