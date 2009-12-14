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
 * of the �cole Normale Sup�rieure and the individual authors.
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
 * This class implements a BioAssayWriter for GAL streams.
 * @author Laurent Jourdren
 */
public class GALWriter extends ATFWriter {

  /** Field name for blocks. */
  public static final String FIELD_NAME_BLOCK = "Block";
  /** Field name for column. */
  public static final String FIELD_NAME_COLUMN = "Column";
  /** Field name for row. */
  public static final String FIELD_NAME_ROW = "Row";

  /** Order of the fields. */
  public static final String[] FIELDS_ORDER =
      {"Block", "Column", "Row", "Name", "ID"};

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  public FieldNameConverter getFieldNameConverter() {
    return new GALConverterFieldNames();
  }

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
    return FIELD_NAME_BLOCK;
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

  private void addFields() {
    addFieldToWrite(BioAssay.FIELD_NAME_ID);
    addFieldToWrite(BioAssay.FIELD_NAME_DESCRIPTION);
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param filename file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public GALWriter(final String filename) throws NividicIOException {

    this(new File(filename));
  }

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public GALWriter(final File file) throws NividicIOException {
    super(file);
    addFields();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public GALWriter(final OutputStream is) throws NividicIOException {
    super(is);
    addFields();
  }

}
