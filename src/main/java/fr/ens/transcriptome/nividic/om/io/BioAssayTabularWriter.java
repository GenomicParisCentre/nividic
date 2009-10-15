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

/**
 * This abstract class define a reader for generic tabular files.
 * @author Laurent Jourdren
 */
public abstract class BioAssayTabularWriter extends BioAssayWriter {

  public String TSV_BACKEND = "tsv";
  public String XSL_BACKEND = "xsl";
  public String OOXML_BACKEND = "ooxml";

  private BioAssayTabularWriterBackend backend;
  private String backendType = "";

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

    // Set the backend
    if (this.backendType.equals(XSL_BACKEND))
      this.backend = new BioAssayTabularWriterXSLBackend(this);
    else if (this.backendType.equals(OOXML_BACKEND))
      this.backend = new BioAssayTabularWriterOOXMLBackend(this);
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
   * Set the backend to use.
   * @param backendType backend name to use
   */
  public void setBackendType(final String backendType) {

    if (backendType == null)
      return;

    final String s = backendType.trim().toLowerCase();

    if (s.equals(TSV_BACKEND)
        || s.equals(XSL_BACKEND) || s.equals(OOXML_BACKEND))
      this.backendType = backendType;
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
