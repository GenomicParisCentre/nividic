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

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This abstract class define a reader for generic tabular files.
 * @author Laurent Jourdren
 */
public abstract class BioAssayTabularWriter extends BioAssayWriter {

  private static final String DOS_EOL = "\r\n";
  private static final char SEPARATOR_TAB = '\t';

  private final String eol = DOS_EOL;
  private final char separator = SEPARATOR_TAB;
  private BufferedWriter bw;

  
  /**
   * Get the meta row field name.
   * @return The name of the field for meta row
   */
  protected String getMetaRowField() {
    return null;
  }

  /**
   * Get the meta column field name.
   * @return The name of the field for meta column
   */
  protected String getMetaColumnField() {
    return null;
  }

  /**
   * Get the row field name.
   * @return The name of the field for row
   */
  protected String getRowField() {
    return null;
  }

  /**
   * Get the column field name.
   * @return The name of the field for column
   */
  protected String getColumnField() {
    return null;
  }

  
  protected void writeHeaders() throws NividicIOException {
    bw = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

    StringBuffer sb = new StringBuffer();

    // Write Fields names
    for (int i = 0; i < getColumnCount(); i++) {
      if (i != 0)
        sb.append(separator);
      sb.append('"');
      sb.append(getFieldName(i));
      sb.append('"');
    }
    sb.append(eol);

    try {
      bw.write(sb.toString());
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream header : "
          + e.getMessage());
    }
  }

  protected void writeData() throws NividicIOException {

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    final int countCol = getColumnCount();
    final int countRow = getRowColumn();

    try {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < countRow; i++) {

        for (int j = 0; j < countCol; j++) {

          String value = getData(i, j);
          if (value == null)
            value = "";

          switch (getFieldType(j)) {
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

          if (j == (countCol - 1))
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
   * Get the names of the fields to read by default.
   * @the names of the fields to read by defaults
   */
  protected String[] getDefaultsFieldsToWrite() {

    return null;
  }

  //
  // Other methods
  //

  /**
   * adds fields to read
   */
  private void addDefaultFieldsToWrite() {

    String[] fieldsToRead = getDefaultsFieldsToWrite();

    if (fieldsToRead == null)
      return;

    for (int i = 0; i < fieldsToRead.length; i++)
      addFieldToWrite(fieldsToRead[i]);
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
  public BioAssayTabularWriter(final File file) throws NividicIOException {
    super(file);
    addDefaultFieldsToWrite();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public BioAssayTabularWriter(final OutputStream is) throws NividicIOException {
    super(is);
    addDefaultFieldsToWrite();
  }

}
