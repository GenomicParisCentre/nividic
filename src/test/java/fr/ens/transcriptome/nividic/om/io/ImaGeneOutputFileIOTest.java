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

package fr.ens.transcriptome.nividic.om.io;

import java.io.IOException;
import java.io.InputStream;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

import junit.framework.TestCase;

public class ImaGeneOutputFileIOTest extends TestCase {

  public void testReader() throws NividicIOException, IOException {

    InputStream is1 = this.getClass().getResourceAsStream(
        "/files/imagene_1_1.txt");
    InputStream is2 = this.getClass().getResourceAsStream(
        "/files/imagene_1_2.txt");

    ImaGeneOutputFileReader igofr = new ImaGeneOutputFileReader(is1, is2);

    BioAssay ba = igofr.read();

    int[] locs = ba.getLocations();
    String[] ids = ba.getIds();
    int[] reds = ba.getReds();
    int[] greens = ba.getGreens();

    int last = locs.length - 1;

    assertNotNull(locs);
    assertNotNull(ids);
    assertNotNull(reds);
    assertNotNull(greens);
    
    
    assertEquals(1, BioAssayUtils.getMetaRow(locs[10]));
    assertEquals(1, BioAssayUtils.getMetaColumn(locs[10]));
    assertEquals(1, BioAssayUtils.getRow(locs[10]));
    assertEquals(11, BioAssayUtils.getColumn(locs[10]));

    assertEquals(4, BioAssayUtils.getMetaRow(locs[last]));
    assertEquals(8, BioAssayUtils.getMetaColumn(locs[last]));
    assertEquals(20, BioAssayUtils.getRow(locs[last]));
    assertEquals(21, BioAssayUtils.getColumn(locs[last]));

    assertEquals(875, greens[0]);
    assertEquals(3314, greens[10]);
    assertEquals(0, reds[0]);
    assertEquals(2268, reds[10]);

    assertEquals(4, BioAssayUtils.getMetaRow(locs[last]));
    assertEquals(8, BioAssayUtils.getMetaColumn(locs[last]));
    assertEquals(20, BioAssayUtils.getRow(locs[last]));
    assertEquals(21, BioAssayUtils.getColumn(locs[last]));

    assertEquals("NO_DATA 5BOV1A8 NO_DATA", ids[0]);
    assertEquals("NO_DATA 5BOV11M23 NO_DATA", ids[10]);
    assertEquals("", ids[last]);

    // 1 1 1 11 NO_DATA 5BOV11M23 NO_DATA 0 3915.3076923076924 (10)

    // 4 8 20 21 (13483)

    /*
     * for (int i = 0; i < locs.length; i++) { int mr =
     * BioAssayUtils.getMetaRow(locs[i]); int mc =
     * BioAssayUtils.getMetaColumn(locs[i]); int r =
     * BioAssayUtils.getRow(locs[i]); int c = BioAssayUtils.getColumn(locs[i]);
     * System.out.println(mr + "\t" + mc + "\t" + r + "\t" + c + "\t[" + locs[i] +
     * "]" + "\t" + ids[i] + "\t" + greens[i] + "\t" + reds[i]); }
     */

    is1.close();
    is2.close();

  }

}

