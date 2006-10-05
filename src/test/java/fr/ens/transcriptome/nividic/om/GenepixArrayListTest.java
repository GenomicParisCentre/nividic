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

import java.io.InputStream;

import fr.ens.transcriptome.nividic.om.ArrayBlock;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.GenepixArrayList;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import junit.framework.TestCase;

/**
 * @author Laurent Jourdren
 */
public class GenepixArrayListTest extends TestCase {

  private static final String FILENAME = "/files/GAL_file_3.gal";
  private final GenepixArrayList gal = read();

  private GenepixArrayList read() {

    InputStream is = this.getClass().getResourceAsStream(FILENAME);

    try {
      GPRReader gprr = new GPRReader(is);
      BioAssay b = gprr.read();
      return new GenepixArrayList(b);
    } catch (NividicIOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  public void testGetBlockCount() {

    /*
     * String [] keys = gal.getBioAssay().getAnnotationKeys(); for (int i = 0; i <
     * keys.length; i++) { System.out.println(keys[i] + " " +
     * gal.getBioAssay().getAnnotation(keys[i])); }
     */

    assertEquals(gal.getBlockCount(), 48);

  }

  public void testGetBlock() {

    ArrayBlock ab = gal.getBlock(1);

    assertEquals(3000, ab.getXOrigin());
    assertEquals(16420, ab.getYOrigin());
    assertEquals(110, ab.getFeatureDiameter());
    assertEquals(18, ab.getXFeatures());
    assertEquals(240, ab.getXSpacing());
    assertEquals(18, ab.getYFeatures());
    assertEquals(240, ab.getYSpacing());

  }

  public void testGetBlocks() {

    ArrayBlock[] ab = gal.getBlocks();
    int x = 1;
    int y = 1;

    for (int i = 0; i < ab.length; i++) {
      //System.out.println(ab[i]);

      assertEquals(x, ab[i].getMetaColumn());
      assertEquals(y, ab[i].getMetaRow());

      x++;
      if (x == 5) {
        x = 1;
        y++;
      }

    }

  }

}