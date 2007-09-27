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

package fr.ens.transcriptome.nividic.taverna.processors;

import java.io.File;
import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.GPRWriter;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;

/**
 * This class define a processor to write GPR files.
 * @author Laurent Jourdren
 */
public class WriteGPR implements NividicLocalWorker {

  /**
   * Get the input names of the processor.
   * @return an array of Strings
   */
  public String[] inputNames() {
    return new String[] {"bioAssay", "filename", "writeAllFields"};
  }

  /**
   * Get the input types of the processor.
   * @return an array of Strings
   */
  public String[] inputTypes() {
    return new String[] {BIOASSAY, STRING, STRING};
  }

  /**
   * Get the output names of the processor.
   * @return an array of Strings
   */
  public String[] outputNames() {
    return new String[] {};
  }

  /**
   * Get the output types of the processor.
   * @return an array of Strings
   */
  public String[] outputTypes() {
    return new String[] {};
  }

  /**
   * Main method of the processor.
   * @throws TaskExecutionException if error occurs while executing the
   *             processor
   */
  public Map<String, DataThing> execute(Map<String, DataThing> inputs)
      throws TaskExecutionException {
    try {

      NividicDataThingAdapter ndta = new NividicDataThingAdapter(inputs);
      BioAssay bioAssay = ndta.getBioAssay("bioAssay");
      String filename = ndta.getString("filename");

      final boolean writeAllFields =
          Boolean.parseBoolean(ndta.getString("writeAllFields"));

      if (filename == null)
        throw new TaskExecutionException("No file to read");

      GPRWriter writer = new GPRWriter(new File(filename));
      if (writeAllFields)
        writer.addAllFieldsToWrite();
      writer.write(bioAssay);

      NividicDataThingAdapter outputs = new NividicDataThingAdapter();

      return outputs.getMap();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new TaskExecutionException(ex);
    }
  }
}
