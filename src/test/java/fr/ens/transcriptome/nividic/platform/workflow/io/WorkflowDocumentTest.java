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

package fr.ens.transcriptome.nividic.platform.workflow.io;

import junit.framework.TestCase;

/**
 * @author jourdren
 */
public class WorkflowDocumentTest extends TestCase {

  /**
   * Constructor for WorkflowDocumentTest.
   * @param arg0
   */
  public WorkflowDocumentTest(String arg0) {
    super(arg0);
  }

  public void testWrite() {
    /*
    String filename = "/home/jourdren/toto.xml";

    Workflow wf1 = new Workflow();

    wf1.setName("workflow1");
    wf1.setDescription("description");
    wf1.setOrganisation("ens");
    wf1.setRootElementName("element1");
    wf1.setGenerator("manual");

    WorkflowElement wfe1 = new WorkflowElement();
    wfe1.setId("id1");
    wfe1.setComment("comment");

    String[] ne1 = {"next1", "next2", "next3"};

    try {
      wfe1.link(ne1[0]);
      wfe1.link(ne1[1]);
      wfe1.link(ne1[2]);

      
      
    } catch (PlatformException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
      assertFalse(false);
    }
    
    ModuleQuery mq1 = new ModuleQuery();
    wfe1.setModuleQuery(mq1);
    
    try {

      mq1.setURL(new URL("http://transcriptome.ens.fr/nividic"));
    } catch (MalformedURLException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
      assertFalse(false);
    }
    mq1.setModuleName("modulename");
    mq1.setMinimalVersion(new Version("0.0.1"));
    mq1.setRecommendedVersion(new Version("0.0.2"));
    mq1.setVersion(new Version("0.0.3"));

    try {
      wf1.addElement(wfe1);
    } catch (PlatformException e3) {
      // TODO Auto-generated catch block
      e3.printStackTrace();
    }

    WorkflowIO wio = new WorkflowXMLIO(filename);
    try {
      wio.write(wf1);
    } catch (PlatformException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    WorkflowIO wio2 = new WorkflowXMLIO(filename);
    Workflow wf2 = null;
    try {
      wf2 = wio2.read();
    } catch (PlatformException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    assertNotNull(wf2);

    assertEquals(wf1.getName(), wf2.getName());
    assertEquals(wf1.getDescription(), wf2.getDescription());
    assertEquals(wf1.getOrganisation(), wf2.getOrganisation());
    assertEquals(wf1.getRootElementName(), wf2.getRootElementName());
    assertEquals(wf1.getGenerator(), wf2.getGenerator());

    WorkflowElement wfe2 = wf2.getElement("id1");
    assertNotNull(wfe2);
    ModuleQuery mq2 = wfe2.getModuleQuery();
    assertNotNull(mq2);

    assertEquals(mq1.getURL(), mq2.getURL());
    assertEquals(wfe1.getId(), wfe2.getId());
    assertEquals(mq1.getVersion(), mq2.getVersion());
    assertEquals(mq1.getMinimalVersion(), mq2.getMinimalVersion());
    assertEquals(mq1.getRecommendedVersion(), mq2.getRecommendedVersion());

    String[] ne2 = wfe2.getNextElementsIdentifiers();
    assertNotNull(ne2);
    Arrays.sort(ne1);
    Arrays.sort(ne2);

    for (int i = 0; i < ne2.length; i++) {
      assertEquals(ne1[i], ne2[i]);
    }
    */
  }

}