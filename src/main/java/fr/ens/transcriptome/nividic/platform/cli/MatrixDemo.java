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

package fr.ens.transcriptome.nividic.platform.cli;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.rosuda.JRclient.RSrvException;
import org.rosuda.JRclient.Rconnection;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixRuntimeException;
import fr.ens.transcriptome.nividic.om.io.BioAssayReader;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixWriter;
import fr.ens.transcriptome.nividic.om.r.RExpressionMatrix;

/**
 * Demo class for assembling an ExpressionMatrixDimension from IDMA files
 * @author Lory Montout
 */
public final class MatrixDemo {

  /**
   * Main method.
   * @param args Arguments of the main method
   */
  public static void main(final String[] args) {

    ExpressionMatrix em = readMatrixM(args);
    saveMatrix(em);
    try {
      exec(em);
    } catch (RSrvException e) {
      e.printStackTrace();
    }

    // demoMA(args);
  }

  private static ExpressionMatrix readMatrixM(final String[] args) {
    if (args == null)
      return null;

    File dir = new File(args[0]);
    File[] filesToRead = dir.listFiles(new FileFilter() {

      public boolean accept(final File pathname) {
        return pathname.getName().endsWith(".txt");
      }
    });

    ArrayList readedFiles = new ArrayList(filesToRead.length);

    for (int i = 0; i < filesToRead.length; i++) {

      try {

        // System.out.println("read : " + filesToRead[i].getAbsolutePath());

        // Read idma file
        InputStream is = new FileInputStream(filesToRead[i].getAbsolutePath());

        BioAssayReader bar = new IDMAReader(is);
        bar.addFieldToRead(BioAssay.FIELD_NAME_ID);
        bar.addFieldToRead(BioAssay.FIELD_NAME_BRIGHT);
        bar.addFieldToRead(BioAssay.FIELD_NAME_RATIO);

        BioAssay b = bar.read();
        b.setName((filesToRead[i].toString()).substring(new String("data/")
            .length(), (filesToRead[i].toString().length())
            - (new String(".txt")).length()));

        is.close();

        readedFiles.add(b);

      } catch (FileNotFoundException e) {
        System.err.println("file " + filesToRead[i] + " not found");
      } catch (NividicIOException e) {
        System.err.println("error while reading " + filesToRead[i]);
      } catch (IOException e) {
        System.err.println("error while reading " + filesToRead[i]);
      }
    }

    // Create matrix
    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.setName("MatrixDemo");

    try {

      for (int i = 0; i < readedFiles.size(); i++) {

        em.addBioAssay((BioAssay) readedFiles.get(i));
      }

    } catch (ExpressionMatrixRuntimeException e) {
      System.err.println("error while assembling ");
    }

    return em;
  }

  private static void saveMatrix(final ExpressionMatrix em) {

    String outputFile = "/tmp/testExpressionMatrix.txt";

    try {

      OutputStream os = new FileOutputStream(outputFile);

      SimpleExpressionMatrixWriter emw = new SimpleExpressionMatrixWriter(
          os);

      emw.write(em);
      os.close();

    } catch (FileNotFoundException e) {
      System.err.println("file " + outputFile + " not found");
    } catch (NividicIOException e) {
      System.err.println("error while writing " + em.getName());
    } catch (IOException e) {
      System.err.println("error while writing " + em.getName());
    }

  }

  private static void demoMA(final String[] args) {

    if (args == null)
      return;

    File dir = new File(args[0]);
    File[] filesToRead = dir.listFiles(new FileFilter() {

      public boolean accept(final File pathname) {
        return pathname.getName().endsWith(".txt");
      }
    });

    ArrayList readedFiles = new ArrayList(filesToRead.length);

    for (int i = 0; i < filesToRead.length; i++) {

      try {

        // System.out.println(filesToRead[i].getAbsolutePath());

        // Read idma file
        InputStream is = new FileInputStream(filesToRead[i].getAbsolutePath());

        BioAssayReader bar = new IDMAReader(is);
        bar.addFieldToRead(BioAssay.FIELD_NAME_ID);
        bar.addFieldToRead(BioAssay.FIELD_NAME_BRIGHT);
        bar.addFieldToRead(BioAssay.FIELD_NAME_RATIO);

        BioAssay b = bar.read();
        b.setName((filesToRead[i].toString()).substring(new String("data/")
            .length(), (filesToRead[i].toString().length())
            - (new String(".txt")).length()));

        is.close();

        readedFiles.add(b);

      } catch (FileNotFoundException e) {
        System.err.println("file " + filesToRead[i] + " not found");
      } catch (NividicIOException e) {
        System.err.println("error while reading " + filesToRead[i]);
      } catch (IOException e) {
        System.err.println("error while reading " + filesToRead[i]);
      }
    }

    // Create matrix
    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.setName("MatrixDemo");
    em.addDimension(BioAssay.FIELD_NAME_A);

    try {

      for (int i = 0; i < readedFiles.size(); i++) {
        em.addBioAssay((BioAssay) readedFiles.get(i));
      }

    } catch (ExpressionMatrixRuntimeException e) {
      System.err.println("error while assembling ");
    }

    // Save matrix

    String outputFile2 = "/tmp/testExpressionMatrixMA.txt";

    try {

      OutputStream os2 = new FileOutputStream(outputFile2);

      SimpleExpressionMatrixWriter emMAw = new SimpleExpressionMatrixWriter(
          os2);

      emMAw.write(em);
      os2.close();

    } catch (FileNotFoundException e) {
      System.err.println("file " + outputFile2 + " not found");
    } catch (NividicIOException e) {
      System.err.println("error while writing " + em.getName());
    } catch (IOException e) {
      System.err.println("error while writing " + em.getName());
    }

  }

  /**
   * @param em
   * @throws RSrvException
   */
  private static void exec(final ExpressionMatrix em) throws RSrvException {

    Rconnection con = new Rconnection("127.0.0.1");

    RExpressionMatrix rexp = new RExpressionMatrix();
    rexp.setConnexion(con);
    rexp.setMatrix(em);

    String rName = "em";

    rexp.put(rName);

    rexp.get(rName);

    // System.out.println("equals=" + em.equals(em2));

    // System.out.println(con.eval("names(em[,1:8])"));

    StringBuffer sb = new StringBuffer();

    sb.append("em2=");
    sb.append(rName);
    sb.append("[,1:");
    sb.append("(dim(");
    sb.append(rName);
    sb.append(")[2])");
    // sb.append(em.getColumnCount() + 0);
    sb.append("]");

    // sending instruction

    // System.out.println(sb);
    con.voidEval(sb.toString());

    // cleaning the StringBuffer
    sb = new StringBuffer();

    con.voidEval("em.t=t(as.matrix(em))");

    con.voidEval("em.d=dist(em.t,method = 'manhattan')");

    con.voidEval("postscript('/tmp/DemoMatrix_cluster.eps')");

    con.voidEval("plot(hclust(em.d,method = \"average\"),main = \"experiences "
        + "cluster\",xlab = \"Experiences\",sub = 'hclust(dist"
        + "(*,manhattan),\"average\")')");

    con.voidEval("dev.off()");

  }

  private MatrixDemo() {
  }

}