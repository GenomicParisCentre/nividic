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

package fr.ens.transcriptome.nividic.om.filters;

import java.io.File;
import java.util.Arrays;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixUtils;
import fr.ens.transcriptome.nividic.om.io.ExpressionMatrixWriter;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixWriter;
import fr.ens.transcriptome.nividic.om.translators.MultiColumnTranslator;

public class ExpressionMatrixMergerTest extends TestCase {

  private static final String[] ids1 = {"id1", "id2", "id3", "id4"};
  private static final String[] ids2 = {"id1", "id2", "id3", "id4"};
  private static final String[] ids3 = {"id1", "id2", "id3", "id4"};
  private static final String[] ids4 = {"id1", "id2", "id3", "id4"};

  private static final double[] double1 = {1.1, 2.2, 3.3, 4.4};
  private static final double[] double2 = {11.1, 44.4, Double.NaN, 77.7};
  private static final double[] double3 = {66.66, 88.88, 11.1, 33.33};
  private static final double[] double4 = {666.6, 888.8, 111.1, 333.3};

  private static final double[] a1 = {11111.1, 22222.2, 33333.3, 444444.4};
  private static final double[] a2 = {111111.1, 444444.4, Double.NaN, 777777.7};
  private static final double[] a3 = {6666666.6, 8888888.8, 1111111.1,
      3333333.3};
  private static final double[] a4 = {6668.6, 8888.8, 1111.1, 3333.3};

  private static final String[][] translatorData = { {"id1", "newid1"},
      {"id2", "newid2"}, {"id3", "newid2"}, {"id4", "id1"},};

  private BioAssay makeBioAssay(final String[] ids, final double[] m,
      final double[] a) {

    BioAssay b = BioAssayFactory.createBioAssay();
    b.setIds(ids);
    b.setMs(m);
    b.setAs(a);

    return b;
  }

  private ExpressionMatrix makeExpressionMatrix() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    // ExpressionMatrixDimension mMatrix = em.getDefaultDimension();
    // ExpressionMatrixDimension aMatrix =
    // em.getDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");
    BioAssay b4 = makeBioAssay(ids4, double4, a4);
    b4.setName("b4");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);
    em.addBioAssay(b4);

    return em;
  }

  private double mean(final double a, final double b) {

    return (a + b) / 2;
  }

  public void testAddExpressionMatrix() {

    ExpressionMatrix em1 = makeExpressionMatrix();

    ExpressionMatrixMerger emm = new ExpressionMatrixMerger();
    emm.addMatrix(em1);

    emm.mergeDimensions(BioAssay.FIELD_NAME_M,
        new String[] {BioAssay.FIELD_NAME_A});

    ExpressionMatrix em2 = emm.getMatrix();

    assertNotNull(em2);
  }

  public void testMergeRow() {

    ExpressionMatrix em1 = makeExpressionMatrix();
    ExpressionMatrixMerger emm = new ExpressionMatrixMerger();
    emm.addMatrix(em1);
    emm.mergeRows(new String[] {"id1", "id2"});

    ExpressionMatrix em2 = emm.getMatrix();

    assertEquals(3, em2.getRowCount());
    assertTrue(em2.containsRow("id1"));
    assertTrue(em2.containsRow("id3"));
    assertTrue(em2.containsRow("id4"));

    // ExpressionMatrixUtils.printExpressionMatrix(em1);
    // ExpressionMatrixUtils.printExpressionMatrix(em2);

    ExpressionMatrixDimension dimM1 = em1.getDimension(BioAssay.FIELD_NAME_M);
    ExpressionMatrixDimension dimM2 = em2.getDimension(BioAssay.FIELD_NAME_M);

    // ExpressionMatrixUtils.printExpressionMatrixDimension(dimM1);
    // ExpressionMatrixUtils.printExpressionMatrixDimension(dimM2);

    double[] row = dimM2.getRowToArray("id1");

    assertTrue(Arrays.equals(dimM1.getRowToArray("id3"), dimM2
        .getRowToArray("id3")));
    assertTrue(Arrays.equals(dimM1.getRowToArray("id4"), dimM2
        .getRowToArray("id4")));

    assertEquals(row[0], mean(1.1, 2.2));
    assertEquals(row[1], mean(11.1, 44.4));
    assertEquals(row[2], mean(66.66, 88.88));
    assertEquals(row[3], mean(666.6, 888.8));

  }

  public void testMergeRowTranslator() {

    ExpressionMatrix em1 = makeExpressionMatrix();
    ExpressionMatrixMerger emm = new ExpressionMatrixMerger();
    emm.addMatrix(em1);

    MultiColumnTranslator translator = new MultiColumnTranslator(new String[] {
        "id", "newId"});

    for (int i = 0; i < translatorData.length; i++) {
      translator.addRow(translatorData[i]);
      // System.out.println(Arrays.toString(translatorData[i]));
    }

    emm.mergeRows(translator);

    ExpressionMatrix em2 = emm.getMatrix();

    assertEquals(3, em2.getRowCount());
    assertTrue(em2.containsRow("newid1"));
    assertTrue(em2.containsRow("newid2"));
    assertTrue(em2.containsRow("id1"));

    // ExpressionMatrixUtils.printExpressionMatrix(em1);
    // ExpressionMatrixUtils.printExpressionMatrix(em2);

    ExpressionMatrixDimension dimM1 = em1.getDimension(BioAssay.FIELD_NAME_M);
    ExpressionMatrixDimension dimM2 = em2.getDimension(BioAssay.FIELD_NAME_M);

    // ExpressionMatrixUtils.printExpressionMatrixDimension(dimM1);
    // ExpressionMatrixUtils.printExpressionMatrixDimension(dimM2);

    double[] row = dimM2.getRowToArray("newid2");

    assertTrue(Arrays.equals(dimM1.getRowToArray("id1"), dimM2
        .getRowToArray("newid1")));
    assertTrue(Arrays.equals(dimM1.getRowToArray("id4"), dimM2
        .getRowToArray("id1")));

    assertEquals(row[0], mean(2.2, 3.3), 0.1);
    assertEquals(row[1], 44.4);
    assertEquals(row[2], mean(88.88, 11.1), 0.1);
    assertEquals(row[3], mean(888.8, 111.1), 0.1);

  }

  public void testMergeColumn() {

    ExpressionMatrix em1 = makeExpressionMatrix();
    ExpressionMatrixMerger emm = new ExpressionMatrixMerger();
    emm.addMatrix(em1);
    emm.mergeColumns(new String[] {"b2", "b3"});

    ExpressionMatrix em2 = emm.getMatrix();

    assertEquals(3, em2.getColumnCount());
    assertTrue(em2.containsColumn("b1"));
    assertTrue(em2.containsColumn("b2"));
    assertTrue(em2.containsColumn("b4"));

    ExpressionMatrixDimension dimM1 = em1.getDimension(BioAssay.FIELD_NAME_M);
    ExpressionMatrixDimension dimM2 = em2.getDimension(BioAssay.FIELD_NAME_M);

    assertTrue(Arrays.equals(dimM1.getColumnToArray("b1"), dimM2
        .getColumnToArray("b1")));
    assertTrue(Arrays.equals(dimM1.getColumnToArray("b4"), dimM2
        .getColumnToArray("b4")));

    double[] col = em2.getDimension(BioAssay.FIELD_NAME_M).getColumnToArray(
        "b2");

    assertEquals(col[0], mean(11.1, 66.66));
    assertEquals(col[1], mean(44.4, 88.88));
    assertEquals(col[2], 11.1);
    assertEquals(col[3], mean(77.7, 33.33));

  }

  public void testMergeDimension() {

    ExpressionMatrix em1 = makeExpressionMatrix();
    em1.addDimension("t");
    ExpressionMatrixMerger emm = new ExpressionMatrixMerger();
    emm.addMatrix(em1);

    emm.mergeDimensions(new String[] {"m", "a"});

    ExpressionMatrix em2 = emm.getMatrix();

    assertEquals(3, em1.getDimensionCount());
    assertEquals(2, em2.getDimensionCount());
    assertTrue(em2.containsDimension("m"));
    assertTrue(em2.containsDimension("t"));

    ExpressionMatrixDimension dimM1 = em1.getDimension(BioAssay.FIELD_NAME_M);
    ExpressionMatrixDimension dimA1 = em1.getDimension(BioAssay.FIELD_NAME_A);
    ExpressionMatrixDimension dimM2 = em2.getDimension(BioAssay.FIELD_NAME_M);

    // ExpressionMatrixUtils.printExpressionMatrix(em2);

    double[] rowM1 = dimM1.getRowToArray("id1");
    double[] rowA1 = dimA1.getRowToArray("id1");
    double[] rowM2 = dimM2.getRowToArray("id1");

    assertEquals(rowM2[0], mean(rowM1[0], rowA1[0]), 0.1);
    assertEquals(rowM2[1], mean(rowM1[1], rowA1[1]), 0.1);
    assertEquals(rowM2[2], mean(rowM1[2], rowA1[2]), 0.1);
    assertEquals(rowM2[3], mean(rowM1[3], rowA1[3]), 0.1);

    double[] rowT2 = em2.getDimension("t").getRowToArray("id1");

    assertEquals(rowT2[0], Double.NaN);
    assertEquals(rowT2[1], Double.NaN);
    assertEquals(rowT2[2], Double.NaN);
    assertEquals(rowT2[3], Double.NaN);

  }

  public void testMergeBioAssay() throws NividicIOException {

    final String[] ids1 = {"id1", "id2", "id3", "id2"};
    final String[] ids2 = {"id1", "id2", "id3", "id2"};

    final double[] m1 = {1.1, 2.2, 3.3, 4.4};
    final double[] m2 = {11.1, 44.4, Double.NaN, 77.7};

    final double[] a1 = {11111.1, 22222.2, 33333.3, 444444.4};
    final double[] a2 = {111111.1, 444444.4, Double.NaN, 777777.7};

    final BioAssay ba1 = makeBioAssay(ids1, m1, a1);
    final BioAssay ba2 = makeBioAssay(ids1, m1, a1);

    ba1.setName("ba1");
    ba2.setName("ba2");

    ExpressionMatrixMerger merger = new ExpressionMatrixMerger();

    merger.addDimension(BioAssay.FIELD_NAME_M);
    merger.addDimension(BioAssay.FIELD_NAME_A);

    merger.addBioAssay(ba1);
    merger.addBioAssay(ba2);

    merger.setAddStatData(true);

    ExpressionMatrix matrix = merger.getMatrix();

    assertNotNull(matrix);
    assertEquals(3, matrix.getRowCount());

    ExpressionMatrixDimension dim = matrix.getDimension("m n");
    assertEquals(2.0, dim.getValue("id2", "ba1"));

    dim = matrix.getDimension("m");
    assertEquals(3.3, dim.getValue("id2", "ba1"), 0.01);
  }

}
