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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class DoubleMatrix {

  //private static final double NATURAL_LOG_OF_2 = 0.69314718;
  private static final double NATURAL_LOG_OF_2 = 0.6931471805595;
  //private static final double LOG2_TO_LOG10 = 0.301029995;
  private static final double LOG2_TO_LOG10 = 0.30102999566398;
  private static final int MAX_STEPS = 1000000;
  
  
  private ExpressionMatrixDimension dimension;
  private String[] columnNames;
  private String[] rowNames;

  /**
   * Get row dimension.
   * @return m, the number of rows.
   */

  public int getRowDimension() {
    return rowNames.length;
  }

  /**
   * Get column dimension.
   * @return n, the number of columns.
   */

  public int getColumnDimension() {
    return columnNames.length;
  }

  /**
   * Set a single element.
   * @param i Row index.
   * @param j Column index.
   * @param s A(i,j).
   * @exception ArrayIndexOutOfBoundsException
   */

  public void set(final int i, final int j, final double s) {

    this.dimension.setValue(this.rowNames[i], this.columnNames[j], s);
  }

  /**
   * Get a single element.
   * @param i Row index.
   * @param j Column index.
   * @return A(i,j)
   * @exception ArrayIndexOutOfBoundsException
   */

  public double get(final int i, final int j) {

    return this.dimension.getValue(this.rowNames[i], this.columnNames[j]);
  }

  //
  // Static methods
  //

  public static void log2Transform(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      for (int j = 0; j < matrix.getColumnDimension(); j++) {

        final double value = matrix.get(i, j);
        if (!Double.isNaN(value))
          if (value > 0)
            matrix.set(i, j, Math.log(value) / NATURAL_LOG_OF_2);
          else
            matrix.set(i, j, Double.NaN);

      }
  }

  public static void unlog2Transform(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      for (int j = 0; j < matrix.getColumnDimension(); j++) {

        final double value = matrix.get(i, j);
        if (!Double.isNaN(value))
          matrix.set(i, j, (Math.pow(2, value)));

      }

  }

  public static void normalizeSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      normalizeGene(matrix, i);

  }

  public static void divideSpotsRMS(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      divideGeneByRMS(matrix, i);

  }

  // pcahan -- affy-abs specific adjustments

  public static void divideGenesMedian(final DoubleMatrix matrix) {

    double median = 0;
    final int numSamples = matrix.getColumnDimension();
    final int numGenes = matrix.getRowDimension();

    List<Double> row = new ArrayList<Double>(numGenes);

    // foreach gene
    for (int gene = 0; gene < numGenes; gene++) {

      median = 0;
      // get median
      for (int sample = 0; sample < numSamples; sample++)
        row.add(sample, new Double(matrix.get(gene, sample)));

      median = getGeneMedian(row);

      // set value = signal/median
      for (int sample = 0; sample < numSamples; sample++) {

        final double value = matrix.get(gene, sample);
        matrix.set(gene, sample, value / median);
      }

      row.clear();
    }

  }

  public static void divideGenesMean(final DoubleMatrix matrix) {

    for (int g = 0; g < matrix.getRowDimension(); g++) {

      double mean = 0.0;
      final int n = matrix.getColumnDimension();
      int validN = 0;

      for (int i = 0; i < n; i++) {

        final double value = matrix.get(g, i);
        if (!Double.isNaN(value)) {

          mean += value;
          validN++;
        }
      }

      if (validN > 0)
        mean /= validN;

      for (int i = 0; i < n; i++) {

        final double value = matrix.get(g, i);
        if (!Double.isNaN(value))
          matrix.set(g, i, (value / mean));
      }

    }
  }

  public static void divideSpotsSD(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      divideGeneBySD(matrix, i);
  }

  public static void meanCenterSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      meanCenterGene(matrix, i);
  }

  public static void medianCenterSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      medianCenterGene(matrix, i);
  }

  public static void digitalSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      makeDigitalGene(matrix, i);
  }

  public static void normalizeExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnDimension(); i++)
      normalizeExperiment(matrix, i);
  }

  public static void divideExperimentsRMS(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnDimension(); i++)
      divideExperimentByRMS(matrix, i);
  }

  public static void divideExperimentsSD(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnDimension(); i++)
      divideExperimentBySD(matrix, i);
  }

  public static void meanCenterExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnDimension(); i++)
      meanCenterExperiment(matrix, i);
  }

  public static void medianCenterExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnDimension(); i++)
      medianCenterExperiment(matrix, i);
  }

  public static void digitalExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnDimension(); i++)
      makeDigitalExperiment(matrix, i);
  }

  public static void log10toLog2(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      for (int j = 0; j < matrix.getColumnDimension(); j++) {

        final double value = matrix.get(i, j);

        if (!Double.isNaN(value))
          matrix.set(i, j, value / LOG2_TO_LOG10);
      }

  }

  public static void log2toLog10(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowDimension(); i++)
      for (int j = 0; j < matrix.getColumnDimension(); j++) {

        final double value = matrix.get(i, j);
        if (!Double.isNaN(value))
          matrix.set(i, j, value * LOG2_TO_LOG10);
      }

  }

  private static void normalizeGene(final DoubleMatrix matrix,
      final int geneNumber) {

    double mean = 0.0;
    double standardDeviation = 0.0;

    final int n = matrix.getColumnDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value)) {
        mean += value;
        validN++;
      }

    }

    if (validN > 0)
      mean /= validN;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value))
        standardDeviation += Math.pow((value - mean), 2);
    }

    if (validN > 1)
      standardDeviation = Math.sqrt(standardDeviation / (validN - 1));
    else
      standardDeviation = 0.0d;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);
      if (!Double.isNaN(value)) {

        if (standardDeviation != 0)
          matrix.set(geneNumber, i, (value - mean) / standardDeviation);
        else
          matrix.set(geneNumber, i, (value - mean) / Double.MIN_VALUE);

      }
    }

  }

  private static void divideGeneByRMS(final DoubleMatrix matrix,
      final int geneNumber) {

    double rms = 0.0;
    final int n = matrix.getColumnDimension();

    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);
      if (!Double.isNaN(value)) {

        rms += Math.pow((value), 2);
        validN++;
      }
    }

    if (validN > 1)
      rms = Math.sqrt(rms / (validN - 1));
    else if (validN == 0)
      rms = Math.sqrt(rms);
    else
      rms = 0.0d;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value)) {

        if (rms != 0)
          matrix.set(geneNumber, i, value / rms);
        else
          matrix.set(geneNumber, i, value / Double.MIN_VALUE);
      }
    }

  }

  private static void divideGeneBySD(final DoubleMatrix matrix,
      final int geneNumber) {

    double mean = 0.0;
    double standardDeviation = 0.0;
    final int n = matrix.getColumnDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value)) {

        mean += value;
        validN++;
      }
    }

    if (validN > 0)
      mean /= validN;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);
      if (!Double.isNaN(value))
        standardDeviation += Math.pow((value - mean), 2);
    }

    if (validN > 1)
      standardDeviation = Math.sqrt(standardDeviation / (validN - 1));
    else
      standardDeviation = 0.0d;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value)) {

        if (standardDeviation != 0)
          matrix.set(geneNumber, i, value / standardDeviation);
        else
          matrix.set(geneNumber, i, value / Double.MIN_VALUE);

      }
    }

  }

  public static void meanCenterGene(final DoubleMatrix matrix,
      final int geneNumber) {

    double mean = 0.0;
    final int n = matrix.getColumnDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value)) {

        mean += value;
        validN++;
      }

    }

    if (validN > 0)
      mean /= validN;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);
      if (!Double.isNaN(value))
        matrix.set(geneNumber, i, value - mean);

    }

  }

  private static void medianCenterGene(final DoubleMatrix matrix,
      final int geneNumber) {

    final int n = matrix.getColumnDimension();
    int k = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);
      if (!Double.isNaN(value))
        k++;
    }

    double[] dummyArray = new double[k];

    k = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value)) {

        dummyArray[k] = value;
        k++;
      }
    }

    Arrays.sort(dummyArray);

    double median = 0.0;

    if (k % 2 == 0) {

      if (k > 0)
        median = (dummyArray[k / 2 - 1] + dummyArray[(k / 2)]) / 2;

    } else
      median = dummyArray[(k + 1) / 2 - 1];

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(geneNumber, i);

      if (!Double.isNaN(value))
        matrix.set(geneNumber, i, value - median);

    }

  }

  private static void makeDigitalGene(final DoubleMatrix matrix, final int gene) {

    final int n = matrix.getColumnDimension();
    final int numberOfBins = (int) Math.floor(Math.log(n) / Math.log(2));
    final int step = MAX_STEPS / numberOfBins;
    double minimum = Double.MAX_VALUE;
    double maximum = 0;

    for (int i = 0; i < n; i++)
      if (matrix.get(gene, i) < minimum)
        minimum = matrix.get(gene, i);

    for (int i = 0; i < n; i++)
      matrix.set(gene, i, matrix.get(gene, i) - minimum);

    for (int i = 0; i < n; i++)
      if (matrix.get(gene, i) > maximum)
        maximum = matrix.get(gene, i);

    for (int i = 0; i < n; i++)
      matrix.set(gene, i, matrix.get(gene, i) / maximum);

    for (int i = 0; i < n; i++)
      if (matrix.get(gene, i) == 1.0)
        matrix.set(gene, i, numberOfBins);
      else
        matrix.set(gene, i,
            Math.floor(matrix.get(gene, i) * MAX_STEPS / step) + 1);

  }

  private static void normalizeExperiment(final DoubleMatrix matrix,
      final int experimentNumber) {

    double mean = 0.0;
    double standardDeviation = 0.0;
    final int n = matrix.getRowDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value)) {

        mean += value;
        validN++;
      }
    }

    if (validN > 0)
      mean /= validN;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value))
        standardDeviation += Math.pow((value - mean), 2);
    }

    if (validN > 1)
      standardDeviation = Math.sqrt(standardDeviation / (validN - 1));
    else
      standardDeviation = 0.0d;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value))
        if (standardDeviation != 0)
          matrix.set(i, experimentNumber, (value - mean) / standardDeviation);
        else
          matrix.set(i, experimentNumber, (value - mean) / Double.MIN_VALUE);

    }

  }

  private static void divideExperimentBySD(final DoubleMatrix matrix,
      final int experimentNumber) {

    double mean = 0.0;
    double standardDeviation = 0.0;
    final int n = matrix.getRowDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value)) {

        mean += value;
        validN++;
      }
    }

    if (validN > 0)
      mean /= validN;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);
      if (!Double.isNaN(value))
        standardDeviation += Math.pow((value - mean), 2);
    }

    if (validN > 1)
      standardDeviation = Math.sqrt(standardDeviation / (validN - 1));
    else
      standardDeviation = 0.0d;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);
      if (!Double.isNaN(value))
        if (standardDeviation != 0)
          matrix.set(i, experimentNumber, (value) / standardDeviation);
        else
          matrix.set(i, experimentNumber, (value) / Double.MIN_VALUE);
    }

  }

  private static void divideExperimentByRMS(final DoubleMatrix matrix,
      final int experimentNumber) {

    double rms = 0.0;
    final int n = matrix.getRowDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value)) {

        rms += Math.pow(value, 2);
        validN++;
      }
    }

    if (validN > 0)
      rms = Math.sqrt(rms / validN);

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);
      if (!Double.isNaN(value))
        if (rms != 0)
          matrix.set(i, experimentNumber, value / rms);
        else
          matrix.set(i, experimentNumber, value / Double.MIN_VALUE);

    }

  }

  private static void meanCenterExperiment(final DoubleMatrix matrix,
      final int experimentNumber) {

    double mean = 0.0;
    final int n = matrix.getRowDimension();
    int validN = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value)) {

        mean += value;
        validN++;
      }
    }

    if (validN > 0)
      mean /= validN;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value))
        matrix.set(i, experimentNumber, value - mean);
    }

  }

  private static void medianCenterExperiment(final DoubleMatrix matrix,
      final int experimentNumber) {

    final int n = matrix.getRowDimension();
    int k = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value))
        k++;
    }

    double[] dummyArray = new double[k];

    k = 0;

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value)) {

        dummyArray[k] = value;
        k++;
      }

    }

    Arrays.sort(dummyArray);

    double median = 0.0;

    if (k % 2 == 0) {

      if (k > 0)
        median = (dummyArray[k / 2 - 1] + dummyArray[(k / 2)]) / 2;

    } else
      median = dummyArray[(k + 1) / 2 - 1];

    for (int i = 0; i < n; i++) {

      final double value = matrix.get(i, experimentNumber);

      if (!Double.isNaN(value))
        matrix.set(i, experimentNumber, value - median);

    }

  }

  private static void makeDigitalExperiment(final DoubleMatrix matrix,
      final int experiment) {

    final int n = matrix.getRowDimension();

    final int numberOfBins = (int) Math.floor(Math.log(n) / Math.log(2));

    final int step = MAX_STEPS / numberOfBins;

    double minimum = Double.MAX_VALUE;

    double maximum = 0;

    for (int i = 0; i < n; i++)
      if (matrix.get(i, experiment) < minimum)
        minimum = matrix.get(i, experiment);

    for (int i = 0; i < n; i++)
      matrix.set(i, experiment, matrix.get(i, experiment) - minimum);

    for (int i = 0; i < n; i++)
      if (matrix.get(i, experiment) > maximum)
        maximum = matrix.get(i, experiment);

    if (maximum != 0)
      for (int i = 0; i < n; i++)
        matrix.set(i, experiment, matrix.get(i, experiment) / maximum);

    for (int i = 0; i < n; i++)
      if (matrix.get(i, experiment) == 1.0)
        matrix.set(i, experiment, numberOfBins);
      else
        matrix.set(i, experiment, Math.floor(matrix.get(i, experiment)
            * MAX_STEPS / step) + 1);

  }

  // pcahan

  public static double getGeneMedian(final List<Double> doubleArray) {

    Collections.sort(doubleArray);

    Double median;

    if (doubleArray.size() == 1)
      return doubleArray.get(0);

    int center = doubleArray.size() / 2;

    if (doubleArray.size() % 2 == 0) {

      final double a, b;

      a = doubleArray.get(center);
      b = doubleArray.get(center - 1);
      median = new Double((a + b) / 2);
    } else
      median = doubleArray.get(center);

    return median.doubleValue();
  }

  //
  // Constructor
  //

  DoubleMatrix(final ExpressionMatrixDimension dimension) {

    if (dimension == null)
      throw new NullPointerException("The dimension is null");

    this.dimension = dimension;

    this.columnNames = dimension.getColumnNames();
    this.rowNames = dimension.getRowNames();
  }
}
