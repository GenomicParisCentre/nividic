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
import java.util.List;
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
import fr.ens.transcriptome.nividic.om.Slide;
import fr.ens.transcriptome.nividic.om.filters.BiologicalFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * Implementation class for ExpressionMatrixDimension objects.
 * @author montout
 */
public class ExpressionMatrixImpl implements ExpressionMatrix,
    ExpressionMatrixListenerHandler {

  private IterableMap idsMap;
  private int rowCreatedCount;
  // private IterableMap referencesToColumnNamesMap;

  private ArrayList columnNamesArrayList;

  private String expressionMatrixName;
  private Annotation annotations = AnnotationFactory.createAnnotation();

  private static int count;
  private static int copyNumber;
  private static Random random = new Random(System.currentTimeMillis());
  private Set listeners = new HashSet();

  private static final int HASHCODE_ODD_NUMBER_1 = 16052005;
  private static final int HASHCODE_ODD_NUMBER_2 = 17;
  private HistoryImpl history = new HistoryImpl();

  private IterableMap dimensionMap;
  private String defaultDimensionName = ExpressionMatrix.DIMENSION_M;

  //
  // Getters
  //

  /**
   * Get the number of rows created in the matrix.
   */
  int getRowCreatedCount() {

    return this.rowCreatedCount;
  }

  /**
   * Get a Set of the listener of the object.
   * @return A Set of the listeners
   */
  public Set getListeners() {
    return this.listeners;
  }

  /**
   * Get a dimension of the matrix.
   * @param name Name of the dimension
   */
  public ExpressionMatrixDimension getDimension(final String name) {

    if (name == null)
      return null;

    return (ExpressionMatrixDimension) this.dimensionMap.get(name);
  }

  /**
   * Test if a dimension exists.
   * @param dimensionName dimension to test
   * @return true if the dimension exists
   */
  public boolean containsDimension(final String dimensionName) {

    if (dimensionName == null)
      return false;

    return this.dimensionMap.containsKey(dimensionName);
  }

  /**
   * Get the names of the dimension of the matrix
   * @return The names of the dimension of the matrix
   */
  public String[] getDimensionNames() {

    int n = this.dimensionMap.size();

    String[] result = new String[n];

    Iterator it = this.dimensionMap.keySet().iterator();

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
  public void removeDimension(final String name) {

    if (!containsDimension(name))
      return;

    ExpressionMatrixDimensionImpl emd = (ExpressionMatrixDimensionImpl) this.dimensionMap
        .get(name);

    removeListener(emd);

    this.dimensionMap.remove(name);

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.REMOVE_DIMENSION_EVENT, name));
  }

  /**
   * Add a dimension.
   * @param dimensionName Name of the dimension to add
   */
  public void addDimension(final String dimensionName) {

    if (containsDimension(dimensionName))
      throw new ExpressionMatrixRuntimeException("Dimension already exists");

    ExpressionMatrixDimensionImpl emd = new ExpressionMatrixDimensionImpl(this,
        dimensionName);
    addListener(emd);

    this.dimensionMap.put(dimensionName, emd);

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.ADD_DIMENSION_EVENT, dimensionName));

  }

  /**
   * Add a new dimension to the matrix with the values of another dimension.
   * @param dimension Dimension to add
   */
  public void addDimension(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      return;

    addDimension(dimension, dimension.getDimensionName());
  }

  /**
   * Add a new dimension to the matrix with the values of another dimension.
   * @param dimension Dimension to add
   * @param dimensionName the name of the dimension in the matrix
   */
  public void addDimension(final ExpressionMatrixDimension dimension,
      final String dimensionName) {

    if (dimension == null || dimensionName == null)
      return;

    addDimension(dimensionName);
    ExpressionMatrixDimension d = getDimension(dimensionName);

    String[] ids = dimension.getRowIds();
    String[] columnNames = dimension.getColumnNames();

    for (int i = 0; i < columnNames.length; i++) {

      final String columnName = columnNames[i];

      d.addColumn(columnName);

      final int columnIndex = d.getColumnIndex(columnName);
      final double[] values = dimension.getColumnToArray(columnIndex);

      d.setValues(ids, columnIndex, values);
    }

  }

  /**
   * Get the index of a row that the Id is known
   * @param rowId The name of the row that you want the index
   * @return an int value
   * @throws ExpressionMatrixRuntimeException if the id which index you want
   *           doesn't exist
   */
  int getInternalRowIdIndex(final String rowId)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, you can't get any values");

    throwExceptionIfRowIdDoesntExists(rowId);

    int index = ((Integer) this.idsMap.get(rowId)).intValue();

    return index;
  }

  /**
   * Get the names of the rows
   * @return return the names of the rows in an array of strings
   * @throws ExpressionMatrixRuntimeException if the matrix is empty
   */
  public String[] getRowIds() throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      return new String[] {};

    String[] ids = new String[this.idsMap.size()];

    idsMap.keySet().toArray(ids);

    return ids;
  }

  /**
   * Get the names of the columns
   * @return return the names of the columns in an array of strings
   * @throws ExpressionMatrixRuntimeException if the matrix is empty
   */
  public String[] getColumnNames() throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      return new String[] {};

    final int n = getColumnCount();

    String[] columnNames = new String[n];

    this.columnNamesArrayList.toArray(columnNames);

    return columnNames;
  }

  /**
   * Get the number of dimensions.
   * @return The number of dimensions
   */
  public int getDimensionCount() {

    return this.dimensionMap.size();
  }

  /**
   * Get the name of the ExpressionMatrixDimension.
   * @return The name of the ExpressionMatrixDimension.
   */
  public String getName() {

    return this.expressionMatrixName;
  }

  /**
   * Get the annotation for the expression matrix.
   * @return The annotation object
   */
  public Annotation getAnnotation() {

    return this.annotations;
  }

  /**
   * Gives the second dimention of the matrix, the number of columns
   * @return an int , the number of columns of the matrix
   */
  public int getColumnCount() {

    return this.columnNamesArrayList.size();
  }

  /**
   * get the index of a column if it exists
   * @param columnName the name of the column which you want the index
   * @return the index of the column
   * @throws ExpressionMatrixRuntimeException if the column doesn't exists
   */
  public int getColumnIndex(final String columnName)
      throws ExpressionMatrixRuntimeException {

    if (columnName == null)
      throw new ExpressionMatrixRuntimeException("Column Name is null");

    return this.columnNamesArrayList.indexOf(columnName);
  }

  /**
   * get the name of a column if it exists
   * @param columnNumber the index of the column which you want the name
   * @return the name of the column
   * @throws ExpressionMatrixRuntimeException if the column doesn't exists
   */
  public String getColumnName(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    throwExceptionIfColumnDoesntExists(columnNumber);

    return (String) this.columnNamesArrayList.get(columnNumber);
  }

  //
  // Setters
  //
  /**
   * Gives a name to the matrix
   * @param name name of the matrix
   */
  public void setName(final String name) {

    this.expressionMatrixName = name;
  }

  /**
   * add a new ExpressionMatrixListener to the set of Listeners
   * @param listener the new ExpressionMatrixListener to add
   */
  public void addListener(final ExpressionMatrixListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Set a new name to a row
   * @param formerName The former name of the row That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException if you try to rename a row that
   *           doesn't exist or if the new name of the row does exist yet
   */
  public void renameRow(final String formerName, final String newName)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, you can't set any values");

    throwExceptionIfRowIdDoesntExists(formerName);

    if (this.idsMap.containsKey(newName))
      throw new ExpressionMatrixRuntimeException("The name " + newName
          + " that you intent to set does exist yet");

    Integer index = new Integer(this.getInternalRowIdIndex(formerName));
    this.removeRow(formerName);

    this.idsMap.put(newName, index);

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.RENAME_ROW_EVENT, new String[] {formerName,
            newName}));

  }

  /**
   * Set a name to a column
   * @param columnNumber The index of the column That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException if you try to rename a column that
   *           doesn't exist or if the new name of the column does exist yet
   */
  public void renameColumn(final int columnNumber, final String newName)
      throws ExpressionMatrixRuntimeException {

    String formerName = getColumnName(columnNumber);

    renameColumn(formerName, newName);
  }

  /**
   * Set a name to a column
   * @param formerName The former name of the column That you want to rename
   * @param newName The name that you want to set
   * @throws ExpressionMatrixRuntimeException if you try to rename a column that
   *           doesn't exist
   */
  public void renameColumn(final String formerName, final String newName)
      throws ExpressionMatrixRuntimeException {

    throwExceptionIfColumnDoesntExists(formerName);

    final int index = getColumnIndex(newName);

    if (index != -1)
      throw new ExpressionMatrixRuntimeException("The name " + newName
          + " that you intent to set does exist yet");

    this.columnNamesArrayList.set(getColumnIndex(formerName), newName);

    // Send a message to all the listener
    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.RENAME_COLUMN_EVENT, new String[] {formerName,
            newName}));
  }

  /**
   * Remove an Id row
   * @param rowId the id of the spot
   * @throws ExpressionMatrixRuntimeException if the row id that you try to
   *           remove doesn't exist
   */
  public void removeRow(final String rowId)
      throws ExpressionMatrixRuntimeException {

    if (this.isNoRow())
      throw new ExpressionMatrixRuntimeException(
          "the matrix is empty, you can't set any values");

    throwExceptionIfRowIdDoesntExists(rowId);

    this.idsMap.remove(rowId);

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.REMOVE_ROW_EVENT, rowId));

  }

  /**
   * Remove an Experience field
   * @param columnName the index of the Experience
   * @throws ExpressionMatrixRuntimeException if the column that you try to
   *           remove doesn't exist
   */
  public void removeColumn(final String columnName)
      throws ExpressionMatrixRuntimeException {

    removeColumn(getColumnIndex(columnName));

  }

  /**
   * Remove an Experience field
   * @param index the index of the Experience
   * @throws ExpressionMatrixRuntimeException if the column that you try to
   *           remove doesn't exist
   */
  public void removeColumn(final int index)
      throws ExpressionMatrixRuntimeException {

    throwExceptionIfColumnDoesntExists(index);

    String columnName = (String) this.columnNamesArrayList.get(index);

    this.columnNamesArrayList.remove(index);

    // Send a message to all the listener
    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.REMOVE_COLUMN_EVENT, columnName));

  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   */
  public void addBioAssay(final BioAssay bioAssay) {

    addBioAssay(bioAssay, null, null, null);
  }

  /**
   * Add all the bioAssays of a design to the matrix.
   * @param design Design to add
   */
  public void addDesign(final Design design) {

    if (design == null)
      throw new NullPointerException("design is null");

    final List<Slide> slides = design.getSlides();

    for (Slide slide : slides) {

      final BioAssay ba = slide.getBioAssay();
      if (ba != null)
        addBioAssay(ba);
    }

  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName) {

    addBioAssay(bioAssay, columnName, null, null);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param columnName The name of column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName,
      final Translator translator) {

    addBioAssay(bioAssay, columnName, translator, null);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator) {

    addBioAssay(bioAssay, null, translator, null);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final Translator translator,
      final String translatorField) {

    addBioAssay(bioAssay, null, translator, translatorField);
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param translator Translator to use to define rowIds
   * @param translatorField Field of the translator to use
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName,
      final Translator translator, final String translatorField) {

    if (bioAssay == null)
      return;

    String[] dimensionNames = getDimensionNames();
    for (int i = 0; i < dimensionNames.length; i++)
      if (bioAssay.isField(dimensionNames[i])) {

        final ExpressionMatrixDimension dimension = getDimension(dimensionNames[i]);

        dimension
            .addBioAssay(bioAssay, columnName, translator, translatorField);
      }
  }

  /**
   * Add a column to the matrix
   * @param bioAssay The new column to add
   * @param fieldName The fieldName of the bioAssay column to add
   * @param dimensionName The name of the dimenson to add
   */
  public void addBioAssayDimension(final BioAssay bioAssay,
      final String fieldName, final String dimensionName) {

    if (bioAssay == null || fieldName == null || dimensionName == null)
      return;

    if (bioAssay.isField(fieldName)) {
      if (!containsDimension(dimensionName))
        addDimension(dimensionName);
      getDimension(dimensionName).addBioAssay(bioAssay);
    }

  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnNumber the index of the column in reference ArrayList
   * @return true if this column exists, false if not
   */
  public boolean containsColumn(final int columnNumber) {

    return columnNumber >= 0 && columnNumber < this.columnNamesArrayList.size();
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnName the column name that we want to check
   * @return true if the column exists, false if not
   */
  public boolean containsColumn(final String columnName) {

    if (columnName == null)
      throw new ExpressionMatrixRuntimeException("String columnName is null");

    return getColumnIndex(columnName) != -1;
  }

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given rowId
   * @param rowId the row name that we want to check
   * @return true if the rowId exists, false if not
   */
  public boolean containsRowId(final String rowId) {

    if (rowId == null)
      throw new ExpressionMatrixRuntimeException("String rowId is null");

    return this.idsMap.containsKey(rowId);

  }

  /**
   * Create a sub matrix, choosing the rows that you want to keep in it
   * @param rowsId Rows that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixRows(final String[] rowsId) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this, rowsId, this
        .getColumnNames());

    addListener(sem);

    return sem;
  }

  /**
   * Create a sub matrix, choosing the rows that you want to keep in it
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixColumns(final int[] columns) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this, this.getRowIds(),
        columns);

    addListener(sem);

    return sem;
  }

  /**
   * Create a sub matrix, choosing the rows that you want to keep in it
   * @param columns Columns that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixColumns(final String[] columns) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this, this.getRowIds(),
        columns);

    addListener(sem);

    return sem;
  }

  /**
   * Create a sub matrix, choosing the dimension that you want to keep in it
   * @param dimensionName Dimension that you want to keep
   * @return An ExpressionMatrixDimension object
   */
  public ExpressionMatrix subMatrixDimensions(final String[] dimensionNames) {

    SubExpressionMatrix sem = new SubExpressionMatrix(this, dimensionNames);

    addListener(sem);

    return sem;
  }

  /**
   * Give the first dimention of the matrix, the number of rows.
   * @return an int , the number of rows of the matrix
   */
  public int getRowCount() {

    if (this.isNoRow())
      return 0;

    return this.idsMap.size();
  }

  /**
   * Checks if <b>this </b> ExpressionMatrixDimension object and the
   * ExpressionMatrixDimension em are equals.
   * @param o The other ExpressionMatrixDimension that you want to compare to
   *          <b>this </b> one
   * @return <b>true </b> if em and this are aquals.
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof ExpressionMatrix))
      return false;

    if (!dataEquals(o))
      return false;

    final ExpressionMatrix em = (ExpressionMatrix) o;

    if (this.annotations == null) {
      if (em.getAnnotation() != null)
        return false;
    } else if (!getAnnotation().equals(em.getAnnotation()))
      return false;

    if (this.expressionMatrixName == null) {
      if (em.getName() != null)
        return false;
    }

    return this.expressionMatrixName.equals(em.getName());
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

    hcb.append(this.getName()).append(this.getColumnNames())
        .append(this.idsMap);

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

    //
    // TODO rename the method
    //

    return this.idsMap.size() == 0;

  }

  /**
   * Add a column.
   * @param columnName Name of the new column
   */
  public void addColumn(final String columnName) {

    if (columnName == null)
      throw new ExpressionMatrixRuntimeException(
          "the name of the column to add is null");

    if (containsColumn(columnName))
      return;

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.ADD_COLUMN_EVENT, columnName));

    this.columnNamesArrayList.add(columnName);
  }

  /**
   * Add a row.
   * @param rowName Name of the new id
   */
  public void addRow(final String rowName) {

    if (containsRowId(rowName))
      throw new ExpressionMatrixRuntimeException(
          "the id that you try to create already exist, id name : " + rowName);

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.ADD_ROW_EVENT, rowName));

    this.idsMap.put(rowName, new Integer(rowCreatedCount++));
  }

  /**
   * Rename a dimension
   * @param oldName Old name of the dimension
   * @param newName New name of the dimension
   */
  public void renameDimension(final String oldName, final String newName) {

    if (!containsDimension(oldName))
      throw new ExpressionMatrixRuntimeException(
          "the dimension doesn't exists name : " + oldName);

    Object d = this.dimensionMap.get(oldName);
    this.dimensionMap.put(newName, d);

    if (this.defaultDimensionName.equals(oldName))
      this.defaultDimensionName = newName;

    sendEvent(new ExpressionMatrixEvent(this,
        ExpressionMatrixEvent.RENAME_DIMENSION_EVENT, oldName, newName));
  }

  /**
   * Get the defaultDimension.
   * @return the default dimension
   */
  public ExpressionMatrixDimension getDefaultDimension() {

    return getDimension(this.defaultDimensionName);
  }

  /**
   * Set the default dimension.
   * @param name Name of the dimension
   */
  public void setDefaultDimensionName(final String name) {

    if (!this.dimensionMap.containsKey(name))
      throw new ExpressionMatrixRuntimeException(
          "the dimension that you try to reach doesn't exist, dimension name : "
              + name);

    this.defaultDimensionName = name;
  }

  /**
   * Get the default dimension name.
   * @return The name of the default dimension
   */
  public String getDefaultDimensionName() {

    return this.defaultDimensionName;
  }

  //
  // Exception methods
  //

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
   * @param columnNumber the rank of the column that you want to test
   * @throws ExpressionMatrixRuntimeException if the column doesn't exist
   */
  void throwExceptionIfColumnDoesntExists(final int columnNumber)
      throws ExpressionMatrixRuntimeException {

    if (!this.containsColumn(columnNumber))
      throw new ExpressionMatrixRuntimeException(
          "the column that you try to reach doesn't exist, column number : "
              + columnNumber);
  }

  /**
   * Throws an ExpressionMatrixRuntimeException if the row doesn't exist
   * @param rowId the name of the row that you want to test
   * @throws ExpressionMatrixRuntimeException if the row doesn't exist
   */
  void throwExceptionIfRowIdDoesntExists(final String rowId)
      throws ExpressionMatrixRuntimeException {

    if (!this.containsRowId(rowId))
      throw new ExpressionMatrixRuntimeException(
          "the row ID that you try to reach doesn't exist: " + rowId);
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

  //
  // Listeners
  //

  /**
   * remove a Listener from the ExpressionMatrixListener list
   * @param listener the listener to remove
   */
  public void removeListener(final ExpressionMatrixListener listener) {
    this.listeners.remove(listener);
  }

  private void sendEvent(final ExpressionMatrixEvent event) {

    if (event == null)
      return;

    Iterator it = getListeners().iterator();

    if (it.hasNext())
      while (it.hasNext()) {
        ExpressionMatrixListener l = (ExpressionMatrixListener) it.next();
        l.expressionMatrixStateChanged(event);
      }

  }

  //
  // Constructor
  //

  /**
   * Constructor of the class ExpressionMatrixImpl
   */
  public ExpressionMatrixImpl() {

    this((String) null);
  }

  /**
   * Constructor of the class ExpressionMatrixImpl
   * @param name the name of the expression matrix
   */
  public ExpressionMatrixImpl(final String name) {
    count++;

    if (name == null)
      this.setName("ExpressionMatrixDimension-" + System.currentTimeMillis()
          + "-" + random.nextInt() + "-" + count);
    else
      this.setName(name);

    this.idsMap = new LinkedMap();
    this.columnNamesArrayList = new ArrayList();
    this.dimensionMap = new LinkedMap();
    addDimension(getDefaultDimensionName());

  }

  /**
   * Constructor of the class ExpressionMatrixImpl, makes a sopy of an other
   * ExpressionMatrixDimension object
   * @param matrix the matrix that you want to copy
   */
  public ExpressionMatrixImpl(final ExpressionMatrix matrix) {

    this(matrix, null);
  }

  /**
   * Constructor of the class ExpressionMatrixImpl, makes a sopy of an other
   * ExpressionMatrixDimension object
   * @param matrix the matrix that you want to copy
   * @param name the new name of the copy
   */
  public ExpressionMatrixImpl(final ExpressionMatrix matrix, final String name) {

    copyNumber++;

    this.idsMap = new LinkedMap();
    this.columnNamesArrayList = new ArrayList();
    this.dimensionMap = new LinkedMap();

    if (name == null)
      this.setName("copy(" + copyNumber + ")of-" + matrix.getName());
    else
      this.setName(name);

    getAnnotation().addProperties(matrix.getAnnotation());

    ExpressionMatrixDimension[] dimensions = matrix.getDimensions();

    for (int i = 0; i < dimensions.length; i++)
      addDimension(dimensions[i]);

    setDefaultDimensionName(matrix.getDefaultDimensionName());
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

  /**
   * Clear the biological object.
   */
  public void clear() {

    throw new NividicRuntimeException("Not yet implemented");
  }

  /**
   * Get the size of the biological object.
   * @return The size of the biological object
   */
  public int size() {

    return getRowCount();
  }

}