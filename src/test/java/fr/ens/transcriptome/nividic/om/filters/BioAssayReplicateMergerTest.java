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

import java.io.InputStream;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixUtils;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.io.TotalSummaryReader;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.sgdb.SGDBDescriptionTranslator;
import fr.ens.transcriptome.nividic.sgdb.SGDBYeastBioAssayTranslator;
import junit.framework.TestCase;

public class BioAssayReplicateMergerTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id2"};
  String[] ids2 = {"id1", "id2", "id3", "id2"};
  String[] ids3 = {"id1", "id2", "id3", "id2"};
  String[] ids4 = {"id1", "id2", "id3", "id2"};

  double[] double1 = {1.1, 11.1, 111.1, 1111.1};
  double[] double2 = {2.2, 22.2, Double.NaN, 2222.2};
  double[] double3 = {3.3, 33.3, 333.3, 3333.3};
  double[] double4 = {5.5, 66.6, 777.7, 8888.8};

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

  public void testFilter() {

    BioAssay b1 = makeBioAssay(ids1, double1, a1);
    b1.setName("b1");
    BioAssay b2 = makeBioAssay(ids2, double2, a2);
    b2.setName("b2");
    BioAssay b3 = makeBioAssay(ids3, double3, a3);
    b3.setName("b3");
    BioAssay b4 = makeBioAssay(ids4, double4, a4);
    b4.setName("b4");

    BioAssay[] in = {b1, b2, b3, b4};

    BioAssayReplicateMerger barm = new BioAssayReplicateMerger();

    BioAssay out = barm.filter(in);

    // BioAssayUtils.printBioAssay(out);

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();

    em.addBioAssay(out, "result");

    ExpressionMatrixDimension mMatrix = em.getDefaultDimension();

    assertEquals(2.75, mMatrix.getValue("id1", "result"), 0);
    assertEquals(588.85, mMatrix.getValue("id2", "result"), 0);
    // Error !!!
    //assertEquals(333.3, mMatrix.getValue("id3", "result"), 0);

  }

  public void testGoulpharOutputFiles() throws NividicIOException {

    InputStream is1 = this.getClass().getResourceAsStream(
        "/files/normalised_chip1.txt");
    IDMAReader idmar = new IDMAReader(is1);
    BioAssay norm1 = idmar.read();

    InputStream is2 = this.getClass().getResourceAsStream(
        "/files/normalised_chip1.txt");
    idmar = new IDMAReader(is2);
    BioAssay norm2 = idmar.read();

    Translator translatorIDMA = new SGDBYeastBioAssayTranslator(norm1);

    BioAssayReplicateMerger barm = new BioAssayReplicateMerger();
    barm.setTranslator(translatorIDMA);

    BioAssay result = barm.filter(new BioAssay[] {norm1, norm2});

    InputStream is3 = this.getClass().getResourceAsStream(
        "/files/total.summary.txt");

    TotalSummaryReader tsr = new TotalSummaryReader(is3);

    BioAssay summary = tsr.read();

    SGDBDescriptionTranslator translatorTotalSummary = new SGDBDescriptionTranslator();

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addBioAssay(result, translatorIDMA);
    em.addBioAssay(summary, translatorTotalSummary);

    // ExpressionMatrixUtils.printExpressionMatrix(em);

  }

}
