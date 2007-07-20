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
import java.io.IOException;
import java.io.OutputStreamWriter;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.translators.Translator;

class BioAssayTabularWriterTSVBackend implements BioAssayTabularWriterBackend {

  private BioAssayWriter writer;

  private static final String DOS_EOL = "\r\n";
  private static final char SEPARATOR_TAB = '\t';

  private static final String eol = DOS_EOL;
  private static final char separator = SEPARATOR_TAB;
  private BufferedWriter bw;

  /**
   * Set the BioAssayWriter.
   * @param writer Writer to set
   */
  public void setBioAssayWriter(final BioAssayWriter writer) {

    this.writer = writer;
  }

  /**
   * Write the data.
   * @throws NividicIOException if an error occurs while writing data
   */
  public void writeData() throws NividicIOException {

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    final BioAssayWriter writer = this.writer;

    final int countCol = writer.getColumnCount();
    final int countRow = writer.getRowColumn();
    final Translator translator = writer.getTranslator();
    final String[] translatorFields;
    final String[] ids =
        writer.getBioAssay() == null ? null : writer.getBioAssay().getIds();

    if (translator == null)
      translatorFields = null;
    else
      translatorFields = translator.getFields();

    try {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < countRow; i++) {

        for (int j = 0; j < countCol; j++) {

          String value = writer.getData(i, j);
          if (value == null)
            value = "";

          switch (writer.getFieldType(j)) {
          case BioAssay.DATATYPE_STRING:
            sb.append('\"');
            sb.append(value);
            sb.append('\"');
            break;

          case BioAssay.DATATYPE_INTEGER:
            sb.append(value);
            break;

          case BioAssay.DATATYPE_DOUBLE:
            if (value.equals("NA"))
              sb.append("Error");
            else
              sb.append(value);
            break;

          // Locations type
          default:
            sb.append(value);
            break;
          }

          if (translatorFields == null && (j == (countCol - 1)))
            sb.append(eol);
          else
            sb.append(separator);
        }

        if (translatorFields != null)
          for (int j = 0; j < translatorFields.length; j++) {

            final String value;

            if (ids == null)
              value = null;
            else
              value = translator.translateField(ids[i], translatorFields[j]);

            if (value != null) {
              sb.append('\"');
              sb.append(value);
              sb.append('\"');
            }

            if (j == (translatorFields.length - 1))
              sb.append(eol);
            else
              sb.append(separator);
          }

        this.bw.write(sb.toString());
        sb.delete(0, sb.length());
      }

      this.bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

  }

  /**
   * Write the header.
   * @throws NividicIOException if an error occurs while writing header
   */
  public void writeHeaders() throws NividicIOException {

    final BioAssayWriter writer = this.writer;

    bw = new BufferedWriter(new OutputStreamWriter(writer.getOutputStream()));

    StringBuffer sb = new StringBuffer();

    // Write Fields names
    for (int i = 0; i < writer.getColumnCount(); i++) {
      if (i != 0)
        sb.append(separator);
      sb.append('"');
      sb.append(writer.getFieldName(i));
      sb.append('"');
    }

    if (writer.getTranslator() != null) {

      String[] fields = writer.getTranslator().getFields();
      if (fields != null)
        for (int i = 0; i < fields.length; i++) {

          sb.append(separator);

          sb.append('"');
          sb.append(fields[i]);
          sb.append('"');
        }

    }

    sb.append(eol);

    try {
      bw.write(sb.toString());
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream header : "
          + e.getMessage());
    }

  }

  //
  // Constructor
  //

  public BioAssayTabularWriterTSVBackend(final BioAssayWriter writer) {

    setBioAssayWriter(writer);
  }

}
