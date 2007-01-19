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

import java.io.InputStream;

import junit.framework.TestCase;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

public class BioAssaySorterTest extends TestCase {

  public void testSorter() throws NividicIOException {

    InputStream is1 = this.getClass().getResourceAsStream(
        "/files/normalised_chip1.txt");
    IDMAReader idmar = new IDMAReader(is1);
    BioAssay norm1 = idmar.read();

    // printBioAssay(norm1);

    String[] ids = norm1.getIds();
    String[] descriptions = norm1.getDescriptions();
    double[] ms = norm1.getMs();
    double[] as = norm1.getAs();

    int n = norm1.size() - 1;

    assertEquals("295150", ids[4]);
    assertEquals(
        "YDR025W_01 |YDR025W|RPS11A|ribosomal protein S11A (S18A) (rp41A) (YS12)",
        descriptions[4]);
    assertEquals(-0.252, ms[4], 0);
    assertEquals(5.994, as[4], 0);

    BioAssay norm2 = norm1.sorter(new BioAssayMASorterComparator(false));

    ids = norm2.getIds();
    descriptions = norm2.getDescriptions();
    ms = norm2.getMs();
    as = norm2.getAs();

    assertEquals("310299", ids[0]);
    assertEquals("YCONTROL19 |||Randomly_generated_negative_control|C2",
        descriptions[0]);
    assertEquals(-2.232, ms[0], 0);
    assertEquals(6.959, as[0], 0);

    assertEquals("298368", ids[n]);
    assertEquals("YDL179W_01 |YDL179W|PCL9|PHO85 cyclin", descriptions[n]);
    assertTrue(Double.isNaN(ms[n]));
    assertEquals(10.465, as[n], 0);

    norm2 = norm1.sorter(new BioAssayMASorterComparator(true));

    ids = norm2.getIds();
    descriptions = norm2.getDescriptions();
    ms = norm2.getMs();
    as = norm2.getAs();

    assertEquals("298368", ids[0]);
    assertEquals("YDL179W_01 |YDL179W|PCL9|PHO85 cyclin", descriptions[0]);
    assertTrue(Double.isNaN(ms[0]));
    assertEquals(10.465, as[0], 0);
    
    assertEquals("310299", ids[n]);
    assertEquals("YCONTROL19 |||Randomly_generated_negative_control|C2",
        descriptions[n]);
    assertEquals(-2.232, ms[n], 0);
    assertEquals(6.959, as[n], 0);

    // printBioAssay(norm2);
  }

  private static void printBioAssay(final BioAssay ba) {

    String[] ids = ba.getIds();
    double[] ms = ba.getMs();
    double[] as = ba.getAs();

    System.out.println();

    for (int i = 0; i < ids.length; i++) {
      System.out.println(i + "\t" + ids[i] + "\t" + ms[i] + "\t" + as[i]);
    }

  }

}
