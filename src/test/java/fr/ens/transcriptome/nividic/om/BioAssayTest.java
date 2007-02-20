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

import java.util.Arrays;
import java.util.Random;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;

import junit.framework.TestCase;

/**
 * @author Laurent Jourdren
 */
public class BioAssayTest extends TestCase {

  /**
   * Constructor for BioAssayImplTest.
   * @param arg0
   */
  public BioAssayTest(String arg0) {
    super(arg0);
  }

  public void testGetName() {

    BioAssay b = getNewBioAssay();

    assertNotNull(b);

    b.setName(null);
    b.setName("ma puce");
    assertEquals("ma puce", b.getName());

  }

  /*
   * Test for VectorInt getLocs()
   */
  public void testGetLocs() {
  }

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5", "id6", "id7", "id8",
      "id9", "id10", "id11", "id12"};

  int[] int1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
  int[] int2 = {11, 22, 33, 44, 55, 66, 77, 88, 99, 1010, 1111, 1212};
  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10, 10,
      11.11, 12.12};
  double[] double2 = {10.1, 20.2, 30.3, 40.4, 50.5, 60.6, 70.7, 80.8, 90.9,
      100.10, 110.11, 120.12};

  int[] locLB = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
  int[] locCB = {1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2};
  int[] locL = {1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2};
  int[] locC = {1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3};

  private final static Random r = new Random(System.currentTimeMillis());

  private static final BioAssay getNewBioAssay() {
    return BioAssayFactory.createBioAssay();
  }

  private static final int[] getRandomInt(int size, int min, int max) {

    if (size < 0)
      return null;
    if (min > max) {
      int tmp = max;
      max = min;
      min = tmp;
    }
    int[] result = new int[size];

    for (int i = 0; i < size; i++) {
      int v = r.nextInt(max - min) - min;
      result[i] = v;
    }
    return result;
  }

  /*
   * Test for void getLocs(VectorInt, VectorInt, VectorInt, VectorInt)
   */
  public void testGetLocsVectorIntVectorIntVectorIntVectorInt() {

    // try {
    BioAssay b = getNewBioAssay();

    // String[] vids = ids1;

    /*
     * VectorInt vlLB = new VectorInt(locLB); VectorInt vlCB = new
     * VectorInt(locCB); VectorInt vlL = new VectorInt(locL); VectorInt vlC =
     * new VectorInt(locC);
     */
    int size = 10000;
    int[] vlLB = getRandomInt(size, 0, 64);
    int[] vlCB = getRandomInt(size, 0, 64);
    int[] vlL = getRandomInt(size, 0, 1024);
    int[] vlC = getRandomInt(size, 0, 1024);

    b.setLocations(null);

    try {
      b.setLocations(null, null, null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }

    try {
      b.setLocations(vlLB, null, null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setLocations(null, vlCB, null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setLocations(null, null, vlL, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setLocations(null, null, null, vlC);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }

    try {
      b.setLocations(vlLB, vlCB, vlL, vlC);
      assertTrue(true);
    } catch (BioAssayRuntimeException e) {
      assertTrue(false);
    }

    int[] rlLB = new int[b.size()];
    int[] rlCB = new int[b.size()];
    int[] rlL = new int[b.size()];
    int[] rlC = new int[b.size()];

    try {
      b.getLocations(null, null, null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }

    try {

      b.getLocations(rlLB, rlCB, rlL, rlC);
      assertTrue(true);
    } catch (BioAssayRuntimeException e) {
      assertTrue(false);
    }

    assertTrue(Arrays.equals(vlLB, rlLB));
    assertTrue(Arrays.equals(vlLB, rlLB));
    assertTrue(Arrays.equals(vlL, rlL));
    assertTrue(Arrays.equals(vlC, rlC));

    /*
     * } catch (Exception e) { e.printStackTrace();
     * System.out.println(e.getMessage()); assertTrue(false); }
     */
  }

  public void testGetReds() {

    BioAssay b = getNewBioAssay();
    int[] vReds = int2;

    assertNull(b.getReds());
    assertFalse(b.isReds());
    try {
      b.setReds(null);
      assertTrue(false);
    } catch (RuntimeException e) {
      assertTrue(true);
    }
    assertNull(b.getReds());
    assertFalse(b.isReds());
    b.setReds(vReds);
    assertTrue(b.isReds());
    assertNotNull(b.getReds());
    assertTrue(vReds.equals(b.getReds()));
    b.removeReds();
    assertNull(b.getReds());
    assertFalse(b.isReds());

  }

  public void testGetGreens() {

    try {
      BioAssay b = getNewBioAssay();
      int[] vGreens = int2;

      assertNull(b.getGreens());
      assertFalse(b.isGreens());
      try {
        b.setGreens(null);
        assertTrue(false);
      } catch (BioAssayRuntimeException e) {
        assertTrue(true);
      }
      assertNull(b.getGreens());
      assertFalse(b.isGreens());
      b.setGreens(vGreens);
      assertTrue(b.isGreens());
      assertNotNull(b.getGreens());
      assertTrue(vGreens.equals(b.getGreens()));
      b.removeGreens();
      assertNull(b.getGreens());
      assertFalse(b.isGreens());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertTrue(false);
    }
  }

  public void testGetIds() {

    try {
      BioAssay b = getNewBioAssay();
      String[] vIds = ids1;

      assertNull(b.getIds());
      assertFalse(b.isIds());
      try {
        b.setIds(null);
        assertTrue(false);
      } catch (BioAssayRuntimeException e) {
        assertTrue(true);
      }
      assertNull(b.getIds());
      assertFalse(b.isIds());
      b.setIds(vIds);
      assertTrue(b.isIds());
      assertNotNull(b.getIds());
      assertTrue(vIds.equals(b.getIds()));
      b.removeIds();
      assertNull(b.getIds());
      assertFalse(b.isIds());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertTrue(false);
    }
  }

  public void testGetRatios() {
    try {
      BioAssay b = getNewBioAssay();
      double[] vRatios = double1;

      assertNull(b.getRatios());
      assertFalse(b.isRatios());
      try {
        b.setRatios(null);
        assertTrue(false);
      } catch (BioAssayRuntimeException e) {
        assertTrue(true);
      }
      assertNull(b.getRatios());
      assertFalse(b.isRatios());
      b.setRatios(vRatios);
      assertTrue(b.isRatios());
      assertNotNull(b.getRatios());
      assertTrue(vRatios.equals(b.getRatios()));
      b.removeRatios();
      assertNull(b.getRatios());
      assertFalse(b.isRatios());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertTrue(false);
    }
  }

  public void testGetBrights() {
    try {
      BioAssay b = getNewBioAssay();
      double[] vBrights = double1;

      assertNull(b.getBrights());
      assertFalse(b.isBrights());
      try {
        b.setBrights(null);
        assertTrue(false);
      } catch (BioAssayRuntimeException e) {
        assertTrue(true);
      }
      assertNull(b.getBrights());
      assertFalse(b.isBrights());
      b.setBrights(vBrights);
      assertTrue(b.isBrights());
      assertNotNull(b.getBrights());
      assertTrue(vBrights.equals(b.getBrights()));
      b.removeBrights();
      assertNull(b.getBrights());
      assertFalse(b.isBrights());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertTrue(false);
    }
  }

  public void testGetFlags() {

    try {
      BioAssay b = getNewBioAssay();
      int[] vFlags = int2;

      assertNull(b.getFlags());
      assertFalse(b.isFlags());
      try {
        b.setFlags(null);
        assertTrue(false);
      } catch (BioAssayRuntimeException e) {
        assertTrue(true);
      }
      assertNull(b.getFlags());
      assertFalse(b.isFlags());
      b.setFlags(vFlags);
      assertTrue(b.isFlags());
      assertNotNull(b.getFlags());
      assertTrue(vFlags.equals(b.getFlags()));
      b.removeFlags();
      assertNull(b.getFlags());
      assertFalse(b.isFlags());

    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertTrue(false);
    }

  }

  public void testIsEmpty() {

  }

}