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

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.ArrayIntList;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayBase;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

/**
 * @author jourdren To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BioAssayBaseTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5"};
  String[] ids2 = {"id1", "id4", "id3", "id4", "id5"};
  String[] ids3 = {"id1", "id4", "id3", "id4", "id5", "id6"};
  String[] values1 = {"v1", "v2", "v3", "v4", "v5"};
  int[] int1 = {1, 22, 333, 4444, 5555};
  int[] int2 = {11111, 2222, 333, 44, 5};
  int[] int3 = {1, 2, 3, 4, 5};
  int[] int4 = {1, 2, 3, 4, 3};
  int[] int5 = {1, 2, 3};

  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5};
  double[] double2 = {11.1, 22.2, 33.3, 44.4, 55.5};
  double[] double3 = {11.1, 22.2, 33.3, 44.4, 55.5, 66.6};

  private static final BioAssay getNewBioAssayBase() {
    return BioAssayFactory.createBioAssay();
  }

  /**
   * Constructor for BioAssayBaseImplTest.
   * @param arg0
   */
  public BioAssayBaseTest(String arg0) {
    super(arg0);
  }

  public void testGetName() {

    BioAssayBase b = getNewBioAssayBase();

    assertNotNull(b);

    b.setName(null);
    b.setName("ma puce");
    assertEquals("ma puce", b.getName());

  }

  public void testGetDataInt() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    try {
      b.setDataFieldInt(null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setDataFieldInt(null, int1);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setDataFieldInt("toto", null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    assertNull(b.getDataFieldInt(null));

    assertNull(b.getDataFieldInt("toto"));
    b.setDataFieldInt("toto", int1);
    assertTrue(Arrays.equals(int1, b.getDataFieldInt("toto")));

    try {
      b.setDataFieldInt("toto", int5);
      assertTrue(false);
    } catch (Exception e) {
      assertTrue(true);
    }

  }

  public void testGetDataDouble() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();
    try {
      b.setDataFieldDouble(null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setDataFieldDouble(null, double1);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setDataFieldDouble("toto", null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    assertNull(b.getDataFieldDouble(null));
    b.setDataFieldDouble("toto", double1);
    assertTrue(Arrays.equals(double1, b.getDataFieldDouble("toto")));

    try {
      b.setDataFieldDouble("toto", double3);
      assertTrue(false);
    } catch (Exception e) {
      assertTrue(true);
    }

  }

  public void testGetDataString() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    try {
      b.setDataFieldString(null, null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setDataFieldString(null, ids1);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    try {
      b.setDataFieldString("toto", null);
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    assertNull(b.getDataFieldString(null));

    b.setDataFieldString("toto", ids1);
    assertTrue(Arrays.equals(ids1, b.getDataFieldString("toto")));

    try {
      b.setDataFieldString("toto", ids3);
      assertTrue(false);
    } catch (Exception e) {
      assertTrue(true);
    }

  }

  public void testSameNameData() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    b.setDataFieldString("toto", ids1);
    assertNotNull(b.getDataFieldString("toto"));
    assertNull(b.getDataFieldInt("toto"));

    b.setDataFieldInt("toto", int1);
    assertNull(b.getDataFieldString("toto"));
    assertNotNull(b.getDataFieldInt("toto"));

    b.setDataFieldString("toto", ids1);
    assertNotNull(b.getDataFieldString("toto"));
    assertNull(b.getDataFieldInt("toto"));

  }

  public void testGetLocs() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    b.setDataFieldString("ID", ids1);
    b.setDataFieldInt("reds", int1);

    b.setLocations(null);

    assertFalse(b.isLocations());
    assertNull(b.getLocations());

    b.setLocations(int3);

    assertTrue(Arrays.equals(int3, b.getLocations()));
    assertTrue(b.isLocations());

    b.removeLocations();

    assertFalse(b.isLocations());
    assertNull(b.getLocations());

  }

  public void testIsField() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    assertFalse(b.isField("toto"));
    assertEquals(0, b.getFields().length);

    b.setDataFieldInt("toto", int1);
    b.setDataFieldInt("titi", int2);
    b.setDataFieldString("tata", ids1);
    b.setDataFieldDouble("tutu", double2);

    assertEquals(4, b.getFields().length);

    b.removeField("titi");

    assertEquals(3, b.getFields().length);

    String[] fields = b.getFields();

    Arrays.sort(fields);

    assertEquals("tata", fields[0]);
    assertEquals("toto", fields[1]);
    assertEquals("tutu", fields[2]);

    assertFalse(b.isField("lolo"));

  }

  public void testGetDataType() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    ArrayIntList vi = new ArrayIntList();
    addToArrayList(vi, int2);

    ArrayDoubleList vd = new ArrayDoubleList();
    addToArrayList(vd, double2);

    ArrayList vs = new ArrayList();
    addToArrayList(vs, ids1);

    b.setDataFieldInt("toto", int2);
    assertEquals(BioAssayBase.DATATYPE_INTEGER, b.getFieldType("toto"));

    b.setDataFieldInt("toto1", int2);
    assertEquals(BioAssayBase.DATATYPE_INTEGER, b.getFieldType("toto1"));

    b.setDataFieldString("titi", ids1);
    assertEquals(BioAssayBase.DATATYPE_STRING, b.getFieldType("titi"));

    b.setDataFieldDouble("tata", double2);
    assertEquals(BioAssayBase.DATATYPE_DOUBLE, b.getFieldType("tata"));
    assertEquals(BioAssayBase.DATATYPE_INTEGER, b.getFieldType("toto"));
    assertEquals(BioAssayBase.DATATYPE_INTEGER, b.getFieldType("toto1"));
    assertEquals(BioAssayBase.DATATYPE_STRING, b.getFieldType("titi"));

  }

  public void testLength() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    assertEquals(0, b.size());

    ArrayIntList vi = new ArrayIntList();
    addToArrayList(vi, int2);

    ArrayDoubleList vd = new ArrayDoubleList();
    addToArrayList(vd, double2);

    ArrayList vs = new ArrayList();
    addToArrayList(vs, ids1);

    b.setDataFieldInt("toto", int2);

    b.setDataFieldInt("toto1", int2);

    b.setDataFieldString("titi", ids1);

    assertEquals(int2.length, b.size());

  }

  public void testLocations() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    b.setDataFieldInt("toto", int1);
    int[] metaRows = {1, 1, 1, 1, 1};
    int[] metaColumns = {1, 1, 1, 1, 1};
    int[] rows = {1, 1, 2, 2, 3};
    int[] columns = {1, 2, 1, 2, 1};

    int[] locations = BioAssayUtils.encodeLocation(metaRows, metaColumns, rows,
        columns);
    b.setLocations(locations);

    assertEquals(locations[0], BioAssayUtils.encodeLocation(1, 1, 1, 1));
    assertEquals(0, b.getIndexFromALocation(BioAssayUtils.encodeLocation(1, 1,
        1, 1)));
    assertEquals(3, b.getIndexFromALocation(BioAssayUtils.encodeLocation(1, 1,
        2, 2)));

  }

  public void testReferences() throws BioAssayRuntimeException {

    BioAssayBase b = getNewBioAssayBase();

    ArrayList ids = new ArrayList();
    addToArrayList(ids, ids1);

    ArrayIntList red = new ArrayIntList();
    addToArrayList(red, int1);

    ArrayIntList green = new ArrayIntList();
    addToArrayList(green, int2);

    ArrayIntList locs = new ArrayIntList();
    addToArrayList(locs, int3);

    b.makeReferences();

    b.setDataFieldString("ID", ids1);
    b.setDataFieldInt("red", int1);
    b.setDataFieldInt("green", int2);
    b.setLocations(int3);

    b.setReferenceField("ID");

    b.setReferenceField(null);
    assertNull(b.getIndexesFromAReference(null));

    assertNull(b.getIndexesFromAReference("id1"));

    b.setReferenceField("ID");

    b.makeReferences();
    assertNull(b.getIndexesFromAReference("id9"));
    int[] result = b.getIndexesFromAReference("id1");

    assertEquals(1, result.length);
    assertEquals(0, result[0]);

    addToArrayList(ids, ids2);
    b.setDataFieldString("ID", ids2);

    b.makeReferences();
    result = b.getIndexesFromAReference("id4");
    Arrays.sort(result);

    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(3, result[1]);

  }

  public void testRenameField() {

    BioAssayBase b = getNewBioAssayBase();

    b.setDataFieldString("toto", ids1);
    assertNotNull(b.getDataFieldString("toto"));

    b.setDataFieldInt("titi", int1);
    assertNotNull(b.getDataFieldInt("titi"));

    b.setDataFieldString("tata", ids1);
    assertNotNull(b.getDataFieldString("tata"));

    assertNull(b.getDataFieldString("tutu"));
    b.renameField("toto", "tutu");
    assertNotNull(b.getDataFieldString("tutu"));

    assertEquals(ids1, b.getDataFieldString("tutu"));
    
    try {
      
      b.renameField("titi",BioAssayBase.FIELD_NAME_LOCATION);
      assertTrue(false);
    }
    catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }
    

  }

  private static void addToArrayList(final ArrayIntList list, final int[] array) {

    if (list == null || array == null)
      return;

    for (int i = 0; i < array.length; i++)
      list.add(array[i]);
  }

  private static void addToArrayList(final ArrayDoubleList list,
      final double[] array) {

    if (list == null || array == null)
      return;

    for (int i = 0; i < array.length; i++)
      list.add(array[i]);
  }

  private static void addToArrayList(final ArrayList list, final String[] array) {

    if (list == null || array == null)
      return;

    for (int i = 0; i < array.length; i++)
      list.add(array[i]);
  }

}