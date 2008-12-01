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

package fr.ens.transcriptome.nividic.om.design;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.ens.transcriptome.nividic.om.PhysicalConstants;
import fr.ens.transcriptome.nividic.om.design.io.LimmaDesignReader;
import fr.ens.transcriptome.nividic.om.design.io.LimmaDesignWriter;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.util.NividicUtils;

import junit.framework.TestCase;

public class LimmaDesignIOTest extends TestCase {

  private static final String file1 = "/files/SwirlSample.txt";
  private static final String file2 = "/files/ApoAITargets.txt";
  private static final String file3 = "/files/SwirlSampleScanSettings.txt";

  public void testRead1() throws NividicIOException, FileNotFoundException,
      IOException {

    InputStream is = this.getClass().getResourceAsStream(file3);

    LimmaDesignReader reader = new LimmaDesignReader(is);

    Design d = reader.read();

    // DesignUtils.showDesign(d);

    assertEquals(4, d.size());
    assertTrue(d.isSlide("81"));
    assertTrue(d.isSlide("82"));
    assertTrue(d.isSlide("93"));
    assertTrue(d.isSlide("94"));
    assertFalse(d.isSlide("80"));

    Slide s = d.getSlide(0);
    assertEquals("81", s.getName());
    assertEquals("swirl.1.spot", s.getSourceInfo());
    assertEquals("swirl", s.getTarget(PhysicalConstants.CY3_COLOR));
    assertEquals("wild type", s.getTarget(PhysicalConstants.CY5_COLOR));
    assertEquals(500, s.getScanLabelsSettings().getGreenSettings().getPMTGain());
    assertEquals(50, s.getScanLabelsSettings().getGreenSettings()
        .getScanPower());
    assertEquals(550, s.getScanLabelsSettings().getRedSettings().getPMTGain());
    assertEquals(55, s.getScanLabelsSettings().getRedSettings().getScanPower());
    assertEquals("2001/9/20", s.getDescription().getDate());

    assertEquals("94", d.getSlide(3).getName());

    LimmaDesignWriter writer =
        new LimmaDesignWriter(new File("/tmp/design.txt"));
    writer.write(d);

    assertTrue(NividicUtils.compareFile(this.getClass().getResourceAsStream(
        file3), new FileInputStream("/tmp/design.txt")));

  }

  public void testRead2() throws NividicIOException, FileNotFoundException,
      IOException {

    try {
      LimmaDesignWriter ldw = new LimmaDesignWriter((File) null);
      assertTrue(false);
    } catch (NividicIOException e) {
      assertTrue(true);
    }

    LimmaDesignWriter ldw =
        new LimmaDesignWriter(new FileOutputStream("/tmp/design2.txt"));

    try {

      ldw.write(null);
      assertTrue(false);
    } catch (NullPointerException e) {
      assertTrue(true);
    }

  }

}
