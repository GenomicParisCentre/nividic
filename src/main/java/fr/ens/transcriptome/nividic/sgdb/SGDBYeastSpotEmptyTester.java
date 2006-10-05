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

package fr.ens.transcriptome.nividic.sgdb;

import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.Spot;
import fr.ens.transcriptome.nividic.om.SpotEmptyTester;

/**
 * This class defines empty spot for bioAssay annotated by the SGDB. A spot is
 * empty if id is equals to null or to "" and if description is null or is in a
 * list in this object.
 * @author Laurent Jourdren
 */
public class SGDBYeastSpotEmptyTester implements SpotEmptyTester {

  /** Idenfiers of the empty spots. */
  String[] EMPTY_SPOTS_IDENTIFIERS = {"", "empty",
      "Corning Pronto Universal Spotting Solution"};

  private String[] emptySpotsIds;

  /**
   * Test if the spot is labeled as empty.
   * @param spot Spot to test
   * @return true if the spot is labeled as empty
   */
  public boolean isEmpty(final Spot spot) {

    if (spot == null)
      return false;

    String id = spot.getId();

    if (id == null)
      return true;

    id = id.trim();

    if ("".equals(id))
      return true;

    final String description = spot.getDescription();

    if (description == null)
      return true;

    final String[] values = description.split("\\|");

    if (values == null || values.length < 2)
      return true;

    final String orf = values[1];

    return BioAssayUtils.isIdEquals(orf, emptySpotsIds);
  }

  /**
   * Overring the toString() method of the Object class.
   * @return information about the tester
   */
  public String toString() {

    StringBuffer sb = new StringBuffer();
    sb.append("DefaultSpotEmptyTester, ");

    if (emptySpotsIds == null)
      sb.append(" no empty identifiers.");
    else {
      sb.append("empty identifiers: ");

      for (int i = 0; i < emptySpotsIds.length; i++) {
        if (i != 0)
          sb.append(",");
        sb.append(emptySpotsIds[i]);
      }
      sb.append(".");

      sb.append(" (field: description)");

    }

    return sb.toString();
  }

  //
  // Constructors
  //

  /**
   * Default contructor.
   * @param emptySpotsIds An array of "empty" identifiers
   * @param descriptionToTest true if the value to test is the description
   */
  public SGDBYeastSpotEmptyTester(final String[] emptySpotsIds) {

    this.emptySpotsIds = emptySpotsIds;

  }

  /**
   * Default contructor. Use as empty identify, the default value in BioAssay
   * interface.
   */
  public SGDBYeastSpotEmptyTester() {
    this.emptySpotsIds = EMPTY_SPOTS_IDENTIFIERS;
  }

}
