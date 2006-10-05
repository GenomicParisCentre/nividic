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

/**
 * An util class for ExpressionMatrixDimension objects
 * @author Lory Montout TODO this class is not yet implemented
 */
public final class ExpressionMatrixUtils {

  /**
   * Compare two spot-id to know if they refer to the same prob-id
   * @param id1 first id to compare
   * @param id2 second id to compare
   * @return true if those spot-ids have the same prob-id
   */
  public boolean compareRef(final String id1, final String id2) {

    // TODO implement it !!!

    return true;
  };

  /**
   * filter the matrix thanks to a value
   * @param value the value used to filter your matrix
   * @param tolerance the tolerance of your fitering
   */
  public void filter(final double value, final double tolerance) {
  };

  // TODO we have to check if it's a M or an A value

  /**
   * Print the an expression matrix dimension on the output stream.
   * @param dimension Dimension to print
   */
  public static void printExpressionMatrixDimension(
      ExpressionMatrixDimension dimension) {

    printExpressionMatrixDimension(dimension, System.out);
  }

  /**
   * Print an expression matrix dimension on a output stream.
   * @param dimension Dimension to print
   * @param out Output stream
   */
  public static void printExpressionMatrixDimension(
      ExpressionMatrixDimension dimension, final PrintStream out) {

    String[] ids = dimension.getRowIds();
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
  public static void printExpressionMatrix(ExpressionMatrix matrix,
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
   * @param out Output stream
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
   * @param out The output stream
   */
  public static void printExpressionMatrixDimensionsNames(
      final ExpressionMatrix matrix) {

    printExpressionMatrixDimensionsNames(matrix, System.out);
  }

}