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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.Spot;
import fr.ens.transcriptome.nividic.om.SpotEmptyTester;
import fr.ens.transcriptome.nividic.om.SpotIterator;
import junit.framework.TestCase;

public class SGDBYeastSpotEmptyTesterTest extends TestCase {

  String[] ids1 = {"295078", "295096", "295132", "295186", "295276", "295330",
      "295080", "295370", null, "295228", "", ""};

  String[] ids2 = {
      "YDR060W_01 |YDR060W|MAK21|essential for 60s ribosome biogenesis; involved in nuclear export of pre-ribosomes",
      "YMR256C_01 |YMR256C|COX7|cytochrome c oxidase subunit VII",
      "YDR065W_01 |YDR065W||Hypothetical ORF",
      "Spotting Buffer Corning|Corning Pronto Universal Spotting Solution|C2",
      "Spotting Buffer Corning|Corning Pronto Universal Spotting Solution|C2",
      "",
      null,
      "YMR228W_01 |YMR228W|MTF1|mitochondrial RNA polymerase specificity factor",
      "YLR102C_01 |YLR102C|APC9|anaphase promoting complex (APC) subunit",
      "YLR141W_01", "",
      "YJL052W_01 |YJL052W|TDH1|glyceraldehyde-3-phosphate dehydrogenase 1|C1"};

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
    SpotEmptyTester spotEmptyTester = new SGDBYeastSpotEmptyTester();
    ba.setSpotEmptyTester(spotEmptyTester);

    int[] trues = {0, 1, 2, 7};
    int[] falses = {3, 4, 5, 6, 8, 9, 10, 11};

    for (int i = 0; i < trues.length; i++) {
      Spot spot = ba.getSpot(trues[i]);
      assertFalse(spot.isEmpty());
    }

    for (int i = 0; i < falses.length; i++) {
      Spot spot = ba.getSpot(falses[i]);
      assertTrue(spot.isEmpty());
    }

  }

}
