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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This implement a class to write ExpressionMatrixDimension objects
 * @author Lory Montout
 */
public class SimpleExpressionMatrixWriter extends ExpressionMatrixWriter {

  private BufferedWriter bw;
  private static final String SEPARATOR = "\t";
  private final String type = "id/M";

  /** Dimension separator. */
  public static final String DIMENSION_SEPARATOR = "$";


  /**
   * Write data.
   * @param em ExpressionMatrixDimension to write
   * @throws NividicIOException if an error occurs while writing data
   */
  public void write(final ExpressionMatrix em) throws NividicIOException {

    bw = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    final int countRow = em.getRowCount();

    final String[] ids = em.getRowIds();
    final String[] columnNames = em.getColumnNames();
    final ExpressionMatrixDimension[] dimensions = em.getDimensions();
    final Translator annot = getTranslator();

    try {

      bw.write(type);

      if (annot != null) {
        final String[] annotationFields = annot.getFields();
        for (int i = 0; i < annotationFields.length; i++) {

          bw.write(SEPARATOR);
          bw.write(annotationFields[i]);

        }
      }

      for (int i = 0; i < columnNames.length; i++) {
        for (int j = 0; j < dimensions.length; j++) {
          bw.write(SEPARATOR);
          bw.write(columnNames[i]);
          bw.write(DIMENSION_SEPARATOR);
          bw.write(dimensions[j].getDimensionName());
        }
      }

      bw.newLine();

      for (int i = 0; i < countRow; i++) {

        final String id = ids[i];
        bw.write(id);

        if (annot != null) {
          final String[] annotationValues = annot.translate(id);
          for (int j = 0; j < annotationValues.length; j++) {
            bw.write(SEPARATOR);
            bw.write(annotationValues[j]);
          }
        }

        for (int j = 0; j < columnNames.length; j++) {
          for (int k = 0; k < dimensions.length; k++) {
            bw.write(SEPARATOR);
            bw.write(Double
                .toString(dimensions[k].getValue(id, columnNames[j])));
          }
        }

        bw.newLine();

      }

      this.bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }
  }

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public SimpleExpressionMatrixWriter(final File file) throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public SimpleExpressionMatrixWriter(final OutputStream is)
      throws NividicIOException {
    super(is);
  }

}
