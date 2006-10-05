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

package fr.ens.transcriptome.nividic.util;

import fr.ens.transcriptome.nividic.util.Version;
import junit.framework.TestCase;

/**
 * @author Laurent jourdren
 */
public class VersionTest extends TestCase {

  /**
   * Constructor for VersionTest.
   * @param arg0
   */
  public VersionTest(String arg0) {
    super(arg0);
  }

  public void testSetMajor() {
    Version v = new Version();
    v.setMajor(5);

    assertTrue(v.getMajor() == 5);
    v.setMajor(-5);
    assertTrue(v.getMajor() == 0);

  }

  public void testSetMinor() {
    Version v = new Version();
    v.setMinor(3);
    assertTrue(v.getMinor() == 3);
    v.setMinor(-3);
    assertTrue(v.getMinor() == 0);
  }

  public void testSetRevision() {
    Version v = new Version();
    v.setRevision(2);
    assertTrue(v.getRevision() == 2);
    v.setRevision(-2);
    assertTrue(v.getRevision() == 0);
  }

  /*
   * Test for void setVersion(int, int, int)
   */
  public void testSetVersionintintint() {
    Version v = new Version();
    v.setVersion(5, 4, 3);
    assertTrue(v.getMajor() == 5);
    assertTrue(v.getMinor() == 4);
    assertTrue(v.getRevision() == 3);
  }

  /*
   * Test for void setVersion(String)
   */
  public void testSetVersionString() {
    Version v = new Version();
    v.setVersion("5.4.3");
    assertTrue(v.getMajor() == 5);
    assertTrue(v.getMinor() == 4);
    assertTrue(v.getRevision() == 3);

    v = new Version();
    v.setVersion(" 5 . 4 . 3 ");
    assertTrue(v.getMajor() == 5);
    assertTrue(v.getMinor() == 4);
    assertTrue(v.getRevision() == 3);
  }

  public void testEquals() {

    Version v1 = new Version(1, 2, 3);
    Version v2 = new Version(1, 3, 3);
    Version v3 = new Version(1, 2, 3);

    assertFalse(v1.equals(null));
    assertFalse(v1.equals(v2));
    assertTrue(v1.equals(v3));

  }

  public void testCompareTo() {

    Version v1 = new Version(1, 2, 3);
    Version v2 = new Version(1, 3, 3);

    int result = v1.compareTo(v2);

    assertTrue(result < 0);

    result = v2.compareTo(v1);

    assertTrue(result > 0);

  }

  public void testGetMinimalVersion() {

    Version[] versions =
      { new Version("5.3.4"), new Version("1.3.4"), new Version("0.3.4"), new Version("6.3.4")};

    assertNull(Version.getMinimalVersion(null));
    assertNull(Version.getMinimalVersion(new Version[0]));

    Version min = Version.getMinimalVersion(versions);

    assertTrue(min.equals(new Version("0.3.4")));
  }

  public void testGetMaximalVersion() {

    Version[] versions =
      { new Version("5.3.4"), new Version("1.3.4"), new Version("0.3.4"), new Version("6.3.4")};

    assertNull(Version.getMinimalVersion(null));
    assertNull(Version.getMinimalVersion(new Version[0]));

    Version min = Version.getMaximalVersion(versions);

    assertTrue(min.equals(new Version("6.3.4")));
  }


}
