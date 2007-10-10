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

import junit.framework.TestCase;

public class ExpressionMatrixRowNAFilterTest extends TestCase {

  public void testTestRow() {

    ExpressionMatrixRowNAFilter filter =
        new ExpressionMatrixRowNAFilter(1.0 / 3.0);

    double[] data1 = {0.5, 0.6, 2.0, 0.8, 3.0, 1.0};

    assertTrue(filter.testRow(data1));

    assertTrue(filter.testRow(data1));

    filter = new ExpressionMatrixRowNAFilter(1.0);
    double[] data2 = {1.5, Double.NaN, 2.0, 0.8, Double.NaN, 1.0};
    assertTrue(filter.testRow(data2));

    assertTrue(filter.testRow(data2));
    filter = new ExpressionMatrixRowNAFilter(1.0 / 3.0);
    assertFalse(filter.testRow(data2));
  }

}
