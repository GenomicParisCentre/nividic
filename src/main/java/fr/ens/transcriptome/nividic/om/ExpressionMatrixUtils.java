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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * An util class for ExpressionMatrixDimension objects
 * @author Lory Montout TODO this class is not yet implemented
 */
public final class ExpressionMatrixUtils {

  /**
   * Print the an expression matrix dimension on the output stream.
   * @param dimension Dimension to print
   */
  public static void printExpressionMatrixDimension(
      final ExpressionMatrixDimension dimension) {

    printExpressionMatrixDimension(dimension, System.out);
  }

  /**
   * Print an expression matrix dimension on a output stream.
   * @param dimension Dimension to print
   * @param out Output stream
   */
  public static void printExpressionMatrixDimension(
      final ExpressionMatrixDimension dimension, final PrintStream out) {

    String[] ids = dimension.getRowNames();
    String[] columnNames = dimension.getColumnNames();

    out.print("Dimension name: " + dimension.getDimensionName());
    out.print("\t[" + dimension.getRowCount() + " row(s)");
    out.println(", " + dimension.getColumnCount() + " column(s)]");

    out.println("-------------");

    out.print("id");

    for (int i = 0; i < columnNames.length; i++) {

      String colName = columnNames[i];

      out.print("\t\t" + colName);

    }
    out.println();

    for (int i = 0; i < ids.length; i++) {

      String id = ids[i];
      out.print(id);

      for (int j = 0; j < columnNames.length; j++) {
        String colName = columnNames[j];
        out.print("\t\t");
        out.print(dimension.getValue(id, colName));

      }
      out.println();

    }

  }

  /**
   * Print an expression matrix on a output stream.
   * @param matrix to print
   * @param out Output stream
   */
  public static void printExpressionMatrix(final ExpressionMatrix matrix,
      final PrintStream out) {

    if (matrix == null || out == null)
      return;

    out.println("Expression Matrix name: " + matrix.getName());

    ExpressionMatrixDimension[] dimensions = matrix.getDimensions();

    for (int i = 0; i < dimensions.length; i++)
      printExpressionMatrixDimension(dimensions[i], out);
  }

  /**
   * Print an expression matrix on a output stream.
   * @param matrix to print
   */
  public static void printExpressionMatrix(final ExpressionMatrix matrix) {

    printExpressionMatrix(matrix, System.out);
  }

  /**
   * Print the columns names of an expression matrix.
   * @param matrix Matrix to display
   * @param out The output stream
   */
  public static void printExpressionMatrixColumnsNames(
      final ExpressionMatrix matrix, final PrintStream out) {

    if (matrix == null || out == null)
      return;

    String[] columnNames = matrix.getColumnNames();

    for (int i = 0; i < columnNames.length; i++) {
      if (i > 0)
        out.print("\t");
      out.print(columnNames[i]);
    }
    out.println();

  }

  /**
   * Print the name columns names of an expression matrix.
   * @param matrix Matrix to display
   */
  public static void printExpressionMatrixColumnsNames(
      final ExpressionMatrix matrix) {

    printExpressionMatrixColumnsNames(matrix, System.out);
  }

  /**
   * Print the dimensions names of an expression matrix.
   * @param matrix Matrix to display
   * @param out The output stream
   */
  public static void printExpressionMatrixDimensionsNames(
      final ExpressionMatrix matrix, final PrintStream out) {

    if (matrix == null || out == null)
      return;

    String[] dimensionsNames = matrix.getDimensionNames();

    for (int i = 0; i < dimensionsNames.length; i++) {
      if (i > 0)
        out.print("\t");
      out.print(dimensionsNames[i]);
    }
    out.println();

  }

  /**
   * Print the dimensions names of an expression matrix.
   * @param matrix Matrix to display
   */
  public static void printExpressionMatrixDimensionsNames(
      final ExpressionMatrix matrix) {

    printExpressionMatrixDimensionsNames(matrix, System.out);
  }

  /**
   * Convert the matrix to HTML.
   * @param matrix Matrix to concert
   * @return a String with the matrix in html
   */
  public static String toHTML(final ExpressionMatrix matrix) {

    if (matrix == null)
      return null;

    StringBuilder sb = new StringBuilder();

    final String[] dimensionsNames = matrix.getDimensionNames();
    final String[] columnNames = matrix.getColumnNames();
    final String[] rowNames = matrix.getRowNames();

    final boolean oneDimension = dimensionsNames.length == 1;

    final double[][] data =
        new double[dimensionsNames.length * columnNames.length][];

    sb.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\">\n");
    sb.append("<tr bgcolor=\"lightgray\" font size=\"+1\"><th>Ids</th>");

    final ExpressionMatrixDimension[] dimensions = matrix.getDimensions();

    int count = 0;

    for (int i = 0; i < columnNames.length; i++)
      for (int j = 0; j < dimensionsNames.length; j++) {

        data[count++] = dimensions[j].getColumnToArray(columnNames[i]);

        sb.append("<th>");
        sb.append(columnNames[i]);
        if (!oneDimension) {
          sb.append("$");
          sb.append(dimensionsNames[j]);
        }
        sb.append("</th>");
      }

    sb.append("</tr>\n");

    for (int i = 0; i < rowNames.length; i++) {

      sb.append("<tr><td>");
      sb.append(rowNames[i]);
      sb.append("</td>");

      for (int j = 0; j < data.length; j++) {
        sb.append("<td>");
        sb.append(data[j][i]);
        sb.append("</td>");
      }
      sb.append("</tr>\n");
    }

    sb.append("</table\n");
    return sb.toString();
  }

  /**
   * Swap M values of a column.
   * @param matrix Matrix to process
   * @param columnName column to process
   */
  public static void swap(final ExpressionMatrix matrix, final String columnName) {

    if (matrix == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "bioassay");

    if (!matrix.containsDimension(BioAssay.FIELD_NAME_M))
      throw new NividicRuntimeException(
          "The M dimension of the bioAssay is not set");

    if (!matrix.containsColumn(columnName))
      throw new NividicRuntimeException("The column doesn't exists");

    ExpressionMatrixDimension dim = matrix.getDimension(BioAssay.FIELD_NAME_M);

    double[] ms = dim.getColumnToArray(columnName);

    for (int i = 0; i < ms.length; i++) {
      ms[i] = -ms[i];
    }

    dim.setValues(matrix.getRowNames(), columnName, ms);

    final HistoryEntry entry =
        new HistoryEntry("Swap", HistoryActionType.MODIFY, columnName,
            HistoryActionResult.PASS);

    matrix.getHistory().add(entry);
  }

  /**
   * Centring columns of the M dimension of an expression matrix.
   * @param matrix matrix to center
   */
  public static void centringColumns(final ExpressionMatrix matrix) {

    if (matrix == null)
      throw new NullPointerException("Expression matrix is null");

    centringColumns(matrix.getDimension(BioAssay.FIELD_NAME_M));
  }

  /**
   * Centring columns of a dimension of an expression matrix.
   * @param dimension dimension to center
   */
  public static void centringColumns(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      throw new NullPointerException("Dimension is null");

    //final DoubleMatrix doubleMatrix = new DoubleMatrix(dimension);
    DoubleMatrixUtils.meanCenterExperiments(dimension);

    final HistoryEntry entry =
        new HistoryEntry("Center columns", HistoryActionType.MODIFY, dimension
            .getDimensionName(), HistoryActionResult.PASS);

    dimension.getMatrix().getHistory().add(entry);
  }

  /**
   * Centring rows of the M dimension of an expression matrix.
   * @param matrix matrix to center
   */
  public static void centringRows(final ExpressionMatrix matrix) {

    if (matrix == null)
      throw new NullPointerException("Expression matrix is null");

    centringRows(matrix.getDimension(BioAssay.FIELD_NAME_M));
  }

  /**
   * Centring rows of a dimension of an expression matrix.
   * @param dimension dimension to center
   */
  public static void centringRows(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      throw new NullPointerException("Dimension is null");

    //final DoubleMatrix doubleMatrix = new DoubleMatrix(dimension);
    DoubleMatrixUtils.meanCenterSpots(dimension);

    final HistoryEntry entry =
        new HistoryEntry("Center rows", HistoryActionType.MODIFY, dimension
            .getDimensionName(), HistoryActionResult.PASS);

    dimension.getMatrix().getHistory().add(entry);
  }

  /**
   * Scaling columns the M dimension of an expression matrix.
   * @param matrix matrix to center
   */
  public static void scalingColumns(final ExpressionMatrix matrix) {

    if (matrix == null)
      throw new NullPointerException("Expression matrix is null");

    scalingColumns(matrix.getDimension(BioAssay.FIELD_NAME_M));
  }

  /**
   * Scaling a dimension of an expression matrix.
   * @param dimension dimension to reduce
   */
  public static void scalingColumns(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      throw new NullPointerException("Dimension is null");

    //final DoubleMatrix doubleMatrix = new DoubleMatrix(dimension);
    DoubleMatrixUtils.divideExperimentsSD(dimension);

    final HistoryEntry entry =
        new HistoryEntry("Reducing columns", HistoryActionType.MODIFY,
            dimension.getDimensionName(), HistoryActionResult.PASS);

    dimension.getMatrix().getHistory().add(entry);
  }

  /**
   * Scaling rows the M dimension of an expression matrix.
   * @param matrix matrix to center
   */
  public static void scalingRows(final ExpressionMatrix matrix) {

    if (matrix == null)
      throw new NullPointerException("Expression matrix is null");

    scalingRows(matrix.getDimension(BioAssay.FIELD_NAME_M));
  }

  /**
   * Scaling a dimension of an expression matrix.
   * @param dimension dimension to reduce
   */
  public static void scalingRows(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      throw new NullPointerException("Dimension is null");

    //final DoubleMatrix doubleMatrix = new DoubleMatrix(dimension);
    DoubleMatrixUtils.divideSpotsSD(dimension);

    final HistoryEntry entry =
        new HistoryEntry("Reducing columns", HistoryActionType.MODIFY,
            dimension.getDimensionName(), HistoryActionResult.PASS);

    dimension.getMatrix().getHistory().add(entry);
  }

  /**
   * Convert an expression matrix to a total summary bioAssay
   * @param matrix matrix to convert
   * @return a new BioAssay
   */
  public static BioAssay convertToTotalSummaryBioAssay(
      final ExpressionMatrix matrix) {

    if (matrix == null || matrix.getColumnCount() == 0)
      return null;

    BioAssay bioAssay = BioAssayFactory.createBioAssay();

    final String col = matrix.getColumnNames()[0];

    bioAssay.setDataFieldString(BioAssay.FIELD_NAME_ID, matrix.getRowNames());

    if (matrix.containsDimension(BioAssay.FIELD_NAME_M))
      bioAssay.setDataFieldDouble(BioAssay.FIELD_NAME_M, matrix.getDimension(
          BioAssay.FIELD_NAME_M).getColumnToArray(col));

    if (matrix.containsDimension(BioAssay.FIELD_NAME_A))
      bioAssay.setDataFieldDouble(BioAssay.FIELD_NAME_A, matrix.getDimension(
          BioAssay.FIELD_NAME_A).getColumnToArray(col));

    if (matrix.containsDimension("m stdDev"))
      bioAssay.setDataFieldDouble(BioAssay.FIELD_NAME_STD_DEV_M, matrix
          .getDimension("m stdDev").getColumnToArray(col));

    if (matrix.containsDimension("m n"))
      bioAssay.setDataFieldInt("n", NividicUtils.toArrayInt(matrix
          .getDimension("m n").getColumnToArray(col)));

    if (matrix.containsDimension("m total n"))
      bioAssay.setDataFieldInt("total n", NividicUtils.toArrayInt(matrix
          .getDimension("m total n").getColumnToArray(col)));

    return bioAssay;
  }

  /**
   * Rename matrix identifier with translation done by translator
   * @param matrix Matrix to use
   * @param translator Translator Translator to use
   */
  public static void renameIdsWithTranslator(final ExpressionMatrix matrix,
      final Translator translator) {

    if (matrix == null || translator == null)
      return;

    renameIdsWithTranslator(matrix, translator, translator.getDefaultField(),
        true);
  }

  /**
   * Rename matrix identifier with translation done by translator
   * @param matrix Matrix to use
   * @param translator Translator Translator to use
   * @param keepUntranslatedIds Keep the untranslatedIds
   */
  public static void renameIdsWithTranslator(final ExpressionMatrix matrix,
      final Translator translator, final boolean keepUntranslatedIds) {

    if (matrix == null || translator == null)
      return;

    renameIdsWithTranslator(matrix, translator, translator.getDefaultField(),
        keepUntranslatedIds);
  }

  /**
   * Rename matrix identifier with translation done by translator
   * @param matrix Matrix to use
   * @param translator Translator Translator to use
   * @param translatorField translator field
   * @param keepUntranslatedIds Keep the untranslatedIds
   */
  public static void renameIdsWithTranslator(final ExpressionMatrix matrix,
      final Translator translator, final String translatorField,
      final boolean keepUntranslatedIds) {

    if (matrix == null || translator == null)
      return;

    Map<String, String> translation = new HashMap<String, String>();
    Map<String, Integer> translationCount = new HashMap<String, Integer>();

    String[] rowNames = matrix.getRowNames();

    for (int i = 0; i < rowNames.length; i++) {

      String row = rowNames[i];
      String t = translator.translateField(row, translatorField);
      if (t == null || "".equals(t))
        if (keepUntranslatedIds)
          t = row;
        else
          t = "UNKNOWN";

      translation.put(row, t);
      if (translationCount.containsKey(t)) {
        int count = translationCount.get(t);
        translationCount.put(t, ++count);

      } else
        translationCount.put(t, 1);

    }

    Map<String, Integer> translationCurrentCount =
        new HashMap<String, Integer>();

    // for (String row : translation.keySet()) {
    for (Map.Entry<String, String> e : translation.entrySet()) {

      // String t = translation.get(row);
      final String row = e.getKey();
      final String t = e.getValue();

      int count = translationCount.get(t);

      if (count > 1) {

        String postfix;

        if (translationCurrentCount.containsKey(t)) {

          int currentCount = translationCurrentCount.get(t);
          currentCount++;
          translationCurrentCount.put(t, currentCount);
          postfix = "#" + currentCount;
        } else {
          translationCurrentCount.put(t, 1);
          postfix = "#1";
        }

        translation.put(row, t + postfix);
      }

    }

    final String prefix = "_TMP_RENAME_";

    for (int i = 0; i < rowNames.length; i++)
      matrix.renameRow(rowNames[i], prefix + i);

    for (int i = 0; i < rowNames.length; i++)
      matrix.renameRow(prefix + i, translation.get(rowNames[i]));
  }

  //
  // Constructor
  //

  private ExpressionMatrixUtils() {
  }

}