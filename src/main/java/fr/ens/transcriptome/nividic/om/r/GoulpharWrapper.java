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
import fr.ens.transcriptome.nividic.om.BioAssayConverter;
import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormatFinderInputStream;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormatRegistery;
import fr.ens.transcriptome.nividic.om.io.GPRWriter;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class define a wrapper for Goulphar.
 * @author Laurent Jourdren
 * @author Marion Gaussen
 */
public class GoulpharWrapper {

  private static final String GOULPHAR_SCRIPT_URL =
      "http://transcriptome.ens.fr/goulphar/Goulphar.R";

  /** Foreground method median. */
  public static final int FOREGROUND_METHOD_MEDIAN = 0;
  /** Foreground method mean. */
  public static final int FOREGROUND_METHOD_MEAN = 1;

  /** Background median substraction. */
  public static final int BACKGROUND_MEDIAN_SUBSTRACTION = 1;
  /** Background mean substraction. */
  public static final int BACKGROUND_MEAN_SUBSTRACTION = 2;
  /** Background no substraction. */
  public static final int BACKGROUND_NO_SUBSTRACTION = 0;

  /** Print-tip group lowess normalization method. */
  public static final String PRINT_TIP_GROUP_NORMAZISATION_METHOD = "p";
  /** Global Lowess normalization method. */
  public static final String GLOBAL_LOWESS_NORMALIZATION_METHOD = "l";
  /** Global Lowess and Print-tip group group normalization method. */
  public static final String GLOBAL_LOWESS_AND_PRINT_TIP_GROUP_NORMALIZATION_METHOD =
      "lmb";
  /** Global median normalization method. */
  public static final String GLOBAL_MEDIAN_NORMALIZATION_METHOD = "m";
  /** Print-tip median normalization method. */
  public static final String PRINT_TIP_GROUP_MEDIAN_NORMALIZATION_METHOD = "mb";

  /** Keep saturing spots. */
  public static final boolean REMOVE_SATURING_SPOTS = true;
  /** Remove saturing spots. */
  public static final boolean KEEP_SATURING_SPOTS = false;

  /** Remove small spots. */
  public static final boolean REMOVE_SMALL_SPOTS = true;
  /** Keep small spots. */
  public static final boolean KEEP_SMALL_SPOTS = false;

  /** Flag filter all. */
  public static final String FLAG_FILTER_ALL = "all";
  /** Flag filter none. */
  public static final String FLAG_FILTER_NONE = "none";

  /** PDF output. */
  public static final int PDF_OUTPUT = 1;
  /** PNG output. */
  public static final int PNG_OUTPUT = 2;
  /** JPEG output. */
  public static final int JPEG_OUTPUT = 3;

  /** Default saturing threshold. */
  public static final int DEFAULT_SATURING_THRESHOLD = 50000;
  /** Default diameter threshold. */
  public static final int DEFAULT_DIAMETER_THRESHOLD = 0;
  /** Default alter print-tip. */
  public static final int DEFAULT_ALERT_PRINT_TIP = 200;
  /** Default flag filter. */
  public static final String DEFAULT_FLAG_FILTER = FLAG_FILTER_ALL;

  private BioAssay bioAssay;
  private String prefixGPRFilename;

  private BioAssay normalizedBioAssay;
  private String script;
  private final RSConnection con;

  // Goulphar parameters

  private int foregroundMethod = FOREGROUND_METHOD_MEDIAN;
  private int backgroundSubstraction = BACKGROUND_NO_SUBSTRACTION;
  // private String normalisationMethod = GLOBAL_LOWESS_NORMALISATION_METHOD;
  private String normalisationMethod =
      GLOBAL_LOWESS_AND_PRINT_TIP_GROUP_NORMALIZATION_METHOD;
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
   * Get the Normalized BioAssay to use.
   * @return Returns the Normalized bioAssay
   */
  public BioAssay getNormalizedBioAssay() {

    if (this.bioAssay == null && !this.designMode)
      return null;

    if (this.normalizedBioAssay == null)
      loadNormalizedBioAssay();

    return this.normalizedBioAssay;
  }

  /**
   * Get the PDF file as array.
   * @return Returns the PDF file as array
   */
  public byte[] getPdf() {

    try {

      System.out.println(this.prefixGPRFilename + ".gpr.pdf");
      byte[] fileasarray =
          this.con.getFileAsArray(this.prefixGPRFilename + ".gpr.pdf");

      if (fileasarray == null) {
        return null;
      }

      return fileasarray;
    }

    catch (RSException e) {
      throw new NividicRuntimeException("Unable to get the report");
    }

  }

  /**
   * Get the Png Images As ByteArray List.
   * @return Returns the Png Images as bytearray list
   * @throws RSException
   */
  public byte[] getPng(String filename) throws RSException {
    byte[] png = this.con.getFileAsArray(filename);
    return png;

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

      final BioAssay ba = getNormalizedBioAssay();

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

    // Avoid issues with '/' in file names
    this.prefixGPRFilename = prefix.replace('/', '\\');
    final String gpr = this.prefixGPRFilename + ".gpr";
    final String param = createParameterFileContent(gpr);

    try {

      final OutputStream os = con.getFileOutputStream(gpr);

      if (slide != null) {

        // Test if bioAssay in memory
        if (slide.getBioAssay() == null) {

          // Find the format of the source
          final DataSource source = slide.getSource();
          final BioAssayFormatFinderInputStream baffis =
              new BioAssayFormatFinderInputStream(source.getInputStream());

          final BioAssayFormat format = baffis.getBioAssayFormat();

          if (format == BioAssayFormatRegistery.GPR_BIOASSAY_FORMAT) {

            // Transmit GPR
            try {
              NividicUtils.writeInputStream(source.getInputStream(), os);
            } catch (IOException e) {
              throw new NividicIOException(e);
            }
          } else if (format == BioAssayFormatRegistery.AGILENT_BIOASSAY_FORMAT) {

            // Convert Agilent result in a GPR bioAssay
            slide.loadSource();
            final BioAssay ba =
                BioAssayConverter.convertAgilentToGPR(slide.getBioAssay());

            // Writer the bioAssay on Rserve server without set the bioAssay in
            // the slide
            final GPRWriter writer = new GPRWriter(os);
            writer.addAllFieldsToWrite();
            writer.write(ba);
          } else
            new NividicIOException("Unable to find the format of the source");

        } else {

          // The bioAssay is in memory (and must be a GPR bioAssay
          GPRWriter writer = new GPRWriter(os);
          writer.addAllFieldsToWrite();
          writer.write(slide.getBioAssay());

          // Keep in memory data
          // slide.setBioAssay(null);
        }

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

    this(null);
  }

  /**
   * Public constructor.
   * @param serverName RServe server name
   */
  public GoulpharWrapper(final String serverName) {

    this.con = RSConnectionFactory.createConnection(serverName);
    loadScript();
  }

}
