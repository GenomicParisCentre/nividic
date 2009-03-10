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

import fr.ens.transcriptome.nividic.om.impl.ExpressionMatrixImpl;

/**
 * This allow to create instances of concrete classes of ExpressioMatrix
 * interface.
 * @author Lory Montout
 */
public final class ExpressionMatrixFactory {

  /**
   * Create a ExpressionMatrix Object.
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrix() {

    return new ExpressionMatrixImpl();
  }

  /**
   * Create a ExpressionMatrix Object.
   * @param matrixName Name of the matrix
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrix(final String matrixName) {

    return new ExpressionMatrixImpl(matrixName);
  }

  /**
   * Create a copy of a expression matrix object.
   * @param matrix Matrix to copy
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrix(
      final ExpressionMatrix matrix) {

    return new ExpressionMatrixImpl(matrix);
  }

  /**
   * Create a copy of a expression matrix object.
   * @param matrix Matrix to copy
   * @param matrixName The name of the copy matrix
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrix(
      final ExpressionMatrix matrix, final String matrixName) {

    return new ExpressionMatrixImpl(matrix, matrixName);
  }

  /**
   * Create a ExpressionMatrix Object with M and A dimensions.
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrixMA() {

    return addADimension(new ExpressionMatrixImpl());
  }

  /**
   * Create a ExpressionMatrix Object with M and A dimensions.
   * @param matrixName The name of the matrix
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrixMA(
      final String matrixName) {

    return addADimension(new ExpressionMatrixImpl(matrixName));
  }

  /**
   * Create a copy of a expression matrix object with M and A dimensions.
   * @param matrix Matrix to copy
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrixMA(
      final ExpressionMatrix matrix) {

    return addADimension(new ExpressionMatrixImpl(matrix));
  }

  /**
   * Create a copy of a expression matrix object with M and A dimensions.
   * @param matrix Matrix to copy
   * @param matrixName The name of the copy matrix
   * @return A new ExpressionMatrix Object
   */
  public static ExpressionMatrix createExpressionMatrixMA(
      final ExpressionMatrix matrix, final String matrixName) {

    return addADimension(new ExpressionMatrixImpl(matrix, matrixName));
  }

  private static ExpressionMatrix addADimension(final ExpressionMatrix matrix) {

    matrix.addDimension(BioAssay.FIELD_NAME_A);
    return matrix;
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private ExpressionMatrixFactory() {
  }

}