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

package fr.ens.transcriptome.nividic.om.experiment;

import junit.framework.TestCase;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

import fr.ens.transcriptome.nividic.NividicException;
import fr.ens.transcriptome.nividic.om.experiment.Condition;
import fr.ens.transcriptome.nividic.om.experiment.ExperimentDesign;
import fr.ens.transcriptome.nividic.om.experiment.ExperimentParameter;

/**
 * @author Laurent Jourdren
 */
public class ExperiementDesignTest extends TestCase {

  public void testAddParameter() throws NividicException {

    BidiMap bm = new DualHashBidiMap();
    Object o1 = new Object();
    Object o2 = new Object();
    bm.put(o1, o2);

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign();
    ed.addParameter(ep1);
    assertEquals(ed.parametersSize(), 1);

    ExperimentParameter r = ed.getParameter("Time");
    assertNotNull(r);
    assertEquals(r, ep1);

    try {
      ed.addParameter(new ExperimentParameter("Time"));
      assertTrue(false);
    } catch (NividicException e) {
      assertTrue(true);
    }

    ed.addCondition("c1");
    ed.addParameter(ep2);
    assertEquals(ed.get("c1", "Time"), ep1.getType().getDefaultValue());
    assertEquals(ed.get("c1", "Drug"), ep2.getType().getDefaultValue());

  }

  public void testAddCondition() throws NividicException {

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign("toto");
    ed.addParameter(ep1);
    ed.addParameter(ep2);

    assertEquals(ed.conditionsSize(), 0);
    ed.addCondition("c1");
    assertEquals(ed.conditionsSize(), 1);
    ed.addCondition("c2");
    assertEquals(ed.conditionsSize(), 2);

    assertEquals(ed.get("c1", "Time"), ep1.getType().getDefaultValue());
    assertEquals(ed.get("c1", "Drug"), ep2.getType().getDefaultValue());

  }

  public void testRemoveParameterExperimentParameter() throws NividicException {

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign();
    ed.addParameter(ep1);
    ed.addParameter(ep2);
    ed.addCondition("c1");
    ed.addCondition("c2");
    
    assertEquals(ed.getParameter("Time"), ep1);
    assertEquals(ed.parametersSize(), 2);
    ed.removeParameter("Time");
    assertNull(ed.getParameter("Time"));
    assertEquals(ed.parametersSize(), 1);

    try {
      ed.get("c1", "Time");
      assertTrue(false);
    } catch (NividicException e) {
      assertTrue(true);
    }

  }

  /*
   * Class under test for void removeParameter(String)
   */
  public void testRemoveParameterString() throws NividicException {

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign();
    ed.addParameter(ep1);
    ed.addParameter(ep2);
    ed.addCondition("c1");
    ed.addCondition("c2");

    assertEquals(ed.getParameter("Time"), ep1);
    assertEquals(ed.parametersSize(), 2);

    try {
      ed.get("c1", "Time");
      assertTrue(true);
    } catch (NividicException e) {
      assertTrue(false);
    }

    ed.removeParameter(ep1);
    assertNull(ed.getParameter("Time"));
    assertEquals(ed.parametersSize(), 1);

    try {
      ed.get("c1", "Time");
      assertTrue(false);
    } catch (NividicException e) {
      assertTrue(true);
    }

  }

  public void testRemovecondition() throws NividicException {

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign();
    ed.addParameter(ep1);
    ed.addParameter(ep2);
    ed.addCondition("c1");
    ed.addCondition("c2");

    assertTrue(ed.isCondition("c1"));
    assertEquals(ed.conditionsSize(), 2);

    try {
      ed.get("c1", "Time");
      assertTrue(true);
    } catch (NividicException e) {
      assertTrue(false);
    }

    ed.removeCondition("c1");
    assertFalse(ed.isCondition("c1"));
    assertEquals(ed.conditionsSize(), 1);

    try {
      ed.get("c1", "Time");
      assertTrue(false);
    } catch (NividicException e) {
      assertTrue(true);
    }

  }

  public void testSet() throws NividicException {

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign();
    ed.addParameter(ep1);
    ed.addParameter(ep2);
    ed.addCondition("c1");
    ed.addCondition("c2");

    ed.set("c1", "Time", "1");
    ed.set("c1", "Drug", "2");
    ed.set("c2", "Time", "3");
    ed.set("c2", "Drug", "4");

    assertEquals("1", ed.get("c1", "Time"));
    assertEquals("2", ed.get("c1", "Drug"));
    assertEquals("3", ed.get("c2", "Time"));
    assertEquals("4", ed.get("c2", "Drug"));

    ed.set("c1", "Time", "5");
    assertEquals("5", ed.get("c1", "Time"));

  }

  public void testGet() {
  }

  public void testIsCondition() throws NividicException {

    ExperimentParameter ep1 = new ExperimentParameter("Time");
    ExperimentParameter ep2 = new ExperimentParameter("Drug");
    ExperimentDesign ed = new ExperimentDesign();
    ed.addParameter(ep1);
    ed.addParameter(ep2);
    ed.addCondition("c1");
    Condition c2 = new Condition("c2");
    ed.addCondition(c2);

    ed.set("c1", "Time", "1");
    ed.set("c1", "Drug", "2");
    ed.set("c2", "Time", "3");
    ed.set("c2", "Drug", "4");

    assertTrue(ed.isCondition("c1"));
    assertTrue(ed.isCondition(c2));

    ed.removeCondition(c2);

    assertTrue(ed.isCondition("c1"));
    assertFalse(ed.isCondition(c2));

    ed.removeCondition("c1");

    assertFalse(ed.isCondition("c1"));
    assertFalse(ed.isCondition(c2));

  }

  /*
   * Class under test for boolean isParameter(String)
   */
  public void testIsParameterString() {
  }

  /*
   * Class under test for boolean isParameter(ExperimentParameter)
   */
  public void testIsParameterExperimentParameter() {
  }

  public void testParameterIterator() {
  }

  public void testConditionIterator() {
  }

  public void testGetParameters() {
  }

  public void testGetConditions() {
  }

  public void testConditionsSize() {
  }

  public void testParametersSize() {
  }

}