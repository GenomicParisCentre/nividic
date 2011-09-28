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

package fr.ens.transcriptome.nividic.om.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This interface defines how to merge multiple BioAssay Objects in a unique
 * BioAssay.
 * @author Laurent Jourdren
 */
public class BioAssayMerger {

  private Set<Integer> rowsToMerge = new HashSet<Integer>();
  private Map<Integer, List<Integer>> toMerge = new HashMap<Integer, List<Integer>>();
  private int sizeNewBioAssay;
  private BioAssay bioAssay;

  /**
   * Merge some rows of the bioAssay
   * @param rowsToMerge index of the roe to merge
   */
  public void mergeRows(final int[] rowsToMerge) {

    if (rowsToMerge == null)
      return;

    List<Integer> listToMerge = new ArrayList<Integer>();

    for (int i = 0; i < rowsToMerge.length; i++) {
      int row = rowsToMerge[i];

      if (this.rowsToMerge.contains(row))
        throw new NividicRuntimeException("Can't merged an already merged row");

      this.rowsToMerge.add(row);
      listToMerge.add(row);
    }

    if (listToMerge.size() > 1) {
      this.toMerge.put(listToMerge.get(0), listToMerge);
      this.sizeNewBioAssay -= listToMerge.size() - 1;
    }
  }

  /**
   * Merge rows with the same identifier of the bioAssay.
   * @param rowIds identifiers of rows to merge
   */
  public void mergeRowsWithIds(final String[] rowIds) {

    mergeRowsWithFieldValues(BioAssay.FIELD_NAME_ID, rowIds);
  }

  /**
   * Merge rows with the same description of the bioAssay.
   * @param rowDescriptions descriptions of rows to merge
   */
  public void mergeRowsWithDescriptions(final String[] rowDescriptions) {

    mergeRowsWithFieldValues(BioAssay.FIELD_NAME_DESCRIPTION, rowDescriptions);
  }

  /**
   * Merge rows of the bioAssay with the same field content.
   * @param field field to use
   * @param fieldValues descriptions of rows to merge
   */
  public void mergeRowsWithFieldValues(final String field,
      final String[] fieldValues) {

    if (fieldValues == null)
      return;

    if (!this.bioAssay.isField(field))
      return;

    String[] ids = this.bioAssay.getDataFieldString(field);
    final List<Integer> listToMerge = new ArrayList<Integer>();

    for (int i = 0; i < ids.length; i++) {
      for (int j = 0; j < fieldValues.length; j++) {

        if (fieldValues[j] != null && fieldValues[j].equals(ids[i])) {
          listToMerge.add(i);
          break;
        }
      }

      mergeRows(NividicUtils.toIntArray(listToMerge));
    }

  }

  /**
   * Merge all the rows which identifiers are the same.
   */
  public void mergeAllIds() {

    mergeAllIRowWitchFieldContentIsTheSame(BioAssay.FIELD_NAME_ID);
  }

  /**
   * Merge all the rows which descriptions are the same.
   */
  public void mergeAllDescription() {

    mergeAllIRowWitchFieldContentIsTheSame(BioAssay.FIELD_NAME_DESCRIPTION);
  }

  /**
   * Merge all the rows which field values are the same.
   * @param field field name
   */
  public void mergeAllIRowWitchFieldContentIsTheSame(final String field) {

    if (!this.bioAssay.isField(field))
      return;

    String[] ids = this.bioAssay.getDataFieldString(field);

    final Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

    for (int i = 0; i < ids.length; i++) {

      final String id = ids[i];
      final List<Integer> list;

      if (!map.containsKey(id)) {
        list = new ArrayList<Integer>();
        map.put(id, list);
      } else
        list = map.get(id);

      list.add(i);
    }

    /*
     * for (String id : map.keySet())
     * mergeRows(NividicUtils.toArray(map.get(id)));
     */

    for (Map.Entry<String, List<Integer>> e : map.entrySet())
      mergeRows(NividicUtils.toIntArray(e.getValue()));
  }

  /**
   * Get the merged bioAssay.
   * @return the merged bioAssay
   */
  public BioAssay getBioAssay() {

    final int n = this.sizeNewBioAssay;

    final String[] ids = this.bioAssay.getIds();
    final String[] descriptions = this.bioAssay.getDescriptions();
    final double[] ms = this.bioAssay.getMs();
    final double[] as = this.bioAssay.getAs();

    final String[] nIds = new String[n];
    final String[] nDescriptions = new String[n];
    final double[] nMs = new double[n];
    final double[] nAs = new double[n];
    final double[] nStdDev = new double[n];
    final double[] nTotalN = new double[n];
    final double[] nN = new double[n];

    int index = 0;
    final StatMerger stat = new StatMerger();

    for (int i = 0; i < n; i++) {

      if (!this.rowsToMerge.contains(i)) {

        //nIds[index] = ids != null ? null : ids[i];
        nIds[index] = ids == null ? null : ids[i];
        nDescriptions[index] = descriptions == null ? null : descriptions[i];
        nMs[index] = ms == null ? 0 : ms[i];
        nStdDev[index] = Double.isNaN(nMs[index]) ? Double.NaN : 0;
        nTotalN[index] = 1;
        nN[index] = Double.isNaN(nMs[index]) ? 0 : 1;

        nAs[index] = as == null ? 0 : as[i];

        index++;
      } else if (this.toMerge.containsKey(i)) {

        final List<Integer> list = this.toMerge.get(i);
        final List<Double> data = new ArrayList<Double>(list.size());

        //nIds[index] = ids != null ? null : ids[i];
        nIds[index] = ids == null ? null : ids[i];
        nDescriptions[index] = descriptions == null ? null : descriptions[i];

        if (ms != null) {

          for (int j : list)
            data.add(ms[j]);

          stat.setValues(data);

          nMs[index] = stat.getMedian();
          nStdDev[index] = stat.getStdDev();
          nTotalN[index] = stat.getTotalN();
          nN[index] = stat.getN();

        }

        if (as != null) {

          for (int j : list)
            data.add(as[j]);

          nAs[index] = stat.getMedian();
        }

        index++;
      }

    }

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.getAnnotation().addProperties(this.bioAssay.getAnnotation());
    ba.setName(this.bioAssay.getName() + " merged");
    ba.getHistory().add(this.bioAssay.getHistory());

    if (ids != null)
      ba.setIds(nIds);
    if (descriptions != null)
      ba.setDescriptions(nDescriptions);
    if (ms != null) {
      ba.setMs(nMs);
      ba.setDataFieldDouble("stdDevM", nStdDev);
      ba.setDataFieldDouble("n", nN);
      ba.setDataFieldDouble("totalN", nTotalN);
    }

    if (as != null)
      ba.setAs(nAs);

    return ba;
  }

  private void setBioAssay(final BioAssay ba) {

    this.bioAssay = ba;
    this.sizeNewBioAssay = ba.size();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param bioAssay BioAssay to merge
   */
  public BioAssayMerger(final BioAssay bioAssay) {

    if (bioAssay == null)
      throw new NullPointerException("BioAssay is null");
    setBioAssay(bioAssay);
  }

}
