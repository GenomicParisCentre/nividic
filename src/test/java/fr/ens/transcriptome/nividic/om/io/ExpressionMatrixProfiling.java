/*
 * ExpressionMatrixProfiling.java
 *
 * Created on 7 mars 2007, 09:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fr.ens.transcriptome.nividic.om.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;

/**
 * @author jourdren
 */
public class ExpressionMatrixProfiling extends TestCase {

  public void testProfiling() throws NividicIOException, FileNotFoundException {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip1.txt"));
    em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip2.txt"));
    // em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip3.txt"));
    // em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip4.txt"));
    // em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip5.txt"));
    // em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip6.txt"));
    // em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip7.txt"));
    // em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip8.txt"));

    System.out.println("nb dimensions: " + em.getDimensionCount());
    System.out.println("nb row: " + em.getRowCount());
    System.out.println("nb col: " + em.getColumnCount());

    File file = new File("/home/jourdren/matrix/profiling.txt");

    SimpleExpressionMatrixWriter emw = new SimpleExpressionMatrixWriter(file);

    long start = System.currentTimeMillis();
    emw.write(em);
    long end = System.currentTimeMillis();

    System.out.println("Time: " + (end - start));

    file.delete();

  }

  public void testProfiling2() throws NividicIOException, FileNotFoundException {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip1.txt"));
    em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip2.txt"));

    final String[] rowNames = em.getRowNames();
    final String[] colNames = em.getColumnNames();
    final String[] dimNames = em.getDimensionNames();

    Random ran = new Random(System.currentTimeMillis());

    final int randomCount = 1000000;

    // get col>row

    long start = System.currentTimeMillis();
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < colNames.length; j++)
        for (int k = 0; k < rowNames.length; k++) {
          dim.getValue(rowNames[k], colNames[j]);
        }
    }
    long end = System.currentTimeMillis();

    System.out.println("get col>row: " + (end - start) + " ms.");

    // get row>col

    start = System.currentTimeMillis();
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < rowNames.length; j++)
        for (int k = 0; k < colNames.length; k++) {
          dim.getValue(rowNames[j], colNames[k]);
        }
    }
    end = System.currentTimeMillis();

    System.out.println("get row>col: " + (end - start) + " ms.");

    // get random row>col

    start = System.currentTimeMillis();
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < randomCount; j++) {
        int l = ran.nextInt(rowNames.length);
        int k = ran.nextInt(colNames.length);
        dim.getValue(rowNames[l], colNames[k]);
      }
    }
    end = System.currentTimeMillis();

    System.out.println("get random("
        + randomCount + "): " + (end - start) + " ms.");

    // get col>row

    start = System.currentTimeMillis();
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < colNames.length; j++)
        for (int k = 0; k < rowNames.length; k++) {
          dim.setValue(rowNames[k], colNames[j], 75.6);
        }
    }
    end = System.currentTimeMillis();

    System.out.println("set loop col>row: " + (end - start) + " ms.");

    // get row>col

    start = System.currentTimeMillis();
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < rowNames.length; j++)
        for (int k = 0; k < colNames.length; k++) {
          dim.setValue(rowNames[j], colNames[k], 75.6);
        }
    }
    end = System.currentTimeMillis();

    System.out.println("set loop row>col: " + (end - start) + " ms.");

    // get random row>col

    start = System.currentTimeMillis();
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < randomCount; j++) {
        int l = ran.nextInt(rowNames.length);
        int k = ran.nextInt(colNames.length);
        dim.setValue(rowNames[l], colNames[k], 75.6);
      }
    }
    end = System.currentTimeMillis();

    System.out.println("set random ("
        + randomCount + "): " + (end - start) + " ms.");

    start = System.currentTimeMillis();

    // Add Rows
    final int addRows = 100000;

    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < addRows; j++) {

        dim.addRow("toti" + j + ran.nextInt());
      }
    }
    end = System.currentTimeMillis();

    System.out
        .println("add rows  (" + addRows + "): " + (end - start) + " ms.");

    // Add column

    em.getDefaultDimension().setValue(rowNames[0], colNames[0], 1234.56789);
    double valBefore =
        em.getDefaultDimension().getValue(rowNames[0], colNames[0]);

    final int addColumn = 30;
    for (int i = 0; i < dimNames.length; i++) {

      final ExpressionMatrixDimension dim = em.getDimension(dimNames[i]);

      for (int j = 0; j < addColumn; j++) {

        em.addColumn("toto" + j);
      }
    }
    end = System.currentTimeMillis();

    System.out.println("add column("
        + addColumn + "): " + (end - start) + " ms.");

    double valAfter =
        em.getDefaultDimension().getValue(rowNames[0], colNames[0]);
    System.out.println("value=" + valBefore + "\t" + valAfter);
    System.out.println(em.getColumnCount() * em.getDimensionCount());
  }

  private void createHudgeMarix(File file) throws IOException {

    final int lengthIds = 12;
    final int columnCount = 10;
    final int rowCount = 50000;

    BufferedWriter bw =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
            "ISO-8859-1"));

    bw.write("ID");
    for (int i = 0; i < columnCount; i++) {
      bw.write("\tcolumn");
      bw.write(Integer.toString(i));
    }

    bw.write("\n");

    Random ran = new Random(System.currentTimeMillis());

    for (int i = 0; i < rowCount; i++) {

      bw.write("ident_");
      bw.write(Integer.toHexString(i));

      for (int j = 0; j < columnCount; j++) {
        bw.write("\t");
        bw.write(Double.toString(ran.nextDouble()));
      }

      bw.write("\n");
    }

    bw.close();

  }

  public void testReadHudgeMatrix() throws NividicIOException, IOException {

    File file = new File("/home/jourdren/matrix/profiling_hudge.txt");

    createHudgeMarix(file);

    SimpleExpressionMatrixReader reader =
        new SimpleExpressionMatrixReader(file);

    ExpressionMatrix matrix = reader.read();

    System.out.println("row: "
        + matrix.size() + "\tcolumns: " + matrix.getColumnCount());

  }

  public BioAssay readIDMA(String filename) throws NividicIOException,
      FileNotFoundException {

    InputStream is = new FileInputStream(filename);
    InputStreamBioAssayReader bar = new IDMAReader(is);

    BioAssay ba = bar.read();

    // ba.setName(filename);
    System.out.println("" + ba.size() + "\t" + ba.getName());

    return ba;

  }

  public static void main(String[] args) throws Exception {

    // new ExpressionMatrixProfiling().testProfiling();
    new ExpressionMatrixProfiling().testReadHudgeMatrix();
  }

}
