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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.builder.HashCodeBuilder;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.AnnotationFactory;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.Design;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;
import fr.ens.transcriptome.nividic.om.History;
import fr.ens.transcriptome.nividic.om.filters.BiologicalFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * implementation of the SubExpressionMatrix class
 * @author Lory Montout
 * @author Laurent Jourdren
 */
public class SubExpressionMatrix implements ExpressionMatrix,
    ExpressionMatrixListener {

  private Set idsSet;
  private Set referencesToColumnNamesSet;
  private ArrayList columnNames;
  private IterableMap dimensions;
  private ExpressionMatrixImpl matrix;
  private String name;
  private String defaultDimensionName;
  private HistoryImpl history = new HistoryImpl();

  private static int count;

  private static final int HASHCODE_ODD_NUMBER_1 = 19052005;
  private static final int HASHCODE_ODD_NUMBER_2 = 117;

  //
  // Getters
  //

  protected Set getIdsSet() {
    return this.idsSet;
  }

  protected Set getReferencesToColumnNamesSet() {
    return this.referencesToColumnNamesSet;
  }

  protected ExpressionMatrix getMatrix() {
    return this.matrix;
  }

  /**
   * Get the names of the rows
   * @return return the names of the rows in an array of strings
   */
  public String[] getRowNames() {

    String[] idsFromExpressionMatrix = this.matrix.getRowNames();
    String[] ids = new String[this.idsSet.size()];

    int j = 0;
    for (int i = 0; i < idsFromExpressionMatrix.length; i++)
      if (this.idsSet.contains(idsFromExpressionMatrix[i]))
        ids[j++] = idsFromExpressionMatrix[i];

    return ids;
  }

  /**
   * Get the names of the columns
   * @return return the names of the columns in an array of strings
   */
  public String[] getColumnNames() {

    String[] columnNames = new String[getColumnCount()];

    this.columnNames.toArray(columnNames);

    return columnNames;
  }

  /**
   * Get the name of the SubExpressionMatrix.
   * @return The name of the SubExpressionMatrix.
   */
  public String getName() {

    return this.name;
  }

  /**
   * Get the annotation for the expression matrix.
   * @return The annotation object
   */
  public Annotation getAnnotation() {
    return this.matrix.getAnnotation();
  }

  /**
   * Get the index of a column if it exists
   * @param columnName the name of the column which you want the index
   * @return the index of the column
   * @throws ExpressionMatrixRuntimeException if the column doesn't exists
   */
  public int getColumnIndex(final String columnName)
      throws ExpressionMatrixRuntimeException {

    throwExceptionIfColumnDoesntExists(columnName);

    return this.columnNames.indexOf(columnName);
  }

  /**
   * Get the name of a column if it exists
   * @param columnNumber the index of the column which you want the name
   * @return the name of the column
   * @throws ExpressionMatrixRuntimeException if the column doesn't exists
   */
  public String getColumnName(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    throwExceptionIfColumnDoesntExists(columnNumber);

    return (String) this.columnNames.get(columnNumber);
  }

  //
  // Setters
  //

  /**
   * Give a name to the matrix
   * @param name name of the matrix
   * @throws ExpressionMatrixRuntimeException if name is null
   */
  public void setName(final String name)
      throws ExpressionMatrixRuntimeException {

    if (name == null)
      throw new ExpressionMatrixRuntimeException("String name is null");

    this.name = name;

  }

  //
  // Other methods
  //

  /**
   * set a new name to a row
   * @param formerName The former name of the row That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void renameRow(final String formerName, final String newName)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * set a name to a column
   * @param columnNumber The index of the column That you want to rename
   * @param name The name that you want to set
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void renameColumn(final int columnNumber, final String name)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * set a new name to a column
   * @param formerName The former name of the column That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void renameColumn(final String formerName, final String newName)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Remove an Id row
   * @param id the id of the spot
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void removeRow(final String id)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Remove an Experience field
   * @param columnName the id of the Experience
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void removeColumn(final String columnName)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Remove an Experience field
   * @param columnNumber the index of the Experience
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void removeColumn(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addBioAssay(final BioAssay bioAssay)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator,
      final String translatorField) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   * @param translator Translator to use to define rowIds
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName,
      final Translator translator) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column to the matrix.
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName,
      final Translator translator, final String translatorField) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add all the bioAssays of a design to the matrix.
   * @param design Design to add
   */
  public void addDesign(final Design design) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add another expression matrix to the expression matrix.
   * @param matrix Matrix to add
   */
  public void addMatrix(final ExpressionMatrix matrix) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column in the matrix, all the values are at NA
   * @param columnName The name of the column that you want to add
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addColumn(final String columnName)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column in the matrix
   * @param column An array of double that you want to add to your matrix
   * @param name The name of the column that you want to add
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addColumn(final String name, final double[] column)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a column in the matrix
   * @param column An array of double that you want to add to your matrix
   * @param name The name of the column that you want to add
   * @param ids An array of string that contains the ID codes
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addColumn(final String name, final String[] ids,
      final double[] column) throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a row in the matrix, all the values are at NA
   * @param name The name of the row that you want to add
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addRow(final String name) throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a row in the matrix
   * @param row An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addRow(final String name, final double[] row)
      throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a row in the matrix
   * @param row An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @param columnName the names of the columns where you want to append a value
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addRow(final String name, final String[] columnName,
      final double[] row) throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a row in the matrix
   * @param row An array of double that you want to add to your matrix
   * @param name The name of the row that you want to add
   * @param columnNumber the number of the columns where you want to append a
   *          value
   * @throws ExpressionMatrixRuntimeException this operation is illegal when
   *           used in a SubExpressionMatrix object
   */
  public void addRow(final String name, final int[] columnNumber,
      final double[] row) throws ExpressionMatrixRuntimeException {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnNumber the index of the column in reference ArrayList
   * @return true if this column exists, false if not
   */
  public boolean containsColumn(final int columnNumber) {

    if (columnNumber < 0)
      return false;

    return columnNumber < this.columnNames.size();
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnName the column name that we want to check
   * @return true if the column exists, false if not
   */
  public boolean containsColumn(final String columnName) {

    if (columnName == null)
      throw new ExpressionMatrixRuntimeException("String columnName is null");

    return referencesToColumnNamesSet.contains(columnName);
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given rowId
   * @param rowId the row name that we want to check
   * @return true if the rowId exists, false if not
   */
  public boolean containsRow(final String rowId) {

    if (rowId == null)
      throw new ExpressionMatrixRuntimeException("String rowId is null");

    return this.idsSet.contains(rowId);

  }

  /**
   * Create a sub matrix, choosing the rows that you want to keep in it
   * @param rowsId Rows that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixRows(final String[] rowsId) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this.matrix, rowsId, this
        .getColumnNames());

    if (getMatrix() instanceof ExpressionMatrixListenerHandler)
      ((ExpressionMatrixListenerHandler) getMatrix()).addListener(sem);

    return sem;
  }

  /**
   * Create a sub matrix, choosing the rows that you want to keep in it
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixColumns(final int[] columns) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this.matrix, this
        .getRowNames(), columns);

    if (getMatrix() instanceof ExpressionMatrixListenerHandler)
      ((ExpressionMatrixListenerHandler) getMatrix()).addListener(sem);

    return sem;
  }

  /**
   * Create a sub matrix, choosing the rows that you want to keep in it
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixColumns(final String[] columns) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this.matrix, this
        .getRowNames(), columns);

    if (getMatrix() instanceof ExpressionMatrixListenerHandler)
      ((ExpressionMatrixListenerHandler) getMatrix()).addListener(sem);

    return sem;
  }

  /**
   * Create a sub matrix, choosing the dimension that you want to keep in it
   * @param dimensionNames Dimension that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixDimensions(final String[] dimensionNames) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this.matrix,
        dimensionNames);

    if (getMatrix() instanceof ExpressionMatrixListenerHandler)
      ((ExpressionMatrixListenerHandler) getMatrix()).addListener(sem);

    return sem;
  }

  /**
   * Creates a sub matrix, choosing the rows that you want to throw out.
   * @param rowsNames Rows that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixRowsExclude(final String[] rowsNames) {

    return subMatrixRows(StringUtils.excludeStrings(rowsNames, getRowNames()));
  }

  /**
   * Creates a sub matrix, choosing the columns that you want to trow out.
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixColumnsExclude(final int[] columns) {

    if (columns == null)
      return subMatrixColumns((int[]) null);

    String[] columnNames = new String[columns.length];
    for (int i = 0; i < columnNames.length; i++)
      columnNames[i] = getColumnName(columns[i]);

    return subMatrixColumns(StringUtils.excludeStrings(columnNames,
        getColumnNames()));
  }

  /**
   * Creates a sub matrix, choosing the rows that you want to throw out.
   * @param columnNames Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixColumnsExclude(final String[] columnNames) {

    return subMatrixColumns(StringUtils.excludeStrings(columnNames,
        getColumnNames()));
  }

  /**
   * Create a sub matrix, choosing the dimension that you want to throw out
   * @param dimensionNames Dimensions that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixDimensionsExclude(
      final String[] dimensionNames) {

    return subMatrixDimensions(StringUtils.excludeStrings(dimensionNames,
        getDimensionNames()));
  }

  /**
   * Give the first dimention of the matrix, the number of rows.
   * @return an int , the number of rows of the matrix
   */
  public int getRowCount() {

    if (this.isNoRow())
      return 0;

    return this.idsSet.size();
  }

  /**
   * Gives the second dimention of the matrix, the number of columns
   * @return an int , the number of columns of the matrix
   */
  public int getColumnCount() {

    return this.referencesToColumnNamesSet.size();
  }

  /**
   * Checks if <b>this </b> ExpressionMatrixDimension object and the
   * ExpressionMatrixDimension em are equals.
   * @param o The ExpressionMatrixDimension that you want to compare to <b>this
   *          </b> one
   * @return <b>true </b> if em and this are aquals.
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof ExpressionMatrix))
      return false;

    if (!dataEquals(o))
      return false;

    final ExpressionMatrix em = (ExpressionMatrix) o;

    if (getAnnotation() == null) {
      if (em.getAnnotation() != null)
        return false;
    } else if (!getAnnotation().equals(em.getAnnotation()))
      return false;

    if (getName() == null) {
      if (em.getName() != null)
        return false;
    }

    return getName().equals(em.getName());
  }

  /**
   * Test if the data inside the object is the same data as another object.
   * @param o Object to test
   * @return true if the 2 objects are equals
   */
  public boolean dataEquals(Object o) {

    if (o == null || !(o instanceof ExpressionMatrix))
      return false;

    final ExpressionMatrix em = (ExpressionMatrix) o;

    if (em.getDimensionCount() != getDimensionCount())
      return false;

    final String[] dimensions = getDimensionNames();

    for (int i = 0; i < dimensions.length; i++)
      if (!em.containsDimension(dimensions[i]))
        return false;

    for (int i = 0; i < dimensions.length; i++) {

      ExpressionMatrixDimension emd1 = getDimension(dimensions[i]);
      ExpressionMatrixDimension emd2 = em.getDimension(dimensions[i]);

      if (!emd1.dataEquals(emd2))
        return false;
    }

    return true;
  }

  /**
   * Returns the hash code value for this object.
   * @return the hash code value for this object.
   */
  public int hashCode() {

    HashCodeBuilder hcb = new HashCodeBuilder(HASHCODE_ODD_NUMBER_1,
        HASHCODE_ODD_NUMBER_2);

    hcb.append(this.getName()).append(this.getColumnNames()).append(
        this.getIdsSet());

    final String[] dimensions = getDimensionNames();

    for (int i = 0; i < dimensions.length; i++)
      hcb.append(getDimension(dimensions[i]));

    return hcb.toHashCode();
  }

  /**
   * Checks is the ExpressionMatrixDimension is empty or not
   * @return return true if the matrix is empty, false else
   */
  public boolean isNoRow() {

    return this.idsSet.size() == 0;
  }

  //
  // private methods
  //

  /**
   * add Ids to the Ids set
   * @param em the ExpressionMatrixDimension from which you create the
   *          SubExpressionMatrix
   * @param ids the ids that you want to put in the set
   */
  private void addIds(final ExpressionMatrix em, final String[] ids) {
    // String[] rowIds = em.getIds();

    for (int i = 0; i < ids.length; i++) {

      if (!em.containsRow(ids[i]))
        this.throwExceptionIfColumnDoesntExists(ids[i]);

      this.idsSet.add(ids[i]);
    }

  }

  private void addColumns(final ExpressionMatrix em, final String[] columns) {

    if (em == null || columns == null)
      return;

    String[] columnNames = em.getColumnNames();

    for (int i = 0; i < columns.length; i++)
      this.referencesToColumnNamesSet.add(columns[i]);

    for (int i = 0; i < columnNames.length; i++) {
      if (containsColumn(columnNames[i]))
        this.columnNames.add(columnNames[i]);
    }
  }

  /**
   * add column names to the column names set
   * @param em the ExpressionMatrixDimension from which you create the
   *          SubExpressionMatrix
   * @param columns the columns that you want to put in the set
   */
  private void addColumns(final ExpressionMatrix em, final int[] columns) {

    if (em == null || columns == null)
      return;

    String[] colNames = new String[columns.length];

    for (int i = 0; i < columns.length; i++)
      colNames[i] = em.getColumnName(columns[i]);

    addColumns(em, colNames);
  }

  private void removeIds(final String id) {

    if (id == null)
      throw new ExpressionMatrixRuntimeException("String id is null");
    if (!this.idsSet.contains(id))
      throw new ExpressionMatrixRuntimeException(
          "the id that you try to remove doesn't exist:" + id);

    this.idsSet.remove(id);
  }

  private void removeColumns(final String columnName) {

    if (columnName == null)
      throw new ExpressionMatrixRuntimeException("String column is null");

    if (!this.containsColumn(columnName))
      throw new ExpressionMatrixRuntimeException(
          "the column that you try to remove doesn't exist:" + columnName);

    this.getReferencesToColumnNamesSet().remove(columnName);
    this.columnNames.remove(columnName);
  }

  // private void removeColumns(final int columnNumber) {
  //
  // if (!this.containsColumn(columnNumber))
  // throw new ExpressionMatrixRuntimeException(
  // "the column that you try to remove doesn't exist:" + columnNumber);
  //
  // String columnName = this.getColumnNames()[columnNumber];
  // removeColumns(columnName);
  // }

  /**
   * rename a row
   * @param formerName the row that you want to rename
   * @param newName the new name of the row
   */
  private void renameIds(final String formerName, final String newName) {

    if (formerName == null)
      throw new ExpressionMatrixRuntimeException("String formerName is null");

    if (newName == null)
      throw new ExpressionMatrixRuntimeException("String newName is null");

    this.removeIds(formerName);
    this.idsSet.add(newName);

  }

  /**
   * rename a column
   * @param formerName the column that you want to rename
   * @param newName the new name of the column
   */
  private void renameColumns(final String formerName, final String newName) {

    if (formerName == null)
      throw new ExpressionMatrixRuntimeException("String formerName is null");

    if (newName == null)
      throw new ExpressionMatrixRuntimeException("String newName is null");

    this.removeColumns(formerName);

    int pos = -1;
    for (int i = 0; i < this.columnNames.size(); i++) {
      if (this.columnNames.get(i).equals(formerName)) {
        pos = i;
        break;
      }
    }
    this.columnNames.set(pos, newName);

    this.referencesToColumnNamesSet.add(newName);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the action that is demanded
   * is illegal in this class
   */
  protected void throwsExpressionMatrixRuntimeExceptionForIllegalActions() {
    throw new ExpressionMatrixRuntimeException(
        "illegal action, not able to rename, add, or delete any row, column or dimension");
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the column doesn't exist
   * @param columnName the name of the column that you want to test
   * @throws ExpressionMatrixRuntimeException if the column doesn't exist
   */
  protected void throwExceptionIfColumnDoesntExists(final String columnName)
      throws ExpressionMatrixRuntimeException {

    if (!this.containsColumn(columnName))
      throw new ExpressionMatrixRuntimeException(
          "the column that you try to reach doesn't exist, column name : "
              + columnName);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the column doesn't exist
   * @param columnName the name of the column that you want to test
   * @throws ExpressionMatrixRuntimeException if the column doesn't exist
   */
  protected void throwExceptionIfColumnDoesntExists(final String[] columnNames)
      throws ExpressionMatrixRuntimeException {

    if (columnNames == null)
      throw new ExpressionMatrixRuntimeException(
          "the column name that you try to reach is null ");

    for (int i = 0; i < columnNames.length; i++)
      throwExceptionIfColumnDoesntExists(columnNames[i]);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the column doesn't exist
   * @param columnNumber the rank of the column that you want to test
   * @throws ExpressionMatrixRuntimeException if the column doesn't exist
   */
  protected void throwExceptionIfColumnDoesntExists(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    if (!this.containsColumn(columnNumber))
      throw new ExpressionMatrixRuntimeException(
          "the column that you try to reach doesn't exist, column number : "
              + columnNumber);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the column doesn't exist
   * @param columnNumber the rank of the column that you want to test
   * @throws ExpressionMatrixRuntimeException if the column doesn't exist
   */
  protected void throwExceptionIfColumnDoesntExists(final int[] columnNumber)
      throws ExpressionMatrixRuntimeException {

    if (columnNames == null)
      throw new ExpressionMatrixRuntimeException(
          "the array of columnNames that you try to reach is null ");

    for (int i = 0; i < columnNumber.length; i++)
      throwExceptionIfColumnDoesntExists(columnNumber[i]);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the row doesn't exist
   * @param rowId the name of the row that you want to test
   * @throws ExpressionMatrixRuntimeException if the row doesn't exist
   */
  protected void throwExceptionIfRowIdDoesntExists(final String rowId)
      throws ExpressionMatrixRuntimeException {

    if (!this.containsRow(rowId))
      throw new ExpressionMatrixRuntimeException(
          "the row ID that you try to reach doesn't exist: " + rowId);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the row doesn't exist
   * @param rowId the name of the row that you want to test
   * @throws ExpressionMatrixRuntimeException if the row doesn't exist
   */
  protected void throwExceptionIfRowIdDoesntExists(final String[] rowIds)
      throws ExpressionMatrixRuntimeException {

    if (rowIds == null)
      throw new ExpressionMatrixRuntimeException(
          "the row names that you try to reach is null ");

    for (int i = 0; i < rowIds.length; i++)
      throwExceptionIfColumnDoesntExists(rowIds[i]);
  }

  /**
   * Add a dimension.
   * @param name Name of the dimension to add
   */
  public void addDimension(final String dimensionName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a new dimension to the matrix with the values of another dimension.
   * @param dimension Dimension to add
   */
  public void addDimension(final ExpressionMatrixDimension dimension) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Add a new dimension to the matrix with the values of another dimension.
   * @param dimension Dimension to add
   * @param dimensionName the name of the dimension in the matrix
   */
  public void addDimension(final ExpressionMatrixDimension dimension,
      final String dimensionName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Test if a dimension exists.
   * @param name dimension to test
   * @return true if the dimension exists
   */
  public boolean containsDimension(final String name) {

    return this.dimensions.containsKey(name);
  }

  /**
   * Get the defaultDimension.
   * @return the default dimension
   */
  public ExpressionMatrixDimension getDefaultDimension() {

    return getDimension(this.defaultDimensionName);
  }

  /**
   * Get the default dimension name.
   * @return The name of the default dimension
   */
  public String getDefaultDimensionName() {

    return this.defaultDimensionName;
  }

  /**
   * Get a dimension of the matrix.
   * @param name Name of the dimension
   */
  public ExpressionMatrixDimension getDimension(final String dimensionName) {

    return (ExpressionMatrixDimension) this.dimensions.get(dimensionName);
  }

  /**
   * Get the number of dimensions.
   * @return The number of dimensions
   */
  public int getDimensionCount() {

    return this.dimensions.size();
  }

  /**
   * Get the names of the dimension of the matrix
   * @return The names of the dimension of the matrix
   */
  public String[] getDimensionNames() {

    int n = this.dimensions.size();

    String[] result = new String[n];

    Iterator it = this.dimensions.keySet().iterator();

    int i = 0;
    while (it.hasNext())
      result[i++] = (String) it.next();

    return result;
  }

  /**
   * Get all the dimensions of the matrix.
   * @return An array of the dimension of the matrix
   */
  public ExpressionMatrixDimension[] getDimensions() {

    final String[] dimensionNames = getDimensionNames();

    ExpressionMatrixDimension[] result = new ExpressionMatrixDimension[dimensionNames.length];

    for (int i = 0; i < result.length; i++)
      result[i] = getDimension(dimensionNames[i]);

    return result;
  }

  /**
   * Remove a dimension.
   * @param name The name of the dimension
   */
  public void removeDimension(final String dimensionName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Rename a dimension
   * @param oldName Old name of the dimension
   * @param newName New name of the dimension
   */
  public void renameDimension(final String oldName, final String newName) {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Set the default dimension.
   * @param name Name of the dimension
   */
  public void setDefaultDimensionName(final String name) {

    if (!this.dimensions.containsKey(name))
      throw new ExpressionMatrixRuntimeException(
          "the dimension that you try to reach doesn't exist, dimension name : "
              + name);

    this.defaultDimensionName = name;
  }

  /**
   * Count the entries of the expression matrix that pass the filter.
   * @param filter Filter to apply
   * @return the number of entries that pass the filter
   */
  public int count(final BiologicalFilter filter) {

    if (filter == null)
      return 0;

    if (!(filter instanceof ExpressionMatrixFilter))
      throw new NividicRuntimeException(
          "Only BioAssayfilter can filter BioAssay");

    return count((ExpressionMatrixFilter) filter);
  }

  /**
   * Count the entries of the expression matrix that pass the filter.
   * @param filter Filter to apply
   * @return the number of entries that pass the filter
   */
  public int count(final ExpressionMatrixFilter filter) {

    if (filter == null)
      return 0;

    return filter.count(this);
  }

  /**
   * Create a submatrix of the matrix as the result of a filter.
   * @param filter Filter to apply
   * @return an expression matrix
   */
  public ExpressionMatrix filter(final BiologicalFilter filter) {

    if (filter == null)
      return null;

    if (!(filter instanceof ExpressionMatrixFilter))
      throw new NividicRuntimeException(
          "Only BiologicalListFilter can filter BiologicalList");

    return filter((ExpressionMatrixFilter) filter);
  }

  /**
   * Create a submatrix of the matrix as the result of a filter.
   * @param filter Filter to apply
   * @return an expression matrix
   */
  public ExpressionMatrix filter(final ExpressionMatrixFilter filter) {

    if (filter == null)
      return null;

    return filter.filter(this);
  }

  private void addInitIds(final ExpressionMatrix em, final String[] ids) {

    int len;
    String[] newIds;

    if (ids == null) {
      newIds = em.getRowNames();
      len = newIds.length;
    } else {
      len = ids.length;
      newIds = ids;
    }

    this.idsSet = new HashSet(len);
    this.addIds(em, newIds);
  }

  /**
   * Get the history of the biological object.
   * @return The history object of the object
   */
  public History getHistory() {

    return this.history;
  }

  /**
   * Copy the BioAssay Object.
   * @return a copy of the biological object
   */
  public BioAssay copy() {

    throw new NividicRuntimeException("copy() is not yet implemented.");
  }

  //
  // Listener
  //

  /**
   * Listener that listen all the changes from the ExpressionMatrixDimension and
   * transfer them to the SubExpressionMatrix
   * @param event the event listened by this class
   */
  public void expressionMatrixStateChanged(final ExpressionMatrixEvent event) {

    if (event == null)
      return;

    switch (event.getId()) {

    case ExpressionMatrixEvent.REMOVE_COLUMN_EVENT:

      removeColumns(event.getStringValue());

      break;

    case ExpressionMatrixEvent.REMOVE_ROW_EVENT:

      removeIds(event.getStringValue());

      break;

    case ExpressionMatrixEvent.RENAME_COLUMN_EVENT:

      String[] names = (String[]) event.getObjectValue();

      if (names != null && names.length == 2)
        renameColumns(names[0], names[1]);

      break;

    case ExpressionMatrixEvent.RENAME_ROW_EVENT:

      String[] ids = (String[]) event.getObjectValue();
      if (ids != null && ids.length == 2) {
        renameIds(ids[0], ids[1]);
      }
      break;

    default:
      break;
    }
  }

  //
  // Constructors
  //

  /**
   * constructor of SubExpressionMatrix
   */
  private SubExpressionMatrix() {
  }

  private SubExpressionMatrix(final ExpressionMatrix em,
      final String[] dimensionNames, final String[] ids, final int columnSize) {

    if (em instanceof ExpressionMatrixImpl)
      this.matrix = (ExpressionMatrixImpl) em;
    else if (em instanceof SubExpressionMatrix)
      this.matrix = ((SubExpressionMatrix) em).matrix;

    this.name = em.getName() + "-submatrix-" + count++;
    this.columnNames = new ArrayList(columnSize);
    this.referencesToColumnNamesSet = new HashSet(columnSize);
    addInitIds(em, ids);

    // Set dimensions

    this.dimensions = new LinkedMap();

    final String[] dNames = em.getDimensionNames();

    for (int i = 0; i < dNames.length; i++) {

      String colName = null;

      if (dimensionNames == null)
        colName = dNames[i];
      else
        for (int j = 0; j < dimensionNames.length; j++) {
          if (dNames[i].equals(dimensionNames[j]))
            colName = dNames[i];
        }

      if (colName != null) {
        SubExpressionMatrixDimension subDimension = new SubExpressionMatrixDimension(
            this, (ExpressionMatrixDimensionImpl) matrix.getDimension(colName));
        this.dimensions.put(colName, subDimension);
      }

    }

    // Set default dimension

    if (containsDimension(em.getDefaultDimensionName()))
      setDefaultDimensionName(em.getDefaultDimensionName());
    else if (getDimensionCount() > 0)
      setDefaultDimensionName(getDimensionNames()[0]);

  }

  /**
   * Clear the biological object.
   */
  public void clear() {

    throwsExpressionMatrixRuntimeExceptionForIllegalActions();
  }

  /**
   * Get the size of the biological object.
   * @return The size of the biological object
   */
  public int size() {

    return getRowCount();
  }

  //
  // Constructors
  //

  SubExpressionMatrix(final ExpressionMatrix em, final String[] ids,
      final String[] columns) {

    this(em, null, ids, columns.length);
    addColumns(em, columns);
  }

  SubExpressionMatrix(final ExpressionMatrix em, final String[] ids,
      final int[] columns) {

    this(em, null, ids, columns.length);
    addColumns(em, columns);
  }

  SubExpressionMatrix(final ExpressionMatrix em, final String[] dimensionNames) {

    this(em, dimensionNames, em.getRowNames(), em.getColumnCount());
    addColumns(em, em.getColumnNames());
  }

}