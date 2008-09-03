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
    //gprr.addAllFieldsToRead();
    gprr.addFieldToRead("Dia.");
    gprr.addFieldToRead("F635 SD");
    gprr.addFieldToRead("F532 SD");
    gprr.addFieldToRead("Ratio of Medians (635/532)");

    this.bioAssay = gprr.read();
  }

  public void testAbscentFlagFilter() {

    BioAssayFilter filter = new BioAssayAbscentSpotFilter();

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(ROW_COUNT - 361, this.bioAssay.filter(filter).size());
  }

  public void testAllBadFlagFilter() {

    BioAssayFilter filter = new BioAssayAllBadFlagsFilter();

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(10079, this.bioAssay.filter(filter).size());
  }

  public void testNotFoundFlagFilter() {

    BioAssayFilter filter = new BioAssayNotfoundFeatureFilter();

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(11777, this.bioAssay.filter(filter).size());
  }

  public void testDiameterFilter() {

    BioAssayFilter filter = new BioAssayDiameterFeatureFilter(145, 155);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(ROW_COUNT - 4732, this.bioAssay.filter(filter).size());

  }

  public void testMinDiameterFilter() {

    BioAssayFilter filter = new BioAssayMinDiameterFeatureFilter(145);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(11529, this.bioAssay.filter(filter).size());
  }

  public void testMaxDiameterFilter() {

    BioAssayFilter filter = new BioAssayMaxDiameterFeatureFilter(155);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(6643, this.bioAssay.filter(filter).size());
  }

  public void testHeterogeneousFeatureFilter() {

    BioAssayFilter filter = new BioAssayHeterogeneousFeatureFilter(100);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(9022, this.bioAssay.filter(filter).size());
  }

  public void testMinimalIntensityFilter() {

    BioAssayFilter filter = new BioAssayMinimalIntensityFilter(100);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(9304, this.bioAssay.filter(filter).size());
  }

  public void testMaxmalIntensityFilter() {

    BioAssayFilter filter = new BioAssayMaximalIntensityFilter(10000);

    BioAssay result = this.bioAssay.filter(filter);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(13141, this.bioAssay.filter(filter).size());
  }

  public void testBioAssayIntegerThresholdFilterFilter() {

    BioAssayFilter filter =
        new BioAssayIntegerThresholdFilter(BioAssay.FIELD_NAME_FLAG, ">=", 0);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(10079, this.bioAssay.filter(filter).size());
  }

  public void testBioAssayDoubleThresholdFilterFilter() {

    BioAssayFilter filter =
        new BioAssayDoubleThresholdFilter("Ratio of Medians (635/532)", ">", 1);

    assertEquals(ROW_COUNT, this.bioAssay.size());
    assertEquals(558, this.bioAssay.filter(filter).size());
  }

}
