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

import java.io.File;
import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.InputStreamBioAssayReader;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This class define a processor to read GPR files.
 * @author Laurent Jourdren
 */
public class ReadGPR implements NividicLocalWorker {

  public String[] inputNames() {
    return new String[] {"filename", "readAllFields"};
  }

  public String[] inputTypes() {
    return new String[] {STRING, STRING};
  }

  public String[] outputNames() {
    return new String[] {"output"};
  }

  public String[] outputTypes() {
    return new String[] {BIOASSAY};
  }

  public Map<String, DataThing> execute(Map<String, DataThing> inputs)
      throws TaskExecutionException {
    try {

      NividicDataThingAdapter ndta = new NividicDataThingAdapter(inputs);

      String filename = ndta.getString("filename");

      final boolean readAllFields =
          Boolean.parseBoolean(ndta.getString("readAllFields"));

      if (filename == null)
        throw new TaskExecutionException("No file to read");

      final File f = new File(filename);

      InputStreamBioAssayReader reader = new GPRReader(f);
      if (readAllFields)
        reader.addAllFieldsToRead();

      BioAssay ba = reader.read();
      ba.setName(StringUtils.getFilenameWithoutExtension(f.getName()));

      NividicDataThingAdapter outputs = new NividicDataThingAdapter();
      outputs.putBioAssay("output", ba);

      return outputs.getMap();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new TaskExecutionException(ex);
    }
  }
}
