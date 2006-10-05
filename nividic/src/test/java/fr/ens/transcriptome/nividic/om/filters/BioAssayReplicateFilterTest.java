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

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.translators.DescriptionBioAssayTranslator;

public class BioAssayReplicateFilterTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5", "id6", "id7", "id8",
      "id9", "id10", "id11", "id12"};

  String[] ids2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"};

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

  public void testFilter() {

    BioAssay b1 = createBioAssay();

    BioAssayFilter baf1 = new BioAssayReplicateFilter();

    BioAssay b2 = b1.filter(baf1);

    assertEquals(b1.size(), b2.size());

    String[] ids1 = {"id1", "id2", "id3", "id4", "id5", "id6", "id7", "id8",
        "id9", "id10", "id11", "id12"};

    ids1[3] = "id1";
    ids1[5] = "id1";
    b1.setIds(ids1);
    b2 = b1.filter(baf1);

    // BioAssayUtils.printBioAssay(b1);

    assertEquals(b1.size() - 2, b2.size());

    ids1[4] = "id1";
    ids1[6] = "id1";
    b1.setIds(ids1);
    b2 = b1.filter(baf1);

    assertEquals(b1.size() - 4, b2.size());

    ids1[2] = "id2";
    ids1[7] = "id2";
    b1.setIds(ids1);
    b2 = b1.filter(baf1);

    assertEquals(b1.size() - 6, b2.size());
  }

  public void testFilterTranslator() {

    BioAssay b1 = createBioAssay();

    DescriptionBioAssayTranslator t = new DescriptionBioAssayTranslator(b1);

    assertEquals("a", t.translateField("id1"));
    assertEquals("c", t.translateField("id3"));

    BioAssayFilter baf1 = new BioAssayReplicateFilter(t);

    BioAssay b2;
    b2 = b1.filter(baf1);

    assertEquals(b1.size(), b2.size());

    String[] ids2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"};

    ids2[3] = "a";
    ids2[5] = "a";
    b1.setDescriptions(ids2);
    t.clear();
    t.addBioAssay(b1);

    b2 = b1.filter(baf1);

    assertEquals(b1.size() - 2, b2.size());

    ids2[4] = "a";
    ids2[6] = "a";
    b1.setDescriptions(ids2);
    t.clear();
    t.addBioAssay(b1);

    b2 = b1.filter(baf1);

    assertEquals(b1.size() - 4, b2.size());

    ids2[2] = "b";
    ids2[7] = "b";
    b1.setDescriptions(ids2);
    t.clear();
    t.addBioAssay(b1);

    b2 = b1.filter(baf1);

    assertEquals(b1.size() - 6, b2.size());
  }

  public void testFilterMedian() {

    String[] ids1 = {"id1", "id2", "id3", "id4", "id5", "id4", "id4", "id4",
        "id4", "id10", "id11", "id11"};

    BioAssay b1 = createBioAssay();
    b1.setIds(ids1);

    BioAssayReplicateFilter baf1 = new BioAssayReplicateFilter();

    BioAssay b2 = b1.filter(baf1);

    double[] ms = b2.getMs();
    double[] as = b2.getAs();

    assertEquals(7, ms.length);
    assertEquals(7, as.length);

    assertEquals(1.1, ms[0], 0);
    assertEquals(10.1, as[0], 0);

    assertEquals(7.7, ms[3], 0);
    assertEquals(70.7, as[3], 0);

    assertEquals(11.62, ms[6], 0.1);
    assertEquals(115.12, as[6], 0.1);

    baf1.setMedianMode(true);

    b2 = b1.filter(baf1);

    ms = b2.getMs();
    as = b2.getAs();

    assertEquals(7, ms.length);
    assertEquals(7, as.length);

    assertEquals(1.1, ms[0], 0);
    assertEquals(10.1, as[0], 0);

    assertEquals(7.7, ms[3], 0);
    assertEquals(70.7, as[3], 0);

    assertEquals(11.62, ms[6], 0.1);
    assertEquals(115.12, as[6], 0.1);

  }

  public void testFilterMean() {
    String[] ids1 = {"id1", "id2", "id3", "id4", "id5", "id4", "id4", "id4",
        "id4", "id10", "id11", "id11"};

    BioAssay b1 = createBioAssay();
    b1.setIds(ids1);

    BioAssayReplicateFilter baf1 = new BioAssayReplicateFilter();

    baf1.setMedianMode(false);

    BioAssay b2 = b1.filter(baf1);

    double[] ms = b2.getMs();
    double[] as = b2.getAs();

    assertEquals(7, ms.length);
    assertEquals(7, as.length);

    assertEquals(1.1, ms[0], 0);
    assertEquals(10.1, as[0], 0);

    assertEquals(7.48, ms[3], 0.1);
    assertEquals(68.68, as[3], 0.1);

    assertEquals(11.62, ms[6], 0.1);
    assertEquals(115.12, as[6], 0.1);

    b2 = b1.filter(baf1);

    ms = b2.getMs();
    as = b2.getAs();

    assertEquals(7, ms.length);
    assertEquals(7, as.length);

    assertEquals(1.1, ms[0], 0);
    assertEquals(10.1, as[0], 0);

    assertEquals(7.48, ms[3], 0.1);
    assertEquals(68.68, as[3], 0.1);

    assertEquals(11.62, ms[6], 0.1);
    assertEquals(115.12, as[6], 0.1);

  }

  private void printMs(BioAssay ba) {

    String[] ids = ba.getIds();
    double[] ms = ba.getMs();
    double[] as = ba.getAs();

    for (int i = 0; i < as.length; i++) {
      System.out.println(ids[i] + "\t" + ms[i] + "\t" + as[i]);
    }
    System.out.println();

  }

}
