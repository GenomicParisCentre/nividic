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
 * This class implement a GAL reader.
 * @author Laurent Jourdren
 */
public class GALReader extends ATFReader {

  /** Fields names usualy readed in a GAL file. */
  private static String[] fieldNameInGAL = {"Block", "Column", "Row", "ID",
      "Name"};

  /** Name of integer field names. */
  private static String[] intFieldNames = {"Block", "Column", "Row"};

  /** Name of double field names. */
  private static String[] doubleFieldNames = {};

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  public FieldNameConverter getFieldNameConverter() {
    return new GALConverterFieldNames();
  }

  private void addDefaultFieldsToRead() {

    for (int i = 0; i < fieldNameInGAL.length; i++)
      addFieldToRead(fieldNameInGAL[i]);
  }

  /**
   * Get the names of the integer fields.
   * @return An string array of field names
   */
  public String[] getIntFieldNames() {
    return GALReader.intFieldNames;
  }

  /**
   * Get the names of the double fields.
   * @return An string array of field names
   */
  public String[] getDoubleFieldNames() {
    return GALReader.doubleFieldNames;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *                 the file is null.
   */
  public GALReader(final File file) throws NividicIOException {

    super(file);
    addDefaultFieldsToRead();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public GALReader(final InputStream is) throws NividicIOException {

    super(is);
    addDefaultFieldsToRead();
  }

}