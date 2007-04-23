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

import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This interface define an expression matrix.
 * @author Laurent Jourdren
 */
public interface ExpressionMatrix extends ExpressionMatrixBase,
    BiologicalObject {

  /** Constant for name of the dimension for A values. */
  String DIMENSION_A = BioAssay.FIELD_NAME_A;
  /** Constant for name of the dimension for M values. */
  String DIMENSION_M = BioAssay.FIELD_NAME_M;

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   */
  void addBioAssay(BioAssay bioAssay);

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   */
  void addBioAssay(BioAssay bioAssay, String columnName);

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   */
  void addBioAssay(BioAssay bioAssay, Translator translator);

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  void addBioAssay(BioAssay bioAssay, Translator translator,
      String translatorField);

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   * @param translator Translator to use to define rowIds
   */
  void addBioAssay(BioAssay bioAssay, String columnName, Translator translator);

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  void addBioAssay(BioAssay bioAssay, String columnName, Translator translator,
      String translatorField);

  /**
   * Add all the bioAssays of a design to the matrix.
   * @param design Design to add
   */
  void addDesign(Design design);

  /**
   * Add another expression matrix to the expression matrix.
   * @param matrix Matrix to add
   */
  void addMatrix(ExpressionMatrix matrix);

  /**
   * Get a dimension.
   * @param dimensionName Name of the dimension to get
   * @return a dimension
   */
  ExpressionMatrixDimension getDimension(String dimensionName);

  /**
   * Add a new dimension to the matrix.
   * @param dimensionName Name of the dimension to add
   */
  void addDimension(String dimensionName);

  /**
   * Add a new dimension to the matrix with the values of another dimension.
   * @param dimension Dimension to add
   */
  void addDimension(ExpressionMatrixDimension dimension);

  /**
   * Add a new dimension to the matrix with the values of another dimension.
   * @param dimension Dimension to add
   * @param dimensionName the name of the dimension in the matrix
   */
  void addDimension(ExpressionMatrixDimension dimension, String dimensionName);

  /**
   * Remove a dimension to the matrix.
   * @param dimensionName Name of the dimension to remove
   */
  void removeDimension(String dimensionName);

  /**
   * Rename a dimension.
   * @param oldName Old name of the dimension
   * @param newName New name of the dimension
   */
  void renameDimension(String oldName, String newName);

  /**
   * Get the defaultDimension.
   * @return the default dimension
   */
  ExpressionMatrixDimension getDefaultDimension();

  /**
   * Set the default dimension.
   * @param name Name of the dimension
   */
  void setDefaultDimensionName(final String name);

  /**
   * Get the default dimension name.
   * @return The name of the default dimension
   */
  String getDefaultDimensionName();

  /**
   * Set a new name to a row
   * @param formerName The former name of the row That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException if you try to rename a row that
   *           doesn't exist or if the new name of the row does exist yet
   */
  void renameRow(final String formerName, final String newName)
      throws ExpressionMatrixRuntimeException;

  /**
   * Set a name to a column
   * @param columnNumber The index of the column That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException if you try to rename a column that
   *           doesn't exist or if the new name of the column does exist yet
   */
  void renameColumn(final int columnNumber, final String newName)
      throws ExpressionMatrixRuntimeException;

  /**
   * Set a name to a column
   * @param formerName The former name of the column That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException if you try to rename a column that
   *           doesn't exist
   */
  void renameColumn(final String formerName, final String newName)
      throws ExpressionMatrixRuntimeException;

  /**
   * Remove an Id row
   * @param rowId the id of the spot
   * @throws ExpressionMatrixRuntimeException if the row id that you try to
   *           remove doesn't exist
   */
  void removeRow(final String rowId) throws ExpressionMatrixRuntimeException;

  /**
   * Remove an Experience field
   * @param columnName the index of the Experience
   * @throws ExpressionMatrixRuntimeException if the column that you try to
   *           remove doesn't exist
   */
  void removeColumn(final String columnName)
      throws ExpressionMatrixRuntimeException;

  /**
   * Remove an Experience field
   * @param index the index of the Experience
   * @throws ExpressionMatrixRuntimeException if the column that you try to
   *           remove doesn't exist
   */
  void removeColumn(final int index) throws ExpressionMatrixRuntimeException;

  /**
   * Creates a sub matrix, choosing the rows that you want to keep in it.
   * @param rowsId Rows that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixRows(String[] rowsId);

  
  /**
   * Creates a sub matrix, choosing the rows that you want to throw out.
   * @param rowsId Rows that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixRowsExclude(String[] rowsId);
  
  /**
   * Creates a sub matrix, choosing the columns that you want to keep in it.
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixColumns(int[] columns);
  
  /**
   * Creates a sub matrix, choosing the columns that you want to trow out.
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixColumnsExclude(int[] columns);

  /**
   * Creates a sub matrix, choosing the rows that you want to keep in it.
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixColumns(String[] columns);
  
  /**
   * Creates a sub matrix, choosing the rows that you want to throw out.
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixColumnsExclude(String[] columns);

  /**
   * Create a sub matrix, choosing the dimension that you want to keep in it
   * @param dimensionNames Dimensions that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixDimensions(String[] dimensionNames);
  
  /**
   * Create a sub matrix, choosing the dimension that you want to throw out
   * @param dimensionNames Dimensions that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  ExpressionMatrix subMatrixDimensionsExclude(String[] dimensionNames);

  /**
   * Get the number of dimensions.
   * @return The number of dimensions
   */
  int getDimensionCount();

  /**
   * Test if a dimension exists.
   * @param name dimension to test
   * @return true if the dimension exists
   */
  boolean containsDimension(String name);

  /**
   * Return the names of the dimension of the matrix.
   * @return The names of the dimension of the matrix
   */
  String[] getDimensionNames();

  /**
   * Get all the dimensions of the matrix.
   * @return An array of the dimension of the matrix
   */
  ExpressionMatrixDimension[] getDimensions();

  /**
   * Create a submatrix of the matrix as the result of a filter.
   * @param filter Filter to apply
   * @return an expression matrix
   */
  ExpressionMatrix filter(ExpressionMatrixFilter filter);

}
