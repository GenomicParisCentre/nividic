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

import fr.ens.transcriptome.nividic.om.impl.SimpleAnnotationsImpl;

/**
 * This allow to create instances of concrete classes of Annotation interface.
 * @author Laurent Jourdren
 */
public final class AnnotationFactory {

  /**
   * Create an annotation Object.
   * @return A new annotation Object
   */
  public static Annotation createAnnotation() {
    return new SimpleAnnotationsImpl();
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private AnnotationFactory() {
  }

}
