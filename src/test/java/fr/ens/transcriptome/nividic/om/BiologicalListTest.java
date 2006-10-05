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

import java.util.Iterator;

import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.BiologicalListFactory;

import junit.framework.TestCase;

/**
 * @author Laurent Jourdren
 */
public class BiologicalListTest extends TestCase {

  /*
   * Class under test for void add(String)
   */
  public void testAddString() {

    BiologicalList l = new BiologicalListFactory().createBiologicalList();
    l.add("toto 1");
    assertTrue(l.contains("toto 1"));
    l.add("toto 1");

    assertEquals(l.size(), 1);

  }

  /*
   * Class under test for void add(String[])
   */
  public void testAddStringArray() {

    String a[] = {"toto1 ", "toto2", "toto3"};
    BiologicalList l = new BiologicalListFactory().createBiologicalList();
    l.add(a);

    assertTrue(l.contains(a[0]));
    assertTrue(l.contains(a[1]));
    assertTrue(l.contains(a[2]));

  }

  /*
   * Class under test for void add(BiologicalList)
   */
  public void testAddBiologicalList() {

    String a[] = {"toto1 ", "toto2", "toto3"};
    BiologicalList l1 = new BiologicalListFactory().createBiologicalList();
    l1.add(a);

    BiologicalList l2 = new BiologicalListFactory().createBiologicalList();
    l2.add("toto0");
    l2.add(l1);

    assertTrue(l2.contains(a[0]));
    assertTrue(l2.contains(a[1]));
    assertTrue(l2.contains(a[2]));
    assertTrue(l2.contains("toto0"));

  }

  public void testRemove() {

    BiologicalList l = new BiologicalListFactory().createBiologicalList();

    String s1 = "toto1";
    String s2 = "toto2";

    assertEquals(l.size(), 0);

    l.add(s1);
    l.add(s2);

    assertTrue(l.contains(s1));
    assertTrue(l.contains(s2));
    assertEquals(l.size(), 2);

    l.remove(s1);

    assertEquals(l.size(), 1);
    assertFalse(l.contains(s1));
    assertTrue(l.contains(s2));

  }

  public void testClear() {

    BiologicalList l = new BiologicalListFactory().createBiologicalList();

    l.add("toto 1");
    l.add("toto 2");

    assertEquals(l.size(), 2);
    l.clear();
    assertEquals(l.size(), 0);

  }

  public void testIterator() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l = new BiologicalListFactory().createBiologicalList();
    l.add(a);

    Iterator it = l.iterator();
    assertNotNull(it);

    int count = 0;
    while (it.hasNext()) {
      String value = (String) it.next();
      if (!(value.equals(a[0]) || value.equals(a[1]) || value.equals(a[2])))
        assertTrue(false);
      count++;
    }

    assertEquals(count, 3);

  }

  public void testSize() {

    BiologicalList l = new BiologicalListFactory().createBiologicalList();

    l.add("toto 1");
    l.add("toto 2");

    assertEquals(l.size(), 2);
  }

  public void testToArray() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l = new BiologicalListFactory().createBiologicalList();
    l.add(a);

    String[] r = l.toArray();

    assertNotNull(r);
    assertEquals(r.length, 3);

    for (int i = 0; i < r.length; i++) {
      String value = r[i];
      if (!(value.equals(a[0]) || value.equals(a[1]) || value.equals(a[2])))
        assertTrue(false);
    }

  }

  public void testContains() {

    String a[] = {"toto1 ", "toto2", "toto3"};
    BiologicalList l = new BiologicalListFactory().createBiologicalList();
    l.add(a);

    assertTrue(l.contains(a[0]));
    assertTrue(l.contains(a[1]));
    assertTrue(l.contains(a[2]));
    assertFalse(l.contains("titi"));

  }

  public void testConcat() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l1 = new BiologicalListFactory().createBiologicalList();
    l1.add(a);

    String b[] = {"titi1", "titi2", "titi3"};
    BiologicalList l2 = new BiologicalListFactory().createBiologicalList();
    l2.add(b);

    BiologicalList l3 = l1.concat(l2);

    assertTrue(l1.contains(a[0]));
    assertTrue(l1.contains(a[1]));
    assertTrue(l1.contains(a[2]));
    assertFalse(l1.contains(b[0]));
    assertFalse(l1.contains(b[1]));
    assertFalse(l1.contains(b[2]));

    assertFalse(l2.contains(a[0]));
    assertFalse(l2.contains(a[1]));
    assertFalse(l2.contains(a[2]));
    assertTrue(l2.contains(b[0]));
    assertTrue(l2.contains(b[1]));
    assertTrue(l2.contains(b[2]));

    assertTrue(l3.contains(a[0]));
    assertTrue(l3.contains(a[1]));
    assertTrue(l3.contains(a[2]));
    assertTrue(l3.contains(b[0]));
    assertTrue(l3.contains(b[1]));
    assertTrue(l3.contains(b[2]));

  }

  public void testInclude() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l1 = new BiologicalListFactory().createBiologicalList();
    l1.add(a);

    String b[] = {"toto2", "toto3", "titi4"};
    BiologicalList l2 = new BiologicalListFactory().createBiologicalList();
    l2.add(b);

    BiologicalList l3 = l1.include(l2);

    assertTrue(l1.contains(a[0]));
    assertTrue(l1.contains(a[1]));
    assertTrue(l1.contains(a[2]));
    assertTrue(l1.contains(b[0]));
    assertTrue(l1.contains(b[1]));
    assertFalse(l1.contains(b[2]));

    assertFalse(l2.contains(a[0]));
    assertTrue(l2.contains(a[1]));
    assertTrue(l2.contains(a[2]));
    assertTrue(l2.contains(b[0]));
    assertTrue(l2.contains(b[1]));
    assertTrue(l2.contains(b[2]));

    assertEquals(l3.size(), 2);
    assertFalse(l3.contains(a[0]));
    assertTrue(l3.contains(a[1]));
    assertTrue(l3.contains(a[2]));
    assertTrue(l3.contains(b[0]));
    assertTrue(l3.contains(b[1]));
    assertFalse(l3.contains(b[2]));

  }

  public void testExclude() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l1 = new BiologicalListFactory().createBiologicalList();
    l1.add(a);

    String b[] = {"toto2", "toto3", "titi4"};
    BiologicalList l2 = new BiologicalListFactory().createBiologicalList();
    l2.add(b);

    BiologicalList l3 = l1.exclude(l2);

    assertTrue(l1.contains(a[0]));
    assertTrue(l1.contains(a[1]));
    assertTrue(l1.contains(a[2]));
    assertTrue(l1.contains(b[0]));
    assertTrue(l1.contains(b[1]));
    assertFalse(l1.contains(b[2]));

    assertFalse(l2.contains(a[0]));
    assertTrue(l2.contains(a[1]));
    assertTrue(l2.contains(a[2]));
    assertTrue(l2.contains(b[0]));
    assertTrue(l2.contains(b[1]));
    assertTrue(l2.contains(b[2]));

    assertEquals(l3.size(), 1);
    assertTrue(l3.contains(a[0]));
    assertFalse(l3.contains(a[1]));
    assertFalse(l3.contains(a[2]));
    assertFalse(l3.contains(b[0]));
    assertFalse(l3.contains(b[1]));
    assertFalse(l3.contains(b[2]));

  }

  public void testExcludeAllLists() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l1 = new BiologicalListFactory().createBiologicalList();
    l1.add(a);

    String b[] = {"toto2", "toto3", "titi4"};
    BiologicalList l2 = new BiologicalListFactory().createBiologicalList();
    l2.add(b);

    BiologicalList l3 = l1.excludeAllLists(l2);

    assertTrue(l1.contains(a[0]));
    assertTrue(l1.contains(a[1]));
    assertTrue(l1.contains(a[2]));
    assertTrue(l1.contains(b[0]));
    assertTrue(l1.contains(b[1]));
    assertFalse(l1.contains(b[2]));

    assertFalse(l2.contains(a[0]));
    assertTrue(l2.contains(a[1]));
    assertTrue(l2.contains(a[2]));
    assertTrue(l2.contains(b[0]));
    assertTrue(l2.contains(b[1]));
    assertTrue(l2.contains(b[2]));

    assertEquals(l3.size(), 1);
    assertTrue(l3.contains(a[0]));
    assertFalse(l3.contains(a[1]));
    assertFalse(l3.contains(a[2]));
    assertFalse(l3.contains(b[0]));
    assertFalse(l3.contains(b[1]));
    assertFalse(l3.contains(b[2]));

  }

  /*
   * Class under test for boolean equals(BiologicalList)
   */
  public void testEqualsBiologicalList() {

    String a[] = {"toto1", "toto2", "toto3"};
    BiologicalList l1 = new BiologicalListFactory().createBiologicalList();
    l1.add(a);

    String b[] = {"toto2", "toto3", "titi4"};
    BiologicalList l2 = new BiologicalListFactory().createBiologicalList();
    l2.add(b);

    String c[] = {"toto1 ", "toto2"};
    BiologicalList l3 = new BiologicalListFactory().createBiologicalList();
    l3.add(c);

    String d[] = {"toto1", "toto2", "toto3"};
    BiologicalList l4 = new BiologicalListFactory().createBiologicalList();
    l4.add(d);

    assertFalse(l1.equals(l2));
    assertFalse(l1.equals(l3));
    assertTrue(l1.equals(l4));

  }

}