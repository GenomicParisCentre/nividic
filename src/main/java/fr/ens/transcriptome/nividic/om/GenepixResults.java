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

package fr.ens.transcriptome.nividic.om;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * A wrapper for a BioAssay object which provides methods for an easy access to
 * the object properties for real GPR data.
 * @author Laurent Jourdren
 */
public final class GenepixResults {

  /** The wrapped object. */
  private BioAssay bioAssay;

  private static final String GPR_MAGIC_STRING = "GenePix Results";

  private static final String GPR_VERSION_1 = "GenePix Results 1.";
  private static final String GPR_VERSION_3 = "GenePix Results 3";

  private static final DateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
  private static final String GPR_HEADER_TYPE = "Type";
  private static final String GPR_HEADER_DATE_TIME = "DateTime";
  private static final String GPR_HEADER_SETTINGS = "Settings";
  private static final String GPR_HEADER_GALFILE = "GalFile";
  private static final String GPR_HEADER_PIXEL_SIZE = "PixelSize";
  private static final String GPR_HEADER_WAVELENGTH = "Wavelengths";
  private static final String GPR_HEADER_IMAGE_FILES = "ImageFiles";
  private static final String GPR_HEADER_NORMALIZATION_METHOD =
      "NormalizationMethod";
  private static final String GPR_HEADER_NORMALIZATION_FACTORS =
      "NormalizationFactors";
  private static final String GPR_HEADER_JPEG_IMAGE = "JpegImage";
  private static final String GPR_HEADER_STD_DEV = "StdDev";
  private static final String GPR_HEADER_RATIO_FORMULATION = "RatioFormulation";
  private static final String GPR_HEADER_FEATURE_TYPE = "FeatureType";
  private static final String GPR_HEADER_BARCODE = "Barcode";
  private static final String GPR_HEADER_BACKGROUND_SUBSTRACTION =
      "BackgroundSubstraction";
  private static final String GPR_HEADER_IMAGE_ORIGIN = "ImageOrigin";
  private static final String GPR_HEADER_CREATOR = "Creator";
  private static final String GPR_HEADER_SCANNER = "Scanner";
  private static final String GPR_HEADER_FOCUS_POSITION = "FocusPosition";
  private static final String GPR_HEADER_TEMPERATOR = "Temperature";
  private static final String GPR_HEADER_LINES_AVERAGED = "LinesAveraged";
  private static final String GPR_HEADER_COMMENT = "Comment";
  private static final String GPR_HEADER_PMT_GAIN = "PMTGain";
  private static final String GPR_HEADER_SCAN_POWER = "ScanPower";
  private static final String GPR_HEADER_LASER_POWER = "LaserPower";
  private static final String GPR_HEADER_LASER_ON_TIME = "LaserOnTime";
  private static final String GPR_HEADER_FILTERS = "FILTERS";
  private static final String GPR_HEADER_SCAN_REGION = "ScanRegion";
  private static final String GPR_HEADER_SUPPLIER = "Supplier";
  private static final String GPR_HEADER_ARRAY_NAME = "ArrayName";
  private static final String GPR_HEADER_DESIGN_ID = "DesignID";

  /** Spot shape circular. */
  public static final int SPOT_SHAPE_CIRCULAR = 0;
  /** Spot shape square. */
  public static final int SPOT_SHAPE_SQUARE = 1;
  /** Spot shape irregular not filled. */
  public static final int SPOT_SHAPE_IRREGULAR_NOT_FILLED = 2;
  /** Spot shape irregular filled. */
  public static final int SPOT_SHAPE_IRREGULAR_FILLED = 3;

  private static final String SPOT_SHAPE_NAME_CIRCULAR = "Circular";
  private static final String SPOT_SHAPE_NAME_SQUARE = "Square";
  private static final String SPOT_SHAPE_NAME_IRREGULAR_NOT_FILLED =
      "Irregular, not Filled";
  private static final String SPOT_SHAPE_NAME_IRREGULAR_FILLED =
      "Irregular, Filled";

  /** Filter empty. */
  public static final String FILTER_EMPTY = "<Empty>";
  /** None normalization methods. */
  public static final String NORMALIZATION_METHODS_NONE = "None";
  /** Standardization ration formulation. */
  public static final String STANDARD_RATIO_FORMULATION = "W1/W2 (635/532)";

  //
  // Method for access to the wrapped object
  //

  /**
   * Return the wrapped object.
   * @return The BioAssay object wrapped in this object.
   */
  public BioAssay getBioAssay() {
    return bioAssay;
  }

  /**
   * Set the wrapped object.
   * @param bioAssay The object to be wrapped
   */
  private void setBioAssay(final BioAssay bioAssay) {
    this.bioAssay = bioAssay;
  }

  //
  // Getters
  //

  /**
   * Return the background substraction method selected.
   * @return The background substraction.
   */
  public String getBackgroundSubstraction() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_BACKGROUND_SUBSTRACTION);
  }

  /**
   * Return the barcode symbols read from the image if exits.
   * @return The barcode
   */
  public String getBarcode() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_BARCODE);
  }

  /**
   * Return the user-entered comment.
   * @return The user-entered comment
   */
  public String getComment() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_COMMENT);
  }

  /**
   * Return the name and the version of the software used to create data.
   * @return The software creator of the data
   */
  public String getCreator() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_CREATOR);
  }

  /**
   * Return the date of the creation of the data.
   * @return The creation date of the data
   */
  public Date getDateTime() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    String date = annotation.getProperty(GPR_HEADER_DATE_TIME);

    try {
      return DATE_FORMAT.parse(date);
    } catch (ParseException e) {
      return null;
    }

  }

  /**
   * Return the feature shape type of the spots of the image.
   * @return The feature type of the spots
   */
  public int getFeatureType() {

    int id = -1;

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return -1;

    String name = annotation.getProperty(GPR_HEADER_FEATURE_TYPE);

    if (name == null)
      return id;

    name = name.trim();

    if (SPOT_SHAPE_NAME_CIRCULAR.equals(name))
      id = SPOT_SHAPE_CIRCULAR;
    else if (SPOT_SHAPE_NAME_SQUARE.equals(name))
      id = SPOT_SHAPE_SQUARE;
    else if (SPOT_SHAPE_NAME_IRREGULAR_FILLED.equals(name))
      id = SPOT_SHAPE_IRREGULAR_FILLED;
    else if (SPOT_SHAPE_NAME_IRREGULAR_NOT_FILLED.equals(name))
      id = SPOT_SHAPE_IRREGULAR_NOT_FILLED;

    return id;
  }

  /**
   * Get the emission filters during acquisition.
   * @return An array of String containing the names of the filters
   */
  public String[] getFilters() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    String result = annotation.getProperty(GPR_HEADER_FILTERS);
    if (result == null)
      return null;

    return result.split("\t");
  }

  /**
   * Get the focus position setting of scanner used to acquire the image.
   * @return The focus position
   */
  public int getFocusPosition() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return -1;

    final String result = annotation.getProperty(GPR_HEADER_FOCUS_POSITION);

    if ("".equals(result))
      return 0;

    return Integer.parseInt(result);
  }

  /**
   * Get the Genepix Array List file used to associate names and ids to each
   * entry.
   * @return The GAL file
   */
  public String getGALFile() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_GALFILE);
  }

  /**
   * Get the name and the path of the associated TIFF file(s).
   * @return The names of the images files
   */
  public String getImageFiles() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_IMAGE_FILES);
  }

  /**
   * Get the origin of the image relative to the scan aera.
   * @return The origin of the image
   */
  public String getImageOrigin() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_IMAGE_ORIGIN);
  }

  /**
   * Get the origin of the result JPEG image.
   * @return The origin of the JPEG image
   */
  public String getJPEGImage() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_JPEG_IMAGE);
  }

  /**
   * Get the laser on-time for each laser, in minutes.
   * @return The laser on-time.
   */
  public int[] getLaserOnTimes() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    String s = annotation.getProperty(GPR_HEADER_LASER_ON_TIME);

    return s == null ? null : NividicUtils.toArrayInt(s.split("\t"));
  }

  /**
   * Get the line average setting used to acquire the image.
   * @return The line average setting used to acquire the image
   */
  public String getLinesAveraged() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_LINES_AVERAGED);
  }

  /**
   * Get the normalization factor applied to each channel.
   * @return The normalization factor
   */
  public String getNormalizationFactors() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_NORMALIZATION_FACTORS);
  }

  /**
   * Get the normalization method used, if applicable.
   * @return the normalization method used
   */
  public String getNormalisationMethod() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_NORMALIZATION_METHOD);
  }

  /**
   * Get the pixel resolution of each pixel in micrometers.
   * @return The pixel resolution size
   */
  public String getPixelSize() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_PIXEL_SIZE);
  }

  /**
   * Get thePMT setting during the acquisition.
   * @return The PMT setting
   */
  public int[] getPMTGains() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    final String field;

    if (getType()!=null && getType().startsWith(GPR_VERSION_1))
      field = "PMTVolts";
    else
      field = GPR_HEADER_PMT_GAIN;

    final String s = annotation.getProperty(field);

    return s == null ? null : NividicUtils.toArrayInt(s.split("\t"));
  }

  /**
   * Get the ration formulation.
   * @return The ratio formulation
   */
  public String getRatioFormulation() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_RATIO_FORMULATION);
  }

  /**
   * Get the amount of laser transmission during acquisition.
   * @return The amount of laser transmission during acquisition
   */
  public int[] getScanPowers() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    String s = annotation.getProperty(GPR_HEADER_SCAN_POWER);

    return s == null ? null : NividicUtils.toArrayInt(s.split("\t"));
  }

  /**
   * Get the amount of laser transmission during acquisition.
   * @return The amount of laser transmission during acquisition
   */
  public double[] getLaserPowers() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    String s = annotation.getProperty(GPR_HEADER_LASER_POWER);

    return s == null ? null : NividicUtils.toArrayDouble(s.split("\t"));
  }

  /**
   * Get the coordinate values of the scan used during the acquisition (in
   * pixels).
   * @return The coordinates of the scan
   */
  public String getScanRegion() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_SCAN_REGION);
  }

  /**
   * Get the type and the serial number of the scanner used to acquire the
   * image.
   * @return The type and the serial number of the scanner used to acquire the
   *         image
   */
  public String getScanner() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_SCANNER);
  }

  /**
   * Get the name of the setting file that was used for analysis.
   * @return The name of the setting file that was used for analysis
   */
  public String getSettings() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_SETTINGS);
  }

  /**
   * Get the type of the standard deviation calculation selected in options
   * settings.
   * @return The type of the standard deviation calculation
   */
  public String getStdDev() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_STD_DEV);
  }

  /**
   * Get the header file supplied in GAL file.
   * @return The header file supplied in GAL file
   */
  public String getSupplier() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_SUPPLIER);
  }

  /**
   * The temperature of the scanner in degrees C.
   * @return The temperature of scanner in degrees C.
   */
  public String getTemperator() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_TEMPERATOR);
  }

  /**
   * Get the type of ATF file.
   * @return The type of ATF file
   */
  public String getType() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_TYPE);
  }

  /**
   * Get the installed laser excitation sources im nm.
   * @return The installed laser excitation sources.
   */
  public int[] getWaveLengths() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    final String field;

    if (getType()!=null && getType().startsWith(GPR_VERSION_1))
      field = "ImageName";
    else
      field = GPR_HEADER_WAVELENGTH;

    final String s = annotation.getProperty(field).replace(" nm", "");

    return s == null ? null : NividicUtils.toArrayInt(s.split("\t"));
  }

  /**
   * Get the name of the array.
   * @return The name of the array
   */
  public String getArrayName() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_ARRAY_NAME);
  }

  /**
   * Get the id of the design.
   * @return The id of the design
   */
  public String getDesignId() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GPR_HEADER_DESIGN_ID);
  }

  //
  // Setters
  //

  /**
   * Set the background substraction method selected.
   * @param method The method used
   */
  public void setBackgroundSubstraction(final String method) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_BACKGROUND_SUBSTRACTION, method);
  }

  /**
   * Set the barcode value.
   * @param barcode Barcode value to set
   */
  public void setBarcode(final String barcode) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_BARCODE, barcode);
  }

  /**
   * Set the comment.
   * @param comment Commment to set
   */
  public void setComment(final String comment) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_COMMENT, comment);
  }

  /**
   * Set the software creator of the data
   * @param creator The creator of the data
   */
  public void setCreator(final String creator) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_CREATOR, creator);
  }

  /**
   * Set the date of the gpr.
   * @param date The date to set
   */
  public void setDateTime(final Date date) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    final String value;

    if (date == null)
      value = "";
    else

      value = DATE_FORMAT.format(date);

    annotation.setProperty(GPR_HEADER_DATE_TIME, value);
  }

  /**
   * Set the feature type.
   * @param type Fearure type
   */
  public void setFeatureType(final String type) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_FEATURE_TYPE, type);
  }

  /**
   * Set the filters.
   * @param filter Filter to set
   */
  public void setFilters(final String filter) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_FILTERS, filter);
  }

  /**
   * Set the focus position.
   * @param focusPosition The focus position to set
   */
  public void setFocusPosition(final String focusPosition) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_FOCUS_POSITION, focusPosition);
  }

  /**
   * Set GAL file.
   * @param galFile The GAL file to set
   */
  public void setGALFile(final String galFile) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_GALFILE, galFile);
  }

  /**
   * Set the images files.
   * @param imagesFiles Images files to set
   */
  public void setImageFiles(final String imagesFiles) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_IMAGE_FILES, imagesFiles);
  }

  /**
   * Set the origin of the image relative to the scan area.
   * @param imageOrigin Origin to set
   */
  public void setImageOrigin(final String imageOrigin) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_IMAGE_ORIGIN, imageOrigin);
  }

  /**
   * Set the name and the path of the JPEG image;
   * @param jpegImage Path to set
   */
  public void setJPEGImage(final String jpegImage) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_JPEG_IMAGE, jpegImage);
  }

  /**
   * Set the laser on-time for each laser (in minutes).
   * @param laserOnTimes Laser on-time to set
   */
  public void setLaserOnTimes(final int[] laserOnTimes) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (laserOnTimes == null)
      return;

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < laserOnTimes.length; i++) {

      if (i > 0)
        sb.append("\t");
      sb.append(laserOnTimes[i]);
    }

    annotation.setProperty(GPR_HEADER_LASER_ON_TIME, sb.toString());
  }

  /**
   * Set the line averaged setting used to acquire the image
   * @param lineAveraged The line average to set
   */
  public void setLinesAveraged(final String lineAveraged) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_LINES_AVERAGED, lineAveraged);
  }

  /**
   * Set the normalization factor
   * @param normalizationFactor The normalization factor to set
   */
  public void setNormalizationFactors(final String normalizationFactor) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_NORMALIZATION_FACTORS,
        normalizationFactor);
  }

  /**
   * Set the mormalization method.
   * @param method Normalization method to set
   */
  public void setNormalizationMethod(final String method) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_NORMALIZATION_METHOD, method);
  }

  /**
   * Set the size of the pixels.
   * @param size The size to set
   */
  public void setPixelSize(final String size) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_PIXEL_SIZE, size);
  }

  /**
   * Set the PMT gain.
   * @param pmts The PMT gain to set
   */
  public void setPMTGains(final int[] pmts) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (pmts == null)
      return;

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < pmts.length; i++) {

      if (i > 0)
        sb.append("\t");
      sb.append(pmts[i]);
    }

    annotation.setProperty(GPR_HEADER_PMT_GAIN, sb.toString());
  }

  /**
   * Set the ratio formulation.
   * @param ratioFormulation The ratio formulation to set
   */
  public void setRatioFormulation(final String ratioFormulation) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_RATIO_FORMULATION, ratioFormulation);
  }

  /**
   * Set the scan power.
   * @param powers The scan power to set
   */
  public void setScanPowers(final int[] powers) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (powers == null)
      return;

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < powers.length; i++) {

      if (i > 0)
        sb.append("\t");
      sb.append(powers[i]);
    }

    annotation.setProperty(GPR_HEADER_SCAN_POWER, sb.toString());
  }

  /**
   * Set the scan power.
   * @param powers The scan power to set
   */
  public void setLaserPowers(final double[] powers) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (powers == null)
      return;

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < powers.length; i++) {

      if (i > 0)
        sb.append("\t");
      sb.append(powers[i]);
    }

    annotation.setProperty(GPR_HEADER_LASER_POWER, sb.toString());
  }

  /**
   * Set the scan region.
   * @param region The region to set
   */
  public void setScanRegion(final String region) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_SCAN_REGION, region);
  }

  /**
   * Set the scanner.
   * @param scanner Scanner to set
   */
  public void setScanner(final String scanner) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_SCANNER, scanner);
  }

  /**
   * Set the name and the path of the settings file used for analisis.
   * @param gpsPath Path to the gps file.
   */
  public void setSettings(final String gpsPath) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_SETTINGS, gpsPath);
  }

  /**
   * Set the type of standard deviation.
   * @param type The type of standard deviation to set
   */
  public void setStdDev(final String type) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_STD_DEV, type);
  }

  /**
   * Set the supplier.
   * @param supplier The supplaier to set
   */
  public void setSupplier(final String supplier) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_SUPPLIER, supplier);
  }

  /**
   * Set the temperature of the scanner (in degrees C.)
   * @param temp The temperature to set
   */
  public void setTemperator(final String temp) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_TEMPERATOR, temp);
  }

  /**
   * Set the type of ATF file.
   * @param atfType The type of ATF file to set
   */
  public void setType(final String atfType) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GPR_HEADER_TYPE, atfType);
  }

  /**
   * Set the wave length.
   * @param waveLengths The waves lengths
   */
  public void setWaveLength(final int[] waveLengths) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (waveLengths == null)
      return;

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < waveLengths.length; i++) {

      if (i > 0)
        sb.append("\t");
      sb.append(waveLengths[i]);
    }

    annotation.setProperty(GPR_HEADER_WAVELENGTH, sb.toString());
  }

  /**
   * Set the name of the array
   * @param arrayName The arrayname
   */
  public void setArrayName(final String arrayName) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (arrayName == null)
      return;

    annotation.setProperty(GPR_HEADER_ARRAY_NAME, arrayName);
  }

  /**
   * Set the id of the design
   * @param designId The design Id
   */
  public void setDesignId(final String designId) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    if (designId == null)
      return;

    annotation.setProperty(GPR_HEADER_DESIGN_ID, designId);
  }

  //
  // Other methods
  //

  /**
   * Return a value containg the name of the feature type.
   * @param featureType Identifier of the feature type.
   * @return a string containg the name of the feature type or null if the
   *         identifier is bad.
   */
  public static String getNameFeatureType(final int featureType) {

    switch (featureType) {
    case SPOT_SHAPE_CIRCULAR:
      return SPOT_SHAPE_NAME_CIRCULAR;
    case SPOT_SHAPE_SQUARE:
      return SPOT_SHAPE_NAME_SQUARE;
    case SPOT_SHAPE_IRREGULAR_NOT_FILLED:
      return SPOT_SHAPE_NAME_IRREGULAR_NOT_FILLED;
    case SPOT_SHAPE_IRREGULAR_FILLED:
      return SPOT_SHAPE_NAME_IRREGULAR_FILLED;
    default:
      return null;
    }
  }

  /**
   * Test if data is data from a GPR
   * @return true if data cames from a GPR
   */
  public boolean isGPRData() {

    String type = getType();

    if (type == null)
      return false;

    return type.startsWith(GPR_MAGIC_STRING);
  }

  //
  // Constructor
  //

  /**
   * Standard constructor.
   * @param bioAssay BioAssay object to be wrapped, must be not null
   * @throws NullPointerException if parameter is null
   */
  public GenepixResults(final BioAssay bioAssay) throws NullPointerException {

    if (bioAssay == null)
      throw new NullPointerException("BioAssay parameter is null");
    setBioAssay(bioAssay);
  }

}
