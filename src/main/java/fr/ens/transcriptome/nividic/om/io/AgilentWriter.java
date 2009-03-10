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

package fr.ens.transcriptome.nividic.om.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.ens.transcriptome.nividic.Globals;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class implement a BioAssayWriter for Agilent streams. There is some
 * issue to have the same float format that the original format.
 * @author Laurent Jourdren
 */
public class AgilentWriter extends BioAssayWriter {

  private static final String ROW_FIELD = "Row";
  private static final String COLUMN_FIELD = "Col";
  private static final String SEPARATOR = "\t";

  private static final String ROW_TYPE_ID = "TYPE";
  private static final String ROW_DATA_ID = "DATA";
  private static final String ROW_END_ID = "*";

  private static final int TEXT_TYPE_ID = 1;
  private static final int INTEGER_TYPE_ID = 2;
  private static final int FLOAT_TYPE_ID = 3;
  private static final int BOOLEAN_TYPE_ID = 4;

  private static final String TEXT_TYPE = "text";
  private static final String INTEGER_TYPE = "integer";
  private static final String FLOAT_TYPE = "float";
  private static final String BOOLEAN_TYPE = "boolean";

  private static final DecimalFormat df =
      new DecimalFormat("0.#########E000", new DecimalFormatSymbols(Locale.US));
  private static final DecimalFormat df2 =
      new DecimalFormat("0.000000000E000", new DecimalFormatSymbols(Locale.US));

  private static final String FEPARAMS_BLOCK = "FEPARAMS";
  private static final String STATS_BLOCK = "STATS";
  private static final String FEATURES_BLOCK = "FEATURES";

  private static final int[] FEPARAMS_TYPES =
      {TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, TEXT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          TEXT_TYPE_ID, TEXT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, INTEGER_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID,
          INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, FLOAT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID, TEXT_TYPE_ID,
          TEXT_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID,
          TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID};

  private static final String[] FEPARAMS_NAMES =
      {"FeatureExtractor_UserName", "FeatureExtractor_ComputerName",
          "FeatureExtractor_ExtractionTime", "SpotFinder",
          "SpotFinder_Version", "SpotFinder_NumRows", "SpotFinder_NumCols",
          "SpotFinder_DevLimit", "SpotFinder_ROIW", "SpotFinder_ROIH",
          "SpotFinder_NomSpotWidth", "SpotFinder_NomSpotHeight",
          "CornerMethod", "SpotFinder_Baseline", "SpotFinder_AutoFindTh",
          "CornerUL_x", "CornerUL_y", "CornerUR_x", "CornerUR_y", "CornerLL_x",
          "CornerLL_y", "CalcSpotStats", "CalcSpotStats_Version",
          "CalcSpotStats_SpotStatsMethod", "CalcSpotStats_RejectMethod",
          "CalcSpotStats_StatBoundFeat", "CalcSpotStats_StatBoundBG",
          "CalcSpotStats_CalculateSpotSize", "CalcSpotStats_CookieWidth",
          "CalcSpotStats_CookieHeight", "CalcSpotStats_BGInnerWidth",
          "CalcSpotStats_BGInnerHeight", "CalcSpotStats_BGOuterWidth",
          "CalcSpotStats_BGOuterHeight", "OutlierFlagger",
          "OutlierFlagger_Version", "OutlierFlagger_NonUnifOLOn",
          "OutlierFlagger_FeatATerm", "OutlierFlagger_FeatBTerm",
          "OutlierFlagger_FeatCTerm", "OutlierFlagger_BGATerm",
          "OutlierFlagger_BGBTerm", "OutlierFlagger_BGCTerm",
          "OutlierFlagger_ConfIntPValue", "OutlierFlagger_PopnOLOn",
          "OutlierFlagger_IQRatio", "OutlierFlagger_MinPopulation",
          "BGSubtractor", "BGSubtractor_Version", "BGSubtractor_BGSubMethod",
          "BGSubtractor_MaxPVal", "BGSubtractor_WellAboveMulti",
          "BGSubtractor_BackgroundCorrectionOn",
          "BGSubtractor_SpatialDetrendOn", "BGSubtractor_DetrendLowPassFilter",
          "BGSubtractor_DetrendLowPassPercentage",
          "BGSubtractor_DetrendLowPassWindow",
          "BGSubtractor_DetrendLowPassIncrement",
          "BGSubtractor_DetrendNeighborhoodSize", "DyeNorm", "DyeNorm_Version",
          "DyeNorm_SelectMethod", "DyeNorm_IsBGPopnOLOn", "DyeNorm_CorrMethod",
          "DyeNorm_RankTolerance", "DyeNorm_LOWESSSmoothFactor",
          "DyeNorm_LOWESSNumSteps", "DyeNorm_LOWESSDelta", "Ratio",
          "Ratio_Version", "Ratio_ErrorModel", "Ratio_AddErrorRed",
          "Ratio_AddErrorGreen", "Ratio_MultErrorRed", "Ratio_MultErrorGreen",
          "Ratio_UseSurrogates", "Ratio_UsePropErrOnly",
          "Ratio_AutoEstimateAddErrorRed", "Ratio_AutoEstimateAddErrorGreen",
          "Ratio_MultNcAutoEstimate", "Ratio_MultRMSAutoEstimate",
          "FeatureExtractor", "FeatureExtractor_Version",
          "FeatureExtractor_ArrayName", "FeatureExtractor_ScanFileName",
          "FeatureExtractor_ScanFileGUID", "FeatureExtractor_Barcode",
          "FeatureExtractor_DesignFileName",
          "FeatureExtractor_PrintingFileName"};

  private static final int[] STATS_TYPES =
      {FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID};

  private static final String[] STATS_NAMES =
      {"gDarkOffsetAverage", "gDarkOffsetMedian", "gDarkOffsetStdDev",
          "gDarkOffsetNumPts", "rDarkOffsetAverage", "rDarkOffsetMedian",
          "rDarkOffsetStdDev", "rDarkOffsetNumPts", "gNumSatFeat",
          "gLocalBGInlierAve", "gLocalBGInlierSDev", "gLocalBGInlierNum",
          "gGlobalBGInlierAve", "gGlobalBGInlierSDev", "gGlobalBGInlierNum",
          "rNumSatFeat", "rLocalBGInlierAve", "rLocalBGInlierSDev",
          "rLocalBGInlierNum", "rGlobalBGInlierAve", "rGlobalBGInlierSDev",
          "rGlobalBGInlierNum", "gNumFeatureNonUnifOL", "gNumPopnOL",
          "gNumNonUnifBGOL", "gNumPopnBGOL", "gOffsetUsed",
          "gGlobalFeatInlierAve", "gGlobalFeatInlierSDev",
          "gGlobalFeatInlierNum", "rNumFeatureNonUnifOL", "rNumPopnOL",
          "rNumNonUnifBGOL", "rNumPopnBGOL", "rOffsetUsed",
          "rGlobalFeatInlierAve", "rGlobalFeatInlierSDev",
          "rGlobalFeatInlierNum", "AllColorPrcntSat", "AnyColorPrcntSat",
          "AnyColorPrcntFeatNonUnifOL", "AnyColorPrcntBGNonUnifOL",
          "AnyColorPrcntFeatPopnOL", "AnyColorPrcntBGPopnOL",
          "TotalPrcntFeatOL", "gNumNegBGSubFeat", "rNumNegBGSubFeat",
          "gLinearDyeNormFactor", "rLinearDyeNormFactor",
          "DyeNormDimensionlessRMS", "DyeNormUnitWeightedRMS",
          "rSpatialDetrendRMSFit", "rSpatialDetrendRMSFilteredMinusFit",
          "rSpatialDetrendSurfaceArea", "rSpatialDetrendVolume",
          "rSpatialDetrendAveFit", "gSpatialDetrendRMSFit",
          "gSpatialDetrendRMSFilteredMinusFit", "gSpatialDetrendSurfaceArea",
          "gSpatialDetrendVolume", "gSpatialDetrendAveFit"};

  private static final int[] COLS_TYPES =
      {INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID, TEXT_TYPE_ID,
          TEXT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, INTEGER_TYPE_ID, INTEGER_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          INTEGER_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID, FLOAT_TYPE_ID,
          FLOAT_TYPE_ID, FLOAT_TYPE_ID};

  private static final String[] COLS_NAMES =
      {"FeatureNum", "Row", "Col", "ProbeUID", "ControlType", "ProbeName",
          "GeneName", "SystematicName", "Description", "PositionX",
          "PositionY", "LogRatio", "LogRatioError", "PValueLogRatio",
          "gSurrogateUsed", "rSurrogateUsed", "gIsFound", "rIsFound",
          "gProcessedSignal", "rProcessedSignal", "gProcessedSigError",
          "rProcessedSigError", "gNumPixOLHi", "rNumPixOLHi", "gNumPixOLLo",
          "rNumPixOLLo", "gNumPix", "rNumPix", "gMeanSignal", "rMeanSignal",
          "gMedianSignal", "rMedianSignal", "gPixSDev", "rPixSDev",
          "gBGNumPix", "rBGNumPix", "gBGMeanSignal", "rBGMeanSignal",
          "gBGMedianSignal", "rBGMedianSignal", "gBGPixSDev", "rBGPixSDev",
          "gNumSatPix", "rNumSatPix", "gIsSaturated", "rIsSaturated",
          "PixCorrelation", "BGPixCorrelation", "gIsFeatNonUnifOL",
          "rIsFeatNonUnifOL", "gIsBGNonUnifOL", "rIsBGNonUnifOL",
          "gIsFeatPopnOL", "rIsFeatPopnOL", "gIsBGPopnOL", "rIsBGPopnOL",
          "IsManualFlag", "gBGSubSignal", "rBGSubSignal", "gBGSubSigError",
          "rBGSubSigError", "BGSubSigCorrelation", "gIsPosAndSignif",
          "rIsPosAndSignif", "gPValFeatEqBG", "rPValFeatEqBG", "gNumBGUsed",
          "rNumBGUsed", "gIsWellAboveBG", "rIsWellAboveBG", "gBGUsed",
          "rBGUsed", "gBGSDUsed", "rBGSDUsed", "IsNormalization",
          "gDyeNormSignal", "rDyeNormSignal", "gDyeNormError", "rDyeNormError",
          "DyeNormCorrelation", "ErrorModel", "xDev",
          "gSpatialDetrendIsInFilteredSet", "rSpatialDetrendIsInFilteredSet",
          "gSpatialDetrendSurfaceValue", "rSpatialDetrendSurfaceValue"};

  private final FieldNameConverter converter = new AgilentConverterFieldNames();
  private BufferedWriter bw;
  private boolean doubleFormatStrict = false;

  @Override
  protected String getColumnField() {

    return COLUMN_FIELD;
  }

  @Override
  protected FieldNameConverter getFieldNameConverter() {

    return converter;
  }

  @Override
  protected String[] getFieldNamesOrder() {

    return COLS_NAMES;
  }

  @Override
  protected String getMetaColumnField() {
    return null;
  }

  @Override
  protected String getMetaRowField() {
    return null;
  }

  @Override
  protected String getRowField() {

    return ROW_FIELD;
  }

  /**
   * Test if the writer is in doubles format strict.
   * @return Returns the doubleFormatStrict
   */
  public boolean isDoubleFormatStrict() {
    return doubleFormatStrict;
  }

  /**
   * Set the mode of writing doubles.
   * @param doubleFormatStrict The doubleFormatStrict to set
   */
  public void setDoubleFormatStrict(final boolean doubleFormatStrict) {
    this.doubleFormatStrict = doubleFormatStrict;
  }

  @Override
  protected void writeData() throws NividicIOException {

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    final int countCol = getColumnCount();
    final int countRow = getRowColumn();
    final boolean doubleStrict = this.doubleFormatStrict;

    try {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < countRow; i++) {

        sb.append(ROW_DATA_ID + SEPARATOR);

        for (int j = 0; j < countCol; j++) {

          String value = getData(i, j);
          if (value == null)
            value = "";

          switch (getFieldType(j)) {
          case BioAssay.DATATYPE_STRING:
            sb.append(value);
            break;

          case BioAssay.DATATYPE_INTEGER:
            sb.append(value);
            break;

          case BioAssay.DATATYPE_DOUBLE:
            if (value.equals("NaN"))
              sb.append("Error");
            else {

              if (doubleStrict)
                sb.append(formatDoubleExp(Double.parseDouble(value)));
              else
                sb.append(value);
            }
            break;

          // Locations type
          default:
            sb.append(value);
            break;
          }

          if (j == (countCol - 1))
            sb.append("\n");
          else
            sb.append(SEPARATOR);
        }
        this.bw.write(sb.toString());
        sb.delete(0, sb.length());
      }

      this.bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

  }

  private static String writeAgilentHeader(final Annotation annot,
      final String[] names, final int[] types, final String headerName,
      final boolean doubleFormatStrict) {

    StringBuilder rowTypes = new StringBuilder();
    StringBuilder rowNames = new StringBuilder();
    StringBuilder rowValues = new StringBuilder();

    rowTypes.append(ROW_TYPE_ID);
    rowNames.append(headerName);
    rowValues.append(ROW_DATA_ID);

    for (int i = 0; i < names.length; i++) {

      final String property = names[i];
      if (annot.containsProperty(property)) {

        rowTypes.append(SEPARATOR);
        rowNames.append(SEPARATOR);
        rowValues.append(SEPARATOR);
        rowNames.append(property);

        final String value = annot.getProperty(property);

        final int type = types[i];

        switch (type) {

        case TEXT_TYPE_ID:
          rowTypes.append(TEXT_TYPE);
          rowValues.append(value);
          break;

        case INTEGER_TYPE_ID:
          rowTypes.append(INTEGER_TYPE);
          rowValues.append(value);
          break;

        case BOOLEAN_TYPE_ID:
          rowTypes.append(BOOLEAN_TYPE);
          rowValues.append(value);
          break;

        case FLOAT_TYPE_ID:
          rowTypes.append(FLOAT_TYPE);

          if (doubleFormatStrict)
            rowValues.append(formatDouble(Double.parseDouble(value)));
          else
            rowValues.append(value);
          // rowValues.append(value);
          break;

        default:
          break;
        }

      }

    }

    rowTypes.append("\n");
    rowNames.append("\n");
    rowValues.append("\n");
    rowTypes.append(rowNames);
    rowTypes.append(rowValues);
    rowTypes.append(ROW_END_ID);
    rowTypes.append("\n");

    return rowTypes.toString();
  }

  private String writeColumnsHeader() {

    Map<String, Integer> mapTypes = new HashMap<String, Integer>();

    for (int i = 0; i < COLS_NAMES.length; i++)
      mapTypes.put(COLS_NAMES[i], COLS_TYPES[i]);

    StringBuilder rowTypes = new StringBuilder();
    StringBuilder rowNames = new StringBuilder();

    rowTypes.append(ROW_TYPE_ID);
    rowNames.append(FEATURES_BLOCK);

    // Write Fields names
    for (int i = 0; i < getColumnCount(); i++) {

      rowTypes.append(SEPARATOR);
      rowNames.append(SEPARATOR);

      final String fieldName = getFieldName(i);
      rowNames.append(fieldName);
      final int type = mapTypes.get(fieldName);

      switch (type) {

      case TEXT_TYPE_ID:
        rowTypes.append(TEXT_TYPE);
        break;

      case INTEGER_TYPE_ID:
        rowTypes.append(INTEGER_TYPE);
        break;

      case FLOAT_TYPE_ID:
        rowTypes.append(FLOAT_TYPE);
        break;

      case BOOLEAN_TYPE_ID:
        rowTypes.append(BOOLEAN_TYPE);
        break;

      default:
        break;
      }

    }

    rowTypes.append("\n");
    rowNames.append("\n");
    rowTypes.append(rowNames);

    return rowTypes.toString();
  }

  @Override
  protected void writeHeaders() throws NividicIOException {

    try {

      bw =
          new BufferedWriter(new OutputStreamWriter(getOutputStream(),
              Globals.DEFAULT_FILE_ENCODING));

      final BioAssay bioAssay = getBioAssay();
      final Annotation annot = bioAssay.getAnnotation();

      bw.write(writeAgilentHeader(annot, FEPARAMS_NAMES, FEPARAMS_TYPES,
          FEPARAMS_BLOCK, isDoubleFormatStrict()));

      bw.write(writeAgilentHeader(annot, STATS_NAMES, STATS_TYPES, STATS_BLOCK,
          isDoubleFormatStrict()));

      bw.write(writeColumnsHeader());

    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream header : "
          + e.getMessage());
    }

  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param filename file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public AgilentWriter(final String filename) throws NividicIOException {
    this(new File(filename));
  }
  
  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public AgilentWriter(final File file) throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public AgilentWriter(final OutputStream is) throws NividicIOException {
    super(is);
  }

  private static String formatDoubleExp(final double value) {

    if (value == 0)
      return "0";

    String s = String.valueOf(value);

    final boolean vInt = value == Math.floor(value);

    if (vInt)
      return df2.format(value).replaceFirst("E-", "e-").replaceFirst("E", "e+")
          .replaceFirst("\\.0e", ".e");

    if (s.length() < 8)
      return s;

    String s2 =
        df.format(value).replaceFirst("E-", "e-").replaceFirst("E", "e+")
            .replaceFirst("\\.0e", ".e");

    return s2;

  }

  private static String formatDoubleExp2(final double value) {

    String s =
        df.format(value).replaceFirst("E-", "e-").replaceFirst("E", "e+");

    if (s.indexOf('.') == -1)
      s = s.replaceFirst("e", ".e");

    return s;

  }

  private static String formatDouble(final double val) {

    if (val == 0)
      return "0.";

    String s = "" + val;

    // if (val < 1 && s.length() > 4)
    if (s.startsWith("0.0"))
      return formatDoubleExp2(val);

    if (val == Math.floor(val))
      return s.substring(0, s.length() - 1);

    return s;
  }

  private static void test1(String s, double value) {

    String s2 = formatDouble(value);
    if (!s.equals(s2))
      System.out.println(s + " is attempt, get " + s2);

  }

  private static void test2(String s, double value) {

    String s2 = formatDoubleExp(value);
    if (!s.equals(s2))
      System.out.println(s + " is attempt, get " + s2);

  }

  private static void main(final String[] args) throws NividicIOException {

    test1("27.00000005", 27.00000005);
    test1("3.", 3.);
    test1("0.3", 0.3);
    test1("0.25", 0.25);
    test1("0.", 0.);
    test1("4.75", 4.75);
    test1("4562.39501990609", 4562.39501990609);
    test1("8.1e-003", 8.1e-003);
    test1("2.25e-002", 2.25e-002);
    test1("5.e-002", 5.e-002);
    test1("0.00455259", 0.00455259);
    test1("0.0863827", 0.0863827);

    System.out.println("---");

    test2("7.731905e+003", 7.731905e+003);
    test2("9.397946e+002", 9.397946e+002);
    test2("-1.607242344e+000", -1.607242344e+000);
    test2("9.191509641e-002", 9.191509641e-002);
    test2("21.8019", 21.8019);
    test2("43.1613", 43.1613);
    test2("30.273", 30.273);
    test2("30.5", 30.5);
    test2("149.024", 149.024);
    test2("-2.000000000e+000", -2.000000000e+000);
    test2("0", 0);
    test2("2.821250e+002", 2.821250e+002);
    test2("1.146400e+001", 1.146400e+001);
    test2("1.405000e+002", 1.405000e+002);
    test2("1.898000e+002", 1.898000e+002);

  }

  private static void main2(final String[] args) throws NividicIOException {

    AgilentReader ar =
        new AgilentReader(new File("/home/jourdren/Desktop/geo/GSM231607.txt"));
    ar.addAllFieldsToRead();
    BioAssay ba = ar.read();

    AgilentWriter aw = new AgilentWriter(new File("/home/jourdren/t.txt"));
    aw.setBioAssay(ba);
    aw.writeHeaders();

    aw.addAllFieldsToWrite();
    aw.write(ba);

  }

}
