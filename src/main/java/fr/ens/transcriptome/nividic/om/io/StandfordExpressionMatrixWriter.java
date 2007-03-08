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

package fr.ens.transcriptome.nividic.om.io;

import java.io.File;
import java.io.OutputStream;

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class define a writer for expression matrix at the Standford format.
 * @author Laurent Jourdren
 */
public class StandfordExpressionMatrixWriter extends
    SimpleExpressionMatrixWriter {

  private void config() {

    addDimensionToWrite(BioAssay.FIELD_NAME_M);
    setShowDimensionName(false);
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public StandfordExpressionMatrixWriter(final File file)
      throws NividicIOException {
    super(file);
    config();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public StandfordExpressionMatrixWriter(final OutputStream is)
      throws NividicIOException {
    super(is);
    config();
  }

}
