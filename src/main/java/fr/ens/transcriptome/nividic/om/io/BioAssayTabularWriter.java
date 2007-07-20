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

/**
 * This abstract class define a reader for generic tabular files.
 * @author Laurent Jourdren
 */
public abstract class BioAssayTabularWriter extends BioAssayWriter {

  private BioAssayTabularWriterBackend backend;
  private boolean xslBackend;

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

    if (isXSLBackend())
      this.backend = new BioAssayTabularWriterXSLBackend(this);
    else
      this.backend = new BioAssayTabularWriterTSVBackend(this);

    this.backend.writeHeaders();
  }

  protected void writeData() throws NividicIOException {

    this.backend.writeData();
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
   * Enable the XSL backend.
   * @param xslBackend switch to enable XSL backend
   */
  public void setXSLBackend(final boolean xslBackend) {

    this.xslBackend = xslBackend;
  }

  /**
   * Test if XSL backend is enabled.
   * @return true if the XSL backend is enable
   */
  public boolean isXSLBackend() {

    return this.xslBackend;
  }

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
