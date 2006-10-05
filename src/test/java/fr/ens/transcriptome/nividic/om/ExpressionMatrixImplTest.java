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

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.impl.ExpressionMatrixImpl;

/**
 * @author Lory Montout
 */
public class ExpressionMatrixImplTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5"};
  String[] ids2 = {"id1", "id4", "id5", "id7"};
  String[] ids3 = {"id6", "id8", "id1", "id3"};

  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5};
  double[] double2 = {11.1, 44.4, Double.NaN, 77.7};
  double[] double3 = {666.6, 888.8, 111.1, 333.3};

  private class IDMAFileToTest {

    String name;
    int nbOptionalheaders;
    int nbFields;
    int totalNbLines;
    int nbUndefLines;

  }

  private BioAssay makeBioAssay(String[] ids, double[] m) {

    BioAssay b = BioAssayFactory.createBioAssay();
    b.setIds(ids);
    b.setMs(m);

    return b;
  }

  private void printValues(ExpressionMatrixDimension em) {

    String[] ids = em.getRowIds();
    String[] columnNames = em.getColumnNames();

    System.out.println("em.size() " + em.getRowCount());
    System.out.println("em.getColumnCount() " + em.getColumnCount());
    System.out.println("em.getColumnNames().length "
        + em.getColumnNames().length);

    System.out.println("-------------");

    System.out.print("id/M");

    for (int i = 0; i < columnNames.length; i++) {

      String colName = columnNames[i];

      System.out.print("\t\t" + colName);

    }
    System.out.println();

    for (int i = 0; i < ids.length; i++) {

      String id = ids[i];
      System.out.print(id);

      for (int j = 0; j < columnNames.length; j++) {
        String colName = columnNames[j];
        System.out.print("\t\t");
        System.out.print(em.getValue(id, colName));

      }
      System.out.println();

    }
  }

  public void testGetBioAssay() {

    ExpressionMatrixImpl em1 = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    ExpressionMatrixDimension d1 = em1.getDefaultDimension();

    d1.addBioAssay(b1);

    d1.addBioAssay(b2);
    d1.addBioAssay(b3);

    BioAssay b4 = d1.getColumn(0);
    assertNotNull(b4);
    double[] val = b4.getMs();
    ExpressionMatrixImpl em2 = new ExpressionMatrixImpl();
    ExpressionMatrixDimension d2 = em2.getDefaultDimension();

    d2.addBioAssay(b4);
    d2.addBioAssay(b2);
    d2.addBioAssay(b3);
    assertTrue(d1.equals(d2));

    BioAssay b5 = d1.getColumn("b2");
    assertNotNull(b5);
    val = b5.getMs();

    ExpressionMatrixImpl em3 = new ExpressionMatrixImpl();
    ExpressionMatrixDimension d3 = em3.getDefaultDimension();

    d3.addBioAssay(b1);
    d3.addBioAssay(b5);
    d3.addBioAssay(b3);
    assertTrue(d1.equals(d3));

  }

  public void testArrayList() {

    ArrayList a = new ArrayList();
    Object o1 = new Object();
    Object o2 = new Object();
    a.add(o1);
    a.add(o2);

    assertNotNull(a.get(0));

    assertNotNull(a.get(1));

    assertEquals(2, a.size());
  }

  public void testRemoveTheSameIdTwice() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    ExpressionMatrixDimension d = em.getDefaultDimension();

    d.addBioAssay(b1);

    d.addBioAssay(b2);

    em.removeRow("id2");

    try {
      em.removeRow("id2");

    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }
  }

  public void testRemoveTheSameColumnTwice() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    ExpressionMatrixDimension d = em.getDefaultDimension();

    d.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));

    d.addBioAssay(b2);

    em.removeColumn("b2");
    try {
      em.removeColumn("b2");

    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(true);
    }
  }

  public void testRemoveThefirstColumnTwice() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    ExpressionMatrixDimension d = em.getDefaultDimension();

    d.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));
    // printValues(em);

    d.addBioAssay(b2);
    d.addBioAssay(b3);

    em.removeColumn(0);
    try {
      em.removeColumn(0);
    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(false);
    }

    assertTrue(em.getColumnCount() == 1);

  }

  public void testAddTheSameRowTwice() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    ExpressionMatrixDimension d = em.getDefaultDimension();

    d.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));

    d.addBioAssay(b2);

    em.addRow("id12");

    try {
      em.addRow("id12");
    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(true);
    }
  }

  public void testAddTheSameRowTwice2() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();

    BioAssay b1 = makeBioAssay(ids1, double1);
    BioAssay b2 = makeBioAssay(ids2, double2);
    BioAssay b3 = makeBioAssay(ids3, double3);

    ExpressionMatrixDimension d = em.getDefaultDimension();

    d.addBioAssay(b1);
    d.addBioAssay(b2);

    double[] id12 = {12.1, 12.2};

    d.addRow("id12", id12);

    try {
      d.addRow("id12", id12);

    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(true);
    }
  }

  public void testAddAndRemoveBioAssay() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    ExpressionMatrixDimension d = em.getDefaultDimension();

    d.addBioAssay(b1);
    Object[] ids = new String[em.getRowCount()];

    d.addBioAssay(b2);
    assertNotNull(em);
    String idAt2 = "id3";

    assertEquals(em.getColumnCount(), 2);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    assertEquals(3.3, d.getValue(idAt2, 0), 0);

    // System.out.println("add b3");
    d.addBioAssay(b3);
    assertEquals(em.getColumnCount(), 3);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    String idAt3 = "id4";
    // System.out.println("remove column 0");
    em.removeColumn(0);
    assertEquals(em.getColumnCount(), 2);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    assertEquals(44.4, d.getValue(idAt3, 0), 0);
    assertEquals(44.4, d.getValue(idAt3, "b2"), 0);

    // System.out.println("add b1 & b2.02");
    d.addBioAssay(b1);

    d.addBioAssay(b2, "b2.02");

    assertNotSame("b2.02", b2.getName());

    assertEquals(em.getColumnCount(), 4);

    // String[] colnames = em.getColumnNames();

    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    String[] idValues = {"id1", "id2", "id3", "id4", "id5", "id7", "id6", "id8"};

    ids = em.getRowIds();
    for (int i = 0; i < ids.length; i++) {
      assertTrue(ids[i].equals(idValues[i]));
    }

    // System.out.println("remove id2");
    String[] idValues2 = {"id1", "id3", "id4", "id5", "id7", "id6", "id8"};
    em.removeRow("id2");
    double[] valb1 = d.getColumnToArray("b1");

    ids = em.getRowIds();
    for (int i = 0; i < ids.length; i++) {
      assertTrue(ids[i].equals(idValues2[i]));
    }

    em.removeColumn("b3");
    assertEquals(em.getColumnCount(), 3);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    em.renameColumn(1, "b1.02");

    assertEquals("b1.02", em.getColumnNames()[1]);
    em.renameColumn("b1.02", "b1.03");
    assertEquals("b1.03", em.getColumnNames()[1]);

    em.renameRow("id1", "id1.02");
    ids = em.getRowIds();
    assertEquals("id1.02", ids[6]);

    d.setValue("id3", 0, 33.3);
    assertEquals(33.3, d.getValue("id3", 0), 0);

    d.setValue("id3", "b2.02", 33.3);
    assertEquals(33.3, d.getValue("id3", "b2.02"), 0);

    double[] id12 = {12.1, 12.2, 12.3};
    int[] colId12 = {0, 1, 2};
    d.addRow("id12", id12);

    assertEquals(12.2, d.getValue("id12", "b1.03"), 0);

    d.addRow("id12.02", colId12, id12);

    assertEquals(12.2, d.getValue("id12.02", "b1.03"), 0);

  }

  public void testGetName() {

    ExpressionMatrix b = new ExpressionMatrixImpl();

    assertNotNull(b);

    b.setName(null);
    b.setName("ma puce");
    assertEquals("ma puce", b.getName());
  }

  public void testGetSize() {

    ExpressionMatrix b = new ExpressionMatrixImpl();

    assertNotNull(b);

    assertEquals(b.getRowCount(), 0);
    assertEquals(b.getColumnCount(), 0);
  }

}