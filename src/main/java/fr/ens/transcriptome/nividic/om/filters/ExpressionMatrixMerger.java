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

package fr.ens.transcriptome.nividic.om.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class define a class to merge ExpressionMatrix data
 * @author Laurent Jourdren
 */
public class ExpressionMatrixMerger {

  private final Map<Integer, String> rowIndex = new HashMap<Integer, String>();
  private final Map<Integer, String> columnIndex =
      new HashMap<Integer, String>();
  private final Map<Integer, String> dimensionIndex =
      new HashMap<Integer, String>();
  private final Map<String, Integer> rowReverseIndex =
      new HashMap<String, Integer>();
  private final Map<String, Integer> columnReverseIndex =
      new HashMap<String, Integer>();
  private final Map<String, Integer> dimensionReverseIndex =
      new HashMap<String, Integer>();

  private final Map<Integer, Location> locations =
      new HashMap<Integer, Location>();
  private final Map<Integer, Double> values = new HashMap<Integer, Double>();

  // private AbstractUnivariateStatistic algo;
  private boolean medianMode = true;

  private int currentDimensionsIndex;
  private int currentValueCount;

  private boolean addStatData = false;

  private final static class Location implements Comparable {

    private static final int INITIAL_NO_ZERO_ODD_NUMBER = 17;
    private static final int MULTIPLIER_NO_ZERO_ODD_NUMBER = 37;

    int row;
    int column;
    int dimension;

    public boolean equals(final Object o) {

      if (o == null || !(o instanceof Location))
        return false;

      final Location loc = (Location) o;

      return loc.row == this.row
          && loc.column == this.column && loc.dimension == this.dimension;
    }

    public int compareTo(final Object o) {

      final Location loc = (Location) o;

      if (loc.dimension != this.dimension)
        return this.dimension - loc.dimension;

      if (loc.column != this.column)
        return this.column - loc.column;

      if (loc.row != this.row)
        return this.row - loc.row;

      return 0;
    }

    public int hashCode() {

      return new HashCodeBuilder(INITIAL_NO_ZERO_ODD_NUMBER,
          MULTIPLIER_NO_ZERO_ODD_NUMBER).append(row).append(column).append(
          dimension).toHashCode();
    }

    public Location(final int row, final int column, final int dimension) {

      this.row = row;
      this.column = column;
      this.dimension = dimension;
    }

  }

  /**
   * Test if the median mode is enable.
   * @return true if the median mode is enable
   */
  public boolean isMedianMode() {

    return medianMode;
  }

  //
  // Setters
  //

  /**
   * Set enable or not the median mode.
   * @param medianMode The median mode
   */
  public void setMedianMode(final boolean medianMode) {
    this.medianMode = medianMode;
  }

  //
  // Other methods
  //

  /**
   * Add a bioAssay to merge.
   * @param bioAssay BioAssay to add
   */
  public void addBioAssay(final BioAssay bioAssay) {

    if (bioAssay == null)
      return;

    addBioAssay(bioAssay, bioAssay.getName());
  }

  /**
   * Add a bioAssay to merge.
   * @param bioAssay BioAssay to add
   * @param columnName name of the bioAssay to add
   */
  public void addBioAssay(final BioAssay bioAssay, final String columnName) {

    if (bioAssay == null || columnName == null)
      return;

    List<String> dimName = new ArrayList<String>();
    for (String dim : this.dimensionReverseIndex.keySet())
      if (bioAssay.isField(dim)
          && (bioAssay.getFieldType(dim) == BioAssay.DATATYPE_DOUBLE || bioAssay
              .getFieldType(dim) == BioAssay.DATATYPE_INTEGER))
        dimName.add(dim);

    final String[] dimensionNames = NividicUtils.toArray(dimName);
    final String[] rowNames = bioAssay.getIds();

    addNames(dimensionNames, dimensionIndex, dimensionReverseIndex);
    addNames(new String[] {columnName}, columnIndex, columnReverseIndex);
    addNames(rowNames, rowIndex, rowReverseIndex);

    // Create index of dimension
    final int[] dimensionIds = new int[dimensionNames.length];
    for (int i = 0; i < dimensionIds.length; i++)
      dimensionIds[i] = dimensionReverseIndex.get(dimensionNames[i]);

    // Create index of column
    int columnId = columnReverseIndex.get(columnName);

    // Create index of row
    final int[] rowIds = new int[rowNames.length];
    for (int i = 0; i < rowIds.length; i++)
      rowIds[i] = rowReverseIndex.get(rowNames[i]);

    for (int i = 0; i < dimensionNames.length; i++) {

      final String dimensionName = dimensionNames[i];

      final double[] data =
          bioAssay.getFieldType(dimensionName) == BioAssay.DATATYPE_DOUBLE
              ? bioAssay.getDataFieldDouble(dimensionName) : NividicUtils
                  .toArrayDouble(bioAssay.getDataFieldInt(dimensionName));

      for (int k = 0; k < data.length; k++) {

        final Location loc = new Location(rowIds[k], columnId, dimensionIds[i]);

        this.locations.put(++this.currentValueCount, loc);
        this.values.put(this.currentValueCount, data[k]);
      }

    }

  }

  /**
   * Add an expression matrix to merge.
   * @param matrix Matrix to merge
   */
  public void addMatrix(final ExpressionMatrix matrix) {

    if (matrix == null)
      return;

    final String[] dimensionNames = matrix.getDimensionNames();
    final String[] columnNames = matrix.getColumnNames();
    final String[] rowNames = matrix.getRowNames();

    addNames(dimensionNames, dimensionIndex, dimensionReverseIndex);
    addNames(columnNames, columnIndex, columnReverseIndex);
    addNames(rowNames, rowIndex, rowReverseIndex);

    // Create index of dimension
    final int[] dimensionIds = new int[dimensionNames.length];
    for (int i = 0; i < dimensionIds.length; i++)
      dimensionIds[i] = dimensionReverseIndex.get(dimensionNames[i]);

    // Create index of column
    final int[] columnIds = new int[columnNames.length];
    for (int i = 0; i < columnIds.length; i++)
      columnIds[i] = columnReverseIndex.get(columnNames[i]);

    // Create index of row
    final int[] rowIds = new int[rowNames.length];
    for (int i = 0; i < rowIds.length; i++)
      rowIds[i] = rowReverseIndex.get(rowNames[i]);

    for (int i = 0; i < dimensionNames.length; i++) {

      final String dimensionName = dimensionNames[i];

      // final int currentDimension = dimensionReverseIndex.get(dimensionName);

      ExpressionMatrixDimension dim = matrix.getDimension(dimensionName);

      for (int j = 0; j < columnNames.length; j++) {

        final String columnName = columnNames[j];
        // int currentColumn = columnReverseIndex.get(columnName);

        final double[] data = dim.getColumnToArray(columnName);

        for (int k = 0; k < data.length; k++) {

          // final Location loc = new Location(k + 1, currentColumn,
          // currentDimension);
          final Location loc =
              new Location(rowIds[k], columnIds[j], dimensionIds[i]);

          this.locations.put(++this.currentValueCount, loc);
          this.values.put(this.currentValueCount, data[k]);
        }

      }

    }

  }

  private void addNames(final String[] name,
      final Map<Integer, String> indexMap,
      final Map<String, Integer> indexReverseMap) {

    for (int i = 0; i < name.length; i++) {
      if (indexReverseMap.containsKey(name[i]))
        continue;
      indexMap.put(++this.currentDimensionsIndex, name[i]);
      indexReverseMap.put(name[i], this.currentDimensionsIndex);
    }

  }

  /**
   * Merge some rows.
   * @param rowsToMerge Names of the rows to merge
   */
  public void mergeRows(final String[] rowsToMerge) {

    if (rowsToMerge == null || rowsToMerge.length < 2)
      return;

    mergeRows(rowsToMerge[0], rowsToMerge);
  }

  /**
   * Merge some rows.
   * @param finalRowName name of the output row
   * @param rowsToMerge Names of the rows to merge
   */
  public void mergeRows(final String finalRowName, final String[] rowsToMerge) {

    if (finalRowName == null
        || rowsToMerge == null
        || !this.rowReverseIndex.containsKey(finalRowName))
      return;

    // Define the list of real rows to merge
    final int finalRowId = this.rowReverseIndex.get(finalRowName);
    final List<Integer> rowIdsToMerge = new ArrayList<Integer>();

    for (int i = 0; i < rowsToMerge.length; i++) {

      final String rowToMerge = rowsToMerge[i];

      if (this.rowReverseIndex.containsKey(rowToMerge)
          && !rowToMerge.equals(finalRowName)) {

        rowIdsToMerge.add(this.rowReverseIndex.get(rowToMerge));
        int indexToRemove = this.rowReverseIndex.remove(rowToMerge);
        this.rowIndex.remove(indexToRemove);
      }
    }

    // Set the new row id of the values to merge
    for (Location loc : locations.values())
      if (rowIdsToMerge.contains(loc.row))
        loc.row = finalRowId;

  }

  /**
   * Merge some columns.
   * @param columnsToMerge name of the output columns
   */
  public void mergeColumns(final String[] columnsToMerge) {

    if (columnsToMerge == null || columnsToMerge.length < 2)
      return;

    mergeColumns(columnsToMerge[0], columnsToMerge);
  }

  /**
   * Merge some columns.
   * @param finalColumnName name of the output column
   * @param columnsToMerge Names of the columns to merge
   */
  public void mergeColumns(final String finalColumnName,
      final String[] columnsToMerge) {

    if (finalColumnName == null
        || columnsToMerge == null
        || !this.columnReverseIndex.containsKey(finalColumnName))
      return;

    // Define the list of real columns to merge
    final int finalColumnId = this.columnReverseIndex.get(finalColumnName);
    final List<Integer> columnIdsToMerge = new ArrayList<Integer>();

    for (int i = 0; i < columnsToMerge.length; i++) {

      final String colToMerge = columnsToMerge[i];

      if (this.columnReverseIndex.containsKey(colToMerge)
          && !colToMerge.equals(finalColumnName)) {

        columnIdsToMerge.add(this.columnReverseIndex.get(colToMerge));
        int indexToRemove = this.columnReverseIndex.remove(colToMerge);
        this.columnIndex.remove(indexToRemove);
      }
    }

    // Set the new column id of the values to merge
    for (Location loc : locations.values())
      if (columnIdsToMerge.contains(loc.column))
        loc.column = finalColumnId;
  }

  /**
   * Merge dimensions.
   * @param dimensionsToMerge names of the dimensions to merge
   */
  public void mergeDimensions(final String[] dimensionsToMerge) {

    if (dimensionsToMerge == null || dimensionsToMerge.length < 2)
      return;

    mergeDimensions(dimensionsToMerge[0], dimensionsToMerge);
  }

  /**
   * Merge dimensions.
   * @param finalDimensionName name of the output dimension
   * @param dimensionsToMerge names of the dimensions to merge
   */
  public void mergeDimensions(final String finalDimensionName,
      final String[] dimensionsToMerge) {

    if (finalDimensionName == null
        || dimensionsToMerge == null
        || !this.dimensionReverseIndex.containsKey(finalDimensionName))
      return;

    // Define the list of real dimension to merge
    final int finalDimensionId =
        this.dimensionReverseIndex.get(finalDimensionName);
    final List<Integer> dimensionIdsToMerge = new ArrayList<Integer>();

    for (int i = 0; i < dimensionsToMerge.length; i++) {

      final String dimToMerge = dimensionsToMerge[i];

      if (this.dimensionReverseIndex.containsKey(dimToMerge)
          && !dimToMerge.equals(finalDimensionName)) {
        dimensionIdsToMerge.add(this.dimensionReverseIndex.get(dimToMerge));
        int indexToRemove = this.dimensionReverseIndex.remove(dimToMerge);
        this.dimensionIndex.remove(indexToRemove);
      }
    }

    // Set the new dimension id of the values to merge
    for (Location loc : locations.values())
      if (dimensionIdsToMerge.contains(loc.dimension))
        loc.dimension = finalDimensionId;

  }

  /**
   * Replace all the row name with a translation. Merge row if needed.
   * @param translator Translator to use
   */
  public void mergeRows(final Translator translator) {

    if (translator == null)
      throw new NullPointerException("The translator is null");

    mergeRows(translator, translator.getDefaultField());
  }

  /**
   * Replace all the row name with a translation. Merge row if needed.
   * @param translator Translator to use
   * @param fieldName Field name of the translator to use
   */
  public void mergeRows(final Translator translator, final String fieldName) {

    if (translator == null)
      throw new NullPointerException("The translator is null");

    if (fieldName == null || !translator.isField(fieldName))
      throw new NividicRuntimeException(
          "The field for the translator is null or not exists");

    Set<String> rowNames = this.rowReverseIndex.keySet();

    final String[] ids = rowNames.toArray(new String[rowNames.size()]);
    final String[] newIds = translator.translateField(ids, fieldName);

    if (newIds == null)
      return;

    // Keep the identifier if there is no translation
    for (int i = 0; i < newIds.length; i++)
      if (newIds[i] == null)
        newIds[i] = ids[i];

    // Create a map with old and new row names
    Map<String, String> mapOldNewRowNames = new HashMap<String, String>();
    for (int i = 0; i < ids.length; i++)
      mapOldNewRowNames.put(ids[i], newIds[i]);

    // Create a map with the rows to merge for each new Id
    Map<String, List<String>> mapIdsToMerge =
        new HashMap<String, List<String>>();

    for (int i = 0; i < newIds.length; i++) {

      final String newId = newIds[i];
      List<String> idsToMerge = mapIdsToMerge.get(newId);

      if (idsToMerge == null) {
        idsToMerge = new ArrayList<String>();
        mapIdsToMerge.put(newId, idsToMerge);
      }

      idsToMerge.add(ids[i]);
    }

    // Merge rows
    for (List<String> idsToMerge : mapIdsToMerge.values())
      mergeRows(NividicUtils.toArray(idsToMerge));

    // Create temporary new rows index
    final Map<Integer, String> newRowIndex = new HashMap<Integer, String>();
    final Map<String, Integer> newRowReverseIndex =
        new HashMap<String, Integer>();

    for (String id : this.rowReverseIndex.keySet()) {
      int index = this.rowReverseIndex.get(id);
      String newId = mapOldNewRowNames.get(id);

      newRowIndex.put(index, newId);
      newRowReverseIndex.put(newId, index);
    }

    // Update the index
    this.rowIndex.clear();
    this.rowReverseIndex.clear();
    this.rowIndex.putAll(newRowIndex);
    this.rowReverseIndex.putAll(newRowReverseIndex);
  }

  /**
   * Build a new Expression matrix from merged data.
   * @return a new ExpressionMatrix
   */
  public ExpressionMatrix getMatrix() {

    // Create a Map<Location, List<Double>
    final Map<Location, List<Double>> locationValues =
        new HashMap<Location, List<Double>>();

    for (int index : this.locations.keySet()) {

      Location loc = this.locations.get(index);

      List<Double> doubleValues = locationValues.get(loc);
      if (doubleValues == null) {

        doubleValues = new ArrayList<Double>();
        locationValues.put(loc, doubleValues);
      }

      doubleValues.add(this.values.get(index));
    }

    // Create a Map<Location, Double>
    final Map<Location, Double> mergedValues = new HashMap<Location, Double>();

    // Create a Map for stats Map<Location, Stat>
    final Map<Location, StatMerger> statsData =
        new HashMap<Location, StatMerger>();

    final boolean medianMode = isMedianMode();

    for (Map.Entry<Location, List<Double>> e : locationValues.entrySet()) {

      final StatMerger statLoc = new StatMerger(e.getValue());
      final double me = medianMode ? statLoc.getMedian() : statLoc.getMean();

      mergedValues.put(e.getKey(), me);
      statsData.put(e.getKey(), statLoc);
    }

    // Rebuild an ExpressionMatrix Object
    final ExpressionMatrix matrix = rebuildMatrix(mergedValues);

    return isAddStatData() ? addStatToMatrix(matrix, statsData) : matrix;
  }

  private ExpressionMatrix rebuildMatrix(
      final Map<Location, Double> mergedValues) {

    final List<Location> locs = new ArrayList<Location>(mergedValues.keySet());

    // Sort the locations
    Collections.sort(locs);

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();

    final List<Integer> dimensionIndexSorted =
        new ArrayList<Integer>(this.dimensionIndex.keySet());
    final List<Integer> columnIndexSorted =
        new ArrayList<Integer>(this.columnIndex.keySet());
    final List<Integer> rowIndexSorted =
        new ArrayList<Integer>(this.rowIndex.keySet());

    Collections.sort(dimensionIndexSorted);
    Collections.sort(columnIndexSorted);
    Collections.sort(rowIndexSorted);

    final String[] rowNames = rebuildRowNames(rowIndexSorted);

    int count = 0;
    for (int dimIndex : dimensionIndexSorted) {

      final String dimensionName = this.dimensionIndex.get(dimIndex);

      if (!em.containsDimension(dimensionName))
        em.addDimension(dimensionName);
      ExpressionMatrixDimension dim = em.getDimension(dimensionName);

      final BioAssay ba = BioAssayFactory.createBioAssay();
      ba.setIds(rowNames);

      for (int indexCol : columnIndexSorted) {

        final String colName = this.columnIndex.get(indexCol);
        double[] data = new double[rowNames.length];

        for (int i = 0; i < data.length; i++)
          data[i] = mergedValues.get(locs.get(count + i));

        count += rowNames.length;

        ba.setName(colName);
        ba.setDataFieldDouble(dimensionName, data);
        dim.addBioAssay(ba);
      }
    }

    return em;
  }

  private String[] rebuildRowNames(final List<Integer> rowIndexSorted) {

    final String[] result = new String[rowIndexSorted.size()];

    int i = 0;
    for (int index : rowIndexSorted)
      result[i++] = this.rowIndex.get(index);

    return result;
  }

  private ExpressionMatrix addStatToMatrix(final ExpressionMatrix em,
      Map<Location, StatMerger> statsData) {

    if (em == null)
      return null;

    final List<Location> locs = new ArrayList<Location>(statsData.keySet());

    // Sort the locations
    Collections.sort(locs);

    final List<Integer> dimensionIndexSorted =
        new ArrayList<Integer>(this.dimensionIndex.keySet());
    final List<Integer> columnIndexSorted =
        new ArrayList<Integer>(this.columnIndex.keySet());
    final List<Integer> rowIndexSorted =
        new ArrayList<Integer>(this.rowIndex.keySet());

    Collections.sort(dimensionIndexSorted);
    Collections.sort(columnIndexSorted);
    Collections.sort(rowIndexSorted);

    final String[] rowNames = rebuildRowNames(rowIndexSorted);

    int count = 0;
    int rowNamesSize = 0;
    for (int dimIndex : dimensionIndexSorted) {

      final String dimensionName = this.dimensionIndex.get(dimIndex);

      final String[] statDimensions =
          {" n", " total n", " stdDev", " mean", " median"};

      for (int i = 0; i < statDimensions.length; i++) {

        final String dimensionNameStat = dimensionName + statDimensions[i];

        if (!em.containsDimension(dimensionNameStat))
          em.addDimension(dimensionNameStat);
        ExpressionMatrixDimension dim = em.getDimension(dimensionNameStat);

        final BioAssay ba = BioAssayFactory.createBioAssay();
        ba.setIds(rowNames);

        for (int indexCol : columnIndexSorted) {

          final String colName = this.columnIndex.get(indexCol);
          double[] data = new double[rowNames.length];

          for (int j = 0; j < data.length; j++) {

            final StatMerger statLoc = statsData.get(locs.get(count + j));

            switch (i) {
            case 0:
              data[j] = statLoc.getN();
              break;
            case 1:
              data[j] = statLoc.getTotalN();
              break;
            case 2:
              data[j] = statLoc.getStdDev();
              break;
            case 3:
              data[j] = statLoc.getMean();
              break;
            case 4:
              data[j] = statLoc.getMedian();
              break;

            default:
              break;
            }

            // data[i] = mergedValues.get(locs.get(count + i));
          }

          rowNamesSize = rowNames.length;
          // count += rowNames.length;

          ba.setName(colName);

          ba.setDataFieldDouble(dimensionNameStat, data);
          dim.addBioAssay(ba);
        }
      }
      count += rowNamesSize;
    }

    return em;

  }

  /**
   * Rename a row.
   * @param oldName Old name of the row
   * @param newName New name of the row
   */
  public void renameRow(final String oldName, final String newName) {

    if (!this.rowReverseIndex.containsKey(oldName))
      throw new NividicRuntimeException("Unknown row to rename");
    if (this.rowReverseIndex.containsKey(newName))
      throw new NividicRuntimeException("The new name of the row already");

    final int index = this.rowReverseIndex.get(oldName);

    // Update rowReverseIndex
    this.rowReverseIndex.remove(oldName);
    this.rowReverseIndex.put(newName, index);

    // Update rowIndex
    this.rowIndex.put(index, newName);
  }

  /**
   * Rename a column.
   * @param oldName Old name of the column
   * @param newName New name of the column
   */
  public void renameColumn(final String oldName, final String newName) {

    if (!this.columnReverseIndex.containsKey(oldName))
      throw new NividicRuntimeException("Unknown row to rename");
    if (this.columnReverseIndex.containsKey(newName))
      throw new NividicRuntimeException("The new name of the row already");

    final int index = this.columnReverseIndex.get(oldName);

    // Update columnReverseIndex
    this.columnReverseIndex.remove(oldName);
    this.columnReverseIndex.put(newName, index);

    // Update columnIndex
    this.columnIndex.put(index, newName);
  }

  /**
   * Rename a dimension.
   * @param oldName Old name of the dimension
   * @param newName New name of the dimension
   */
  public void renameDimension(final String oldName, final String newName) {

    if (!this.dimensionReverseIndex.containsKey(oldName))
      throw new NividicRuntimeException("Unknown row to rename");
    if (this.dimensionReverseIndex.containsKey(newName))
      throw new NividicRuntimeException("The new name of the row already");

    final int index = this.dimensionReverseIndex.get(oldName);

    // Update dimensionReverseIndex
    this.dimensionReverseIndex.remove(oldName);
    this.dimensionReverseIndex.put(newName, index);

    // Update dimensionIndex
    this.dimensionIndex.put(index, newName);
  }

  /**
   * Remove a row.
   * @param rowName name of the row to remove
   */
  public void removeRow(final String rowName) {

    if (!this.rowReverseIndex.containsKey(rowName))
      throw new NividicRuntimeException("Unknown row to remove");

    // Update rowReverseIndex
    final int index = this.rowReverseIndex.remove(rowName);

    // Update rowIndex
    this.rowIndex.remove(index);
  }

  /**
   * Remove a column.
   * @param columnName name of the column to remove
   */
  public void removeColumn(final String columnName) {

    if (!this.columnReverseIndex.containsKey(columnName))
      throw new NividicRuntimeException("Unknown row to remove");

    // Update columnReverseIndex
    final int index = this.columnReverseIndex.remove(columnName);

    // Update columnIndex
    this.columnIndex.remove(index);
  }

  /**
   * Remove a dimension.
   * @param dimensionName name of the dimension to remove
   */
  public void removeDimension(final String dimensionName) {

    if (!this.dimensionReverseIndex.containsKey(dimensionName))
      throw new NividicRuntimeException("Unknown row to remove");

    // Update columnReverseIndex
    final int index = this.dimensionReverseIndex.remove(dimensionName);

    // Update dimensionIndex
    this.dimensionIndex.remove(index);
  }

  /**
   * Add a dimension.
   * @param dimensionName name of the dimension to add
   */
  public void addDimension(final String dimensionName) {

    if (dimensionName == null
        || this.dimensionReverseIndex.containsKey(dimensionName))
      return;

    this.dimensionIndex.put(++this.currentDimensionsIndex, dimensionName);
    this.dimensionReverseIndex.put(dimensionName, this.currentDimensionsIndex);

    final int dimensionIndex = this.dimensionReverseIndex.get(dimensionName);

    final Set<String> columnNames = this.columnReverseIndex.keySet();
    final Set<String> rowNames = this.rowReverseIndex.keySet();

    for (String columnName : columnNames) {

      final int columnIndex = this.columnReverseIndex.get(columnName);

      for (String rowName : rowNames) {

        final int rowIndex = this.rowReverseIndex.get(rowName);

        final Location loc =
            new Location(rowIndex, columnIndex, dimensionIndex);
        this.locations.put(++this.currentValueCount, loc);
        this.values.put(this.currentValueCount, Double.NaN);
      }
    }

  }

  /**
   * Test if stats data must be added to the output matrix.
   * @return Returns the addStatData
   */
  public boolean isAddStatData() {
    return addStatData;
  }

  /**
   * Set if the stats data must be added to the output matrix.
   * @param addStatData The addStatData to set
   */
  public void setAddStatData(final boolean addStatData) {
    this.addStatData = addStatData;
  }

}
