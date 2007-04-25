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
 * Utils methods for history.
 * @author Laurent Jourdren
 */
public final class HistoryUtils {

  /**
   * Show log history about a biological object
   * @param bioObject bioObject tu use
   */
  public static void showHistory(final BiologicalObject bioObject) {

    if (bioObject == null)
      return;

    System.out.println("Date\tType\tName\tResult\tArguments\tComments\n");

    for (HistoryEntry entry : bioObject.getHistory().getEntries()) {

      StringBuffer sb = new StringBuffer();

      sb.append(entry.getDate());
      sb.append("\t");
      sb.append(entry.getActionType());
      sb.append("\t");
      sb.append(entry.getActionName());
      sb.append("\t");
      sb.append(entry.getActionResult());
      sb.append("\t");
      sb.append(entry.getArguments());
      sb.append("\t");
      sb.append(entry.getComments());

      System.out.println(sb.toString());
    }

  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private HistoryUtils() {
  }

}
