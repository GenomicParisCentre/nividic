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

import java.util.List;

import fr.ens.transcriptome.nividic.om.impl.SlideDescription;

/**
 * Utils methods for Design.
 * @author Laurent Jourdren
 */
public final class DesignUtils {

  /**
   * Show a design
   * @param design Design to show
   */
  public static void showDesign(final Design design) {

    List<String> labels = design.getLabelsNames();
    List<String> descriptionFields = design.getDescriptionFieldsNames();

    StringBuffer sb = new StringBuffer();

    // Write header
    sb.append("SlideNumber");
    sb.append("\t");

    sb.append("FileName");

    for (String l : labels) {
      sb.append("\t");
      sb.append(l);
    }

    for (String f : descriptionFields) {

      sb.append("\t");
      sb.append(f);
    }

    System.out.println(sb.toString());

    // Write data
    List<Slide> slides = design.getSlides();

    for (Slide s : slides) {

      sb.setLength(0);

      sb.append(s.getName());
      sb.append("\t");

      String sourceInfo = s.getSourceInfo();
      if (sourceInfo != null)
        sb.append(sourceInfo);

      for (String l : labels) {
        sb.append("\t");
        sb.append(s.getTarget(l));
      }

      for (String f : descriptionFields) {

        sb.append("\t");
        sb.append(s.getDescription().getDescription(f));
      }

      System.out.println(sb.toString());
    }

  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private DesignUtils() {
  }

}
