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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.Spot;

import junit.framework.TestCase;

/**
 * @author Laurent Jourdren
 */
public class SpotTest extends TestCase {

  private static final int LOCATION_DEFAULT_VALUE = 1111;
  private static final int RED_DEFAULT_VALUE = 2222;
  private static final int GREEN_DEFAULT_VALUE = 3333;
  private static final int FLAG_DEFAULT_VALUE = 3333;
  private static final double A_DEFAULT_VALUE = 4444;
  private static final double M_DEFAULT_VALUE = 5555;
  private static final double RATIOS_DEFAULT_VALUE = 9999;
  private static final double BRIGHT_DEFAULT_VALUE = 6666;
  private static final String ID_DEFAULT_VALUE = "7777";
  private static final String DESCRIPTION_DEFAULT_VALUE = "8888";

  private static final int ARRAY_SIZE = 5;
  private static final int INT_VALUE = 1234;
  private static final double DOUBLE_VALUE = 1234;
  private static final String STRING_VALUE = "1234";

  private BioAssay createBioAssay() {

    final int size = ARRAY_SIZE;

    BioAssay ba = BioAssayFactory.createBioAssay();

    int[] locs = new int[size];
    Arrays.fill(locs, LOCATION_DEFAULT_VALUE);
    ba.setLocations(locs);

    int[] reds = new int[size];
    Arrays.fill(reds, RED_DEFAULT_VALUE);
    ba.setReds(reds);

    int[] greens = new int[size];
    Arrays.fill(greens, GREEN_DEFAULT_VALUE);
    ba.setGreens(greens);

    int[] flags = new int[size];
    Arrays.fill(flags, FLAG_DEFAULT_VALUE);
    ba.setFlags(flags);

    double[] as = new double[size];
    Arrays.fill(as, A_DEFAULT_VALUE);
    ba.setAs(as);

    double[] ms = new double[size];
    Arrays.fill(ms, M_DEFAULT_VALUE);
    ba.setMs(ms);

    double[] brights = new double[size];
    Arrays.fill(brights, BRIGHT_DEFAULT_VALUE);
    ba.setBrights(brights);

    double[] ratios = new double[size];
    Arrays.fill(ratios, RATIOS_DEFAULT_VALUE);
    ba.setRatios(ratios);

    String[] ids = new String[size];
    Arrays.fill(ids, ID_DEFAULT_VALUE);
    ba.setIds(ids);

    String[] descriptions = new String[size];
    Arrays.fill(descriptions, DESCRIPTION_DEFAULT_VALUE);
    ba.setDescriptions(descriptions);
    
    return ba;
  }

  public void testGetBioAssay() {

    BioAssay ba = createBioAssay();

    Spot s = ba.getSpot(0);

    assertEquals(ba, s.getBioAssay());
  }

  public void testGetIndex() {

    BioAssay ba = createBioAssay();

    Spot s = ba.getSpot(3);

    assertEquals(3, s.getIndex());
  }

  public void testGetRed() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals(RED_DEFAULT_VALUE, s.getRed());

    s.setRed(INT_VALUE);
    assertEquals(INT_VALUE, s.getRed());

  }

  public void testGetGreen() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals(GREEN_DEFAULT_VALUE, s.getGreen());

    s.setGreen(INT_VALUE);
    assertEquals(INT_VALUE, s.getGreen());
  }

  public void testGetId() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals(ID_DEFAULT_VALUE, s.getId());

    s.setId(STRING_VALUE);
    assertEquals(STRING_VALUE, s.getId());

  }

  public void testGetFlags() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals(FLAG_DEFAULT_VALUE, s.getFlag());

    s.setFlag(INT_VALUE);
    assertEquals(INT_VALUE, s.getFlag());

  }

  public void testGetDescriptions() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals(DESCRIPTION_DEFAULT_VALUE, s.getDescription());

    s.setDescription(STRING_VALUE);
    assertEquals(STRING_VALUE, s.getDescription());
  }

  public void testGetA() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals("" + s.getA(), "" + A_DEFAULT_VALUE);

    s.setA(DOUBLE_VALUE);
    assertEquals("" + DOUBLE_VALUE, "" + s.getA());

  }

  public void testGetM() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals("" + s.getM(), "" + M_DEFAULT_VALUE);

    s.setM(DOUBLE_VALUE);
    assertEquals("" + DOUBLE_VALUE, "" + s.getM());

  }

  public void testIsGreen() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isGreen());

    ba.setGreens(new int[ARRAY_SIZE]);
    assertTrue(s.isGreen());

  }

  public void testIsRed() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setGreens(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isRed());

    ba.setReds(new int[ARRAY_SIZE]);
    assertTrue(s.isRed());

  }

  public void testIsRatio() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isRatio());

    ba.setRatios(new double[ARRAY_SIZE]);
    assertTrue(s.isRatio());

  }

  public void testIsFlag() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isFlag());

    ba.setFlags(new int[ARRAY_SIZE]);
    assertTrue(s.isFlag());

  }

  public void testGetBright() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals("" + BRIGHT_DEFAULT_VALUE, "" + s.getBright());

    s.setBright(DOUBLE_VALUE);
    assertEquals("" + DOUBLE_VALUE, "" + s.getBright());
  }

  public void testIsBright() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isBright());

    ba.setBrights(new double[ARRAY_SIZE]);
    assertTrue(s.isBright());

  }

  public void testIsId() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isId());

    ba.setIds(new String[ARRAY_SIZE]);
    assertTrue(s.isId());

  }

  public void testIsDescription() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isId());

    ba.setIds(new String[ARRAY_SIZE]);
    assertTrue(s.isId());

  }

  public void testIsA() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isA());

    ba.setAs(new double[ARRAY_SIZE]);
    assertTrue(s.isA());
  }

  public void testIsM() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isM());

    ba.setMs(new double[ARRAY_SIZE]);
    assertTrue(s.isM());
  }

  public void testIsLocation() {

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.setReds(new int[ARRAY_SIZE]);

    Spot s = ba.getSpot(0);

    assertFalse(s.isLocation());

    ba.setLocations(new int[ARRAY_SIZE]);
    assertTrue(s.isLocation());
  }

  public void testGetRatio() {

    Spot s = createBioAssay().getSpot(0);

    s.setRatio(DOUBLE_VALUE);
    assertEquals("" + DOUBLE_VALUE, "" + s.getRatio());

  }

  public void testGetLocation() {

    Spot s = createBioAssay().getSpot(0);

    assertEquals(LOCATION_DEFAULT_VALUE, s.getLocation());
  }

  public void testGetMetaRow() {
  }

  public void testGetMetaColumn() {
  }

  public void testGetRow() {
  }

  public void testGetColumn() {
  }

  /*
   * Class under test for boolean isEmpty(String[])
   */
  public void testIsEmptyStringArray() {
  }

  /*
   * Class under test for boolean isEmpty()
   */
  public void testIsEmpty() {
  }

}