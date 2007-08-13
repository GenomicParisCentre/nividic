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

/**
 * Math methods for DoubleMatrix.
 * @author Laurent Jourdren
 */
public final class DoubleMatrixUtils {

  // private static final double NATURAL_LOG_OF_2 = 0.69314718;
  private static final double NATURAL_LOG_OF_2 = 0.6931471805595;
  // private static final double LOG2_TO_LOG10 = 0.301029995;
  private static final double LOG2_TO_LOG10 = 0.30102999566398;
  private static final int MAX_STEPS = 1000000;

  /**
   * Convert a doubleMatrix to a 2D float array.
   * @param doubleMatrix matrix to convert
   * @return an array of array of float
   */
  public static float[][] toArrayFloat(final DoubleMatrix doubleMatrix) {

    if (doubleMatrix == null)
      return null;

    final int rowCount = doubleMatrix.getRowCount();
    final int columnCount = doubleMatrix.getColumnCount();

    final float[][] result = new float[doubleMatrix.getRowCount()][];

    for (int i = 0; i < rowCount; i++) {
      result[i] = new float[columnCount];
      for (int j = 0; j < result.length; j++)
        result[i][j] = (float) doubleMatrix.get(i, j);
    }

    return result;
  }

  /**
   * Set values of a 2D array of double to a DoubleMatrix
   * @param doubleMatrix Matrix to update
   * @param doubleValues values to set
   */
  public static void set(final DoubleMatrix doubleMatrix,
      final double[][] doubleValues) {

    if (doubleMatrix == null || doubleValues == null)
      return;

    final int rowCount = doubleMatrix.getRowCount();
    final int columnCount = doubleMatrix.getColumnCount();

    final int iMax = Math.min(rowCount, doubleValues.length);

    for (int i = 0; i < iMax; i++) {

      final double[] row = doubleValues[i];

      final int jMax = Math.min(columnCount, row.length);
      for (int j = 0; j < jMax; j++)
        doubleMatrix.set(i, j, doubleValues[i][j]);
    }
  }

  /**
   * Set values of a 2D array of float to a DoubleMatrix
   * @param doubleMatrix Matrix to update
   * @param floatValues values to set
   */
  public static void set(final DoubleMatrix doubleMatrix,
      final float[][] floatValues) {

    if (doubleMatrix == null || floatValues == null)
      return;

    final int rowCount = doubleMatrix.getRowCount();
    final int columnCount = doubleMatrix.getColumnCount();

    final int iMax = Math.min(rowCount, floatValues.length);

    for (int i = 0; i < iMax; i++) {

      final float[] row = floatValues[i];

      final int jMax = Math.min(columnCount, row.length);
      for (int j = 0; j < jMax; j++)
        doubleMatrix.set(i, j, floatValues[i][j]);
    }
  }

  public static void log2Transform(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      for (int j = 0; j < matrix.getColumnCount(); j++) {

        final double value = matrix.get(i, j);
        if (!Double.isNaN(value))
          if (value > 0)
            matrix.set(i, j, Math.log(value) / NATURAL_LOG_OF_2);
          else
            matrix.set(i, j, Double.NaN);

      }
  }

  public static void unlog2Transform(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      for (int j = 0; j < matrix.getColumnCount(); j++) {

        final double value = matrix.get(i, j);
        if (!Double.isNaN(value))
          matrix.set(i, j, (Math.pow(2, value)));

      }

  }

  public static void normalizeSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      normalizeGene(matrix, i);

  }

  public static void divideSpotsRMS(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      divideGeneByRMS(matrix, i);

  }

  public static void divideGenesMedian(final DoubleMatrix matrix) {

    double median = 0;
    final int numSamples = matrix.getColumnCount();
    final int numGenes = matrix.getRowCount();

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

    for (int g = 0; g < matrix.getRowCount(); g++) {

      double mean = 0.0;
      final int n = matrix.getColumnCount();
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

    for (int i = 0; i < matrix.getRowCount(); i++)
      divideGeneBySD(matrix, i);
  }

  public static void meanCenterSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      meanCenterGene(matrix, i);
  }

  public static void medianCenterSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      medianCenterGene(matrix, i);
  }

  public static void digitalSpots(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      makeDigitalGene(matrix, i);
  }

  public static void normalizeExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnCount(); i++)
      normalizeExperiment(matrix, i);
  }

  public static void divideExperimentsRMS(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnCount(); i++)
      divideExperimentByRMS(matrix, i);
  }

  public static void divideExperimentsSD(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnCount(); i++)
      divideExperimentBySD(matrix, i);
  }

  public static void meanCenterExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnCount(); i++)
      meanCenterExperiment(matrix, i);
  }

  public static void medianCenterExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnCount(); i++)
      medianCenterExperiment(matrix, i);
  }

  public static void digitalExperiments(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getColumnCount(); i++)
      makeDigitalExperiment(matrix, i);
  }

  public static void log10toLog2(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      for (int j = 0; j < matrix.getColumnCount(); j++) {

        final double value = matrix.get(i, j);

        if (!Double.isNaN(value))
          matrix.set(i, j, value / LOG2_TO_LOG10);
      }

  }

  public static void log2toLog10(final DoubleMatrix matrix) {

    for (int i = 0; i < matrix.getRowCount(); i++)
      for (int j = 0; j < matrix.getColumnCount(); j++) {

        final double value = matrix.get(i, j);
        if (!Double.isNaN(value))
          matrix.set(i, j, value * LOG2_TO_LOG10);
      }

  }

  static void normalizeGene(final DoubleMatrix matrix, final int geneNumber) {

    double mean = 0.0;
    double standardDeviation = 0.0;

    final int n = matrix.getColumnCount();
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
    final int n = matrix.getColumnCount();

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
    final int n = matrix.getColumnCount();
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
    final int n = matrix.getColumnCount();
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

  static void medianCenterGene(final DoubleMatrix matrix, final int geneNumber) {

    final int n = matrix.getColumnCount();
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

  static void makeDigitalGene(final DoubleMatrix matrix, final int gene) {

    final int n = matrix.getColumnCount();
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

  static void normalizeExperiment(final DoubleMatrix matrix,
      final int experimentNumber) {

    double mean = 0.0;
    double standardDeviation = 0.0;
    final int n = matrix.getRowCount();
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
    final int n = matrix.getRowCount();
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

  static void divideExperimentByRMS(final DoubleMatrix matrix,
      final int experimentNumber) {

    double rms = 0.0;
    final int n = matrix.getRowCount();
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

  static void meanCenterExperiment(final DoubleMatrix matrix,
      final int experimentNumber) {

    double mean = 0.0;
    final int n = matrix.getRowCount();
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

  static void medianCenterExperiment(final DoubleMatrix matrix,
      final int experimentNumber) {

    final int n = matrix.getRowCount();
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

  static void makeDigitalExperiment(final DoubleMatrix matrix,
      final int experiment) {

    final int n = matrix.getRowCount();

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

  private DoubleMatrixUtils() {
  }

}
