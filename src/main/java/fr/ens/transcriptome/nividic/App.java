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

package fr.ens.transcriptome.nividic;

import fr.ens.transcriptome.nividic.platform.Platform;
import fr.ens.transcriptome.nividic.platform.PlatformException;
import fr.ens.transcriptome.nividic.platform.module.ModuleLocation;
import fr.ens.transcriptome.nividic.platform.module.ModuleManager;
import fr.ens.transcriptome.nividic.platform.workflow.Workflow;
import fr.ens.transcriptome.nividic.platform.workflow.io.WorkflowIO;
import fr.ens.transcriptome.nividic.platform.workflow.io.WorkflowXMLIO;
import fr.ens.transcriptome.nividic.util.parameter.ParameterException;

/**
 * Hello world!
 * @author Laurent Jourdren </a>
 */
public final class App {

  /**
   * Main method,
   * @param args arguments
   */
  public static void main(final String[] args) {

    try {

      // Start the platform
      Platform.start();

      // List available modules
      ModuleLocation[] ml = ModuleManager.getManager().getListModules();
      for (int i = 0; i < ml.length; i++) {
        System.out.println(ml[i].getName());
      }

      // Describe the workflow
      //SimpleWorkflowBuilder swb = new SimpleWorkflowBuilder();
      /*
       * Workflow w = swb .addElement("LoadGPRNames")
       * .addElement("TranslateGPRToBioAssay") .addElement("ComputeRatios")
       * .addElement("WriteRomdeOMToGPRFile") .getWorkflow();
       */

      WorkflowIO wfio = new WorkflowXMLIO("/home/jourdren/wf1.xml");
      //wfio.write(w);
      Workflow w = wfio.read();

      // set the parameter for the first algorithm
      w.getRootElement().getParameters().setParameter("files",
          "/home/jourdren/data/1.gpr");

      // activate the workflow
      w.activate();

      // start the workflow
      new Thread(w).start();

    } catch (PlatformException e) {

      System.out.println("error : " + e.getMessage());
      e.printStackTrace();
    } catch (ParameterException e) {
      System.out.println("Parameter error : " + e.getMessage());
      e.printStackTrace();
    }

    /*
     * try { final StartAlgorithm e1 = new LoadGPRNamesElement(); final
     * FilterAlgorithm e2 = new TranslateGPRToBioAssayElement(); final
     * FilterAlgorithm e3 = new ComputeRatiosElement(); final EndAlgorithm e4 =
     * new WriteBioAssayOMToGPRFileElement(); e1.out().chain(e2.in());
     * e2.out().chain(e3.in()); e3.out().chain(e4.in());
     * e1.getParameters().setParameter("files",
     * "/home/toto/romde/src/test/files/testGPR14.gpr"); e1.start();
     * e1.getParameters().setParameter("files",
     * "/home/toto/romde/src/test/files/testGPR3.gpr"); e1.start(); } catch
     * (PlatformException e) { System.err.println("Error in the workflow : " +
     * e.getMessage()); } catch (ParameterException e) { // TODO Auto-generated
     * catch block NividicUtils.nop(); }
     */

  }

  private App() {
  }

}
