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
import java.io.InputStream;

/**
 * This implements a BioAssay reader for ID-M-A files (Goulphar output files)
 * @author Lory Montout
 * @author Laurent Jourdren
 */
public class IDMAReader extends BioAssayTabularReader {

  /** Fields names usualy readed in a ID-M-A file. */
  private static String[] fieldsNamesInIDMA =
      {"ID", "Name", "Mnorm", "A", "R", "G"};
  /** Name of integer field names. */
  private static String[] intFieldsNames = {};
  /** Name of double field names. */
  private static String[] doubleFieldsNames =
      {"Mnorm", "A", "R", "Rb", "G", "Gb"};

  //
  // Implememented methods
  //

  /**
   * Get the names of the integer fields.
   * @return An string array of field names
   */
  protected String[] getIntFieldNames() {
    return IDMAReader.intFieldsNames;
  }

  /**
   * Get the names of the double fields.
   * @return An string array of field names
   */
  protected String[] getDoubleFieldNames() {
    return IDMAReader.doubleFieldsNames;
  }

  /**
   * Get the order of the names of the fields.
   * @return A string array of field names
   */
  protected String[] getFieldNamesOrder() {
    return getFieldNames();
  }

  /**
   * Get the convert of fieldnames
   * @return The converter of fieldnames
   */
  protected FieldNameConverter getFieldNameConverter() {
    return new IDMAConverterFieldNames();
  }

  /**
   * Get the names of the fields to read by default.
   * @the names of the fields to read by defaults
   */
  protected String[] getDefaultFieldToRead() {

    return fieldsNamesInIDMA;
  }

  //
  // Constructors
  //

  /**
   * Public constructor
   * @param filename file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public IDMAReader(final String filename) throws NividicIOException {

    this(new File(filename));
  }

  /**
   * Public constructor
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public IDMAReader(final File file) throws NividicIOException {

    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public IDMAReader(final InputStream is) throws NividicIOException {

    super(is);
  }

}
