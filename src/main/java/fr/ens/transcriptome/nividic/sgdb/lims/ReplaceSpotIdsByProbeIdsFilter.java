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

package fr.ens.transcriptome.nividic.sgdb.lims;

import org.apache.commons.collections.primitives.ArrayIntList;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;

/**
 * This class implements a filter to rename spotId indentifiers by probeId from
 * the ENS microarray lims.
 * @author Laurent Jourdren
 */
public class ReplaceSpotIdsByProbeIdsFilter implements BioAssayFilter {

  /** New name for unknown spotId. */
  public static final String NEW_IDENTIFIER_UNKNOWN_SPOTID = "unknown";

  private SpotProbeTranslator annot;
  private boolean removeUnknownSpotId;
  private boolean renameUnknownSpotId;
  private String newNameUnknownSpotId = NEW_IDENTIFIER_UNKNOWN_SPOTID;

  //
  // Getters
  //

  /**
   * Get the new name of unknown spotIds.
   * @return Returns the newNameUnknownSpotId
   */
  public String getNewNameUnknownSpotId() {
    return newNameUnknownSpotId;
  }

  /**
   * Test if a spot must be remove if its spotId is unknown.
   * @return Returns the removeUnknownSpotId
   */
  public boolean isRemoveUnknownSpotId() {
    return removeUnknownSpotId;
  }

  /**
   * Teft if a spot must be renamed if its spotId is unknown.
   * @return Returns the renameUnknownSpotId
   */
  public boolean isRenameUnknownSpotId() {
    return renameUnknownSpotId;
  }

  //
  // Setters
  //

  /**
   * Set the new name of the unknown spotId.
   * @param newNameUnknownSpotId The newNameUnknownSpotId to set
   */
  public void setNewNameUnknownSpotId(final String newNameUnknownSpotId) {

    if (newNameUnknownSpotId != null)
      this.newNameUnknownSpotId = newNameUnknownSpotId;
  }

  /**
   * Set if unknown spotId must be removed.
   * @param removeUnknownSpotId The removeUnknownSpotId to set
   */
  public void setRemoveUnknownSpotId(final boolean removeUnknownSpotId) {
    this.removeUnknownSpotId = removeUnknownSpotId;
  }

  /**
   * Set if an unknown spotId must be renamed.
   * @param renameUnknownSpotId The renameUnknownSpotId to set
   */
  public void setRenameUnknownSpotId(final boolean renameUnknownSpotId) {
    this.renameUnknownSpotId = renameUnknownSpotId;
  }

  //
  // Interface method
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

    String[] ids = bioAssay.getIds();

    String[] newIds = this.annot.getAnnotations(ids,
        SpotProbeTranslator.PROBE_ID_FIELD);

    System.out.println("newids: " + newIds);
    if (newIds == null)
      return bioAssay;

    ArrayIntList toRemove;

    if (this.removeUnknownSpotId)
      toRemove = new ArrayIntList();
    else
      toRemove = null;

    for (int i = 0; i < newIds.length; i++) {
      if (newIds[i] == null) {

        if (this.removeUnknownSpotId)
          toRemove.add(i);
        else if (this.renameUnknownSpotId)
          newIds[i] = this.newNameUnknownSpotId;
      }
    }

    bioAssay.setIds(newIds);

    if (this.removeUnknownSpotId)
      return BioAssayUtils.removeRowsFromBioAssay(bioAssay, toRemove.toArray());

    return bioAssay;
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
  // Constructors
  //

  /**
   * Public constructor.
   * @param connection The connection to the lims.
   */
  public ReplaceSpotIdsByProbeIdsFilter(final LimsConnection connection) {

    this.annot = new SpotProbeTranslator(connection);
  }

  /**
   * Public constructor
   */
  public ReplaceSpotIdsByProbeIdsFilter() {

    this(null);
  }

}
