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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ImaGeneArrayList;

public class ImaGeneArrayListReader extends BioAssayReaderWithoutHeaders {

  /** Fields names usualy readed in a Imagene Array list file. */
  private static String[] fieldNameInImageneArrayListFile = {
      BioAssayReaderWithoutHeaders.META_ROW_FIELD,
      BioAssayReaderWithoutHeaders.META_COLUMN_FIELD,
      BioAssayReaderWithoutHeaders.ROW_FIELD,
      BioAssayReaderWithoutHeaders.COLUMN_FIELD, BioAssay.FIELD_NAME_ID};

  /** Name of integer field names. */
  private static String[] intFieldNames = {
      BioAssayReaderWithoutHeaders.META_ROW_FIELD,
      BioAssayReaderWithoutHeaders.META_COLUMN_FIELD,
      BioAssayReaderWithoutHeaders.ROW_FIELD,
      BioAssayReaderWithoutHeaders.COLUMN_FIELD};

  /** Name of double field names. */
  private static String[] doubleFieldNames = {};

  public void readHeader() throws NividicIOException {

    // Set the type of the array list.
    ImaGeneArrayList imal = new ImaGeneArrayList(getBioAssay());
    imal.setType(ImaGeneArrayList.IMAGENE_ARRAYLIST_MAGIC_STRING);
  }

  protected FieldNameConverter getFieldNameConverter() {

    return new FieldNameConverter() {

    };
  }

  public String getFormatVersion() {

    return null;
  }

  protected String getSeparatorField() {

    return "\t";
  }

  protected boolean isStringQuotesBeRemoved() {

    return true;
  }

  protected String getEndTag() {

    return null;
  }

  protected String[] getIntFieldNames() {

    return intFieldNames;
  }

  protected String[] getDoubleFieldNames() {

    return doubleFieldNames;
  }

  protected String[] getFieldNamesOrder() {

    return fieldNameInImageneArrayListFile;
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
  public ImaGeneArrayListReader(final File file) throws NividicIOException {

    super(file);
    addAllFieldsToRead();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ImaGeneArrayListReader(final InputStream is) throws NividicIOException {

    super(is);
    addAllFieldsToRead();
  }

}
