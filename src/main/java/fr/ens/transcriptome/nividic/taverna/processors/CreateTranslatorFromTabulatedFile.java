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
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.om.translators.io.MultiColumnTranslatorReader;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;

/**
 * This class define a processor to create a translator from a tabulated file.
 * @author Laurent Jourdren
 */
public class CreateTranslatorFromTabulatedFile implements NividicLocalWorker {

  public String[] inputNames() {
    return new String[] {"filename"};
  }

  public String[] inputTypes() {
    return new String[] {"'text/plain'"};
  }

  public String[] outputNames() {
    return new String[] {"output"};
  }

  public String[] outputTypes() {
    return new String[] {TRANSLATOR};
  }

  public Map<String, DataThing> execute(Map<String, DataThing> inputs)
      throws TaskExecutionException {
    try {

      NividicDataThingAdapter ndta = new NividicDataThingAdapter(inputs);

      String filename = ndta.getString("filename");

      if (filename == null)
        throw new RuntimeException("No file to read");

      final File f = new File(filename);

      MultiColumnTranslatorReader reader = new MultiColumnTranslatorReader(f);

      Translator t = reader.read();

      NividicDataThingAdapter outputs = new NividicDataThingAdapter();
      outputs.putTranslator("output", t);

      return outputs.getMap();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }
}
