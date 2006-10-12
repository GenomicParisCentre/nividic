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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.descriptive.rank.Median;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.Spot;
import fr.ens.transcriptome.nividic.om.SpotIterator;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This class define a class to merge 2 or more bioAssay into one BioAssay.
 * @author Laurent Jourdren
 */
public class BioAssayReplicateMerger implements BioAssayMerger {

  private boolean medianMode = true;
  private boolean standardDeviationA;
  private boolean standardDeviationM;
  private Translator translator;
  private String translatorField;

  private SpotFilter spotFilter = new SpotFilter() {

    public boolean filter(final Spot spot) {

      if (!spot.isFlag())
        return true;

      return spot.getFlag() >= 0;
    }
  };

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

  /**
   * Test if information about standard deviation for A field must be added.
   * @return true if information about standard deviation for A field must be
   *         added
   */
  public boolean isStandardDeviationA() {

    return standardDeviationA;
  }

  /**
   * Test if information about standard deviation for M field must be added.
   * @return true if information about standard deviation for M field must be
   *         added
   */
  public boolean isStandardDeviationM() {

    return standardDeviationM;
  }

  /**
   * Get the translator.
   * @return The translator of the filter
   */
  public Translator getTranslator() {

    return this.translator;
  }

  /**
   * Get the field of the translator to use during the translation.
   * @return a translator field
   */
  public String getTranslatorField() {

    return this.translatorField;
  }

  //
  // Setters
  //

  /**
   * Set if information about standard deviation for A field must be added.
   * @param standardDeviationA enable/disable value
   */
  public void setStandardDeviationA(final boolean standardDeviationA) {

    this.standardDeviationA = standardDeviationA;
  }

  /**
   * Set if information about standard deviation for M field must be added.
   * @param standardDeviationM enable/disable value
   */
  public void setStandardDeviationM(final boolean standardDeviationM) {

    this.standardDeviationM = standardDeviationM;
  }

  /**
   * Set if information about standard deviation for A and M field must be
   * added.
   * @param standardDeviations enable/disable value
   */
  public void setStandardDeviations(final boolean standardDeviations) {

    setStandardDeviationA(standardDeviations);
    setStandardDeviationM(standardDeviations);
  }

  /**
   * Set enable or not the median mode.
   * @param medianMode The median mode
   */
  public void setMedianMode(final boolean medianMode) {
    this.medianMode = medianMode;
  }

  /**
   * Set the translator of the filter.
   * @param translator The transloator to set
   */
  public void setTranslator(final Translator translator) {

    this.translator = translator;
  }

  /**
   * Set the field of the translator to use during the translation.
   * @param translatorField the translator field. If null is set, the default
   *          translator field will be used
   */
  public void setTranslatorField(final String translatorField) {

    this.translatorField = translatorField;
  }

  //
  // Method from BioAssayMerger
  //

  /**
   * Get the spot filter.
   * @return The spot filter
   */
  public SpotFilter getSpotFilter() {

    return this.spotFilter;
  }

  /**
   * Set the spot filter of the merger
   * @param spotFilter the spot filter to apply
   */
  public void setSpotFilter(final SpotFilter spotFilter) {

    this.spotFilter = spotFilter;
  }

  private void testLayoutLayout(final BioAssay[] bioAssays) {

    int size = -1;
    String[] ids = null;
    String[] descriptions = null;
    int[] locations = null;

    for (int i = 0; i < bioAssays.length; i++) {

      final BioAssay ba = bioAssays[i];

      if (ba == null)
        throw new BioAssayRuntimeException("One of the BioAssay is null");

      if (!ba.isMs())
        throw new BioAssayRuntimeException(
            "Nothing to merge, there is no M column");

      if (i == 0) {
        ids = ba.getIds();
        size = ba.size();
        descriptions = ba.getDescriptions();
        locations = ba.getLocations();
      } else if (size != ba.size() || !Arrays.equals(ids, ba.getIds())
          || !Arrays.equals(descriptions, ba.getDescriptions())
          || !Arrays.equals(locations, ba.getLocations()))
        throw new BioAssayRuntimeException(
            "Layout in the bioAssay is not the same");

    }

  }

  /**
   * Filter bioAssays.
   * @param bioAssays BioAssay to filter
   * @return a bioAssay filtered
   * @throws if one of the bioAssays is null
   */
  public BioAssay filter(final BioAssay[] bioAssays)
      throws BioAssayRuntimeException {

    if (bioAssays == null || bioAssays.length == 0)
      throw new BioAssayRuntimeException("No BioAssay to filter");

    testLayoutLayout(bioAssays);

    final int nba = bioAssays.length;
    SpotIterator si[] = new SpotIterator[nba];

    final SpotFilter spotFilter = getSpotFilter();

    final boolean isA = bioAssays[0].isAs();
    final boolean isDescription = bioAssays[0].isDescriptions();

    final Map mapIds = new HashMap();
    final Map mapDescs = isDescription ? new HashMap() : null;
    final Map mapValuesM = new HashMap();
    final Map mapValuesA = isA ? new HashMap() : null;

    final Translator translator = getTranslator();
    final boolean translate = translator != null;
    String translatorField = getTranslatorField();

    for (int i = 0; i < nba; i++)
      si[i] = bioAssays[i].iterator();

    int count = 0;
    while (si[0].hasNext()) {

      String id = null;

      for (int i = 0; i < nba; i++) {
        si[i].next();

        if (i == 0) {
          id = translate ? translator.translateField(si[0].getId(),
              translatorField) : si[0].getId();

          if (!mapIds.containsKey(id))
            mapIds.put(id, si[0].getId());
          if (isDescription && !mapDescs.containsKey(id))
            mapDescs.put(id, si[0].getDescription());
        }

        if (spotFilter == null || spotFilter.filter(si[i])) {

          final ArrayDoubleList ms;
          final ArrayDoubleList as;

          if (mapValuesM.containsKey(id))
            ms = (ArrayDoubleList) mapValuesM.get(id);
          else {
            ms = new ArrayDoubleList();
            mapValuesM.put(id, ms);
          }

          final double valueM = si[i].getM();
          // if (!Double.isNaN(valueM) && !Double.isInfinite(valueM))
          ms.add(valueM);

          if (isA) {

            if (mapValuesA.containsKey(id))
              as = (ArrayDoubleList) mapValuesA.get(id);
            else {
              as = new ArrayDoubleList();
              mapValuesA.put(id, as);
            }

            final double valueA = si[i].getA();
            // if (!Double.isNaN(valueA) && !Double.isInfinite(valueA))
            as.add(valueA);

          }

        }
      }
    }

    // Set the algorithm to use
    final AbstractUnivariateStatistic algo;

    if (isMedianMode())
      algo = new Median();
    else
      algo = new Mean();

    final StandardDeviation stdDevAlgo = new StandardDeviation();

    // Create array to put in the BioAssay result
    final int size = mapIds.size();
    String[] ids = new String[size];
    String[] descs = isDescription ? new String[size] : null;
    double[] ms = new double[size];
    double[] as = isA ? new double[size] : null;
    double[] stdDevMs = isStandardDeviationM() ? new double[size] : null;
    double[] stdDevAs = isStandardDeviationA() ? new double[size] : null;

    Iterator it = mapIds.keySet().iterator();

    count = 0;
    while (it.hasNext()) {

      Object key = it.next();

      ids[count] = (String) mapIds.get(key);
      if (isDescription)
        descs[count] = (String) mapDescs.get(key);

      double[] valueMs = ((ArrayDoubleList) mapValuesM.get(key)).toArray();

      ms[count] = algo.evaluate(valueMs);

      if (stdDevMs != null)
        stdDevMs[count] = stdDevAlgo.evaluate(valueMs);

      if (isA) {

        double[] valueAs = ((ArrayDoubleList) mapValuesA.get(key)).toArray();

        as[count] = algo.evaluate(valueAs);

        if (stdDevAs != null)
          stdDevAs[count] = stdDevAlgo.evaluate(valueAs);
      }

      count++;
    }

    // Create the final result
    final BioAssay result = BioAssayFactory.createBioAssay();

    result.setIds(ids);
    result.setMs(ms);
    if (isDescription)
      result.setDescriptions(descs);
    if (stdDevMs != null)
      result.setStdDevMs(stdDevMs);
    if (isA)
      result.setAs(as);
    if (stdDevAs != null)
      result.setStdDevMs(stdDevAs);

    return result;
  }

  //
  // Constructors
  //

  public BioAssayReplicateMerger() {

  }

}
