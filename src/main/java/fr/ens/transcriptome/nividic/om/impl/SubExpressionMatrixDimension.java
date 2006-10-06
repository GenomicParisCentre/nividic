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

package fr.ens.transcriptome.nividic.om.impl;

import org.apache.commons.collections.primitives.ArrayDoubleList;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;
import fr.ens.transcriptome.nividic.om.translators.Translator;

public class SubExpressionMatrixDimension implements ExpressionMatrixDimension {

  private SubExpressionMatrix matrix;
  private ExpressionMatrixDimensionImpl matrixDimension;

  //
  // Getters
  //

  /**
   * Get the name of the dimension.
   * @return Returns the name of the dimension
   */
  public String getDimensionName() {

    return this.matrixDimension.getDimensionName();
  }

  /**
   * Extract a row from the matrix
   * @param rowName The name of the row to extract
   * @return a tab of double values
   * @throws ExpressionMatrixRuntimeException if the matrix is empty or if the
   *           row that you want to extract doesn't exist
   */
  public double[] getRow(final String rowName)
      throws ExpressionMatrixRuntimeException {

    this.matrix.throwExceptionIfRowIdDoesntExists(rowName);

    return this.matrixDimension.getRow(rowName);
  }

  /**
   * Extract a column from the matrix
   * @param columnNumber The index of the column to extract
   * @return a BioAssay Object
   * @throws ExpressionMatrixRuntimeException if we are unable to create the new
   *           BioAssay object
   */
  public BioAssay getColumn(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    return getColumn(getColumnName(columnNumber));
  }

  /**
   * Extract a column from the matrix
   * @param columnName The name of the column to extract
   * @return a BioAssay object
   * @throws ExpressionMatrixRuntimeException if we are unable to create the new
   *           BioAssay object
   */
  public BioAssay getColumn(final String columnName)
      throws ExpressionMatrixRuntimeException {

    this.matrix.throwExceptionIfColumnDoesntExists(columnName);

    final String[] ids = this.matrix.getRowIds();
    final double[] values = new double[ids.length];
    final int indexColumn = this.matrixDimension.getColumnIndex(columnName);

    for (int i = 0; i < values.length; i++)
      values[i] = this.matrixDimension.getValue(ids[i], indexColumn);

    BioAssay bioAssay;

    try {

      bioAssay = BioAssayFactory.createBioAssay();
      bioAssay.setIds(ids);

      bioAssay.setDataFieldDouble(getDimensionName(), values);
      bioAssay.setName(columnName);

    } catch (BioAssayRuntimeException e) {
      throw new ExpressionMatrixRuntimeException(
          "Unable to create a new BioAssay object (" + e.getMessage() + ")");
    }

    return bioAssay;

  }

  /**
   * Extract a column from the matrix
   * @param columnNumber The index of the column to extract
   * @return a tab of double values
   * @throws ExpressionMatrixRuntimeException if you try to extract a column
   *           that doesn't exist
   */
  public double[] getColumnToArray(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    return getColumnToArray(getColumnName(columnNumber));
  }

  /**
   * Extract a column from the matrix
   * @param columnName The column to extract
   * @return a tab of double values
   * @throws ExpressionMatrixRuntimeException if the matrix is empty or if the
   *           column doesn't exist
   */
  public double[] getColumnToArray(final String columnName)
      throws ExpressionMatrixRuntimeException {

    this.matrix.throwExceptionIfColumnDoesntExists(columnName);

    final String[] ids = this.matrix.getRowIds();
    final double[] values = new double[ids.length];
    final int indexColumn = this.matrixDimension.getColumnIndex(columnName);

    for (int i = 0; i < values.length; i++)
      values[i] = this.matrixDimension.getValue(ids[i], indexColumn);

    return values;
  }

  /**
   * Extract a value from the matrix
   * @param rowId the id of the spot, name of the row where the value is to be
   *          found
   * @param columnNumber the xp code, name of the column where the value is to
   *          be found
   * @return a double value
   * @throws ExpressionMatrixRuntimeException if the row or the column don't
   *           exist
   */
  public double getValue(final String rowId, final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    return this.matrixDimension.getValue(rowId, getColumnName(columnNumber));
  }

  /**
   * Extract a value from the matrix
   * @param rowId the id of the spot, name of the row where the value is to be
   *          found
   * @param columnName the xp code, name of the column where the value is to be
   *          found
   * @return a double value
   * @throws ExpressionMatrixRuntimeException if the column or the row doesn't
   *           exist
   */
  public double getValue(final String rowId, final String columnName)
      throws ExpressionMatrixRuntimeException {

    this.matrix.throwExceptionIfColumnDoesntExists(columnName);
    this.matrix.throwExceptionIfRowIdDoesntExists(rowId);

    return this.matrixDimension.getValue(rowId, columnName);
  }

  /**
   * Get all the values of the expression matrix
   * @return An array of doubles
   * @throws ExpressionMatrixRuntimeException if the matrix is empty
   */
  public double[][] getValues() throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, you can't get any values");

    final int n = matrix.getColumnCount();

    double[][] allValues = new double[n][];

    for (int i = 0; i < n; i++)
      allValues[i] = getColumnToArray(i);

    return allValues;
  }

  //
  // Setters
  //

  /**
   * Set a value in the matrix
   * @param value the value to set in
   * @param rowId The name of the row where you want to insert your value
   * @param columnName The name of the column where you want to insert your
   *          value
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *           reach doesn't exist
   */
  public void setValue(final String rowId, final String columnName,
      final double value) throws ExpressionMatrixRuntimeException {

    this.matrix.throwExceptionIfColumnDoesntExists(columnName);
    this.matrix.throwExceptionIfRowIdDoesntExists(rowId);

    this.matrixDimension.setValue(rowId, columnName, value);
  }

  /**
   * Set a value in the matrix
   * @param value the value to set in
   * @param rowId The name of the row where you want to insert your value
   * @param columnNumber The index of the column where you want to insert your
   *          value
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *           reach doesn't exist
   */
  public void setValue(final String rowId, final int columnNumber,
      final double value) throws ExpressionMatrixRuntimeException {

    this.matrix.throwExceptionIfRowIdDoesntExists(rowId);

    this.matrixDimension.setValue(rowId, getColumnName(columnNumber), value);
  }

  /**
   * Set in a column <code>ArrayDoubleList</code> a given value
   * @param ids The index in the <code>ArrayDoubleList</code> where you put
   *          the value
   * @param column the <code>ArrayDoubleList</code> to fill
   * @param values The values to set in the column
   * @throws ExpressionMatrixRuntimeException if the rowId is invalide or the
   *           column to fill doesn't exist
   */
  private void setValues(final String[] ids, final ArrayDoubleList column,
      final double[] values) throws ExpressionMatrixRuntimeException {

    if (ids == null)
      throw new ExpressionMatrixRuntimeException("String identifiers is null");

    if (column == null)
      throw new ExpressionMatrixRuntimeException(
          "ArrayDoubleList column is null");

    if (ids.length != values.length)
      throw new ExpressionMatrixRuntimeException(
          "The sizes of the arrays of identifier and data to add are not the same");

    this.matrix.throwExceptionIfRowIdDoesntExists(ids);

    this.matrixDimension.setValues(ids, column, values);
  }

  /**
   * Set a value in the matrix
   * @param ids The names of the rows where you want to insert your value
   * @param columnName The name of the column where you want to insert your
   *          value
   * @param values the values to set in the matrix
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *           reach doesn't exist
   */
  public void setValues(final String[] ids, final String columnName,
      final double[] values) throws ExpressionMatrixRuntimeException {

    matrix.throwExceptionIfColumnDoesntExists(columnName);

    ArrayDoubleList columnToFill = (ArrayDoubleList) matrixDimension
        .getReferencesToColumnNamesMap().get(columnName);

    setValues(ids, columnToFill, values);
  }

  /**
   * Set a value in the matrix
   * @param ids The name of the row where you want to insert your value
   * @param columnNumber The index of the column where you want to insert your
   *          value
   * @param values the values to set in the matrix
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *           reach doesn't exist
   */
  public void setValues(final String[] ids, final int columnNumber,
      final double[] values) throws ExpressionMatrixRuntimeException {

    setValues(ids, getColumnName(columnNumber), values);
  }

  /**
   * Checks if <b>this </b> ExpressionMatrixDimension object and the
   * ExpressionMatrixDimension em are equals.
   * @param o The other ExpressionMatrixDimension that you want to compare to
   *          <b>this </b> one
   * @return <b>true </b> if em and this are aquals.
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof ExpressionMatrixDimension))
      return false;

    if (!dataEquals(o))
      return false;

    ExpressionMatrixDimension emd = (ExpressionMatrixDimension) o;

    if (this.matrixDimension.getDimensionName() == null)
      return emd.getDimensionName() == null;

    return this.matrixDimension.getDimensionName().equals(
        emd.getDimensionName());
  }

  /**
   * Test if the data inside the object is the same data as another object.
   * @param o Object to test
   * @return true if the 2 objects are equals
   */
  public boolean dataEquals(Object o) {

    if (o == null || !(o instanceof ExpressionMatrixDimension))
      return false;

    ExpressionMatrixDimension em = (ExpressionMatrixDimension) o;

    int emSize = em.getRowCount();
    int emColumnCount = em.getColumnCount();

    if (emSize != this.getRowCount() || emColumnCount != this.getColumnCount())
      return false;

    final int size = getColumnCount();
    final int idsMapSize = getRowCount();

    for (int j = 0; j < size; j++) {

      double[] columnFromThis = this.getColumnToArray(j);
      double[] columnFromEm = em.getColumnToArray(j);

      for (int i = 0; i < idsMapSize; i++) {

        if ((Double.isNaN(columnFromEm[i]))
            && (Double.isNaN(columnFromThis[i])))
          continue;

        if ((columnFromThis[i] != columnFromEm[i]))
          return false;
      }
    }

    return true;
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the action that is demanded
   * is illegal in this class
   */
  private void throwsExpressionMatrixRuntimeExceptionForIllegalActions() {
    throw new ExpressionMatrixRuntimeException(
        "illegal action, not able to rename, add, or delete any row, column or dimension");
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumnToAdd The name of the column of the bioAssay to add
   * @param newColumnName The name of the new column to add
   */
  public void addBioAssay(final BioAssay bioAssay, final String bioAssayColumn,
      String newColumnName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay, bioAssayColumn, newColumnName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param name The name of the new column
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addBioAssay(final BioAssay bioAssay, final String newColumnName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay, newColumnName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowId
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay, newColumnName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowId
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator,
      final String translatorField) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay, newColumnName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumnToAdd The name of the column of the bioAssay to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final String newColumnName,
      final Translator translator, final String translatorField) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay, newColumnName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumnToAdd The name of the column of the bioAssay to add
   * @param newColumnName The name of the new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay,
      final String bioAssayColumnToAdd, final String newColumnName,
      final Translator translator, final String translatorField) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay, newColumnName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addBioAssay(final BioAssay bioAssay) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addBioAssay(bioAssay);
  }

  /**
   * Add a column in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param columnName The name of the column that you want to add
   */
  public void addColumn(final String name, final double[] column) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addColumn(name, column);
  }

  /**
   * Add a column in the matrix
   * @param columnName The name of the column that you want to add
   * @param ids An array of string that contains the ID codes
   * @param data An array of double that you want to add to your matrix
   */
  public void addColumn(final String name, final String[] ids,
      final double[] column) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addColumn(name, ids, column);
  }

  /**
   * Add a column in the matrix, all the values are at NA
   * @param columnName The name of the column that you want to add
   */
  public void addColumn(final String columnName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addColumn(columnName);
  }

  /**
   * Add a row in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   */
  public void addRow(final String name, final double[] row) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addRow(name, row);
  }

  /**
   * Add a row in the matrix
   * @param name The name of the row that you want to add
   * @param columnNumber the number of the columns where you want to append a
   * @param data An array of double that you want to add to your matrix value
   */
  public void addRow(final String name, final int[] columnNumber,
      final double[] row) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addRow(name, columnNumber, row);
  }

  /**
   * Add a row in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @param columnNames the names of the columns where you want to append a
   *          value
   */
  public void addRow(final String name, final String[] columnName,
      final double[] row) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addRow(name, columnName, row);
  }

  /**
   * Add a row in the matrix, all the values are at NA
   * @param name The name of the row that you want to add
   */
  public void addRow(final String name) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.addRow(name);
  }

  /**
   * Set the name of the dimension.
   * @param name The name of the dimension
   */
  public void setDimensionName(final String name) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
    // this.subMatrix.setDimensionName(name);
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnNumber the index of the column in reference ArrayList
   * @return true if this column exists, false if not
   */
  public boolean containsColumn(final int columnNumber) {

    return containsColumn(getColumnName(columnNumber));
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnName the column name that we want to check
   * @return true if the column exists, false if not
   */
  public boolean containsColumn(final String columnName) {

    return this.matrix.containsColumn(columnName);
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given rowId
   * @param rowId the row name that we want to check
   * @return true if the rowId exists, false if not
   */
  public boolean containsRowId(final String rowId) {

    return this.matrix.containsRowId(rowId);
  }

  /**
   * Gives the second dimention of the matrix, the number of columns
   * @return an int , the number of columns of the matrix
   */
  public int getColumnCount() {

    return this.matrix.getColumnCount();
  }

  /**
   * Get the index of a column if it exists
   * @param columnName the name of the column which you want the index
   * @return the index of the column
   * @throws ExpressionMatrixRuntimeException if the column doesn't exists
   */
  public int getColumnIndex(final String columnName) {

    return this.matrix.getColumnIndex(columnName);
  }

  /**
   * Get the index of a column if it exists
   * @param columnName the name of the column which you want the index
   * @return the index of the column
   * @throws ExpressionMatrixRuntimeException if the column doesn't exists
   */
  public String getColumnName(final int columnNumber) {

    return this.matrix.getColumnName(columnNumber);
  }

  /**
   * Get the names of the columns
   * @return return the names of the columns in an array of strings
   */
  public String[] getColumnNames() {

    return this.matrix.getColumnNames();
  }

  /**
   * Give the first dimention of the matrix, the number of rows.
   * @return an int , the number of rows of the matrix
   */
  public int getRowCount() {

    return this.matrix.getRowCount();
  }

  /**
   * Get the names of the rows
   * @return return the names of the rows in an array of strings
   */
  public String[] getRowIds() {

    return this.matrix.getRowIds();
  }

  /**
   * Checks is the ExpressionMatrixDimension is empty or not
   * @return return true if the matrix is empty, false else
   */
  public boolean isNoRow() {

    return this.matrix.isNoRow();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param submatrix Sub matrix
   * @param matrixDimension dimension
   */
  public SubExpressionMatrixDimension(final SubExpressionMatrix submatrix,
      ExpressionMatrixDimensionImpl matrixDimension) {

    this.matrix = submatrix;
    this.matrixDimension = matrixDimension;
  }

}
