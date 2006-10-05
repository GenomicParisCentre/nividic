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

import java.io.InputStream;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;

/**
 * JUnit test class for IDMAReader object
 * @author Lory Montout
 */
public class IDMAReaderTest extends TestCase {

  private class IDMAFileToTest {

    private String name;
    private int nbOptionalheaders;
    private int nbFields;
    private int totalNbLines;
  }

  public void testGoulphar() {
    IDMAFileToTest idma = new IDMAFileToTest();
    idma.name = "/files/testID_M_A.txt";
    idma.nbFields = 3;
    idma.nbOptionalheaders = 0;
    idma.totalNbLines = 13441;

    read(idma);
  }

  /**
   * read an ID-M-A file
   * @param idma the file to read
   * @return a bioAssay object
   */
  private BioAssay read(final IDMAFileToTest idma) {

    try {

      InputStream is = this.getClass().getResourceAsStream(idma.name);

      IDMAReader idmar = new IDMAReader(is);

      BioAssay b = idmar.read();

      String[] keys = b.getAnnotation().getPropertiesKeys();

      assertEquals(idma.nbOptionalheaders, b.getAnnotation()
          .getPropertiesKeys().length);

      String[] tab = b.getAnnotation().getPropertiesKeys();
      assertNotNull(tab);
      assertEquals(idma.nbOptionalheaders, tab.length);

      String[] ids = b.getIds();
      assertNotNull(ids);

      //failure if the last ID is not the correct one

      assertEquals("PSA2", ids[ids.length - 1]);

      double[] mnorms = b.getMs();
      
      assertEquals(-0.242, mnorms[955 - 2], 0);

      assertEquals(idma.totalNbLines - 1, b.getIds().length);
      //1 is the nb of anotation lines

      return b;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      assertTrue(false);
    }
    return null;
  }
}