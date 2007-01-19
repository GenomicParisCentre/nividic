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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;

public final class EasyIO {

  public static void writeExpressionMatrix(final ExpressionMatrix matrix,
      final String filename) throws NividicIOException, FileNotFoundException {

    if (filename == null)
      throw new NividicIOException("file is null");

    writeExpressionMatrix(matrix, new File(filename));
  }

  public static void writeExpressionMatrix(final ExpressionMatrix matrix,
      final File file) throws NividicIOException, FileNotFoundException {

    if (matrix == null)
      throw new NividicIOException("matrix is null");
    if (file == null)
      throw new NividicIOException("file is null");

    final OutputStream os = new FileOutputStream(file);

    SimpleExpressionMatrixWriter semw = new SimpleExpressionMatrixWriter(os);

    semw.write(matrix);

  }

}
