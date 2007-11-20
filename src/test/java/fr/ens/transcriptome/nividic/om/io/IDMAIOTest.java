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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * Test class for IDMAIO classes
 * @author montout
 */
public class IDMAIOTest extends TestCase {

  /**
   * Constructor for GPRIOTest.
   * @param arg0
   */
  public IDMAIOTest(final String arg0) {
    super(arg0);
  }

  public void testReadWriteIDMA() {

    try {
      // get access to the test file
      String file1 = "/files/testID_M_A.txt";

      InputStream is = this.getClass().getResourceAsStream(file1);
      assertNotNull(is);

      InputStreamBioAssayReader bar1 = new IDMAReader(is);
      bar1.addFieldToRead(BioAssay.FIELD_NAME_ID);
      bar1.addFieldToRead(BioAssay.FIELD_NAME_BRIGHT);
      bar1.addFieldToRead(BioAssay.FIELD_NAME_RATIO);

      BioAssay b = bar1.read();

      is.close();

      assertNotNull(b);

      File tmpFile1 = File.createTempFile("idma", ".txt");

      OutputStream os = new FileOutputStream(tmpFile1);

      BioAssayWriter baw = new IDMAWriter(os);
      baw.addAllFieldsToWrite();

      baw.write(b);

      InputStream is2 = new FileInputStream(tmpFile1);
      InputStreamBioAssayReader bar2 = new IDMAReader(is2);
      bar2.addAllFieldsToRead();

      BioAssay b2 = bar2.read();
      is2.close();

      assertTrue(Arrays.equals(b.getIds(), b2.getIds()));
      assertTrue(Arrays.equals(b.getBrights(), b2.getBrights()));
      assertTrue(Arrays.equals(b.getRatios(), b2.getRatios()));
      assertTrue(Arrays.equals(b.getDescriptions(), b2.getDescriptions()));

      File tmpFile2 = File.createTempFile("idma", ".txt");

      OutputStream os2 = new FileOutputStream(tmpFile2);
      BioAssayWriter baw2 = new IDMAWriter(os2);
      baw2.addAllFieldsToWrite();
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_ID);
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_RATIO);
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_BRIGHT);
      baw2.addFieldToWrite(BioAssay.FIELD_NAME_DESCRIPTION);

      baw2.write(b2);
      os2.close();
      os.close();


      assertTrue(NividicUtils.compareFile(tmpFile2, tmpFile1));

      tmpFile1.delete();
      tmpFile2.delete();

    } catch (Exception e) {

      System.out.println(e.getMessage());
      e.printStackTrace();
      assertTrue(false);
    }
  }

  public void testReadWriteAllIDMA() {

    try {
      // get access to the test file

      String file1 = "/files/testID_M_A.txt";

      InputStream is = this.getClass().getResourceAsStream(file1);
      assertNotNull(is);
      InputStreamBioAssayReader bar1 = new IDMAReader(is);
      bar1.addAllFieldsToRead();
      BioAssay b = bar1.read();
      assertNotNull(b);
      is.close();

      String outputFile = "/tmp/testWriteIDMA.txt";

      OutputStream os = new FileOutputStream(outputFile);
      BioAssayWriter baw = new IDMAWriter(os);
      baw.addAllFieldsToWrite();
      baw.write(b);
      os.close();

      InputStream is2 = new FileInputStream(outputFile);
      assertNotNull(is2);
      InputStreamBioAssayReader bar2 = new IDMAReader(is2);
      bar2.addAllFieldsToRead();
      BioAssay b2 = bar2.read();
      assertNotNull(b2);
      is2.close();

      String outputFile2 = "/tmp/testWriteIDMA2.txt";

      OutputStream os2 = new FileOutputStream(outputFile2);
      BioAssayWriter baw2 = new IDMAWriter(os2);
      baw2.addAllFieldsToWrite();
      baw2.write(b);
      os2.close();

      // Do a written copy of an IDMA file is the same as the readed one
      assertTrue(NividicUtils.compareFile(outputFile, outputFile2));

      // assertTrue(compareFile(
      //
      // this.getClass().getResourceAsStream("/files/testID_M_A.txt"),
      // new FileInputStream("/tmp/testWriteIDMA.txt")));

      // System.out.println("good");

    } catch (Exception e) {

      System.out.println(e.getMessage());
      e.printStackTrace();
      // assertTrue(false);
    }
  }

}