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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.descriptive.rank.Median;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.SpotIterator;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This class implements a method to merge replicates.
 * @author Laurent Jourdren
 */
public class BioAssayReplicateFilter implements BioAssayFilter {

  private boolean medianMode = true;
  private boolean standardDeviationA;
  private boolean standardDeviationM;
  private Translator translator;
  private String translatorField;

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
  // Method from BioAssayFilter
  //

  /**
   * Filter a bioAssay object.
   * @param bioAssay BioAssay to filter
   * @return A new filtered bioAssay object
   * @throws BioAssayRuntimeException if an error occurs while filtering data
   */
  public BioAssay filter(final BioAssay bioAssay)
      throws BioAssayRuntimeException {

    if (bioAssay == null)
      return null;

    final Translator translator = getTranslator();
    final boolean translate = translator != null;
    String translatorField = getTranslatorField();

    double[] ms = bioAssay.getMs();
    double[] as = bioAssay.getAs();

    ArrayIntList idsToRemove = new ArrayIntList();

    AbstractUnivariateStatistic algo;
    StandardDeviation stdDevAlgo = new StandardDeviation();

    if (isMedianMode())
      algo = new Median();
    else
      algo = new Mean();

    final boolean mField = bioAssay.isMs();
    final boolean aField = bioAssay.isAs();

    Map mapIds = getMapIds(bioAssay);

    // Create the result bioAssay without replicates

    Iterator it = mapIds.keySet().iterator();

    while (it.hasNext()) {

      String id = (String) it.next();
      ArrayIntList ail = (ArrayIntList) mapIds.get(id);

      if (ail.size() > 1) {

        final int nValues = ail.size();

        for (int j = 1; j < nValues; j++)
          idsToRemove.add(ail.get(j));
      }
    }

    BioAssay result = BioAssayUtils.removeRowsFromBioAssay(bioAssay,
        idsToRemove.toArray());

    // Set the new values of m,a sd in the result bioAssay

    final int resultSize = result.size();
    final String[] resultIds = result.getIds();
    final double[] resultMs = result.getMs();
    final double[] resultAs = result.getAs();
    double[] resultStdDevAs = null;
    double[] resultStdDevMs = null;

    if (isStandardDeviationA()) {
      resultStdDevAs = new double[ms.length];
      bioAssay.setStdDevAs(resultStdDevAs);
    }

    if (isStandardDeviationM()) {
      resultStdDevMs = new double[ms.length];
      bioAssay.setStdDevMs(resultStdDevMs);
    }

    for (int i = 0; i < resultSize; i++) {

      final String id = translate ? translator.translateField(resultIds[i],
          translatorField) : resultIds[i];

      ArrayIntList ail = (ArrayIntList) mapIds.get(id);

      if (ail.size() > 1) {

        final int nValues = ail.size();

        final double[] mValues = mField ? new double[nValues] : null;
        final double[] aValues = aField ? new double[nValues] : null;

        for (int j = 0; j < nValues; j++) {
          final int pos = ail.get(j);

          if (mField)
            mValues[j] = ms[pos];
          if (aField)
            aValues[j] = as[pos];
        }

        if (mField)
          resultMs[i] = algo.evaluate(mValues);
        if (aField)
          resultAs[i] = algo.evaluate(aValues);

        if (isStandardDeviationA())
          resultStdDevAs[i] = stdDevAlgo.evaluate(aValues);

        if (isStandardDeviationM())
          resultStdDevMs[i] = stdDevAlgo.evaluate(mValues);

      }

    }

    return result;
  }

  /**
   * Count the number of spots that pass the filter.
   * @param bioAssay The bioAssay to filter
   * @return the number of spot that pass the filter
   */
  public int count(final BioAssay bioAssay) {

    BioAssay result = filter(bioAssay);

    return result == null ? 0 : result.size();
  }

  //
  // Other methods
  //

  private Map getMapIds(final BioAssay bioAssay) {

    if (bioAssay == null)
      return null;

    String[] ids = bioAssay.getIds();

    Map result = new HashMap();

    final Translator translator = getTranslator();
    final boolean translate = translator != null;
    String translatorField = getTranslatorField();

    for (int i = 0; i < ids.length; i++) {

      final String id = translate ? translator.translateField(ids[i],
          translatorField) : ids[i];

      ArrayIntList ail;
      if (!result.containsKey(id)) {

        ail = new ArrayIntList();
        result.put(id, ail);
      } else
        ail = (ArrayIntList) result.get(id);

      ail.add(i);

    }

    return result;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public BioAssayReplicateFilter() {
  }

  /**
   * Public constructor.
   * @param translator Translator to use
   */
  public BioAssayReplicateFilter(final Translator translator) {

    setTranslator(translator);
  }

  /**
   * Public constructor.
   * @param medianMode Set merge algorithm to use
   * @param standardDeviations set the standard Deviations
   */
  public BioAssayReplicateFilter(final boolean medianMode,
      final boolean standardDeviations) {

    this(null, medianMode, standardDeviations);
  }

  /**
   * Public constructor.
   * @param translator Translator to use
   * @param medianMode Set merge algorithm to use
   * @param standardDeviations set the standard Deviations
   */
  public BioAssayReplicateFilter(final Translator translator,
      final boolean medianMode, final boolean standardDeviations) {

    setTranslator(translator);
    setMedianMode(medianMode);
    if (standardDeviations) {
      setStandardDeviationA(true);
      setStandardDeviationM(true);
    }

  }

}
