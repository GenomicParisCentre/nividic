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

public class ImaGeneArrayListWriter extends BioAssayWriter {

  private static final String DOS_EOL = "\r\n";
  private static final char SEPARATOR_TAB = '\t';

  private static final String eol = DOS_EOL;
  private static final char separator = SEPARATOR_TAB;

  /** Field name for meta rows. */
  public static final String FIELD_NAME_META_ROW = "Meta Row";
  /** Field name for meta columns. */
  public static final String FIELD_NAME_META_COLUMN = "Meta Column";
  /** Field name for row. */
  public static final String FIELD_NAME_ROW = "Row";
  /** Field name for column. */
  public static final String FIELD_NAME_COLUMN = "Column";

  /** Order of the fields. */
  public static final String[] FIELDS_ORDER = {FIELD_NAME_META_ROW,
      FIELD_NAME_META_COLUMN, FIELD_NAME_ROW, FIELD_NAME_COLUMN,
      BioAssay.FIELD_NAME_ID};

  protected FieldNameConverter getFieldNameConverter() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Get the meta row field name.
   * @return The name of the field for meta row
   */
  protected String getMetaRowField() {
    return FIELD_NAME_META_ROW;
  }

  /**
   * Get the meta column field name.
   * @return The name of the field for meta column
   */
  protected String getMetaColumnField() {
    return FIELD_NAME_META_COLUMN;
  }

  /**
   * Get the row field name.
   * @return The name of the field for row
   */
  protected String getRowField() {
    return FIELD_NAME_ROW;
  }

  /**
   * Get the column field name.
   * @return The name of the field for column
   */
  protected String getColumnField() {
    return FIELD_NAME_COLUMN;
  }

  protected String[] getFieldNamesOrder() {
    return FIELDS_ORDER;
  }

  protected void writeHeaders() throws NividicIOException {

    // Nothing to do
  }

  private void addFields() {
    addFieldToWrite(BioAssay.FIELD_NAME_ID);
  }

  protected void writeData() throws NividicIOException {

    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        getOutputStream()));

    if (bw == null)
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
            sb.append(value);
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
        bw.write(sb.toString());
        sb.delete(0, sb.length());
      }

      bw.close();
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
   *           the file is null.
   */
  public ImaGeneArrayListWriter(final File file) throws NividicIOException {
    super(file);
    addFields();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ImaGeneArrayListWriter(final OutputStream is)
      throws NividicIOException {
    super(is);
    addFields();
  }

}
