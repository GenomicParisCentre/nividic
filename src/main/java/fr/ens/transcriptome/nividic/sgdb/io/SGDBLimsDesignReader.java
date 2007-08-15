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

package fr.ens.transcriptome.nividic.sgdb.io;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.PhysicalConstants;
import fr.ens.transcriptome.nividic.om.datasources.URLDataSource;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.DesignFactory;
import fr.ens.transcriptome.nividic.om.design.ScanLabelSettings;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.design.io.DesignReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.Hybridization;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsService;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsServiceLocator;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.Scan;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.Target;

/**
 * This class implemements a design reader to load design from the sgdb lims.
 * @author Laurent Jourdren
 */
public class SGDBLimsDesignReader implements DesignReader {

  private LimsPort port;

  private static final String CY3_DYE = "Mono-Cy3";
  private static final String CY5_DYE = "Mono-Cy5";

  private static final String URL_GPR_LIMS = "sgdb://gpr/";

  private List<String> slideSerialNumbers;

  private void init() throws ServiceException {

    LimsService service = new LimsServiceLocator();
    this.port = service.getLimsPort();
  }

  /**
   * Get the source of the data.
   * @return The source of the data
   */
  public String getDataSource() {

    if (this.slideSerialNumbers == null)
      return null;

    StringBuilder sb = new StringBuilder();
    sb.append("sgdb_design:");

    boolean first = true;

    for (String serialNumber : slideSerialNumbers) {

      if (first)
        first = false;
      else
        sb.append(",");

      sb.append(serialNumber);
    }

    return sb.toString();
  }

  /**
   * Read the design.
   * @return a new Design object
   * @throws NividicIOException if an error occurs while reading the design
   */
  public Design read() throws NividicIOException {

    if (this.slideSerialNumbers == null)
      return null;

    Design design = DesignFactory.createEmptyDesign();

    try {

      for (String serialNumber : slideSerialNumbers) {

        Hybridization hyb = this.port.getHybridization(serialNumber);
        if (hyb != null)
          processHybridization(design, hyb);

      }
    } catch (RemoteException e) {
      throw new NividicIOException("Unable to read the design: "
          + e.getMessage());
    }

    return design;
  }

  private void processHybridization(final Design design, final Hybridization hyb)
      throws RemoteException {

    final String hybName = hyb.getName();

    if (hybName == null)
      return;

    // Test if the hybridization is a swap
    final boolean swap = isSwap(hybName);

    // Get the target and scan from the server
    final Target[] targets = this.port.getTargets(hyb.getId());
    final Scan[] scans = this.port.getScans(hyb.getId());

    Map<String, String> labels = new HashMap<String, String>();

    // Analyse the targets
    for (int i = 0; i < targets.length; i++) {

      String labelName = targets[i].getDye();
      String sample = removeStringCR(targets[i].getComment());

      if (labelName == null)
        continue;

      if (CY3_DYE.equals(labelName))
        labelName = PhysicalConstants.CY3_COLOR;
      if (CY5_DYE.equals(labelName))
        labelName = PhysicalConstants.CY5_COLOR;

      if (!design.isLabel(labelName))
        design.addLabel(labelName);

      if (!design.getSamples().isSample(sample))
        design.getSamples().addSample(sample);

      labels.put(labelName, sample);
    }

    // Add the scan to the design
    for (int i = 0; i < scans.length; i++) {

      // Create the slide
      final String slideName = hyb.getName() + "#" + (i + 1);
      design.addSlide(slideName);

      Slide slide = design.getSlide(slideName);

      // Add the target for the slides
      for (Map.Entry<String, String> entry : labels.entrySet())
        slide.setTarget(entry.getKey(), entry.getValue());

      // Add the datasource
      slide.setSource(new URLDataSource(URL_GPR_LIMS + scans[i].getId()));

      // Set the scan labels settings
      ScanLabelSettings settings =
          slide.getScanLabelsSettings().getSetting(
              PhysicalConstants.getColorOfWaveLength(scans[i].getChannel1()));

      settings.setWaveLength(scans[i].getChannel1());
      settings.setPMTGain(scans[i].getPmt1());
      settings.setScanPower(scans[i].getPower1());

      settings =
          slide.getScanLabelsSettings().getSetting(
              PhysicalConstants.getColorOfWaveLength(scans[i].getChannel2()));

      settings.setWaveLength(scans[i].getChannel2());
      settings.setPMTGain(scans[i].getPmt2());
      settings.setScanPower(scans[i].getPower2());

      // add additional descriptions
      slide.getDescription().setDate(hyb.getDate());
      slide.getDescription().setSwap(swap);
      slide.getDescription().setSerialNumber(hyb.getSlideSerialNumber());
      slide.getDescription().setOperator(hyb.getOwner());
    }

  }

  private static String removeStringCR(final String s) {

    if (s == null)
      return null;

    return s.replaceAll("\n", " ");
  }

  private static boolean isSwap(final String s) {

    if (s == null)
      return false;

    final String lower = s.toLowerCase().trim();

    return lower.endsWith("dye swap") || lower.endsWith("dye");
  }

  //
  // Constructor
  //

  private SGDBLimsDesignReader() {

    try {
      init();
    } catch (ServiceException e) {

      throw new NividicRuntimeException("Unable to initialize the service");
    }
  }

  /**
   * Public constructor.
   * @param slideSerialNumbers a list of the slides names using in the design
   */
  public SGDBLimsDesignReader(final List<String> slideSerialNumbers) {

    this();

    if (slideSerialNumbers == null)
      throw new NullPointerException("slideSerialNumbers can't be null.");

    this.slideSerialNumbers = new ArrayList<String>(slideSerialNumbers);
  }

  /**
   * Public constructor.
   * @param slideSerialNumbers an array of the slides names using in the design
   */
  public SGDBLimsDesignReader(final String[] slideSerialNumbers) {

    this(Arrays.asList(slideSerialNumbers));
  }

}
