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

/**
 * This class define the default spot empty tester. if the identifier of a spot
 * is equals to one of the values in the empty identifier list, the spot is
 * declared empty.
 * @author Laurent Jourdren
 */
public class DefaultSpotEmptyTester implements SpotEmptyTester {

  private String[] emptySpotsIds;
  private boolean descriptionToTest;

  /**
   * Test if the spot is labeled as empty.
   * @param spot Spot to test
   * @return true if the spot is labeled as empty
   */
  public boolean isEmpty(final Spot spot) {

    if (spot == null)
      return false;

    final String id;

    if (descriptionToTest)
      id = spot.getDescription();
    else
      id = spot.getId();

    if (id == null)
      return true;

    return BioAssayUtils.isIdEquals(id, emptySpotsIds);
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
      if (this.descriptionToTest)
        sb.append(" (field: description)");
      else
        sb.append(" (field: identifier)");
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
  public DefaultSpotEmptyTester(final String[] emptySpotsIds,
      final boolean descriptionToTest) {

    this.emptySpotsIds = emptySpotsIds;
    this.descriptionToTest = descriptionToTest;
  }

  /**
   * Default contructor.
   * @param emptySpotsIds An array of "empty" identifiers
   */
  public DefaultSpotEmptyTester(final String[] emptySpotsIds) {
    this(emptySpotsIds, false);
  }

  /**
   * Default contructor. Use as empty identify, the default value in BioAssay
   * interface.
   * @param descriptionToTest true if the value to test is the description
   */
  public DefaultSpotEmptyTester(final boolean descriptionToTest) {

    this();
    this.descriptionToTest = descriptionToTest;
  }

  /**
   * Default contructor. Use as empty identify, the default value in BioAssay
   * interface.
   */
  public DefaultSpotEmptyTester() {
    this.emptySpotsIds = BioAssay.EMPTY_SPOTS_IDENTIFIERS;
  }

}