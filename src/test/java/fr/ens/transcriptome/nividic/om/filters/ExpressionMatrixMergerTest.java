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

import fr.ens.transcriptome.nividic.js.Util;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixUtils;
import junit.framework.TestCase;

public class ExpressionMatrixMergerTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4"};
  String[] ids2 = {"id1", "id2", "id3", "id4"};
  String[] ids3 = {"id1", "id2", "id3", "id4"};
  String[] ids4 = {"id1", "id2", "id3", "id4"};

  double[] double1 = {1.1, 2.2, 3.3, 4.4};
  double[] double2 = {11.1, 44.4, Double.NaN, 77.7};
  double[] double3 = {66.66, 88.88, 11.1, 33.33};
  double[] double4 = {666.6, 888.8, 111.1, 333.3};

  double[] a1 = {11111.1, 22222.2, 33333.3, 444444.4};
  double[] a2 = {111111.1, 444444.4, Double.NaN, 777777.7};
  double[] a3 = {6666666.6, 8888888.8, 1111111.1, 3333333.3};
  double[] a4 = {6668.6, 8888.8, 1111.1, 3333.3};

  private BioAssay makeBioAssay(String[] ids, double[] m, double[] a) {

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
    BioAssay b4 = makeBioAssay(ids3, double3, a3);
    b4.setName("b4");

    em.addBioAssay(b1);
    em.addBioAssay(b2);
    em.addBioAssay(b3);
    em.addBioAssay(b4);

    return em;
  }

  public void testAddExpressionMatrix() {

    ExpressionMatrix em1 = makeExpressionMatrix();

    ExpressionMatrixMerger emm = new ExpressionMatrixMerger();
    emm.addExpressionMatrix(em1);

    ExpressionMatrixUtils.printExpressionMatrix(em1);

    //emm.mergeColumn("b2", new String[] {"b2", "b3"});
    //emm.mergeRow("id1", new String[] {"id1", "id4"});
    
    emm.mergeDimension(BioAssay.FIELD_NAME_M, new String[] {BioAssay.FIELD_NAME_A});

    ExpressionMatrix em2 = emm.getExpressionMatrix();

    ExpressionMatrixUtils.printExpressionMatrix(em2);

    // assertTrue(em1.equals(em2));

    // fail("Not yet implemented");
  }

  public void testMergeRow() {
    // fail("Not yet implemented");
  }

  public void testMergeColumn() {
    // fail("Not yet implemented");
  }

  public void testMergeDimension() {
    // fail("Not yet implemented");
  }

}
