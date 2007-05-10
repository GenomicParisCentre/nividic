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

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;

public class UniversalBioAssayReaderTest extends TestCase {

  private class FileToTest {

    String name;
    String version;
    int nbOptionalheaders;
    int nbFields;
    int totalNbLines;
  }

  public void testReadGPR3() {

    FileToTest atf = new FileToTest();
    atf.name = "/files/testGPR3.gpr";
    atf.version = "GenePix Results 3";
    atf.nbOptionalheaders = 29;
    atf.nbFields = 48;
    atf.totalNbLines = 13472;

    readATF(atf, false);

  }

  public void testReadGAL1() {

    FileToTest atf = new FileToTest();
    atf.name = "/files/testGAL10.gal";
    atf.version = "GenePix ArrayList V1.0";
    atf.nbOptionalheaders = 54;
    atf.nbFields = 5;
    atf.totalNbLines = 4137;

    readATF(atf, true);

  }

  public void testReadGPR14() {

    FileToTest atf = new FileToTest();
    atf.name = "/files/testGPR14.gpr";
    atf.version = "GenePix Results 1.4";
    atf.nbOptionalheaders = 27;
    atf.nbFields = 43;
    atf.totalNbLines = 13470;

    readATF(atf, false);

  }

  public void testGoulphar() {
    FileToTest idma = new FileToTest();
    idma.name = "/files/testID_M_A.txt";
    idma.nbFields = 3;
    idma.nbOptionalheaders = 0;
    idma.totalNbLines = 13441;

    readIDMA(idma);
  }

  private void readATF(FileToTest atf, boolean gal) {

    try {

      InputStream is = this.getClass().getResourceAsStream(atf.name);

      BioAssayReader bar = new UniversalBioAssayReader(is);
      if (!gal)
        bar.addFieldToRead("Log Ratio (532/635)");

      BioAssay b = bar.read();

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

  /**
   * read an ID-M-A file
   * @param idma the file to read
   * @param comma true if the decimal separator is a comma
   * @return a bioAssay object
   */
  private BioAssay readIDMA(final FileToTest idma) {

    try {

      InputStream is = this.getClass().getResourceAsStream(idma.name);

      BioAssayReader idmar = new UniversalBioAssayReader(is);

      BioAssay b = idmar.read();

      String[] keys = b.getAnnotation().getPropertiesKeys();

      assertEquals(idma.nbOptionalheaders, b.getAnnotation()
          .getPropertiesKeys().length);

      String[] tab = b.getAnnotation().getPropertiesKeys();
      assertNotNull(tab);
      assertEquals(idma.nbOptionalheaders, tab.length);

      String[] ids = b.getIds();
      assertNotNull(ids);

      // failure if the last ID is not the correct one

      assertEquals("PSA2", ids[ids.length - 1]);

      double[] mnorms = b.getMs();

      assertEquals(-0.242, mnorms[955 - 2], 0);

      assertEquals(idma.totalNbLines - 1, b.getIds().length);
      // 1 is the nb of anotation lines

      return b;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      assertTrue(false);
    }
    return null;
  }
  
  public void testReadTotalSummary() throws NividicIOException, IOException {

    String file1 = "/files/total.summary.txt";

    InputStream is = this.getClass().getResourceAsStream(file1);
    assertNotNull(is);

    BioAssayReader bar1 = new UniversalBioAssayReader(is);
    bar1.addAllFieldsToRead();

    BioAssay b = bar1.read();

    is.close();

    assertNotNull(b);

    String[] fields = b.getFields();

    String[] ids = b.getIds();
    assertNotNull(ids);

    double[] ms = b.getMs();
    assertNotNull(ms);

    double[] as = b.getAs();
    assertNotNull(as);

    double[] sds = b.getStdDevMs();
    assertNotNull(sds);

    int[] ns = b.getDataFieldInt("n");
    assertNotNull(ns);

    int[] totalns = b.getDataFieldInt("total n");
    assertNotNull(totalns);

    assertEquals("Q0045_01   |Q0045|COX1|cytochrome c oxidase subunit I",
        ids[3]);
    assertEquals(0.917, ms[3], 0);
    assertEquals(8.239, as[3], 0);
    assertEquals(0.398, sds[3], 0);
    assertEquals(4, ns[3], 0);
    assertEquals(4, totalns[3], 0);

    // medianMnorm medianA SDMnorm n total n
    // 0.917 8.239 0.398 4 4
  }

}
