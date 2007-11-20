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

package fr.ens.transcriptome.nividic.om.r;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.ens.transcriptome.nividic.Globals;
import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormatRegistery;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.GPRWriter;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.IDMAWriter;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class define a wrapper for Goulphar.
 * @author Laurent Jourdren
 */
public class GoulpharWrapper {

  private static final String GOULPHAR_SCRIPT_URL =
      "http://hestia.ens.fr/R/Goulphar.R";

  public static final int FOREGROUND_METHOD_MEDIAN = 0;
  public static final int FOREGROUND_METHOD_MEAN = 1;

  public static final int BACKGROUND_MEDIAN_SUBSTRACTION = 1;
  public static final int BACKGROUND_MEAN_SUBSTRACTION = 2;
  public static final int BACKGROUND_NO_SUBSTRACTION = 0;

  public static final String PRINT_TIP_GROUP_LOWESS_NORMALISATION_METHOD = "p";
  public static final String GLOBAL_LOWESS_NORMALISATION_METHOD = "l";
  public static final String GLOBAL_LOWESS_AND_PRINT_TIP_GROUP_NORMALISATION_METHOD =
      "lmb";
  public static final String GLOBAL_MEDIAN_NORMALISATION_METHOD = "m";
  public static final String PRINT_TIP_GROUP_MEDIAN_NORMALISATION_METHOD = "mb";

  public static final boolean REMOVE_SATURING_SPOTS = true;
  public static final boolean KEEP_SATURING_SPOTS = false;

  public static final boolean REMOVE_SMALL_SPOTS = true;
  public static final boolean KEEP_SMALL_SPOTS = false;

  public static final String FLAG_FILTER_ALL = "all";
  public static final String FLAG_FILTER_NONE = "none";

  public static final int PDF_OUTPUT = 1;
  public static final int PNG_OUTPUT = 2;
  public static final int JPEG_OUTPUT = 3;

  public static final int DEFAULT_SATURING_THRESHOLD = 50000;
  public static final int DEFAULT_DIAMETER_THRESHOLD = 0;
  public static final int DEFAULT_ALERT_PRINTTIP = 200;
  public static final String DEFAULT_FLAG_FILTER = FLAG_FILTER_ALL;

  private BioAssay bioAssay;
  private String prefixGPRFilename;

  private BioAssay normalizedBioAssay;
  private String script;
  private RSConnection con = new RSConnection();

  // Goulphar parameters

  private int foregroundMethod = FOREGROUND_METHOD_MEDIAN;
  private int backgroundSubstraction = BACKGROUND_NO_SUBSTRACTION;
  // private String normalisationMethod = GLOBAL_LOWESS_NORMALISATION_METHOD;
  private String normalisationMethod =
      GLOBAL_LOWESS_AND_PRINT_TIP_GROUP_NORMALISATION_METHOD;
  private String flagFilter = DEFAULT_FLAG_FILTER;
  // private String flagFilterValue;
  private boolean removeSaturingSpots = REMOVE_SATURING_SPOTS;
  private int removeSaturingSpotsIntensity = DEFAULT_SATURING_THRESHOLD;

  private boolean removeSmallSpots = KEEP_SMALL_SPOTS;
  private int removeSmallSpotsDiameter = DEFAULT_DIAMETER_THRESHOLD;

  private int alertPrintTip;

  private int graphicalOutputType = PDF_OUTPUT;

  private boolean designMode;

  //
  // Getters
  //

  /**
   * Get the alert print tip value.
   * @return Returns the alertPrintTip
   */
  public int getAlertPrintTip() {
    return alertPrintTip;
  }

  /**
   * Get the method of background substraction.
   * @return Returns the backgroundSubstraction
   */
  public int getBackgroundSubstraction() {
    return backgroundSubstraction;
  }

  /**
   * Get the BioAssay to use.
   * @return Returns the bioAssay
   */
  public BioAssay getBioAssay() {
    return bioAssay;
  }

  /**
   * Get the BioAssay to use.
   * @return Returns the bioAssay
   */
  public BioAssay getNormalizedBioAssay() {

    if (this.bioAssay == null && !this.designMode)
      return null;

    if (this.normalizedBioAssay == null)
      loadNormalizedBioAssay();

    return this.normalizedBioAssay;
  }

  /**
   * Get the flagfilter.
   * @return Returns the flagFilter
   */
  public String getFlagFilter() {
    return flagFilter;
  }

  /**
   * Get the foregroundMethod.
   * @return Returns the foregroundMethod
   */
  public int getForegroundMethod() {
    return foregroundMethod;
  }

  /**
   * Get the graphical output type.
   * @return Returns the graphicalOutputType
   */
  public int getGraphicalOutputType() {
    return graphicalOutputType;
  }

  /**
   * Get the normalisation method.
   * @return Returns the normalisationMethod
   */
  public String getNormalisationMethod() {

    return normalisationMethod;
  }

  /**
   * Get the remove staturing spot value.
   * @return Returns the removeSaturingSpots
   */
  public boolean isRemoveSaturingSpots() {
    return removeSaturingSpots;
  }

  /**
   * Get the maximal intensity of the spot to remove.
   * @return Returns the removeSaturingSpotsIntensity
   */
  public int getRemoveSaturingSpotsIntensity() {
    return removeSaturingSpotsIntensity;
  }

  /**
   * Get the diameter of small spots.
   * @return Returns the removeSmallSpotsDiameter
   */
  public int getRemoveSmallSpotsDiameter() {
    return removeSmallSpotsDiameter;
  }

  /**
   * Get if small spots must be removed.
   * @return Returns the removeSmallSpots
   */
  public boolean isRemoveSmallSpots() {
    return removeSmallSpots;
  }

  //
  // Setters
  //

  /**
   * set the number of spot to show an alert message using print tip method.
   * @param alertPrintTip The alertPrintTip to set
   */
  public void setAlertPrintTip(final int alertPrintTip) {
    this.alertPrintTip = alertPrintTip;
  }

  /**
   * Set the background substraction to use.
   * @param backgroundSubstraction The backgroundSubstraction to set
   */
  public void setBackgroundSubstraction(final int backgroundSubstraction) {
    this.backgroundSubstraction = backgroundSubstraction;
  }

  /**
   * Set the bioAssay to use.
   * @param bioAssay The bioAssay to set
   */
  private void setBioAssay(final BioAssay bioAssay) {
    this.bioAssay = bioAssay;
  }

  /**
   * Set the normalized bioAssay to use.
   * @param bioAssay The bioAssay to set
   */
  private void setNormalizedBioAssay(final BioAssay bioAssay) {
    this.normalizedBioAssay = bioAssay;
  }

  /**
   * Set the flagfilter to use.
   * @param flagFilter The flagFilter to set
   */
  public void setFlagFilter(final String flagFilter) {
    this.flagFilter = flagFilter;
  }

  /**
   * Set the statistical forground method to use.
   * @param foregroundMethod The foregroundMethod to set
   */
  public void setForegroundMethod(final int foregroundMethod) {
    this.foregroundMethod = foregroundMethod;
  }

  /**
   * Set the graphical output type to use.
   * @param graphicalOutputType The graphicalOutputType to set
   */
  public void setGraphicalOutputType(final int graphicalOutputType) {
    this.graphicalOutputType = graphicalOutputType;
  }

  /**
   * @param normalisationMethod The normalisationMethod to set
   */
  public void setNormalisationMethod(final String normalisationMethod) {
    this.normalisationMethod = normalisationMethod;
  }

  /**
   * Set if saturing spot must be removed.
   * @param removeSaturingSpots The removeSaturingSpots to set
   */
  public void setRemoveSaturingSpots(final boolean removeSaturingSpots) {
    this.removeSaturingSpots = removeSaturingSpots;
  }

  /**
   * Set the saturing spot intensity.
   * @param removeSaturingSpotsIntensity The removeSaturingSpotsIntensity to set
   */
  public void setRemoveSaturingSpotsIntensity(
      final int removeSaturingSpotsIntensity) {
    this.removeSaturingSpotsIntensity = removeSaturingSpotsIntensity;
  }

  /**
   * Set the smal spot diameter.
   * @param removeSmallSpotsDiameter The removeSmallSpotsDiameter to set
   */
  public void setRemoveSmallSpotsDiameter(final int removeSmallSpotsDiameter) {
    this.removeSmallSpotsDiameter = removeSmallSpotsDiameter;
  }

  /**
   * Set if small spots must be removed.
   * @param removeSmallSpots The removeSmallSpots to set
   */
  public void setRemoveSmallSpots(final boolean removeSmallSpots) {
    this.removeSmallSpots = removeSmallSpots;
  }

  //
  // Other methods
  //

  private String createParameterFileContent(final String resultFile) {

    final StringBuffer sb = new StringBuffer();

    sb.append("result.file\t"
        + "software\t" + "foreground\t" + "do.flagremoval\t" + "do.bgcorr\t"
        + "do.saturating\t" + "saturating\t" + "do.diameter\t" + "diameter\t"
        + "norma\t" + "alert.printtip\t" + "imagefile\t" + "gal.file\n");
    sb.append(resultFile);
    sb.append("\t");
    sb.append("genepix");
    sb.append("\t");
    sb.append(getForegroundMethod());
    sb.append("\t");
    sb.append(getFlagFilter());
    sb.append("\t");
    sb.append(getBackgroundSubstraction());
    sb.append("\t");
    sb.append(isRemoveSaturingSpots() ? 1 : 0);
    sb.append("\t");
    sb.append(getRemoveSaturingSpotsIntensity());
    sb.append("\t");
    sb.append(isRemoveSmallSpots() ? 1 : 0);
    sb.append("\t");
    sb.append(getRemoveSmallSpotsDiameter());
    sb.append("\t");
    sb.append(getNormalisationMethod());
    sb.append("\t");
    sb.append(getAlertPrintTip());
    sb.append("\t");
    sb.append(getGraphicalOutputType());
    sb.append("\t");
    sb.append(0);
    sb.append("\n");

    return sb.toString();
  }

  private void loadScript() {

    try {

      InputStream is = new URL(GOULPHAR_SCRIPT_URL).openStream();

      BufferedReader in =
          new BufferedReader(new InputStreamReader(is,
              Globals.DEFAULT_FILE_ENCODING));

      String line;
      StringBuilder sb = new StringBuilder();

      while ((line = in.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
      }

      in.close();

      this.script = sb.toString();

    } catch (MalformedURLException e) {
      this.script = null;
    } catch (IOException e) {
      this.script = null;
    }

  }

  /**
   * Normalize the bioAssay of a design.
   * @param design Design which bioAssay must be normalized
   */
  public void normalize(final Design design) {

    if (design == null)
      return;

    this.designMode = true;

    for (Slide s : design.getSlides()) {

      final String prefix = design.getBiologicalId() + "-" + s.getName();

      normalize(s, null, prefix);

      BioAssay ba = getNormalizedBioAssay();

      s.setBioAssay(ba);
      s.setSource(s.getName() + ".gpr_norm.txt");
      s.setSourceFormat(BioAssayFormatRegistery.IDMA_BIOASSAY_FORMAT);
      s.swapSlide();
    }

    this.designMode = false;
  }

  private void normalize(final Slide slide, final BioAssay bioAssay,
      final String prefix) {

    if (this.script == null)
      throw new NividicRuntimeException("Unable to load Goulphar script");

    clean();
    setBioAssay(bioAssay);
    setNormalizedBioAssay(null);

    this.prefixGPRFilename = prefix;
    String gpr = this.prefixGPRFilename + ".gpr";
    String param = createParameterFileContent(gpr);

    try {

      OutputStream os = con.getFileOutputStream(gpr);

      if (slide != null) {

        if (slide.getBioAssay() != null)
          this.bioAssay = slide.getBioAssay();
        else {

          final DataSource source = slide.getSource();

          if (BioAssayFormatRegistery.GPR_BIOASSAY_FORMAT != slide.getFormat())
            throw new NividicIOException("Invalid format of BioAssay: "
                + source.getBioAssayFormat());

          if (source != null)
            try {
              NividicUtils.writeInputStream(source.getInputStream(), os);
            } catch (IOException e) {

              throw new NividicIOException(e);
            }
        }
      }

      if (this.bioAssay != null) {

        GPRWriter gw = new GPRWriter(os);
        gw.addAllFieldsToWrite();
        gw.write(getBioAssay());
      }

      con.writeStringAsFile("param_goulphar.dat", param);

      con.executeRCode(this.script);

    } catch (RSException e) {
      e.printStackTrace();
    } catch (NividicIOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   * Normalize a bioAssay.
   * @param bioAssay bioAssay to normalize
   */
  public void normalize(final BioAssay bioAssay) {

    if (bioAssay == null)
      return;

    normalize(null, bioAssay, "toprocess-" + System.currentTimeMillis());
  }

  private void loadNormalizedBioAssay() {

    try {

      InputStream is =
          this.con.getFileInputStream(this.prefixGPRFilename + ".gpr_norm.txt");

      this.normalizedBioAssay = new IDMAReader(is).read();

    } catch (RSException e) {

      throw new NividicRuntimeException("Unable to get the normalized bioAssay");

    } catch (NividicIOException e) {

      throw new NividicRuntimeException("Unable to get the normalized bioAssay");
    }

  }

  /**
   * Save the report.
   * @param outputfile Output file
   */
  public void saveNormalizationReport(final File outputfile) {

    String pdfFilename = this.prefixGPRFilename + ".gpr.pdf";

    try {
      this.con.getFile(pdfFilename, outputfile);
    } catch (RSException e) {
      throw new NividicRuntimeException(
          "An error occur while downloading the file");
    }

  }

  /**
   * Clean imported and generated file on the RServer.
   */
  public void clean() {

    if (this.prefixGPRFilename == null)
      return;

    List<String> files = new ArrayList<String>();

    files.add("param_goulphar.dat");
    files.add(this.prefixGPRFilename + ".gpr_norm.txt");
    files.add(this.prefixGPRFilename + ".gpr.pdf");
    files.add(this.prefixGPRFilename + ".gpr");

    for (String filename : files)
      try {
        this.con.removeFile(filename);
      } catch (RSException e) {
        throw new NividicRuntimeException("An error occur while remove a file");
      }

    this.prefixGPRFilename = null;
  }

  /**
   * Disconnect from RServe.
   */
  public void disConnect() {

    clean();
    this.con.disConnect();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public GoulpharWrapper() {

    loadScript();
  }

  public static void main(final String[] args) throws NividicIOException {

    System.out.println("start");

    // String filename = "/tmp/genepix.gpr";
    String filename =
        "/home/jourdren/analyses/7eme/agilent_serie1/"
            + "D-cone-croissance_900-950_0635.gpr";

    GPRReader reader = new GPRReader(new File(filename));
    reader.addAllFieldsToRead();

    BioAssay ba = reader.read();

    long start = System.currentTimeMillis();

    GoulpharWrapper gw = new GoulpharWrapper();

    gw.normalize(ba);

    BioAssay nba = gw.getNormalizedBioAssay();

    long end1 = System.currentTimeMillis();
    System.out.println("time: " + (end1 - start));

    System.out.println("Write idma");
    IDMAWriter writer = new IDMAWriter(new File("/tmp/toto.idma"));
    writer.write(nba);

    long end2 = System.currentTimeMillis();
    System.out.println("time: " + (end2 - start));

    System.out.println("Write pdf");
    gw.saveNormalizationReport(new File("/tmp/toto.pdf"));

    long end3 = System.currentTimeMillis();
    System.out.println("time: " + (end3 - start));

    System.out.println("time: " + (end3 - end2));

    gw.disConnect();

    System.out.println("end.");

  }

}
