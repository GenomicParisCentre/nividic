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

/**
 * This class defines the parameters for the scan of a label.
 * @author Laurent Jourdren
 */
public interface ScanLabelsSettings {

  /**
   * Get the setting for a label.
   * @param labelName The name of a label
   * @return a ScanLabelSettings if exists
   */
  ScanLabelSettings getSetting(final String labelName);

  /**
   * Get the setting for scan of the green dye.
   * @return a ScanLabelSettings if exists
   */
  ScanLabelSettings getGreenSettings();

  /**
   * Get the setting for scan of the red dye.
   * @return a ScanLabelSettings if exists
   */
  ScanLabelSettings getRedSettings();

}
