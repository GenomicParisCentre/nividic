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

package fr.ens.transcriptome.nividic.sgdb.io;

import java.io.File;
import java.io.InputStream;

import fr.ens.transcriptome.nividic.om.io.BioAssayTabularReader;
import fr.ens.transcriptome.nividic.om.io.FieldNameConverter;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This implements a BioAssay reader for totalsummaty files (Goulphar output
 * files)
 * @author Laurent Jourdren
 */
public class TotalSummaryReader extends BioAssayTabularReader {

  /** Fields names usualy readed in a ID-M-A file. */
  private static String[] fieldNameInIDMA = {"Name", "medianMnorm", "medianA",
      "SDMnorm", "n", "total n"};
  /** Name of integer field names. */
  private static String[] intFieldNames = {"n", "total n"};
  /** Name of double field names. */
  private static String[] doubleFieldNames = {"medianMnorm", "medianA",
      "SDMnorm"};

  //
  // Implememented methods
  //

  /**
   * Get the names of the integer fields.
   * @return An string array of field names
   */
  protected String[] getIntFieldNames() {
    return TotalSummaryReader.intFieldNames;
  }

  /**
   * Get the names of the double fields.
   * @return An string array of field names
   */
  protected String[] getDoubleFieldNames() {
    return TotalSummaryReader.doubleFieldNames;
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
    return new TotalSummaryConverterFieldNames();
  }

  /**
   * Get the names of the fields to read by default.
   * @the names of the fields to read by defaults
   */
  protected String[] getDefaultFieldToRead() {

    return fieldNameInIDMA;
  }

  //
  // Constructor
  //

  /**
   * Public constructor
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public TotalSummaryReader(final File file) throws NividicIOException {

    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public TotalSummaryReader(final InputStream is) throws NividicIOException {

    super(is);
  }

}
