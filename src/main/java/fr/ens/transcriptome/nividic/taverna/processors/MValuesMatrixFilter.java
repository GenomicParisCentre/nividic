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

import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.filters.BioAssayDoubleThresholdFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixRowFilterBioAssayFilterAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;

public class MValuesMatrixFilter implements NividicLocalWorker {

  public String[] inputNames() {
    return new String[] {"matrix", "threshold", "comparator", "absolute"};
  }

  public String[] inputTypes() {
    return new String[] {MATRIX, STRING, STRING, STRING};
  }

  public String[] outputNames() {
    return new String[] {"output"};
  }

  public String[] outputTypes() {
    return new String[] {MATRIX};
  }

  public Map<String, DataThing> execute(Map inputs)
      throws TaskExecutionException {
    try {
      NividicDataThingAdapter ndta = new NividicDataThingAdapter(inputs);

      final ExpressionMatrix matrix = ndta.getExpressionMatrix("matrix");
      final double threshold = Double.parseDouble(ndta.getString("threshold"));
      final String comparator = ndta.getString("comparator");
      final boolean absolute = Boolean.parseBoolean(ndta.getString("absolute"));

      System.out.println("matrix: "+matrix.getBiologicalId());
      System.out.println("threshold: "+threshold);
      System.out.println("comparator: "+comparator);
      System.out.println("absolute: "+absolute);
      
      
      if (matrix == null)
        throw new RuntimeException("No Matrix");

      BioAssayDoubleThresholdFilter bioAssayFilter =
          new BioAssayDoubleThresholdFilter(BioAssay.FIELD_NAME_M, threshold,
              comparator, absolute);

      ExpressionMatrixFilter matrixfilter =
          new ExpressionMatrixRowFilterBioAssayFilterAdapter(bioAssayFilter, 0);

      NividicDataThingAdapter outputs = new NividicDataThingAdapter();
      outputs.putExpressionMatrix("output", matrix.filter(matrixfilter));

      return outputs.getMap();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }

}
