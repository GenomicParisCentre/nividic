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
 * of the �cole Normale Sup�rieure and the individual authors.
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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.GenepixResults;
import fr.ens.transcriptome.nividic.om.PhysicalConstants;

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
    List<String> scanLabelSettingsNames = design.getScanLabelSettingsKeys();

    StringBuffer sb = new StringBuffer();

    // Write header
    sb.append("SlideNumber");
    sb.append("\t");

    sb.append("FileName");

    for (String l : labels) {
      sb.append("\t");
      sb.append(l);
    }

    for (String l : labels)
      for (String setting : scanLabelSettingsNames) {
        sb.append("\t");
        sb.append(l);
        sb.append("_");
        sb.append(setting);
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

      final ScanLabelsSettings ssls = s.getScanLabelsSettings();
      for (String l : labels) {

        ScanLabelSettings sls = ssls.getSetting(l);

        for (String setting : scanLabelSettingsNames) {
          sb.append("\t");
          sb.append(sls.getSetting(setting));
        }
      }

      for (String f : descriptionFields) {

        sb.append("\t");
        sb.append(s.getDescription().getDescription(f));
      }

      System.out.println(sb.toString());
    }

  }

  /**
   * Set the values of the scan settings for labels for a slide. This methods
   * doesn't load data if bioassay is null.
   * @param slide The slide
   */
  public static void addBioAssayScanSettingToDesign(final Slide slide) {

    if (slide == null)
      return;

    BioAssay ba = slide.getBioAssay();

    if (ba == null)
      return;

    GenepixResults gpr = new GenepixResults(ba);

    if (!gpr.isGPRData())
      return;

    final int[] wavelengths = gpr.getWavelengths();
    final int[] pmts = gpr.getPMTGains();
    final int[] scanPowers = gpr.getScanPowers();
    final double[] laserPowers = gpr.getLaserPowers();
    final int[] laserOnTimes = gpr.getLaserOnTimes();

    if (wavelengths == null)
      return;

    for (int i = 0; i < wavelengths.length; i++) {

      final String color =
          PhysicalConstants.getColorOfWaveLength(wavelengths[i]);

      ScanLabelSettings settings =
          slide.getScanLabelsSettings().getSetting(color);

      if (settings == null)
        continue;

      if (wavelengths != null)
        settings.setWaveLength(wavelengths[i]);
      if (pmts != null)
        settings.setPMTGain(pmts[i]);
      if (scanPowers != null)
        settings.setScanPower(scanPowers[i]);
      if (laserPowers != null)
        settings.setLaserPower(laserPowers[i]);
      if (laserOnTimes != null)
        settings.setLaserOnTime(laserOnTimes[i]);
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
