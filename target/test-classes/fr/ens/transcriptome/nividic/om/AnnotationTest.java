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

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.AnnotationFactory;
import junit.framework.TestCase;

/**
 * This class test the annotations.
 * @author Laurent Jourdren
 */
public class AnnotationTest extends TestCase {

  
  String[] ids1 = {"id1", "id2", "id3", "id4", "id5"};
  String[] ids2 = {"id1", "id4", "id3", "id4", "id5"};
  String[] values1 = {"v1", "v2", "v3", "v4", "v5"};
  
  public void testGetAnnotation() {

    Annotation b = AnnotationFactory.createAnnotation();

    b.setProperty(null, null);
    b.setProperty(null, "toot");
    b.setProperty("toot", null);

    for (int i = 0; i < values1.length; i++) {
      b.setProperty(ids1[i], values1[i]);
    }

    for (int i = 0; i < values1.length; i++) {
      assertEquals(values1[i], b.getProperty(ids1[i]));
    }

    for (int i = 0; i < values1.length; i++) {
      b.setProperty(ids1[i], ids2[i]);
    }

    for (int i = 0; i < values1.length; i++) {
      assertEquals(ids2[i], b.getProperty(ids1[i]));
    }

    b.setProperty("toto", "titi");
    assertEquals("titi", b.getProperty("toto"));
    b.removeProperty("toto");
    assertNull(b.getProperty("toto"));

  }

  public void testgetPropertyKeys() {

    Annotation b = AnnotationFactory.createAnnotation();

    assertNotNull(b.getPropertiesKeys());

    for (int i = 0; i < values1.length; i++) {
      b.setProperty(ids1[i], values1[i]);
    }

    String[] keys = b.getPropertiesKeys();

    assertEquals(values1.length, keys.length);
    b.removeProperty(ids1[1]);
    keys = b.getPropertiesKeys();
    assertEquals(values1.length - 1, keys.length);

  }
  
}
