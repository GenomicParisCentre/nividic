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

package fr.ens.transcriptome.nividic.om;

import java.util.Arrays;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.SpotIterator;

import junit.framework.TestCase;

/**
 * @author Laurent Jourdren
 */
public class SpotIteratorTest extends TestCase {

  private static final int LOCATION_DEFAULT_VALUE = 1111;
  private static final int RED_DEFAULT_VALUE = 2222;
  private static final int GREEN_DEFAULT_VALUE = 3333;
  private static final int FLAG_DEFAULT_VALUE = 3333;
  private static final double A_DEFAULT_VALUE = 4444;
  private static final double M_DEFAULT_VALUE = 5555;
  private static final double RATIOS_DEFAULT_VALUE = 9999;
  private static final double BRIGHT_DEFAULT_VALUE = 6666;
  private static final String ID_DEFAULT_VALUE = "7777";
  private static final String DESCRIPTION_DEFAULT_VALUE = "8888";

  private static final int ARRAY_SIZE = 5;

  private BioAssay createBioAssay() {

    final int size = ARRAY_SIZE;

    BioAssay ba = BioAssayFactory.createBioAssay();

    int[] locs = new int[size];
    Arrays.fill(locs, LOCATION_DEFAULT_VALUE);
    ba.setLocations(locs);

    int[] reds = new int[size];
    Arrays.fill(reds, RED_DEFAULT_VALUE);
    ba.setReds(reds);

    int[] greens = new int[size];
    Arrays.fill(greens, GREEN_DEFAULT_VALUE);
    ba.setGreens(greens);

    int[] flags = new int[size];
    Arrays.fill(flags, FLAG_DEFAULT_VALUE);
    ba.setFlags(flags);

    double[] as = new double[size];
    Arrays.fill(as, A_DEFAULT_VALUE);
    ba.setAs(as);

    double[] ms = new double[size];
    Arrays.fill(ms, M_DEFAULT_VALUE);
    ba.setMs(ms);

    double[] brights = new double[size];
    Arrays.fill(brights, BRIGHT_DEFAULT_VALUE);
    ba.setBrights(brights);

    double[] ratios = new double[size];
    Arrays.fill(ratios, RATIOS_DEFAULT_VALUE);
    ba.setRatios(ratios);

    String[] ids = new String[size];
    Arrays.fill(ids, ID_DEFAULT_VALUE);
    ba.setIds(ids);

    String[] descriptions = new String[size];
    Arrays.fill(descriptions, DESCRIPTION_DEFAULT_VALUE);
    ba.setDescriptions(descriptions);

    return ba;
  }

  public void testHasNext() {

    BioAssay ba = createBioAssay();

    SpotIterator si = ba.iterator();

    try {
      si.getId();
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }

    int i = 0;
    while (si.hasNext()) {
      si.next();
      assertEquals(i, si.getIndex());
      si.getId();
      i++;
    }
    assertEquals(ba.size(), i--);
  }

  public void testHasPrevious() {

    BioAssay ba = createBioAssay();

    SpotIterator si = ba.iterator();

    try {
      si.getId();
      assertTrue(false);
    } catch (BioAssayRuntimeException e) {
      assertTrue(true);
    }

    si.last();

    int i = ba.size()-1;
    while (si.hasPrevious()) {

      assertEquals(i, si.getIndex());
      si.getId();
      i--;
      si.previous();
    }
    assertEquals(0, i);
  }

}