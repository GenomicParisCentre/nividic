/*
 * ExpressionMatrixProfiling.java
 *
 * Created on 7 mars 2007, 09:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fr.ens.transcriptome.nividic.om.io;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * @author jourdren
 */
public class ExpressionMatrixProfiling extends TestCase {

  public void testProfiling() throws NividicIOException, FileNotFoundException {

    ExpressionMatrix em = ExpressionMatrixFactory.createExpressionMatrix();
    em.addDimension(BioAssay.FIELD_NAME_A);

    em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip1.txt"));
    em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip2.txt"));
    //em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip3.txt"));
    //em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip4.txt"));
    //em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip5.txt"));
    //em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip6.txt"));
    //em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip7.txt"));
    //em.addBioAssay(readIDMA("/home/jourdren/matrix/normalised_chip8.txt"));
    

    System.out.println("nb dimensions: " + em.getDimensionCount());
    System.out.println("nb row: " + em.getRowCount());
    System.out.println("nb col: " + em.getColumnCount());

    SimpleExpressionMatrixWriter emw = new SimpleExpressionMatrixWriter(
        new File("/home/jourdren/matrix/profiling.txt"));

    long start = System.currentTimeMillis();
    emw.write(em);
    long end = System.currentTimeMillis();

    System.out.println("Time: " + (end - start));

  }

  public BioAssay readIDMA(String filename) throws NividicIOException,
      FileNotFoundException {

    InputStream is = new FileInputStream(filename);
    BioAssayReader bar = new IDMAReader(is);

    BioAssay ba = bar.read();

    // ba.setName(filename);
    System.out.println("" + ba.size() + "\t" + ba.getName());

    return ba;

  }

  public static void main (String []  args) throws Exception{
      
      new ExpressionMatrixProfiling().testProfiling();
  }
  
}
