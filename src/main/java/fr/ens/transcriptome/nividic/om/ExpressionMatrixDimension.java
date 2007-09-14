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

import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This interface discribe a ExpressionMatrixDimension object. extends
 * ExpressionMatrixBase
 * @author Montout Lory
 */
public interface ExpressionMatrixDimension extends ExpressionMatrixSizes,
    DoubleMatrix {

  /**
   * Get the main matrix of the dimension.
   * @return an Expression matrix object
   */
  ExpressionMatrix getMatrix();

  /**
   * Get the name of the dimension.
   * @return the name of the dimension
   */
  String getDimensionName();

  /**
   * Set the name of the dimension.
   * @param name The name of the dimension
   */
  void setDimensionName(String name);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   */
  void addBioAssay(BioAssay bioAssay);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param newColumnName The name of the new column to add
   */
  void addBioAssay(BioAssay bioAssay, String newColumnName);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumn The name of the column of the bioAssay to add
   * @param newColumnName The name of the new column to add
   */
  void addBioAssay(BioAssay bioAssay, String bioAssayColumn,
      String newColumnName);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowId
   */
  void addBioAssay(BioAssay bioAssay, Translator translator);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowId
   * @param translatorField Field of the translator to use
   */
  void addBioAssay(BioAssay bioAssay, Translator translator,
      String translatorField);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param newColumnName The name of the new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  void addBioAssay(BioAssay bioAssay, String newColumnName,
      Translator translator, String translatorField);

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumnToAdd The name of the column of the bioAssay to add
   * @param newColumnName The name of the new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  void addBioAssay(BioAssay bioAssay, String bioAssayColumnToAdd,
      String newColumnName, Translator translator, String translatorField);

  /**
   * extract a row from the matrix
   * @param rowName The name of the row to extract
   * @return a tab of double values
   */
  double[] getRowToArray(String rowName);

  /**
   * Extract a column from the matrix
   * @param columnNumber The index of the column to extract
   * @return a BioAssay Object
   */
  BioAssay getColumn(int columnNumber);

  /**
   * Extract a column from the matrix
   * @param columnName The name of the column to extract
   * @return a BioAssay object
   */
  BioAssay getColumn(String columnName);

  /**
   * Extract a column from the matrix
   * @param columnNumber The index of the column to extract
   * @return a tab of double values
   */
  double[] getColumnToArray(int columnNumber);

  /**
   * Extract a column from the matrix
   * @param columnName The column to extract
   * @return a tab of double values
   */
  double[] getColumnToArray(String columnName);

  /**
   * Extract a value from the matrix
   * @param id The name of the row where the value is to be found
   * @param columnNumber The index of the column where the value is to be found
   * @return a double value
   */
  double getValue(String id, int columnNumber);

  /**
   * extract a value from the matrix
   * @param id the id of the spot, name of the row where the value is to be
   *          found
   * @param columnName the xp code, name of the column where the value is to be
   *          found
   * @return a double value
   */
  double getValue(String id, String columnName);

  /**
   * Set a value in the matrix
   * @param ids The names of the rows where you want to insert your value
   * @param columnName The name of the column where you want to insert your
   *          value
   * @param values the values to set in the matrix
   */
  void setValues(String[] ids, String columnName, double[] values);

  /**
   * Get all the values of the expression matrix
   * @return An array of doubles
   */
  double[][] getValues();

  /**
   * Set a value in the matrix
   * @param id The name of the row where you want to insert your value
   * @param columnName The name of the column where you want to insert your
   *          value
   * @param value the value to set in
   */
  void setValue(String id, String columnName, double value);

  /**
   * Set a value in the matrix
   * @param id The name of the row where you want to insert your value
   * @param columnNumber The index of the column where you want to insert your
   *          value
   * @param value the value to set in
   */
  void setValue(String id, int columnNumber, double value);

  /**
   * Set a value in the matrix
   * @param ids The name of the row where you want to insert your value
   * @param columnNumber The index of the column where you want to insert your
   *          value
   * @param values the values to set in the matrix
   */
  void setValues(String[] ids, int columnNumber, double[] values);

  /**
   * Add a column in the matrix, all the values are at NA
   * @param columnName The name of the column that you want to add
   */
  void addColumn(String columnName);

  /**
   * Add a column in the matrix
   * @param column An array of double that you want to add to your matrix
   * @param columnName The name of the column that you want to add
   */
  void addColumn(String columnName, double[] column);

  /**
   * Add a column in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param columnName The name of the column that you want to add
   * @param ids An array of string that contains the ID codes
   */
  void addColumn(String columnName, String[] ids, double[] data);

  /**
   * Add a row in the matrix, all the values are at NA
   * @param name The name of the row that you want to add
   */
  void addRow(String name);

  /**
   * Add a row in the matrix
   * @param row An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   */
  void addRow(String name, double[] row);

  /**
   * Add a row in the matrix
   * @param row An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @param columnName the names of the columns where you want to append a value
   */
  void addRow(String name, String[] columnName, double[] row);

  /**
   * Add a row in the matrix
   * @param row An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @param columnNumber the number of the columns where you want to append a
   *          value
   */
  void addRow(String name, int[] columnNumber, double[] row);

}