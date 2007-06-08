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

import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

final class StatMerger {

  // Get a DescriptiveStatistics instance using factory method
  private DescriptiveStatistics stats = DescriptiveStatistics.newInstance();

  private static final double MEDIAN_PERCENTILE = 50.0;

  private int n;
  private int totalN;
  private double stdDev;
  private double median;
  private double mean;

  //
  // Getters
  //

  public int getN() {

    return this.n;
  }

  public int getTotalN() {

    return this.totalN;
  }

  public double getStdDev() {

    return stdDev;
  }

  public double getMedian() {

    return this.median;
  }

  public double getMean() {

    return this.mean;
  }

  private int countNotNan(final List<Double> doubleValues) {

    if (doubleValues == null)
      return 0;

    int count = 0;
    for (double d : doubleValues)
      if (!Double.isNaN(d))
        count++;

    return count;
  }

  public void setValues(final List<Double> doubleValues) {

    if (doubleValues.size() == 1) {

      final double val = doubleValues.get(0);

      this.totalN = 1;

      this.n = Double.isNaN(val) ? 0 : 1;
      this.mean = val;
      this.median = val;

      return;
    }

    stats.clear();

    for (double d : doubleValues)
      if (!Double.isNaN(d))
        stats.addValue(d);

    this.totalN = doubleValues.size();
    this.n = (int) stats.getN();
    // this.n = countNotNan(doubleValues);
    this.mean = stats.getMean();
    this.median = stats.getPercentile(MEDIAN_PERCENTILE);
    this.stdDev = stats.getStandardDeviation();
  }

  StatMerger() {

  }

  StatMerger(final List<Double> doubleValues) {

    setValues(doubleValues);
  }

}
