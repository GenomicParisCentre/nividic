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
import java.io.FileInputStream;
import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.ImaGeneOutputFileReader;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This class define a processor to read ImaGene files.
 * @author Laurent Jourdren
 */
public class ReadImaGene implements NividicLocalWorker {

  public String[] inputNames() {
    return new String[] {"greenFilename", "redFilename", "readAllFields"};
  }

  public String[] inputTypes() {
    return new String[] {STRING, STRING, STRING};
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

      String filenameGreen = ndta.getString("greenFilename");
      String filenameRed = ndta.getString("redFilename");

      final boolean readAllFields =
          Boolean.parseBoolean(ndta.getString("readAllFields"));

      if (filenameGreen == null)
        throw new TaskExecutionException("No green file to read");

      if (filenameRed == null)
        throw new TaskExecutionException("No red file to read");

      final File greenFile = new File(filenameGreen);
      final File redFile = new File(filenameRed);

      ImaGeneOutputFileReader reader =
          new ImaGeneOutputFileReader(new FileInputStream(greenFile),
              new FileInputStream(redFile));

      if (readAllFields)
        reader.addAllFieldsToRead();

      BioAssay ba = reader.read();
      ba.setName(StringUtils.getFilenameWithoutExtension(greenFile.getName()));

      NividicDataThingAdapter outputs = new NividicDataThingAdapter();
      outputs.putBioAssay("output", ba);

      return outputs.getMap();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new TaskExecutionException(ex);
    }
  }
}
