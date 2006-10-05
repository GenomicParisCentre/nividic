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

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;
import fr.ens.transcriptome.nividic.om.impl.ExpressionMatrixImpl;

/**
 * JUnit Test class for SubExpressionMatrix
 * @author Lory Montout
 */
public class SubExpressionMatrixTest extends TestCase {

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

  private void printValues(ExpressionMatrix em) {

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();

    double[][] values = mMatrix.getValues();
    String[] ids = em.getRowIds();
    String[] columnNames = em.getColumnNames();

    System.out.println(em.getName());
    System.out.println("em.size() " + em.getRowCount());
    System.out.println("em.getColumnCount() " + em.getColumnCount());
    System.out.println("em.getColumnNames().length "
        + em.getColumnNames().length);

    System.out.println("-------------");

    System.out.print("id");

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
        System.out.print(mMatrix.getValue(id, colName));

      }
      System.out.println();

    }
  }

  public void testSubMatrixByCol() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix("em");
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);

    int[] col = {0, 2};
    String[] colNames = {"b1", "b3"};

    ExpressionMatrix em2 = em.subMatrixColumns(col);

    assertNotNull(em2);
    assertEquals(2, em2.getColumnCount());
    assertEquals("b1", em2.getColumnNames()[0]);
    assertEquals("b3", em2.getColumnNames()[1]);

    ExpressionMatrix em3 = em.subMatrixColumns(colNames);
    ExpressionMatrixDimension mMatrix3 = em3.getDefaultDimension();

    assertNotNull(em3);
    assertEquals(2, em3.getColumnCount());
    assertEquals("b1", em2.getColumnNames()[0]);
    assertEquals("b3", em2.getColumnNames()[1]);

    em.removeColumn("b1");

    assertTrue(em3.getColumnCount() == 1);
    assertEquals("b3", em3.getColumnNames()[0]);

    try {
      em3.removeColumn(0);
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.removeColumn("b3");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.removeColumn("b1");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.removeRow("id1");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.renameColumn("b3", "newcol");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.renameColumn(0, "newcol");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.renameRow("id1", "newrow");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.addBioAssay(b1);
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      em3.addColumn("newcol");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    assertFalse(em3.containsColumn("b1"));
    assertTrue(em3.containsColumn("b3"));

    try {
      em3.addRow("newRow");
      assertTrue(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    double[] em3Values = mMatrix3.getColumnToArray("b3");
    double[] emValues = mMatrix3.getColumnToArray("b3");

    for (int i = 0; i < emValues.length; i++) {

      if ((Double.isNaN(em3Values[i])) && (Double.isNaN(emValues[i])))
        continue;

      assertTrue(em3Values[i] == emValues[i]);
    }

    em3Values = mMatrix3.getColumnToArray(0);
    emValues = mMatrix3.getColumnToArray("b3");

    for (int i = 0; i < emValues.length; i++) {

      if ((Double.isNaN(em3Values[i])) && (Double.isNaN(emValues[i])))
        continue;

      assertTrue(em3Values[i] == emValues[i]);
    }

    mMatrix3.setValue("id3", 0, 123);
    mMatrix3.setValue("id3", "b3", 123);

    em3Values = mMatrix3.getColumnToArray(0);
    emValues = mMatrix3.getColumnToArray("b3");

    for (int i = 0; i < emValues.length; i++) {

      if ((Double.isNaN(em3Values[i])) && (Double.isNaN(emValues[i])))
        continue;

      assertTrue(em3Values[i] == emValues[i]);
    }

    assertEquals(mMatrix3.getValue("id3", 0), mMatrix3.getValue("id3", "b3"), 0);
    assertEquals(123, mMatrix3.getValue("id3", 0), 0);
    assertEquals(123, mMatrix3.getValue("id3", "b3"), 0);

    mMatrix3.setValue("id3", 0, 0);
    // em.setValue("id3", "b3", 123);

    em3Values = mMatrix3.getColumnToArray(0);
    emValues = mMatrix3.getColumnToArray("b3");

    for (int i = 0; i < emValues.length; i++) {

      if ((Double.isNaN(em3Values[i])) && (Double.isNaN(emValues[i])))
        continue;

      assertTrue(em3Values[i] == emValues[i]);
    }

    assertEquals(mMatrix3.getValue("id3", 0), mMatrix3.getValue("id3", "b3"), 0);
    assertEquals(0, mMatrix3.getValue("id3", 0), 0);
    assertEquals(0, mMatrix3.getValue("id3", "b3"), 0);

    mMatrix3.setValue("id3", "b3", 456);

    assertEquals(mMatrix3.getValue("id3", 0), mMatrix3.getValue("id3", "b3"), 0);
    assertEquals(456, mMatrix3.getValue("id3", 0), 0);
    assertEquals(456, mMatrix3.getValue("id3", "b3"), 0);

    em.removeRow("id2");
    assertTrue(em3.getRowCount() == 7);

    try {
      mMatrix3.getValue("id3", 1);
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix3.getValue("id3", "b2");
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix3.getColumn(2);
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix3.getColumn("b2");
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix3.getColumnToArray(3);
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix3.getColumnToArray("b1");
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

  }

  public void testSubMatrixByIds() {

    ExpressionMatrixImpl em = new ExpressionMatrixImpl();
    BioAssay b1 = makeBioAssay(ids1, double1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3);
    b3.setName("b3");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);

    String[] ids = {"id3", "id4", "id6"};

    ExpressionMatrix em2 = em.subMatrixRows(ids);
    ExpressionMatrixDimension mMatrix2 = em2.getDefaultDimension();

    assertNotNull(em2);
    assertEquals(3, em2.getRowCount());
    assertEquals(3, em2.getColumnCount());

    String[] idsValues = em2.getRowIds();
    for (int i = 0; i < ids.length; i++) {
      assertTrue(ids[i].equals(idsValues[i]));
    }

    em.removeRow("id3");
    assertTrue(em2.getRowCount() == 2);

    try {
      em.removeRow("id3");
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    BioAssay b = mMatrix2.getColumn(0);
    assertNotNull(b);

    ids = b.getIds();
    idsValues = mMatrix2.getRowIds();

    double[] valBioAssay = b.getMs();
    double[] valMatrix = mMatrix2.getColumnToArray(b.getName());

    double[] valMatrix2 = mMatrix2.getColumnToArray(0);

    for (int i = 0; i < valBioAssay.length; i++) {
      assertTrue(ids[i].equals(idsValues[i]));
      if ((Double.isNaN(valBioAssay[i])) && (Double.isNaN(valMatrix[i])))
        continue;
      assertEquals(valBioAssay[i], valMatrix[i], 0);
      assertEquals(valBioAssay[i], valMatrix2[i], 0);

    }

    try {
      mMatrix2.getRow("id2");
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix2.getValue("id3", 1);
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

    try {
      mMatrix2.getValue("id3", "b3");
      assertFalse(false);
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

  }

}