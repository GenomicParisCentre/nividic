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

package fr.ens.transcriptome.nividic.om.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixListener;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class implemenents a expression matrix dimension.
 * @author Laurent Jourdren
 */
public class ExpressionMatrixDimensionImpl implements
    ExpressionMatrixDimension, ExpressionMatrixListener {

  // private static final int INIRIAL_NON_ZERO_ODD_NUMMER = 11;
  // private static final int MULTIPLIER_NON_ZERO_ODD_NUMBER = 29;

  private String name;
  private ExpressionMatrixImpl matrix;

  private Set<ExpressionMatrixListener> listeners =
      new HashSet<ExpressionMatrixListener>();

  private Map<String, List<Double>> referencesToColumnNamesMap;

  private String lastGetSetValueRowName;
  private int lastGetSetValueRowIndex;
  private String lastGetSetValueColumnName;
  private List<Double> lastGetSetValueColumnData;

  // for DoubleMatrix
  private int rowCount;
  private int columnCount;
  private String[] columnNames;
  private String[] rowNames;
  private boolean columnNamesChanged = true;
  private boolean rowNamesChanged = true;

  //
  // Getters
  //

  /**
   * Get the main matrix of the dimension.
   * @return an Expression matrix object
   */
  public ExpressionMatrix getMatrix() {

    return this.matrix;
  }

  Map<String, List<Double>> getReferencesToColumnNamesMap() {

    return this.referencesToColumnNamesMap;
  }

  /**
   * Get the name of the dimension.
   * @return Returns the name of the dimension
   */
  public String getDimensionName() {
    return name;
  }

  /**
   * Get a Set of the listener of the object.
   * @return A Set of thImple listeners
   */
  public Set getListeners() {
    return this.listeners;
  }

  /**
   * Extract a row from the matrix
   * @param rowName The name of the row to extract
   * @return a tab of double values
   * @throws ExpressionMatrixRuntimeException if the matrix is empty or if the
   *             row that you want to extract doesn't exist
   */
  public double[] getRowToArray(final String rowName)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, the row " + rowName + "doesn't exist");

    matrix.throwExceptionIfRowNameDoesntExists(rowName);

    double[] rowValues = new double[getColumnCount()];

    // Get the index of the row in the ArrayDoubleList
    final int index = matrix.getInternalRowIdIndex(rowName);

    // create a loop on all the arrayDoubleList
    final String[] columnNames = getColumnNames();

    for (int i = 0; i < columnNames.length; i++) {
      final List<Double> list = referencesToColumnNamesMap.get(columnNames[i]);
      rowValues[i] = list.get(index);
    }

    return rowValues;
  }

  /**
   * Extract a column from the matrix
   * @param columnNumber The index of the column to extract
   * @return a BioAssay Object
   * @throws ExpressionMatrixRuntimeException if we are unable to create the new
   *             BioAssay object
   */
  public BioAssay getColumn(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    matrix.throwExceptionIfColumnDoesntExists(columnNumber);

    String columnName = this.getColumnName(columnNumber);

    return this.getColumn(columnName);
  }

  /**
   * Extract a column from the matrix
   * @param columnName The name of the column to extract
   * @return a BioAssay object
   * @throws ExpressionMatrixRuntimeException if we are unable to create the new
   *             BioAssay object
   */
  public BioAssay getColumn(final String columnName)
      throws ExpressionMatrixRuntimeException {

    // TODO create a adapted BioAssay Object without copying all value in a new
    // BioAssayImpl
    // TODO Change the type of field when it is not a double

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException("Expression Matrix is empty");

    matrix.throwExceptionIfColumnDoesntExists(columnName);

    BioAssay bioAssay;

    try {

      bioAssay = BioAssayFactory.createBioAssay();
      bioAssay.setIds(getRowNames());

      bioAssay.setDataFieldDouble(getDimensionName(),
          getColumnToArray(columnName));
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
   *             that doesn't exist
   */
  public double[] getColumnToArray(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, the column number "
              + columnNumber + " doesn't exist");

    String columnName = this.getColumnName(columnNumber);

    return getColumnToArray(columnName);
  }

  /**
   * Extract a column from the matrix
   * @param columnName The column to extract
   * @return a tab of double values
   * @throws ExpressionMatrixRuntimeException if the matrix is empty or if the
   *             column doesn't exist
   */
  public double[] getColumnToArray(final String columnName)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, the column " + columnName + " doesn't exist");

    matrix.throwExceptionIfColumnDoesntExists(columnName);

    /*
     * double[] columnValues = new double[matrix.getRowCount()]; columnValues =
     * NividicUtils.toArray(referencesToColumnNamesMap .get(columnName)); return
     * columnValues;
     */

    return NividicUtils.toArray(referencesToColumnNamesMap.get(columnName));
  }

  /**
   * Extract a value from the matrix
   * @param rowId the id of the spot, name of the row where the value is to be
   *            found
   * @param columnNumber the xp code, name of the column where the value is to
   *            be found
   * @return a double value
   * @throws ExpressionMatrixRuntimeException if the row or the column don't
   *             exist
   */
  public double getValue(final String rowId, final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, the column number"
              + columnNumber + "and the row " + rowId + " dont exist");

    String columnName = this.getColumnName(columnNumber);

    return this.getValue(rowId, columnName);
  }

  /**
   * Extract a value from the matrix
   * @param rowName the id of the spot, name of the row where the value is to be
   *            found
   * @param columnName the xp code, name of the column where the value is to be
   *            found
   * @return a double value
   * @throws ExpressionMatrixRuntimeException if the column or the row doesn't
   *             exist
   */
  public double getValue(final String rowName, final String columnName)
      throws ExpressionMatrixRuntimeException {

    updateLastGetSetRowColumn(rowName, columnName);

    return this.lastGetSetValueColumnData.get(this.lastGetSetValueRowIndex);
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
   * add a new ExpressionMatrixListener to the set of Listeners
   * @param listener the new ExpressionMatrixListener to add
   */
  public void addListener(final ExpressionMatrixListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Set in a column <code>ArrayDoubleList</code> a given value
   * @param value The value to set in the column
   * @param rowId The index in the <code>ArrayDoubleList</code> where you put
   *            the value
   * @param column the <code>ArrayDoubleList</code> to fill
   * @throws ExpressionMatrixRuntimeException if the rowId is invalide or the
   *             column to fill doesn't exist
   */
  private void setValue(final String rowId, final List<Double> column,
      final double value) throws ExpressionMatrixRuntimeException {

    if (rowId == null)
      throw new ExpressionMatrixRuntimeException("String rowId is null");

    if (column == null)
      throw new ExpressionMatrixRuntimeException(
          "ArrayDoubleList column is null");

    if (!matrix.containsRow(rowId))
      this.addRow(rowId);

    int indexFromIdsHashMap = matrix.getInternalRowIdIndex(rowId);

    column.set(indexFromIdsHashMap, value);
  }

  private void updateLastGetSetRowColumn(final String rowName,
      final String columnName) {

    if (this.lastGetSetValueColumnName == null
        || !this.lastGetSetValueColumnName.equals(columnName)) {

      matrix.throwExceptionIfColumnDoesntExists(columnName);
      this.lastGetSetValueColumnName = columnName;
      this.lastGetSetValueColumnData =
          this.referencesToColumnNamesMap.get(columnName);
    }

    if (this.lastGetSetValueRowName == null
        || !this.lastGetSetValueRowName.equals(rowName)) {

      matrix.throwExceptionIfRowNameDoesntExists(rowName);
      this.lastGetSetValueRowName = rowName;
      this.lastGetSetValueRowIndex = matrix.getInternalRowIdIndex(rowName);
    }
  }

  /**
   * Set a value in the matrix
   * @param value the value to set in
   * @param rowName The name of the row where you want to insert your value
   * @param columnName The name of the column where you want to insert your
   *            value
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *             reach doesn't exist
   */
  public void setValue(final String rowName, final String columnName,
      final double value) throws ExpressionMatrixRuntimeException {

    updateLastGetSetRowColumn(rowName, columnName);

    this.lastGetSetValueColumnData.set(this.lastGetSetValueRowIndex, value);
  }

  /**
   * Set a value in the matrix
   * @param value the value to set in
   * @param rowId The name of the row where you want to insert your value
   * @param columnNumber The index of the column where you want to insert your
   *            value
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *             reach doesn't exist
   */
  public void setValue(final String rowId, final int columnNumber,
      final double value) throws ExpressionMatrixRuntimeException {

    String columnName = getColumnName(columnNumber);

    setValue(rowId, columnName, value);
  }

  /**
   * Set in a column <code>ArrayDoubleList</code> a given value
   * @param ids The index in the <code>ArrayDoubleList</code> where you put
   *            the value
   * @param column the <code>ArrayDoubleList</code> to fill
   * @param values The values to set in the column
   * @throws ExpressionMatrixRuntimeException if the rowId is invalide or the
   *             column to fill doesn't exist
   */
  void setValues(final String[] ids, final List<Double> column,
      final double[] values) throws ExpressionMatrixRuntimeException {

    if (ids == null)
      throw new ExpressionMatrixRuntimeException("String identifiers is null");

    if (column == null)
      throw new ExpressionMatrixRuntimeException(
          "ArrayDoubleList column is null");

    if (ids.length != values.length)
      throw new ExpressionMatrixRuntimeException(
          "The sizes of the arrays of identifier and data to add are not the same");

    for (int i = 0; i < ids.length; i++)
      setValue(ids[i], column, values[i]);

    this.rowCount = this.matrix.getRowCount();
  }

  /**
   * Set a value in the matrix
   * @param ids The names of the rows where you want to insert your value
   * @param columnName The name of the column where you want to insert your
   *            value
   * @param values the values to set in the matrix
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *             reach doesn't exist
   */
  public void setValues(final String[] ids, final String columnName,
      final double[] values) throws ExpressionMatrixRuntimeException {

    matrix.throwExceptionIfColumnDoesntExists(columnName);

    List<Double> columnToFill = referencesToColumnNamesMap.get(columnName);

    setValues(ids, columnToFill, values);
  }

  /**
   * Set a value in the matrix
   * @param ids The name of the row where you want to insert your value
   * @param columnNumber The index of the column where you want to insert your
   *            value
   * @param values the values to set in the matrix
   * @throws ExpressionMatrixRuntimeException if the column that you want to
   *             reach doesn't exist
   */
  public void setValues(final String[] ids, final int columnNumber,
      final double[] values) throws ExpressionMatrixRuntimeException {

    setValues(ids, getColumnName(columnNumber), values);
  }

  /**
   * Checks if <b>this </b> ExpressionMatrixDimension object and the
   * ExpressionMatrixDimension em are equals.
   * @param o The other ExpressionMatrixDimension that you want to compare to
   *            <b>this </b> one
   * @return <b>true </b> if em and this are aquals.
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof ExpressionMatrixDimension))
      return false;

    if (!dataEquals(o))
      return false;

    ExpressionMatrixDimension emd = (ExpressionMatrixDimension) o;

    if (this.name == null)
      return emd.getDimensionName() == null;

    return this.name.equals(emd.getDimensionName());
  }

  /**
   * Get the hashCode of the object.
   * @return the hascode of the object
   */
  public int hashCode() {

    /*
     * return new HashCodeBuilder(INIRIAL_NON_ZERO_ODD_NUMMER,
     * MULTIPLIER_NON_ZERO_ODD_NUMBER).append(name).append(matrix).append(
     * referencesToColumnNamesMap).toHashCode();
     */

    return super.hashCode();
  }

  /**
   * Test if the data inside the object is the same data as another object.
   * @param o Object to test
   * @return true if the 2 objects are equals
   */
  public boolean dataEquals(final Object o) {

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

  //
  // Methods handled by Expression Matrix Impl
  //

  /**
   * Add a column in the matrix, all the values are at NA
   * @param columnName The name of the column that you want to add
   */
  public void addColumn(final String columnName) {

    this.matrix.addColumn(columnName);
  }

  /**
   * Add a column in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param columnName The name of the column that you want to add
   */
  public void addColumn(final String columnName, final double[] data) {

    if (data == null)
      throw new ExpressionMatrixRuntimeException("Data to add is null");

    addColumn(columnName);

    if (getRowCount() != data.length)
      throw new ExpressionMatrixRuntimeException(
          "The length of data to add is not equals to the row count");

    final List<Double> columnToFill =
        referencesToColumnNamesMap.get(columnName);

    final String[] ids = getRowNames();

    for (int i = 0; i < ids.length; i++) {
      setValue(ids[i], columnToFill, data[i]);
    }

  }

  /**
   * Add a column in the matrix
   * @param columnName The name of the column that you want to add
   * @param ids An array of string that contains the ID codes
   * @param data An array of double that you want to add to your matrix
   */
  public void addColumn(final String columnName, final String[] ids,
      final double[] data) {

    if (data == null)
      throw new ExpressionMatrixRuntimeException("Data to add is null");

    if (ids == null)
      throw new ExpressionMatrixRuntimeException("Identifiers is null");

    if (ids.length != data.length)
      throw new ExpressionMatrixRuntimeException(
          "The size of the arrays of identifiers and data are not the same");

    if (!containsColumn(columnName))
      addColumn(columnName);

    if (ids.length != data.length)
      throw new ExpressionMatrixRuntimeException(
          "The length of data to add is not equals to the row count");

    final List<Double> columnToFill =
        referencesToColumnNamesMap.get(columnName);

    for (int i = 0; i < ids.length; i++) {
      if (ids[i] != null)
        setValue(ids[i], columnToFill, data[i]);
    }

  }

  /**
   * Add a row in the matrix, all the values are at NA
   * @param name The name of the row that you want to add
   */
  public void addRow(final String name) {
    this.matrix.addRow(name);
  }

  /**
   * Add a row in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   */
  public void addRow(final String name, final double[] data) {

    final String[] columnNames = getColumnNames();

    addRow(name, columnNames, data);
  }

  /**
   * Add a row in the matrix
   * @param data An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @param columnNames the names of the columns where you want to append a
   *            value
   */
  public void addRow(final String name, final String[] columnNames,
      final double[] data) {

    if (data == null)
      throw new ExpressionMatrixRuntimeException("Row to add is null");
    if (columnNames == null)
      throw new ExpressionMatrixRuntimeException("Column names is null");

    addRow(name);

    if (data.length != columnNames.length)
      throw new ExpressionMatrixRuntimeException(
          "Row data size is not the same that the number of columns");

    for (int i = 0; i < data.length; i++)
      setValue(name, columnNames[i], data[i]);
  }

  /**
   * Add a row in the matrix
   * @param name The name of the row that you want to add
   * @param columnNumber the number of the columns where you want to append a
   * @param data An array of double that you want to add to your matrix value
   */
  public void addRow(final String name, final int[] columnNumber,
      final double[] data) {

    if (columnNumber == null)
      throw new ExpressionMatrixRuntimeException("column numbers  is null");

    String[] columnNames = new String[columnNumber.length];

    for (int i = 0; i < columnNumber.length; i++)
      columnNames[i] = getColumnName(i);

    addRow(name, columnNames, data);
  }

  /**
   * Set the name of the dimension.
   * @param name The name of the dimension
   */
  public void setDimensionName(final String name) {

    matrix.renameDimension(this.name, name);
  }

  /**
   * gives the second dimention of the matrix, the number of columns
   * @return an int , the number of columns of the matrix
   */
  public int getColumnCount() {

    // return this.matrix.getColumnCount();
    return columnCount;
  }

  /**
   * get the index of a column if it exists
   * @param columnName the name of the column which you want the index
   * @return the index of the column
   */
  public int getColumnIndex(final String columnName) {

    return this.matrix.getColumnIndex(columnName);
  }

  /**
   * get the name of a column if it exists
   * @param columnNumber the index of the column which you want the name
   * @return the name of the column
   */
  public String getColumnName(final int columnNumber) {

    return this.matrix.getColumnName(columnNumber);
  }

  /**
   * gives the first dimention of the matrix, the number of rows
   * @return an int , the number of rows of the matrix
   */
  public int getRowCount() {

    // return this.matrix.getRowCount();
    return this.rowCount;
  }

  /**
   * Get the names of the columns
   * @return return the names of the columns in an array of strings
   */
  public String[] getColumnNames() {

    return this.matrix.getColumnNames();
  }

  /**
   * Get the identifiers of the rows
   * @return return the names of the rows in an array of strings
   */
  public String[] getRowNames() {

    return this.matrix.getRowNames();
  }

  /**
   * Checks is the ExpressionMatrixDimension is empty or not
   * @return return true if the matrix is empty, false else
   */
  public boolean isNoRow() {

    return this.matrix.isNoRow();
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnNumber the index of the column in reference ArrayList
   * @return true if this column exists, false if not
   */
  public boolean containsColumn(final int columnNumber) {

    return this.matrix.containsColumn(columnNumber);
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
  public boolean containsRow(final String rowId) {

    return this.matrix.containsRow(rowId);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   */
  public void addBioAssay(final BioAssay bioAssay) {

    addBioAssay(bioAssay, bioAssay.getName());
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator) {

    addBioAssay(bioAssay, bioAssay.getName(), null, translator, null);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use
   * @param translatorFieldName Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator,
      final String translatorFieldName) {

    addBioAssay(bioAssay, bioAssay.getName(), null, translator,
        translatorFieldName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param newColumnName Name of the new Column
   * @param translator Translator to use
   * @param translatorFieldName Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final String newColumnName,
      final Translator translator, final String translatorFieldName) {

    addBioAssay(bioAssay, null, newColumnName, translator, translatorFieldName);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param newColumnName The name of the new column to add
   */
  public void addBioAssay(final BioAssay bioAssay, final String newColumnName) {

    addBioAssay(bioAssay, null, newColumnName, null, null);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumnToAdd Field of the bioAssay to add
   * @param newColumnName The name of the new column to add
   */
  public void addBioAssay(final BioAssay bioAssay,
      final String bioAssayColumnToAdd, final String newColumnName) {

    addBioAssay(bioAssay, bioAssayColumnToAdd, newColumnName, null);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param bioAssayColumnToAdd Field of the bioAssay to add
   * @param newColumnName The name of the new column to add
   * @param translator Translator to use to define rowId
   */
  public void addBioAssay(final BioAssay bioAssay,
      final String bioAssayColumnToAdd, final String newColumnName,
      final Translator translator) {

    addBioAssay(bioAssay, bioAssayColumnToAdd, newColumnName, translator, null);
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

    if (bioAssay == null)
      throw new ExpressionMatrixRuntimeException("The BioAssay to add is null");

    // Define new column name if not set
    final String lNewColumnName;

    if (newColumnName == null)
      lNewColumnName = bioAssay.getName();
    else
      lNewColumnName = newColumnName;

    // Define bioAssayColumnToAdd if not set
    final String lbioAssayColumnToAdd;

    if (bioAssayColumnToAdd == null)
      lbioAssayColumnToAdd = getDimensionName();
    else
      lbioAssayColumnToAdd = newColumnName;

    // Create the array od identifers
    String[] ids = bioAssay.getIds();

    if (translator != null)
      ids = translator.translateField(ids, translatorField);

    final double[] values = bioAssay.getDataFieldDouble(lbioAssayColumnToAdd);

    // Add data to the matrix
    addColumn(lNewColumnName, ids, values);

  }

  //
  // DoubleMatrix methods
  //

  private void testAndUpdateRowColumnNames() {

    if (rowNamesChanged) {
      this.rowNames = getRowNames();
      rowNamesChanged = false;
    }

    if (columnNamesChanged) {
      this.columnNames = getColumnNames();
      columnNamesChanged = false;
    }

  }

  /**
   * Set a single element.
   * @param i Row index.
   * @param j Column index.
   * @param s A(i,j).
   * @exception ArrayIndexOutOfBoundsException
   */
  public void set(final int i, final int j, final double s) {

    testAndUpdateRowColumnNames();
    setValue(this.rowNames[i], this.columnNames[j], s);
  }

  /**
   * Get a single element.
   * @param i Row index.
   * @param j Column index.
   * @return A(i,j)
   * @exception ArrayIndexOutOfBoundsException
   */

  public double get(final int i, final int j) {

    testAndUpdateRowColumnNames();
    return getValue(this.rowNames[i], this.columnNames[j]);
  }

  //
  // Listeners
  //

  /**
   * Create internal column.
   * @param event event to process
   */
  private void execMsgAddColumn(final ExpressionMatrixEvent event) {

    String columnName = event.getStringValue();

    if (this.referencesToColumnNamesMap.containsKey(columnName))
      throw new ExpressionMatrixRuntimeException(
          "The name of the column to add " + columnName + " does exist yet");

    this.lastGetSetValueColumnName = null;

    int index = this.matrix.getColumnCount();

    if (columnName == null)
      columnName = "#" + index;

    final int nRows = this.matrix.getRowCreatedCount();
    List<Double> columnToAdd = new ArrayList<Double>(nRows);

    for (int i = 0; i < nRows; i++)
      columnToAdd.add(i, Double.NaN);

    this.referencesToColumnNamesMap.put(columnName, columnToAdd);
    this.columnCount++;
    columnNamesChanged = true;
  }

  /**
   * Create internal column.
   * @param event event to process
   */
  private void execMsgAddRow() {

    this.lastGetSetValueRowName = null;
    // String rowName = event.getStringValue();

    // final int nRows = this.matrix.getRowCreatedCount() + 1;
    /*
     * final int nColumns = this.matrix.getColumnCount(); for (int i = 0; i <
     * nColumns; i++) { final String columnName = this.matrix.getColumnName(i);
     * final List<Double> refColNum = this.referencesToColumnNamesMap
     * .get(columnName); // refColNum.add(nRows-1, Double.NaN);
     * refColNum.add(Double.NaN); }
     */

    for (Map.Entry<String, List<Double>> entry : this.referencesToColumnNamesMap
        .entrySet())
      entry.getValue().add(Double.NaN);

    this.rowCount++; // this.matrix.getRowCount();

    rowNamesChanged = true;
  }

  /**
   * Remove a column.
   * @param event event to process
   */
  private void execMsgRemoveColumn(final ExpressionMatrixEvent event) {

    String columnName = event.getStringValue();

    if (!this.referencesToColumnNamesMap.containsKey(columnName))
      throw new ExpressionMatrixRuntimeException(
          "The name of the column to remove " + columnName + " does not exist");

    this.lastGetSetValueColumnName = null;
    this.referencesToColumnNamesMap.remove(columnName);
    this.columnCount--;
    columnNamesChanged = true;
  }

  /**
   * Rename a column.
   * @param event event to process
   */
  private void execMsgRenameColumn(final ExpressionMatrixEvent event) {

    final String[] names = (String[]) event.getObjectValue();
    final String oldName = names[0];
    final String newName = names[1];

    if (!this.referencesToColumnNamesMap.containsKey(oldName))
      throw new ExpressionMatrixRuntimeException(
          "The name of the column to rename " + oldName + " does not exist");

    this.lastGetSetValueColumnName = null;
    List<Double> col = this.referencesToColumnNamesMap.get(oldName);

    this.referencesToColumnNamesMap.remove(oldName);
    this.referencesToColumnNamesMap.put(newName, col);
    columnNamesChanged = true;

  }

  /**
   * Rename the dimension.
   * @param event event to process
   */
  private void execMsgRenameDimension(final ExpressionMatrixEvent event) {
    String[] names = (String[]) event.getObjectValue();

    final String oldname = names[0];
    final String newname = names[1];

    if (oldname.equals(this.name))
      this.name = newname;
  }

  /**
   * Rename the dimension.
   * @param event event to process
   */
  private void execMsgAddDimension() {

    final String[] columnNames = getColumnNames();
    final int nRows = this.matrix.getRowCreatedCount();

    for (int i = 0; i < columnNames.length; i++) {

      final String columnName = columnNames[i];

      if (this.referencesToColumnNamesMap.containsKey(columnName))
        continue;

      List<Double> columnToAdd = new ArrayList<Double>(nRows);

      for (int j = 0; j < nRows; j++)
        columnToAdd.add(j, Double.NaN);

      this.referencesToColumnNamesMap.put(columnName, columnToAdd);

    }

  }

  /**
   * Invoked when the target of the listener has changed its state.
   * @param event a ExpressionMatrixEvent object
   */
  public void expressionMatrixStateChanged(final ExpressionMatrixEvent event) {

    if (event == null)
      return;

    switch (event.getId()) {

    case ExpressionMatrixEvent.ADD_COLUMN_EVENT:
      execMsgAddColumn(event);
      break;

    case ExpressionMatrixEvent.ADD_ROW_EVENT:
      execMsgAddRow();
      break;

    case ExpressionMatrixEvent.ADD_DIMENSION_EVENT:
      execMsgAddDimension();
      break;

    case ExpressionMatrixEvent.REMOVE_COLUMN_EVENT:
      execMsgRemoveColumn(event);
      break;

    case ExpressionMatrixEvent.REMOVE_ROW_EVENT:
      this.lastGetSetValueRowName = null;
      this.rowCount = this.matrix.getRowCount();
      rowNamesChanged = true;
      break;

    case ExpressionMatrixEvent.RENAME_COLUMN_EVENT:
      execMsgRenameColumn(event);
      break;

    case ExpressionMatrixEvent.RENAME_ROW_EVENT:
      this.lastGetSetValueRowName = null;
      rowNamesChanged = true;
      break;

    case ExpressionMatrixEvent.RENAME_DIMENSION_EVENT:
      execMsgRenameDimension(event);
      break;

    default:
      break;
    }
  }

  //
  // Constructors
  //

  ExpressionMatrixDimensionImpl(final ExpressionMatrixImpl matrix,
      final String name) {

    this.matrix = matrix;
    this.name = name;
    this.referencesToColumnNamesMap = new LinkedHashMap<String, List<Double>>();
    this.columnCount = matrix.getColumnCount();
    this.rowCount = matrix.getRowCount();
  }

}
