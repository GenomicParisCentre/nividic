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

/**
 * @author Laurent Jourdren
 */
public class GPRReaderTest extends TestCase {

  private class ATFFileToTest {

    String name;
    String version;
    int nbOptionalheaders;
    int nbFields;
    int totalNbLines;
  }

  /**
   * Constructor for ATFReaderTest.
   * @param arg0
   */
  public GPRReaderTest(String arg0) {
    super(arg0);
  }

  public void testReadGPR3WithOptionalFields() {

    ATFFileToTest atf = new ATFFileToTest();
    atf.name = "/files/testGPR3_with_optional_fields.gpr";
    atf.version = "GenePix Results 3";
    atf.nbOptionalheaders = 30;
    atf.nbFields = 61;
    atf.totalNbLines = 15777;

    read(atf, false);

  }

  public void testReadGPR3() {

    ATFFileToTest atf = new ATFFileToTest();
    atf.name = "/files/testGPR3.gpr";
    atf.version = "GenePix Results 3";
    atf.nbOptionalheaders = 29;
    atf.nbFields = 48;
    atf.totalNbLines = 13472;

    read(atf, false);

  }

  public void testReadGAL1() {

    ATFFileToTest atf = new ATFFileToTest();
    atf.name = "/files/testGAL10.gal";
    atf.version = "GenePix ArrayList V1.0";
    atf.nbOptionalheaders = 54;
    atf.nbFields = 5;
    atf.totalNbLines = 4137;

    read(atf, true);

  }

  public void testReadGPR14() {

    ATFFileToTest atf = new ATFFileToTest();
    atf.name = "/files/testGPR14.gpr";
    atf.version = "GenePix Results 1.4";
    atf.nbOptionalheaders = 27;
    atf.nbFields = 43;
    atf.totalNbLines = 13470;

    read(atf, false);

  }

  private void read(ATFFileToTest atf, boolean gal) {

    try {

      InputStream is = this.getClass().getResourceAsStream(atf.name);

      GPRReader gprr = new GPRReader(is);
      if (!gal)
        gprr.addFieldToRead("Log Ratio (532/635)");

      BioAssay b = gprr.read();

      String[] keys = b.getAnnotation().getPropertiesKeys();

      assertEquals(atf.nbOptionalheaders,
          b.getAnnotation().getPropertiesKeys().length);
      assertEquals(atf.version, b.getAnnotation().getProperty("Type"));

      String[] tab = b.getAnnotation().getPropertiesKeys();
      assertNotNull(tab);
      assertEquals(atf.nbOptionalheaders, tab.length);

      assertEquals(atf.totalNbLines - 2 - atf.nbOptionalheaders - 1,
          b.getIds().length);

      String[] ids = b.getIds();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      assertTrue(false);
    }
  }

}
