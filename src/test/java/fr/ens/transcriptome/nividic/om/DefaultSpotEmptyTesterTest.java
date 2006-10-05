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

import junit.framework.TestCase;

public class DefaultSpotEmptyTesterTest extends TestCase {

  String[] ids1 = {"295078", "295096", "295132", "empty", "295276", "",
      "295080", "295370", null, "295228", "Spotting Buffer Corning", ""};

  String[] ids2 = {"YDR060W_01", "YMR256C_01", "YDR065W_01",
      "Spotting Buffer Corning", "empty", "", null, "YMR228W_01", "YLR102C_01",
      "YLR141W_01", "", "YJL052W_01"};

  int[] int1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
  int[] int2 = {11, 22, 33, 44, 55, 66, 77, 88, 99, 1010, 1111, 1212};
  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10,
      11.11, 12.12};
  double[] double2 = {10.1, 20.2, 30.3, 40.4, 50.5, 60.6, 70.7, 80.8, 90.9,
      100.10, 110.11, 120.12};

  int[] locRB = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
  int[] locCB = {1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2};
  int[] locR = {1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2};
  int[] locC = {1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3};

  private BioAssay createBioAssay() {

    BioAssay b = BioAssayFactory.createBioAssay();

    int[] locations = BioAssayUtils.encodeLocation(locRB, locCB, locR, locC);
    b.setLocations(locations);
    b.setIds(ids1);
    b.setDescriptions(ids2);
    b.setMs(double1);
    b.setAs(double2);

    return b;
  }

  public void testIsEmpty() {

    BioAssay ba = createBioAssay();

    int[] notEmpty = {0, 1, 2, 4, 6, 7, 9, 10};
    int[] empty = {3, 5, 8, 11};

    for (int i = 0; i < notEmpty.length; i++) {
      Spot spot = ba.getSpot(notEmpty[i]);
      assertFalse(spot.isEmpty());
    }

    for (int i = 0; i < empty.length; i++) {
      Spot spot = ba.getSpot(empty[i]);
      assertTrue(spot.isEmpty());
    }

    SpotEmptyTester spotEmptyTester = new DefaultSpotEmptyTester();
    ba.setSpotEmptyTester(spotEmptyTester);

    for (int i = 0; i < notEmpty.length; i++) {
      Spot spot = ba.getSpot(notEmpty[i]);
      assertFalse(spot.isEmpty());
    }

    for (int i = 0; i < empty.length; i++) {
      Spot spot = ba.getSpot(empty[i]);
      assertTrue(spot.isEmpty());
    }

  }

  public void testDefaultSpotEmptyTesterStringBoolean() {

    BioAssay ba = createBioAssay();
    SpotEmptyTester spotEmptyTester = new DefaultSpotEmptyTester(true);
    ba.setSpotEmptyTester(spotEmptyTester);

    int[] empty = {4, 5, 6, 10};
    int[] notEmpty = {0, 1, 2, 3, 7, 8, 9, 11};

    for (int i = 0; i < empty.length; i++) {
      Spot spot = ba.getSpot(empty[i]);
      assertTrue(spot.isEmpty());
    }

    for (int i = 0; i < notEmpty.length; i++) {
      Spot spot = ba.getSpot(notEmpty[i]);
      assertFalse(spot.isEmpty());
    }

  }

  public void testDefaultSpotEmptyTesterStringArray() {

    BioAssay ba = createBioAssay();
    SpotEmptyTester spotEmptyTester = new DefaultSpotEmptyTester(new String[] {
        "", "empty", "Spotting Buffer Corning",});
    ba.setSpotEmptyTester(spotEmptyTester);

    int[] empty = {3, 5, 8, 10, 11};
    int[] notEmpty = {0, 1, 2, 4, 6, 7, 9};

    for (int i = 0; i < empty.length; i++) {
      Spot spot = ba.getSpot(empty[i]);
      assertTrue(spot.isEmpty());
    }

    for (int i = 0; i < notEmpty.length; i++) {
      Spot spot = ba.getSpot(notEmpty[i]);
      assertFalse(spot.isEmpty());
    }
  }

}
