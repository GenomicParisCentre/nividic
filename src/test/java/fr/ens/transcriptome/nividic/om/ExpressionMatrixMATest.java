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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.io.InputStreamBioAssayReader;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * JUnit Test class for ExpressionMatrixMA
 * @author Lory Montout
 */
public class ExpressionMatrixMATest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5"};
  String[] ids2 = {"id1", "id4", "id5", "id7"};
  String[] ids3 = {"id6", "id8", "id1", "id3"};

  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5};
  double[] double2 = {11.1, 44.4, Double.NaN, 77.7};
  double[] double3 = {666.6, 888.8, 111.1, 333.3};

  double[] a1 = {11111.1, 22222.2, 33333.3, 444444.4, 55555.5};
  double[] a2 = {111111.1, 444444.4, Double.NaN, 777777.7};
  double[] a3 = {6666666.6, 8888888.8, 1111111.1, 3333333.3};

  private class IDMAFileToTest {

    String name;
    int nbOptionalheaders;
    int nbFields;
    int totalNbLines;
    int nbUndefLines;

  }

  private BioAssay makeBioAssay(String[] ids, double[] m, double[] a) {

    BioAssay b = BioAssayFactory.createBioAssay();
    b.setIds(ids);
    b.setMs(m);
    b.setAs(a);

    return b;
  }

  public void testGetBioAssay() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    double[] ms = b1.getMs();
    assertNotNull(ms);

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);

    BioAssay b4 = mMatrix.getColumn(0);
    BioAssay b6 = aMatrix.getColumn(0);

    assertNotNull(b4);
    assertNotNull(b6);

    ExpressionMatrix em2 = ExpressionMatrixFactory.createExpressionMatrix();
    em2.addDimension(BioAssay.FIELD_NAME_A);

    em2.addBioAssay(b4);
    em2.getDimension(BioAssay.FIELD_NAME_A).addBioAssay(b6);
    em2.addBioAssay(b2);
    em2.addBioAssay(b3);

    assertTrue(em.dataEquals(em2));

    BioAssay b5 = mMatrix.getColumn("b2");
    assertNotNull(b5);

    ExpressionMatrix em3 = ExpressionMatrixFactory.createExpressionMatrix();
    em3.addDimension(BioAssay.FIELD_NAME_A);

    em3.addBioAssay(b1);
    em3.addBioAssay(b5);
    em3.addBioAssay(b3);
    assertTrue(em.getDefaultDimension().dataEquals(em3.getDefaultDimension()));

  }

  public void testRemoveTheSameIdTwice() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));

    em.addBioAssay(b2);

    em.removeRow("id2");

    try {
      em.removeRow("id2");
    } catch (ExpressionMatrixRuntimeException e) {
      assertTrue(true);
    }

  }

  public void testRemoveTheSameColumnTwice() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));

    em.addBioAssay(b2);

    em.removeColumn("b2");

    try {
      em.removeColumn("b2");

    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(true);
    }
  }

  public void testRemoveThefirstColumnTwice() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));

    em.addBioAssay(b2);
    em.addBioAssay(b3);

    em.removeColumn(0);
    em.removeColumn(0);

    assertTrue(em.getColumnCount() == 1);

  }

  public void testAddTheSameRowTwice() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    assertTrue(("id4").equals("id4"));

    em.addBioAssay(b2);

    em.addRow("id12");

    try {
      em.addRow("id12");
    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(true);
    }
  }

  public void testAddTheSameRowTwice2() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);

    BioAssay b2 = makeBioAssay(ids2, double2, a2);

    BioAssay b3 = makeBioAssay(ids3, double3, a3);

    em.addBioAssay(b1);
    em.addBioAssay(b2);

    double[] id12 = {12.1, 12.2};

    mMatrix.addRow("id12", id12);

    try {
      mMatrix.addRow("id12", id12);
    } catch (ExpressionMatrixRuntimeException e) {

      assertTrue(true);
    }
  }

  public void testAddAndRemoveBioAssay() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    // add b1;
    em.addBioAssay(b1);

    Object[] ids = new String[em.getRowCount()];

    // add b2
    em.addBioAssay(b2);

    assertNotNull(em);
    String idAt2 = "id3";

    assertEquals(em.getColumnCount(), 2);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    assertEquals(3.3, mMatrix.getValue(idAt2, 0), 0);

    // add b3
    em.addBioAssay(b3);

    assertEquals(em.getColumnCount(), 3);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    String idAt3 = "id4";

    assertEquals(em.getColumnCount(), 3);

    // Remove first column

    em.removeColumn(0);

    assertEquals(em.getColumnCount(), 2);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    assertEquals(44.4, mMatrix.getValue(idAt3, 0), 0);
    assertEquals(44.4, mMatrix.getValue(idAt3, "b2"), 0);

    // add b1 & b2.02
    em.addBioAssay(b1);

    assertEquals(em.getColumnCount(), 3);

    em.addBioAssay(b2, "b2.02");

    assertEquals(em.getColumnCount(), 4);
    assertNotSame("b2.02", b2.getName());

    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    String[] idValues = {"id1", "id2", "id3", "id4", "id5", "id7", "id6", "id8"};

    ids = em.getRowNames();
    for (int i = 0; i < ids.length; i++) {
      assertTrue(ids[i].equals(idValues[i]));
    }

    // remove id2
    String[] idValues2 = {"id1", "id3", "id4", "id5", "id7", "id6", "id8"};
    em.removeRow("id2");
    double[] valb1 = mMatrix.getColumnToArray("b1");

    ids = em.getRowNames();
    for (int i = 0; i < ids.length; i++) {
      assertTrue(ids[i].equals(idValues2[i]));
    }

    // remove b3
    em.removeColumn("b3");

    assertEquals(em.getColumnCount(), 3);
    assertEquals(em.getColumnCount(), em.getColumnNames().length);

    em.renameColumn(1, "b1.02");

    assertEquals("b1.02", em.getColumnNames()[1]);

    em.renameColumn("b1.02", "b1.03");

    assertEquals("b1.03", em.getColumnNames()[1]);

    em.renameRow("id1", "id1.02");

    ids = em.getRowNames();
    assertEquals("id1.02", ids[6]);

    mMatrix.setValue("id3", 0, 33.3);
    assertEquals(33.3, mMatrix.getValue("id3", 0), 0);

    mMatrix.setValue("id3", "b2.02", 33.3);
    assertEquals(33.3, mMatrix.getValue("id3", "b2.02"), 0);

    double[] id12 = {12.1, 12.2, 12.3};
    int[] colId12 = {0, 1, 2};

    mMatrix.addRow("id12", id12);

    assertEquals(12.3, mMatrix.getValue("id12", "b2.02"), 0);

    mMatrix.addRow("id12.02", colId12, id12);

    assertEquals(12.3, mMatrix.getValue("id12.02", "b2.02"), 0);
  }

  public void testGetName() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    assertNotNull(em);

    em.setName(null);
    em.setName("ma puce");
    assertEquals("ma puce", em.getName());
  }

  public void testSubMatrixByCol() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);

    int[] col = {0, 2};
    String[] colNames = {"b1", "b3"};

    ExpressionMatrix em2 = em.subMatrixColumns(col);
    assertNotNull(em2);

    ExpressionMatrix em3 = em.subMatrixColumns(colNames);

    assertNotNull(em3);

    assertTrue(em2.dataEquals(em3));

    em.removeColumn("b1");

    assertTrue(em3.getColumnCount() == 1);

  }

  public void testSubMatrixByIds() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);

    String[] ids = {"id3", "id4", "id6"};

    ExpressionMatrix em2 = em.subMatrixRows(ids);
    assertNotNull(em2);
    assertEquals(3, em2.getRowCount());
    assertEquals(3, em2.getColumnCount());

    String[] idsValues = em2.getRowNames();
    for (int i = 0; i < ids.length; i++) {
      assertTrue(ids[i].equals(idsValues[i]));
    }

  }

  public void testConstructMatrixWithTwoIDMA() throws NividicIOException,
      IOException {

    IDMAFileToTest idma1 = new IDMAFileToTest();
    idma1.name = "/files/genepix01_878.gpr_norm.txt";
    idma1.nbFields = 3;
    idma1.nbOptionalheaders = 0;
    idma1.totalNbLines = 23232;
    idma1.nbUndefLines = 960;

    IDMAFileToTest idma2 = new IDMAFileToTest();
    idma2.name = "/files/genepix01_928.gpr_norm.txt";
    idma2.nbFields = 3;
    idma2.nbOptionalheaders = 0;
    idma2.totalNbLines = 23232;
    idma2.nbUndefLines = 960;

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    assertNotNull(em);
    assertEquals(em.getRowCount(), 0);
    assertEquals(em.getColumnCount(), 0);

    // get access to the test file
    String file1 = "/files/genepix01_878.gpr_norm.txt";

    InputStream is = this.getClass().getResourceAsStream(file1);
    assertNotNull(is);
    InputStreamBioAssayReader bar1 = new IDMAReader(is);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_ID);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_M);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_A);
    BioAssay b = bar1.read();
    assertNotNull(b);
    is.close();

    String file2 = "/files/genepix01_928.gpr_norm.txt";

    InputStream is2 = this.getClass().getResourceAsStream(file1);
    assertNotNull(is2);
    InputStreamBioAssayReader bar2 = new IDMAReader(is2);
    bar2.addFieldToRead(BioAssay.FIELD_NAME_ID);
    bar2.addFieldToRead(BioAssay.FIELD_NAME_M);
    bar2.addFieldToRead(BioAssay.FIELD_NAME_A);
    BioAssay b2 = bar2.read();
    assertNotNull(b2);
    is2.close();

    assertNotNull(b.getMs());
    assertNotNull(b2.getMs());
    assertNotNull(b.getAs());
    assertNotNull(b2.getAs());
    em.addBioAssay(b);
    em.addBioAssay(b2);
    assertEquals(em.getColumnCount(), 2);
    assertEquals(em.getRowCount(),
        (idma1.totalNbLines - idma1.nbUndefLines + 1));

  }

  public void testEquals() throws NividicIOException, IOException {

    ExpressionMatrix em1 = constructMatrix();
    ExpressionMatrix em2 = constructMatrix();
    ExpressionMatrix em3 = em1;

    assertTrue(em1.dataEquals(em2));
    assertEquals(em1.hashCode(), em1.hashCode());
    assertEquals(em1.hashCode(), em3.hashCode());
    assertFalse(em1.hashCode() == em2.hashCode());

  }

  public ExpressionMatrix constructMatrix() throws IOException,
      NividicIOException {

    IDMAFileToTest idma1 = new IDMAFileToTest();
    idma1.name = "/files/genepix01_878.gpr_norm.txt";
    idma1.nbFields = 3;
    idma1.nbOptionalheaders = 0;
    idma1.totalNbLines = 23232;
    idma1.nbUndefLines = 960;

    IDMAFileToTest idma2 = new IDMAFileToTest();
    idma2.name = "/files/genepix01_928.gpr_norm.txt";
    idma2.nbFields = 3;
    idma2.nbOptionalheaders = 0;
    idma2.totalNbLines = 23232;
    idma2.nbUndefLines = 960;

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    // get access to the test file
    String file1 = "/files/genepix01_878.gpr_norm.txt";

    InputStream is = this.getClass().getResourceAsStream(file1);
    InputStreamBioAssayReader bar1 = new IDMAReader(is);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_ID);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_M);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_A);
    BioAssay b = bar1.read();
    is.close();

    String file2 = "/files/genepix01_928.gpr_norm.txt";

    InputStream is2 = this.getClass().getResourceAsStream(file2);
    InputStreamBioAssayReader bar2 = new IDMAReader(is2);
    bar2.addFieldToRead(BioAssay.FIELD_NAME_ID);
    bar2.addFieldToRead(BioAssay.FIELD_NAME_M);
    bar1.addFieldToRead(BioAssay.FIELD_NAME_A);
    BioAssay b2 = bar2.read();
    is2.close();

    em.addBioAssay(b, "b1");
    em.addBioAssay(b2, "b2");

    return em;
  }

  public void testCopy() throws NividicIOException, IOException {

    ExpressionMatrix em1 = constructMatrix();

    ExpressionMatrix em2 = ExpressionMatrixFactory.createExpressionMatrix(em1,
        "copy_of_em1");

    assertEquals("copy_of_em1", em2.getName());
    assertTrue(em1.dataEquals(em2));
  }

  public void testRemove() throws NividicIOException, IOException {

    String at1 = "70844";

    ExpressionMatrix em = constructMatrix();

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    ExpressionMatrixDimension aMatrix = em.getDimension(BioAssay.FIELD_NAME_A);

    assertEquals(em.getColumnCount(), 2);

    em.removeColumn(0);
    assertEquals(em.getColumnCount(), 1);

    assertEquals(-0.338, mMatrix.getValue(at1, 0), 0);

  }

  public void testGetColumn() throws NividicIOException, IOException {

    ExpressionMatrix em = constructMatrix();

    BioAssay ba = em.getColumn("b1");

    assertEquals(3, ba.getFieldCount());
    assertTrue(ba.isField(BioAssay.FIELD_NAME_ID));
    assertTrue(ba.isField(BioAssay.FIELD_NAME_M));
    assertTrue(ba.isField(BioAssay.FIELD_NAME_A));

  }

}