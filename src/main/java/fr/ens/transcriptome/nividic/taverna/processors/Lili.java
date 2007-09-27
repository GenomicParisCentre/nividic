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

package fr.ens.transcriptome.nividic.taverna.processors;

import java.util.HashMap;
import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;

public class Lili implements NividicLocalWorker {

  public String[] inputNames() {
      return new String[] { "string1", "string2" };
  }

  public String[] inputTypes() {
      return new String[] { "'text/plain'", "'text/plain'" };
  }

  public String[] outputNames() {
      return new String[] { "output" };
  }

  public String[] outputTypes() {
      return new String[] { "'text/plain'" };
  }

  public Map<String, DataThing> execute(Map inputs) throws TaskExecutionException {
      try {
          String firstString = (String) ((DataThing) inputs.get("string1")).getDataObject();
          String secondString = (String) ((DataThing) inputs.get("string2")).getDataObject();
          Map<String, DataThing> outputs = new HashMap<String, DataThing>();
          outputs.put("output", new DataThing(firstString + this.getClass().getName() + secondString));
          return outputs;
      } catch (Exception ex) {
          ex.printStackTrace();
          throw new RuntimeException(ex);
      }
  }

}

