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

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixReader;

public class ExpressionMatrixRowFilterSpeedTest extends TestCase {

  public void testFilter() throws NividicIOException {

    String file1 = "/files/PDR1_huge.txt";

    InputStream is = this.getClass().getResourceAsStream(file1);
    SimpleExpressionMatrixReader reader = new SimpleExpressionMatrixReader(is);

    ExpressionMatrix matrix = reader.read();
    
    //System.out.println(matrix.size() + " rows, "+ matrix.getColumnCount()+ " columns.");

    final double threshold = 1.0;
    final String comparator = "<";
    final boolean absolute = true;

    BioAssayDoubleThresholdFilter bioAssayFilter =
        new BioAssayDoubleThresholdFilter(BioAssay.FIELD_NAME_M, threshold,
            comparator, absolute);

    ExpressionMatrixFilter matrixfilter =
        new ExpressionMatrixRowFilterBioAssayFilterAdapter(bioAssayFilter, 0);

    
    long start = System.currentTimeMillis();

    ExpressionMatrix m2 = matrix.filter(matrixfilter);

    long end = System.currentTimeMillis();

    //System.out.println(m2.size() + " rows, result in " + (end - start) + " ms.");

  }

}
