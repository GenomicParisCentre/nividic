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

/**
 * This class implement a BioAssayWriter for Totalsummary(Goulphar) streams
 * @author Laurent Jourdren
 */
public class TotalSummaryWriter extends BioAssayTabularWriter {

  /** Order of the fields. */
  public static final String[] FIELDS_ORDER = {"Name", "medianMnorm",
      "medianA", "SDMnorm", "n", "total n"};

  /** Fields names usualy readed in a ID-M-A file. */
  private static final String[] DEFAULT_FIELD_TO_WRITE = {
      BioAssay.FIELD_NAME_ID, BioAssay.FIELD_NAME_M, BioAssay.FIELD_NAME_A};

  //
  // Implememented methods
  //

  /**
   * Get the convert of fieldnames
   * @return The converter of fieldnames
   */
  protected FieldNameConverter getFieldNameConverter() {
    return new TotalSummaryConverterFieldNames();
  }

  /**
   * Get an array countaining the names of the fields
   * @return an array of string countaining the names of the fields
   */
  protected String[] getFieldNamesOrder() {
    return FIELDS_ORDER;
  }

  /**
   * Get the names of the fields to read by default.
   * @the names of the fields to read by defaults
   */
  protected String[] getDefaultsFieldsToWrite() {

    return DEFAULT_FIELD_TO_WRITE;
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
  public TotalSummaryWriter(final File file) throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public TotalSummaryWriter(final OutputStream is) throws NividicIOException {
    super(is);
  }

}
