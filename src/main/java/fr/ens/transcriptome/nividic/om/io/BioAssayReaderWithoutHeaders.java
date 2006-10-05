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
import java.io.InputStream;

/**
 * Define an abstract class to read BioAssay without headers.
 *
 * @author Laurent Jourdren
 */
public abstract class BioAssayReaderWithoutHeaders extends BioAssayReader {

  public  static final String META_ROW_FIELD = "metaRowField";
  public static final String META_COLUMN_FIELD = "metaColumnField";
  public static final String ROW_FIELD = "rowField";
  public static final String COLUMN_FIELD = "columnField";

  /**
   * Read the header of the file.
   * @throws NividicIOException if an error occurs while reading headers
   */
  public void readHeader() throws NividicIOException {
    // TODO Auto-generated method stub
  }

  protected String getMetaRowField() {

    return META_ROW_FIELD;
  }

  protected String getMetaColumnField() {

    return META_COLUMN_FIELD;
  }

  protected String getRowField() {

    return ROW_FIELD;
  }

  protected String getColumnField() {

    return COLUMN_FIELD;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public BioAssayReaderWithoutHeaders(final File file)
      throws NividicIOException {

    super(file);

  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public BioAssayReaderWithoutHeaders(final InputStream is)
      throws NividicIOException {

    super(is);
  }

}
