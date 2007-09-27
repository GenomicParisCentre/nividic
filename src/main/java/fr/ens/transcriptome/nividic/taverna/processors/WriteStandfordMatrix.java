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
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.io.StandfordExpressionMatrixWriter;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;

/**
 * This class define a processor to write expression matrix files at the
 * standford format.
 * @author Laurent Jourdren
 */
public class WriteStandfordMatrix implements NividicLocalWorker {

  public String[] inputNames() {
    return new String[] {"matrix", "filename", "translator"};
  }

  public String[] inputTypes() {
    return new String[] {MATRIX, STRING, TRANSLATOR};
  }

  public String[] outputNames() {
    return new String[] {};
  }

  public String[] outputTypes() {
    return new String[] {};
  }

  public Map<String, DataThing> execute(Map<String, DataThing> inputs)
      throws TaskExecutionException {
    try {

      final NividicDataThingAdapter ndta = new NividicDataThingAdapter(inputs);
      final ExpressionMatrix matrix = ndta.getExpressionMatrix("matrix");
      final String filename = ndta.getString("filename");
      final Translator translator = ndta.getTranslator("translator");

      if (filename == null)
        throw new TaskExecutionException("No file to write");
      if (matrix == null)
        throw new TaskExecutionException("No matrix to write");

      StandfordExpressionMatrixWriter writer =
          new StandfordExpressionMatrixWriter(new File(filename));
      writer.setTranslator(translator);
      writer.write(matrix);

      NividicDataThingAdapter outputs = new NividicDataThingAdapter();

      return outputs.getMap();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new TaskExecutionException(ex);
    }
  }
}
