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
 * This class implement a GPR reader.
 * @author Laurent Jourdren
 */
public class GPRReader extends ATFReader {

  /** Fields names usualy readed in a GPR file. */
  private static final String[] fieldNameInGPR =
      {"Block", "Column", "Row", "Name", "ID", "F635 Median", "F532 Median",
          "Flags"};

  /** Name of integer field names. */
  private static final String[] intFieldNames =
      {"Block", "Column", "Row", "X", "Y", "Dia.", "F635 Median", "F635 Mean",
          "F635 SD", "F635 CV", "B635 CV", "B635", "B635 Median", "B635 Mean",
          "B635 SD", "% > B635+1SD", "% > B635+2SD", "F635 % Sat.",
          "F532 Median", "F532 Mean", "F532 SD", "F532 CV", "B532 CV", "B532",
          "B532 Median", "B532 Mean", "B532 SD", "% > B532+1SD",
          "% > B532+2SD", "F532 % Sat.", "F Pixels", "B Pixels",
          "Sum of Medians", "Sum of Means", "F635 Median - B635",
          "F532 Median - B532", "F635 Mean - B635", "F532 Mean - B532",
          "F635 Total Intensity", "F532 Total Intensity",
          "Sum of Medians (635/532)", "Sum of Means (635/532)", "Circularity",
          "Flags", "Normalize", "Autoflag"};

  /** Name of double field names. */
  private static final String[] doubleFieldNames =
      {"Ratio of Medians (532/635)", "Ratio of Means (532/635)",
          "Median of Ratios (532/635)", "Mean of Ratios (532/635)",
          "Ratios SD (532/635)", "Rgn Ratio (532/635)", "Rgn R2 (532/635)",
          "Log Ratio (532/635)", "SNR 635", "SNR 532"};

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  public FieldNameConverter getFieldNameConverter() {
    return new GPRConverterFieldNames();
  }

  private void addDefaultFieldsToRead() {

    for (int i = 0; i < fieldNameInGPR.length; i++)
      addFieldToRead(fieldNameInGPR[i]);
  }

  /**
   * Get the names of the integer fields.
   * @return An string array of field names
   */
  public String[] getIntFieldNames() {
    return GPRReader.intFieldNames.clone();
  }

  /**
   * Get the names of the double fields.
   * @return An string array of field names
   */
  public String[] getDoubleFieldNames() {
    return GPRReader.doubleFieldNames.clone();
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
  public GPRReader(final File file) throws NividicIOException {

    super(file);
    addDefaultFieldsToRead();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public GPRReader(final InputStream is) throws NividicIOException {

    super(is);
    addDefaultFieldsToRead();
  }

}