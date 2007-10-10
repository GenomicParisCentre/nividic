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
import fr.ens.transcriptome.nividic.om.io.GPRReader;

import junit.framework.TestCase;

public class BioAssayCommonFiltersTest extends TestCase {

  private BioAssay bioAssay;

  private static final String FILE = "/files/testGPR3.gpr";
  private static final int ROW_COUNT = 13440;

  /*
   * (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception {

    InputStream is = this.getClass().getResourceAsStream(FILE);

    GPRReader gprr = new GPRReader(is);
    gprr.addAllFieldsToRead();

    this.bioAssay = gprr.read();
  }

  public void testAbscentFlagFilter() {

    BioAssayFilter filter = new BioAssayAbscentSpotFilter();

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(ROW_COUNT - 361, result.size());
  }

  public void testAllBadFlagFilter() {

    BioAssayFilter filter = new BioAssayAllBadFlagsFilter();

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(10079, result.size());
  }

  public void testNotFoundFlagFilter() {

    BioAssayFilter filter = new BioAssayNotfoundFeatureFilter();

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(11777, result.size());
  }

  public void testDiameterFilter() {

    BioAssayFilter filter = new BioAssayDiameterFeatureFilter(145, 155);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(ROW_COUNT - 4732, result.size());

  }

  public void testMinDiameterFilter() {

    BioAssayFilter filter = new BioAssayMinDiameterFeatureFilter(145);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(11529, result.size());
  }

  public void testMaxDiameterFilter() {

    BioAssayFilter filter = new BioAssayMaxDiameterFeatureFilter(155);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(6643, result.size());
  }

  public void testHeterogeneousFeatureFilter() {

    BioAssayFilter filter = new BioAssayHeterogeneousFeatureFilter(100);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(9022, result.size());
  }

  public void testMinimalIntensityFilter() {

    BioAssayFilter filter = new BioAssayMinimalIntensityFilter(100);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(9304, result.size());
  }

  public void testMaxmalIntensityFilter() {

    BioAssayFilter filter = new BioAssayMaximalIntensityFilter(10000);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(13141, result.size());
  }

}
