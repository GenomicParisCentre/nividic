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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

public class ImaGeneArrayListReaderTest extends TestCase {

  public void testReader() throws NividicIOException, IOException {

    InputStream is = this.getClass().getResourceAsStream("/files/imagenespots");

    ImaGeneArrayListReader igalr = new ImaGeneArrayListReader(is);

    BioAssay ba = igalr.read();
    assertNotNull(ba);

    int[] locs = ba.getLocations();
    assertNotNull(locs);

    String[] ids = ba.getIds();
    // assertNotNull(ids);

    assertEquals(1, BioAssayUtils.getMetaRow(locs[10]));
    assertEquals(1, BioAssayUtils.getMetaColumn(locs[10]));
    assertEquals(1, BioAssayUtils.getRow(locs[10]));
    assertEquals(11, BioAssayUtils.getColumn(locs[10]));

    int last = locs.length - 1;

    assertEquals(4, BioAssayUtils.getMetaRow(locs[last]));
    assertEquals(8, BioAssayUtils.getMetaColumn(locs[last]));
    assertEquals(20, BioAssayUtils.getRow(locs[last]));
    assertEquals(19, BioAssayUtils.getColumn(locs[last]));

    assertEquals("NO_DATA 5BOV1A8 NO_DATA", ids[0]);
    assertEquals("NO_DATA 5BOV11M23 NO_DATA", ids[10]);
    assertEquals("NO_DATA NO_DATA NO_DATA", ids[last]);

    /*for (int i = 0; i < locs.length; i++) {
      int mr = BioAssayUtils.getMetaRow(locs[i]);
      int mc = BioAssayUtils.getMetaColumn(locs[i]);
      int r = BioAssayUtils.getRow(locs[i]);
      int c = BioAssayUtils.getColumn(locs[i]);
      System.out.println(mr + "\t" + mc + "\t" + r + "\t" + c + "\t[" + locs[i]
          + "]" + "\t" + ids[i]);
    }*/

    is.close();
    
    ImaGeneArrayListWriter igalw = new ImaGeneArrayListWriter(new File("/home/jourdren/igal.txt"));
    igalw.write(ba);
    

  }

}
