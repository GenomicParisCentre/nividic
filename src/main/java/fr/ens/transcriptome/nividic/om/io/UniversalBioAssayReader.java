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
import java.io.IOException;
import java.io.InputStream;

import fr.ens.transcriptome.nividic.om.BioAssay;

public class UniversalBioAssayReader extends InputStreamBioAssayReader {

  private InputStreamBioAssayReader br;

  @Override
  protected String getColumnField() {

    return br.getColumnField();
  }

  @Override
  protected String[] getDoubleFieldNames() {

    return br.getDoubleFieldNames();
  }

  @Override
  protected FieldNameConverter getFieldNameConverter() {

    return br.getFieldNameConverter();
  }

  @Override
  protected String[] getFieldNamesOrder() {

    return br.getFieldNamesOrder();
  }

  @Override
  public String getFormatVersion() {

    return br.getFormatVersion();
  }

  @Override
  protected String[] getIntFieldNames() {

    return br.getIntFieldNames();
  }

  @Override
  protected String getMetaColumnField() {

    return br.getMetaColumnField();
  }

  @Override
  protected String getMetaRowField() {

    return br.getMetaRowField();
  }

  @Override
  protected String getRowField() {

    return br.getRowField();
  }

  @Override
  protected String getSeparatorField() {

    return br.getSeparatorField();
  }

  @Override
  public BioAssay read() throws NividicIOException {

    return br.read();
  }

  private void setBioAssayReader() throws NividicIOException {

    try {
      this.br =
          new BioAssayFormatFinderInputStream(getInputStream())
              .getBioAssayReader();

      if (this.br == null)
        throw new NividicIOException("Unable to find the format");
    } catch (NividicIOException e) {

      throw new NividicIOException("Unable to find the format");

    } catch (IOException e) {
      throw new NividicIOException("Unable to find the format");
    }
  }

  /**
   * Add all the field to read.
   */
  public void addAllFieldsToRead() {

    this.br.addAllFieldsToRead();
  }

  /**
   * Get the source of the data
   * @return The source of the data
   */
  public String getDataSource() {

    return this.br.getDataSource();
  }

  /**
   * Inititialize varibles each time an input stream is set.
   */
  public void clear() {

    this.br.clear();
  }

  /**
   * Add an annotation.
   * @param key Key of the annotation
   * @param value Value of the annotation
   */
  public void addAnnotation(final String key, final String value) {

    this.br.addAnnotation(key, value);
  }

  /**
   * Add a field to read.
   * @param fieldName Fieldname to read
   */
  public void addFieldToRead(final String fieldName) {

    this.br.addFieldToRead(fieldName);
  }

  //
  // Public contrustors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public UniversalBioAssayReader(final File file) throws NividicIOException {

    super(file);
    setBioAssayReader();
    setDataSource(file.getAbsolutePath());
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public UniversalBioAssayReader(final InputStream is)
      throws NividicIOException {

    super(is);
    setBioAssayReader();
  }

}
