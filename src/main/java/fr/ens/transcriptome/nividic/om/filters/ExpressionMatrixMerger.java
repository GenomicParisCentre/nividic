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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.rank.Median;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;

/**
 * This class define a class to merge ExpressionMatrix data
 * @author Laurent Jourdren
 */
public class ExpressionMatrixMerger {

  private Map<Integer, String> rowIndex = new HashMap<Integer, String>();
  private Map<Integer, String> columnIndex = new HashMap<Integer, String>();
  private Map<Integer, String> dimensionIndex = new HashMap<Integer, String>();
  private Map<String, Integer> rowReverseIndex = new HashMap<String, Integer>();
  private Map<String, Integer> columnReverseIndex = new HashMap<String, Integer>();
  private Map<String, Integer> dimensionReverseIndex = new HashMap<String, Integer>();

  private Map<Integer, Location> locations = new HashMap<Integer, Location>();
  private Map<Integer, Double> values = new HashMap<Integer, Double>();

  private AbstractUnivariateStatistic algo;
  private boolean medianMode = true;

  private final class Location implements Comparable {

    int row;
    int column;
    int dimension;

    public boolean equals(Object o) {

      final Location loc = (Location) o;

      return loc.row == this.row && loc.column == this.column
          && loc.dimension == this.dimension;
    }

    public int compareTo(Object o) {

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

      return new HashCodeBuilder(17, 37).append(row).append(column).append(
          dimension).toHashCode();
    }

    public Location(int row, int column, int dimension) {

      this.row = row;
      this.column = column;
      this.dimension = dimension;
    }

  }

  //
  // Getters
  //

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
   * Add an expression matrix to merge.
   * @param matrix Matrix to merge
   */
  public void addExpressionMatrix(final ExpressionMatrix matrix) {

    if (matrix == null)
      return;

    final String[] dimensionNames = matrix.getDimensionNames();
    final String[] columnNames = matrix.getColumnNames();
    final String[] rowNames = matrix.getRowNames();

    addNames(dimensionNames, dimensionIndex, dimensionReverseIndex);
    addNames(columnNames, columnIndex, columnReverseIndex);
    addNames(rowNames, rowIndex, rowReverseIndex);

    int currentValueCount = this.values.size();

    for (int i = 0; i < dimensionNames.length; i++) {

      final String dimensionName = dimensionNames[i];
      final int currentDimension = dimensionReverseIndex.get(dimensionName);

      ExpressionMatrixDimension dim = matrix.getDimension(dimensionName);

      for (int j = 0; j < columnNames.length; j++) {

        final String columnName = columnNames[j];
        int currentColumn = columnReverseIndex.get(columnName);

        final double[] data = dim.getColumnToArray(columnName);

        for (int k = 0; k < data.length; k++) {

          final Location loc = new Location(k + 1, currentColumn,
              currentDimension);
          this.locations.put(++currentValueCount, loc);
          this.values.put(currentValueCount, data[k]);
        }

      }

    }

  }

  private void addNames(final String[] name,
      final Map<Integer, String> indexMap, Map<String, Integer> indexReverseMap) {

    int currentIndex = indexMap.size();

    for (int i = 0; i < name.length; i++) {
      indexMap.put(++currentIndex, name[i]);
      indexReverseMap.put(name[i], currentIndex);
    }

  }

  public void mergeRow(final String finalRowName, final String[] rowsToMerge) {

    if (finalRowName == null || rowsToMerge == null
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

  public void mergeColumn(final String finalColumnName,
      final String[] columnsToMerge) {

    if (finalColumnName == null || columnsToMerge == null
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
   * Merge dimensions
   * @param finalDimensionName
   * @param dimensionsToMerge
   */
  public void mergeDimension(final String finalDimensionName,
      final String[] dimensionsToMerge) {

    if (finalDimensionName == null || dimensionsToMerge == null
        || !this.dimensionReverseIndex.containsKey(finalDimensionName))
      return;

    // Define the list of real dimension to merge
    final int finalColumnId = this.dimensionReverseIndex
        .get(finalDimensionName);
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
        loc.dimension = finalColumnId;

  }

  /**
   * Build a new Expression matrix from merged data.
   * @return a new ExpressionMatrix
   */
  public ExpressionMatrix getExpressionMatrix() {

    // Create a Map<Location, List<Double>
    final Map<Location, List<Double>> locationValues = new HashMap<Location, List<Double>>();

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

    defineAlgo();

    for (Location loc : locationValues.keySet()) {

      List<Double> doubleValues = locationValues.get(loc);
      mergedValues.put(loc, algo(doubleValues));
    }

    // Rebuild an ExpressionMatrix Object
    return rebuildMatrix(mergedValues);
  }

  private ExpressionMatrix rebuildMatrix(Map<Location, Double> mergedValues) {

    final List<Location> locs = new ArrayList<Location>(mergedValues.keySet());

    // Sort the locations
    Collections.sort(locs);

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();

    final List<Integer> dimensionIndexSorted = new ArrayList<Integer>(
        this.dimensionIndex.keySet());
    final List<Integer> columnIndexSorted = new ArrayList<Integer>(
        this.columnIndex.keySet());
    final List<Integer> rowIndexSorted = new ArrayList<Integer>(this.rowIndex
        .keySet());

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

  private String[] rebuildRowNames(List<Integer> rowIndexSorted) {

    final String[] result = new String[rowIndexSorted.size()];

    int i = 0;
    for (int index : rowIndexSorted)
      result[i++] = this.rowIndex.get(index);

    return result;
  }

  private void defineAlgo() {

    if (isMedianMode())
      algo = new Median();
    else
      algo = new Mean();

  }

  private double algo(final List<Double> doubleValues) {

    if (doubleValues.size() == 1)
      return doubleValues.get(0);

    // Define the algorithm
    return this.algo.evaluate(toArrayDouble(doubleValues));
  }

  private double[] toArrayDouble(final List<Double> doubleValues) {

    if (doubleValues == null)
      return null;

    double[] result = new double[doubleValues.size()];

    int i = 0;
    for (double d : doubleValues)
      result[i++] = d;

    return result;
  }

}
