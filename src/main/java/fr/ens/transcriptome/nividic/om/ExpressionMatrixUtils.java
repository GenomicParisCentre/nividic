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

import java.io.PrintStream;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

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
  }

  //
  // Constructor
  //

  private ExpressionMatrixUtils() {
  }

}