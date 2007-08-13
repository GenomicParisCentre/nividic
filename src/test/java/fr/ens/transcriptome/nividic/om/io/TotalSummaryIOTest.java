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
 * of the �cole Normale Sup�rieure and the individual authors.
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.sgdb.io.TotalSummaryReader;
import fr.ens.transcriptome.nividic.sgdb.io.TotalSummaryWriter;
import fr.ens.transcriptome.nividic.util.NividicUtils;

public class TotalSummaryIOTest extends TestCase {

  /**
   * Constructor for GPRIOTest.
   * @param arg0
   */
  public TotalSummaryIOTest(final String arg0) {
    super(arg0);
  }

  public void testReadTotalSummary() throws NividicIOException, IOException {

    String file1 = "/files/total.summary.txt";

    InputStream is = this.getClass().getResourceAsStream(file1);
    assertNotNull(is);

    BioAssayReader bar1 = new TotalSummaryReader(is);
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

  public void testReadWriteTotalSummary() {

    try {
      // get access to the test file
      String file1 = "/files/total.summary.txt";

      InputStream is = this.getClass().getResourceAsStream(file1);
      assertNotNull(is);

      BioAssayReader bar1 = new TotalSummaryReader(is);
      bar1.addFieldToRead(BioAssay.FIELD_NAME_ID);
      bar1.addFieldToRead(BioAssay.FIELD_NAME_BRIGHT);
      bar1.addFieldToRead(BioAssay.FIELD_NAME_RATIO);

      BioAssay b = bar1.read();

      is.close();

      assertNotNull(b);

      String outputFile1 = "/tmp/test.total.summary1.txt";

      OutputStream os = new FileOutputStream(outputFile1);

      BioAssayWriter baw = new TotalSummaryWriter(os);
      baw.addAllFieldsToWrite();

      baw.write(b);

      InputStream is2 = new FileInputStream(outputFile1);
      BioAssayReader bar2 = new TotalSummaryReader(is2);
      bar2.addAllFieldsToRead();

      BioAssay b2 = bar2.read();
      is2.close();

      int n = b.size();
      String[] ids1 = b.getIds();
      String[] ids2 = b2.getIds();
      for (int i = 0; i < n; i++) {
        if (!ids1[i].equals(ids2[i]))
          System.out.println(ids1[i] + "\n" + ids2[i] + "\n\n");
      }

      assertTrue(Arrays.equals(b.getIds(), b2.getIds()));
      assertTrue(Arrays.equals(b.getBrights(), b2.getBrights()));
      assertTrue(Arrays.equals(b.getRatios(), b2.getRatios()));
      // assertFalse(b.isDescriptions());
      // assertFalse(b2.isDescriptions());

      // assertTrue(Arrays.equals(b.getDescriptions(), b2.getDescriptions()));

      String outputFile2 = "/tmp/test.total.summary2.txt";
      OutputStream os2 = new FileOutputStream(outputFile2);
      BioAssayWriter baw2 = new TotalSummaryWriter(os2);
      baw2.addAllFieldsToWrite();
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_ID);
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_RATIO);
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_BRIGHT);
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_DESCRIPTION);

      baw2.write(b2);
      os2.close();
      os.close();

      assertTrue(NividicUtils.compareFile(outputFile1, outputFile2));

      new File(outputFile1).delete();
      new File(outputFile2).delete();

    } catch (Exception e) {

      System.out.println(e.getMessage());
      e.printStackTrace();
      assertTrue(false);
    }
  }

  private BioAssay readStream(InputStream is) throws NividicIOException,
      IOException {

    BioAssayReader bar1 = new TotalSummaryReader(is);
    bar1.addAllFieldsToRead();
    BioAssay b = bar1.read();
    assertNotNull(b);
    is.close();

    return b;
  }

  private void writeStream(OutputStream os, BioAssay b)
      throws NividicIOException, IOException {

    BioAssayWriter baw = new TotalSummaryWriter(os);
    baw.addAllFieldsToWrite();
    baw.write(b);
    os.close();
  }

  public void testReadWriteAllTotalSummary() {

    try {
      // get access to the test file

      String file1 = "/files/total.summary.txt";

      InputStream is = this.getClass().getResourceAsStream(file1);
      assertNotNull(is);
      BioAssay b1 = readStream(is);

      String outputFile1 = "/tmp/test.total.summary1.txt";

      OutputStream os1 = new FileOutputStream(outputFile1);
      writeStream(os1, b1);

      InputStream is2 = new FileInputStream(outputFile1);
      assertNotNull(is2);
      BioAssay b2 = readStream(is2);

      String outputFile2 = "/tmp/test.total.summary2.txt";

      OutputStream os2 = new FileOutputStream(outputFile2);
      writeStream(os2, b2);

      // Do a written copy of an IDMA file is the same as the readed one
      assertTrue(NividicUtils.compareFile(outputFile1, outputFile2));

      // assertTrue(compareFile(
      //
      // this.getClass().getResourceAsStream("/files/testID_M_A.txt"),
      // new FileInputStream("/tmp/testWriteIDMA.txt")));

      // System.out.println("good");

      new File(outputFile1).delete();
      new File(outputFile2).delete();

    } catch (Exception e) {

      System.out.println(e.getMessage());
      e.printStackTrace();
      // assertTrue(false);
    }

  }

}
