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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixUtils;
import fr.ens.transcriptome.nividic.om.impl.ExpressionMatrixImpl;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixReader;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixWriter;
import fr.ens.transcriptome.nividic.om.translators.DummyTranslator;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * JUnit class for SimpleExpressionMatrixReader and SimpleExpressionMatrixWriter
 * objects
 * @author Lory Montout
 */
public class ExpressionMatrixIOTest extends TestCase {

  String[] ids1 = {"id1", "id2", "id3", "id4", "id5"};
  String[] ids2 = {"id1", "id4", "id5", "id7"};
  String[] ids3 = {"id6", "id8", "id1", "id3"};

  double[] double1 = {1.1, 2.2, 3.3, 4.4, 5.5};
  double[] double2 = {11.1, 44.4, Double.NaN, 77.7};
  double[] double3 = {666.6, 888.8, 111.1, 333.3};

  private BioAssay makeBioAssay(String[] ids, double[] m) {

    BioAssay b = BioAssayFactory.createBioAssay();
    b.setIds(ids);
    b.setMs(m);

    return b;
  }

  private void printValues(ExpressionMatrixDimension em) {

    String[] ids = em.getRowNames();
    String[] columnNames = em.getColumnNames();

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
    System.out.println();
  }

  private ExpressionMatrix makeExpressionMatrix() {

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

    return em;
  }

  public void testWriteAndReadExpressionMatrix() throws NividicIOException,
      IOException {

    ExpressionMatrix em = makeExpressionMatrix();
    assertNotNull(em);

    String outputFile = "/tmp/testExpressionMatrix.txt";

    OutputStream os = new FileOutputStream(outputFile);

    SimpleExpressionMatrixWriter emw = new SimpleExpressionMatrixWriter(os);

    emw.write(em);
    os.close();

    InputStream is = new FileInputStream(outputFile);
    SimpleExpressionMatrixReader emr = new SimpleExpressionMatrixReader(is);
    ExpressionMatrix em2 = emr.read();
    is.close();

    assertTrue(em2.dataEquals(em));

  }

  public void testReadWriteExistingMatrix() throws NividicIOException,
      IOException {

    String file1 = "/files/PDR1gal_ctrred.txt";
    //String file1 = "/files/PDR1_huge.txt";

    InputStream is = this.getClass().getResourceAsStream(file1);
    SimpleExpressionMatrixReader reader = new SimpleExpressionMatrixReader(is);

    ExpressionMatrix matrix = reader.read();

    Translator t = new DummyTranslator(3);

    String outputFile = "/tmp/testExpressionMatrix2.txt";

    OutputStream os = new FileOutputStream(outputFile);

    SimpleExpressionMatrixWriter emw = new SimpleExpressionMatrixWriter(os);
    emw.setTranslator(t);

    emw.write(matrix);
    os.close();

    is = new FileInputStream(outputFile);
    SimpleExpressionMatrixReader emr = new SimpleExpressionMatrixReader(is, 4);
    ExpressionMatrix em2 = emr.read();
    is.close();

    Translator t2 = emr.getTranslator();
    assertNotNull(t2);

    assertTrue(em2.dataEquals(matrix));

    String outputFile2 = "/tmp/testExpressionMatrix3.txt";

    os = new FileOutputStream(outputFile2);

    emw = new SimpleExpressionMatrixWriter(os);
    emw.setTranslator(t2);

    emw.write(em2);
    os.close();

    assertTrue(NividicUtils.compareFile(outputFile, outputFile2));

  }

}