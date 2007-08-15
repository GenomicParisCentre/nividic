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

package fr.ens.transcriptome.nividic.sgdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.DesignUtils;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.design.io.DesignReader;
import fr.ens.transcriptome.nividic.om.io.GPRWriter;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.r.RSConnection;
import fr.ens.transcriptome.nividic.om.r.RSException;
import fr.ens.transcriptome.nividic.sgdb.io.SGDBLimsDesignReader;

/**
 * This class define a wrapper for Goulphar aroma.
 * @author Laurent Jourdren
 */
public class GoulpharAromaWrapper {

  private static final String GOULPHAR_AROMA_SCRIPT_URL =
      "http://hestia.ens.fr/R/Goulphar_aroma.R";

  public static final String PRINT_TIP_GROUP_LOWESS_NORMALISATION_METHOD = "p";
  public static final String GLOBAL_LOWESS_NORMALISATION_METHOD = "l";
  public static final String GLOBAL_LOWESS_AND_PRINT_TIP_GROUP_NORMALISATION_METHOD =
      "lmb";
  public static final String GLOBAL_MEDIAN_NORMALISATION_METHOD = "m";
  public static final String PRINT_TIP_GROUP_MEDIAN_NORMALISATION_METHOD = "mb";

  private String script;
  private RSConnection con = new RSConnection();

  // Goulphar aroma parameters

  private boolean genepixFiles = true;
  private int saturating = 60000;
  private boolean backgroundSubstraction;
  private boolean filterValues;
  private String normalisationMethod =
      GLOBAL_LOWESS_AND_PRINT_TIP_GROUP_NORMALISATION_METHOD;
  private boolean commaDecimalSeparator;
  private String title = "noname";
  private String arrayType = "rng";

  private Design design;
  private boolean normlalizationDone;

  //
  // Getters
  //

  /**
   * Test if the files are from Genepix.
   * @return Returns the genepixFiles
   */
  public boolean isGenepixFiles() {
    return genepixFiles;
  }

  /**
   * Get the saturating value.
   * @return Returns the saturating
   */
  public int getSaturating() {
    return saturating;
  }

  /**
   * Test if the background substraction is enabled.
   * @return Returns the backgroundSubstraction
   */
  public boolean isBackgroundSubstraction() {
    return backgroundSubstraction;
  }

  /**
   * Test if values must be filtered.
   * @return Returns the filterValues
   */
  public boolean isFilterValues() {
    return filterValues;
  }

  /**
   * Get the normalization method.
   * @return Returns the normalisationMethod
   */
  public String getNormalisationMethod() {
    return normalisationMethod;
  }

  /**
   * Test if the comma is the decimal separator.
   * @return Returns the commaDecimalSeparator
   */
  public boolean isCommaDecimalSeparator() {
    return commaDecimalSeparator;
  }

  /**
   * Get the title of the experiment.
   * @return Returns the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Get the array type.
   * @return Returns the arrayType
   */
  public String getArrayType() {
    return arrayType;
  }

  //
  // Setters
  //

  /**
   * Set if the files are genepixFiles.
   * @param genepixFiles The genepixFiles to set
   */
  public void setGenepixFiles(final boolean genepixFiles) {
    this.genepixFiles = genepixFiles;
  }

  /**
   * Set the value of saturating.
   * @param saturating The saturating to set
   */
  public void setSaturating(final int saturating) {
    this.saturating = saturating;
  }

  /**
   * Enable or not the background substraction.
   * @param backgroundSubstraction The backgroundSubstraction to set
   */
  public void setBackgroundSubstraction(final boolean backgroundSubstraction) {
    this.backgroundSubstraction = backgroundSubstraction;
  }

  /**
   * Enable or not the filtering of values.
   * @param filterValues The filterValues to set
   */
  public void setFilterValues(final boolean filterValues) {
    this.filterValues = filterValues;
  }

  /**
   * Set the normalization method to use.
   * @param normalisationMethod The normalisationMethod to set
   */
  public void setNormalisationMethod(final String normalisationMethod) {
    this.normalisationMethod = normalisationMethod;
  }

  /**
   * Set the decimal separator
   * @param commaDecimalSeparator The commaDecimalSeparator to set
   */
  public void setCommaDecimalSeparator(final boolean commaDecimalSeparator) {
    this.commaDecimalSeparator = commaDecimalSeparator;
  }

  /**
   * Set the title of the experiment.
   * @param title The title to set
   */
  public void setTittle(final String title) {
    this.title = title;
  }

  /**
   * Set the type of array
   * @param arrayType The arrayType to set
   */
  public void setArrayType(final String arrayType) {
    this.arrayType = arrayType;
  }

  //
  // Other methods
  //

  private String createParameterFileContent() {

    final StringBuffer sb = new StringBuffer();

    sb.append("gpr\tswap\tswap.of\tgenepix.flag\tsat\tbgsub\tcontrolspot\t"
        + "duplicatedspot\tfilter.value\t"
        + "norma\tdec\ttitle\thighlight\ttype2plot\tarraytype\n");

    for (int i = 0; i < this.design.getSlideCount(); i++) {

      Slide slide = this.design.getSlide(i);

      sb.append((i + 1));
      sb.append(".gpr\t");
      sb.append(slide.getDescription().getSwap());

      if (i != 0) {
        sb.append("\n");
        continue;
      }

      // swap.of
      sb.append("\tNA\t");
      sb.append(isGenepixFiles() ? 1 : 0);
      sb.append("\t");
      sb.append(getSaturating());
      sb.append("\t");
      sb.append(isBackgroundSubstraction() ? 1 : 0);
      // controlspot + duplicatedspot
      sb.append("\tNA\tNA\t");
      sb.append(isFilterValues() ? 1 : 0);
      sb.append("\t");
      sb.append(getNormalisationMethod());
      sb.append("\t");
      sb.append(isCommaDecimalSeparator() ? ',' : '.');
      sb.append("\t");
      sb.append(getTitle());
      sb.append("\tNA\tNA\t");
      sb.append(getArrayType());
      sb.append("\n");

    }

    return sb.toString();
  }

  private void loadScript() {

    try {

      InputStream is = new URL(GOULPHAR_AROMA_SCRIPT_URL).openStream();

      BufferedReader in = new BufferedReader(new InputStreamReader(is));

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

  private void writeDesignBioAssays(final Design design)
      throws NividicIOException, RSException {

    for (int i = 0; i < design.size(); i++) {

      Slide slide = design.getSlide(i);

      OutputStream os = con.getFileOutputStream((i + 1) + ".gpr");
      GPRWriter gw = new GPRWriter(os);
      gw.addAllFieldsToWrite();

      boolean bioAssayAlreadyLoaded = false;

      if (slide.getBioAssay() == null)
        slide.loadSource();
      else
        bioAssayAlreadyLoaded = true;

      gw.write(slide.getBioAssay());

      if (bioAssayAlreadyLoaded)
        slide.setBioAssay(null);
    }

  }

  /**
   * Normalize all the bioAssays of a design
   * @param design design to use
   */
  public void normalize(final Design design) {

    clean();
    this.design = design;
    this.normlalizationDone = false;

    if (this.script == null)
      throw new NividicRuntimeException("Unable to load Goulphar aroma script");

    if (design == null)
      return;

    String param = createParameterFileContent();

    try {

      writeDesignBioAssays(design);
      con.writeStringAsFile("param.dat", param);
      con.executeRCode(this.script);
      this.normlalizationDone = true;

    } catch (RSException e) {
      e.printStackTrace();
    } catch (NividicIOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   * Load normalized files after normalization
   */
  public void loadNormalizedBioAssays() {

    if (this.design == null || !this.normlalizationDone)
      return;

    try {

      for (int i = 0; i < design.size(); i++) {

        Slide slide = design.getSlide(i);

        InputStream is = this.con.getFileInputStream((i + 1) + ".gpr");

        BioAssay ba = new IDMAReader(is).read();

        slide.setBioAssay(ba);
      }

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

    if (this.design == null || !this.normlalizationDone)
      return;

    String pdfFilename = "report.pdf";

    try {
      this.con.getFile(pdfFilename, outputfile);
    } catch (RSException e) {
      throw new NividicRuntimeException(
          "An error occur while downloading the file");
    }

  }

  /**
   * Save the report.
   * @param outputfile Output file
   */
  public void saveAboutSpotsAndTresholdReport(final File outputfile) {

    String reportFilename = "about_spots_and_threshold.txt";

    try {
      this.con.getFile(reportFilename, outputfile);
    } catch (RSException e) {
      throw new NividicRuntimeException(
          "An error occur while downloading the file");
    }

  }

  /**
   * Disconnect from RServe.
   */
  public void disConnect() {

    clean();
    this.con.disConnect();
  }

  /**
   * Clean imported and generated file on the RServer.
   */
  public void clean() {

    if (this.design == null || !this.normlalizationDone)
      return;

    List<String> files = new ArrayList<String>();

    String[] singleFiles =
        new String[] {"A_and_M_slide_boxplot.png",
            "about_spots_and_threshold.txt", "after_norm_results.txt",
            "distributions.png", "filtered_and_unique_results.txt",
            "final_results.txt", "medianA_Histogram.png",
            "medianMnorm_vs_medianA.png", "Mnorm_matrix.txt", "param.dat",
            "report.pdf", "summary.txt", "total.summary.txt",
            "spatial_AThreshold.png", "A_threshold,boxplot.png"};

    files.addAll(Arrays.asList(singleFiles));

    final int n = design.size();
    for (int i = 0; i < n; i++) {

      int index = i + 1;

      files.add(index + ".gpr");
      files.add("bloc_" + index + "_boxplots.png");
      files.add("hist" + index + ".png");
      files.add("mamanorm_" + index + ".png");
      files.add("normalised_chip" + index + ".txt");
      files.add("NotFound" + index + ".txt");
      files.add("spatial_" + index + ".png");

    }

    for (String filename : files)
      try {
        this.con.removeFile(filename);
      } catch (RSException e) {
        throw new NividicRuntimeException("An error occur while remove a file");
      }

  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public GoulpharAromaWrapper() {

    loadScript();
  }

  //
  // Main file
  //

  public static final void main(final String[] args) throws NividicIOException {

    DesignReader designReader =
        new SGDBLimsDesignReader(new String[] {"RNG AVE010"});

    Design d = designReader.read();
    // DesignUtils.showDesign(d);

    GoulpharAromaWrapper goulphar = new GoulpharAromaWrapper();
    goulphar.normalize(d);
    goulphar.saveAboutSpotsAndTresholdReport(new File("/tmp/zuzu.txt"));
    goulphar.saveNormalizationReport(new File("/tmp/zaza.pdf"));

    goulphar.disConnect();
  }

}
