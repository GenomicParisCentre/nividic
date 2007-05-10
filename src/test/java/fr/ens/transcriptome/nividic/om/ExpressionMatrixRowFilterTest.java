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
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixMFloorRowFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixMThresholdRowFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixNARowFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixRowFilter;

/**
 * JUnit test class for ExpressionMatrixNARowFilter
 * @author Lory Montout
 */
public class ExpressionMatrixRowFilterTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5"};
  String[] ids2 = {"id1", "id4", "id5", "id7"};
  String[] ids3 = {"id6", "id8", "id1", "id3"};

  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5};
  double[] double2 = {1.11, 4.44, Double.NaN, 7.77};
  double[] double3 = {6.666, 8.888, 1.111, 3.333};

  double[] a1 = {1111.1, 2222.2, 3333.3, 4444.4, 5555.5};
  double[] a2 = {11111.1, 44444.4, Double.NaN, 77777.7};
  double[] a3 = {666666.6, 888888.8, 111111.1, 333333.3};

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

  private void printValues(ExpressionMatrixDimension emd) {

    double[][] values = emd.getValues();
    String[] ids = emd.getRowNames();
    String[] columnNames = emd.getColumnNames();

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
        System.out.print(emd.getValue(id, colName));

      }
      System.out.println();

    }
    System.out.println();
  }

  private void printValues(ExpressionMatrix em) {

    printValues(em.getDimension(BioAssay.FIELD_NAME_M));
    printValues(em.getDimension(BioAssay.FIELD_NAME_M));
  }

  private ExpressionMatrix makeExpressionMatrix() {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);

    return em;
  }

  public void testFilter() {

    ExpressionMatrix em = makeExpressionMatrix();
    em.addRow("NAN");
    assertNotNull(em);
    // printValues(em);

    ExpressionMatrixRowFilter emrf = new ExpressionMatrixNARowFilter();

    ExpressionMatrix em2 = emrf.filter(em);
    ExpressionMatrixDimension mMatrix2 = em2.getDefaultDimension();

    ExpressionMatrixDimension dimensions[] = em2.getDimensions();

    final String[] ids = em2.getRowNames();
    for (int i = 0; i < em2.getRowCount(); i++) {
      assertTrue(emrf.testRow(mMatrix2.getRowToArray(ids[i])));
    }
  }

  public void testExpressionMatrixMThresholdRowFilter() {

    ExpressionMatrix em = makeExpressionMatrix();
    em.addRow("NAN");
    assertNotNull(em);
    // printValues(em);

    ExpressionMatrixRowFilter emrf = new ExpressionMatrixMThresholdRowFilter();

    ExpressionMatrix em2 = emrf.filter(em);
    ExpressionMatrixDimension mMatrix2 = em2.getDefaultDimension();

    final String[] ids = em2.getRowNames();
    for (int i = 0; i < em2.getRowCount(); i++) {
      assertTrue(emrf.testRow(mMatrix2.getRowToArray(ids[i])));
    }

  }

  public void testExpressionMatrixMFloorRowFilter() {

    ExpressionMatrix em = makeExpressionMatrix();
    em.addRow("NAN");
    assertNotNull(em);
    // printValues(em);

    ExpressionMatrixRowFilter emrf = new ExpressionMatrixMFloorRowFilter();

    ExpressionMatrix em2 = emrf.filter(em);
    ExpressionMatrixDimension mMatrix2 = em2.getDefaultDimension();

    // printValues(em2);
    final String[] ids = em2.getRowNames();
    for (int i = 0; i < em2.getRowCount(); i++) {
      assertTrue(emrf.testRow(mMatrix2.getRowToArray(ids[i])));
    }

  }

}