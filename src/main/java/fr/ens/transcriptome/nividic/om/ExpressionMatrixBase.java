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

/**
 * This interface define a common base interface for ExpressionMatrixDimension
 * and ExpressionMatrix..
 * @author Laurent Jourdren
 */
public interface ExpressionMatrixBase extends ExpressionMatrixSizes {

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   */
  // void addBioAssay(BioAssay bioAssay);
  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param name The name of the new column
   */
  // void addBioAssay(BioAssay bioAssay, String name);
  /**
   * Get the annotation for the expression matrix.
   * @return The annotation object
   */
  Annotation getAnnotation();

  /**
   * Add a column.
   * @param columnName Name of the new column
   */
  void addColumn(String columnName);

  /**
   * Add a row.
   * @param rowName Name of the new id
   */
  void addRow(String rowName);

  /**
   * Remove an Experience field
   * @param columnNumber the index of the Experience
   */
  void removeColumn(int columnNumber);

  /**
   * Remove an Experience field
   * @param columnName the id of the Experience
   */
  void removeColumn(String columnName);

  /**
   * Remove a row
   * @param rowName the id of the spot
   */
  void removeRow(String rowName);

  /**
   * set a name to a column
   * @param columnNumber The index of the column That you want to rename
   * @param name The name that you want to set
   */
  void renameColumn(int columnNumber, String name);

  /**
   * set a new name to a column
   * @param formerName The former name of the column That you want to rename
   * @param newName The name that you want to set
   */
  void renameColumn(String formerName, String newName);

  //
  // Other methods
  //

  /**
   * set a new name to a row
   * @param formerName The former name of the row That you want to rename
   * @param newName The name that you want to set
   */
  void renameRow(String formerName, String newName);

}
