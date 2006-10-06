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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.io.BioAssayReader;
import fr.ens.transcriptome.nividic.om.io.BioAssayWriter;
import fr.ens.transcriptome.nividic.om.io.GALReader;
import fr.ens.transcriptome.nividic.om.io.GALWriter;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.GPRWriter;

import junit.framework.TestCase;

/**
 * @author Laurent Jourdren
 */
public class NewGPRIOTest extends TestCase {

  /**
   * Constructor for GPRIOTest.
   * @param arg0
   */
  public NewGPRIOTest(final String arg0) {
    super(arg0);
  }

  private boolean compareFile(InputStream a, InputStream b) throws Exception {

    BufferedReader bra = new BufferedReader(new InputStreamReader(a));
    BufferedReader brb = new BufferedReader(new InputStreamReader(b));

    StringBuffer sba = new StringBuffer();
    StringBuffer sbb = new StringBuffer();

    String line;
    while ((line = bra.readLine()) != null) {
      sba.append(line);
    }
    bra.close();

    while ((line = brb.readLine()) != null) {
      sbb.append(line);
    }
    brb.close();

    return sba.toString().equals(sbb.toString());
  }

  private boolean compareFile(String a, String b) throws Exception {
    return compareFile(new FileInputStream(a), new FileInputStream(b));
  }

  private boolean compareFile(InputStream a, String b) throws Exception {
    return compareFile(a, new FileInputStream(b));
  }

  public void testReadWriteGPR() {

    try {
      //  get access to the test file
      String file1 = "/files/testGPR14" + ".gpr";

      InputStream is = this.getClass().getResourceAsStream(file1);
      assertNotNull(is);

      BioAssayReader bar1 = new GPRReader(is);
      bar1.addAllFieldsToRead();

      BioAssay b = bar1.read();

      assertNotNull(b);

      String outputFile = "/tmp/test3.gpr";

      OutputStream os = new FileOutputStream(outputFile);

      BioAssayWriter baw = new GPRWriter(os);
      baw.addAllFieldsToWrite();

      baw.write(b);

      InputStream is2 = new FileInputStream(outputFile);
      BioAssayReader bar2 = new GPRReader(is2);
      bar2.addAllFieldsToRead();

      BioAssay b2 = bar2.read();
      is2.close();

      assertTrue(Arrays.equals(b.getIds(), b2.getIds()));
      assertTrue(Arrays.equals(b.getReds(), b2.getReds()));
      assertTrue(Arrays.equals(b.getGreens(), b2.getGreens()));
      assertTrue(Arrays.equals(b.getFlags(), b2.getFlags()));

      OrderedMap map = new LinkedMap();
      map.put("FIVE", "5");
      map.put("SIX", "6");
      map.put("SEVEN", "7");

      map.firstKey(); // returns "FIVE"
      map.nextKey("FIVE"); // returns "SIX"
      map.nextKey("SIX"); // returns "SEVEN"

      OutputStream os2 = new FileOutputStream("/tmp/test4.gpr");
      BioAssayWriter baw2 = new GPRWriter(os2);
      baw2.addAllFieldsToWrite();

      baw2.write(b2);
      os2.close();

      assertTrue(compareFile("/tmp/test3.gpr", "/tmp/test4.gpr"));

    } catch (Exception e) {

      System.out.println(e.getMessage());
      e.printStackTrace();
      assertTrue(false);
    }
  }

  private void ReadWriteGAL() throws Exception {

    //  get access to the test file
    String file1 = "/files/testGAL3.gal";

    InputStream is = this.getClass().getResourceAsStream(file1);
    assertNotNull(is);

    BioAssayReader bar1 = new GALReader(is);
    bar1.addAllFieldsToRead();

    BioAssay b1 = bar1.read();
    is.close();

    BioAssay b2 = BioAssayFactory.createBioAssay();

    //  Set the annotations
    String[] aKeys = b1.getAnnotation().getPropertiesKeys();
    for (int i = 0; i < aKeys.length; i++) {
      b2.getAnnotation().setProperty(aKeys[i],
          b1.getAnnotation().getProperty(aKeys[i]));
    }

    int[] galLocation = b1.getLocations();
    String[] galNames = b1.getDescriptions();
    String[] galIds = b1.getIds();

    b2.setLocations(galLocation);
    b2.setDescriptions(galNames);
    b2.setIds(galIds);

    OutputStream os2 = new FileOutputStream("/tmp/test5.gpr");
    BioAssayWriter baw2 = new GALWriter(os2);
    baw2.addAllFieldsToWrite();

    baw2.write(b2);
    os2.close();

    assertTrue(compareFile(this.getClass().getResourceAsStream(file1),
        "/tmp/test4.gpr"));

  }

}