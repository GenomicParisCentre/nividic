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

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class implement a BioAssayWriter for ATF streams
 * @author Laurent Jourdren
 */
public class ATFWriter extends BioAssayWriter {

  private static final String MAGICSTRING = "ATF";
  private static final String DOS_EOL = "\r\n";
  private static final char SEPARATOR_TAB = '\t';

  private final String eol = DOS_EOL;
  private final char separator = SEPARATOR_TAB;
  private BufferedWriter bw;

  protected FieldNameConverter getFieldNameConverter() {
    return null;
  }

  protected String[] getFieldNamesOrder() {
    return null;
  }

  protected String getMetaColumnField() {
    return null;
  }

  protected String getMetaRowField() {
    return null;
  }

  protected String getColumnField() {
    return null;
  }

  protected String getRowField() {
    return null;
  }

  protected void writeHeaders() throws NividicIOException {
    bw = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

    // Write MagicString

    StringBuffer sb = new StringBuffer();
    sb.append(MAGICSTRING);
    sb.append(separator);
    sb.append("1.0");
    sb.append(eol);

    Annotation annotations = getBioAssay().getAnnotation();

    // Write second header line
    sb.append(annotations == null ? 0 : annotations.size());
    sb.append(separator);
    sb.append(getColumnCount());
    sb.append(eol);

    if (annotations != null) {

      String[] keys = annotations.getPropertiesKeys();
      // Write optional headers
      for (int i = 0; i < keys.length; i++) {
        final String key = keys[i];
        final String value = annotations.getProperty(key);
        sb.append('"');
        sb.append(key);
        sb.append('=');
        sb.append(value);
        sb.append('"');
        sb.append(eol);
      }
    }

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
            if (value.equals("NaN"))
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

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *                 the file is null.
   */
  public ATFWriter(final File file) throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ATFWriter(final OutputStream is) throws NividicIOException {
    super(is);
  }

}