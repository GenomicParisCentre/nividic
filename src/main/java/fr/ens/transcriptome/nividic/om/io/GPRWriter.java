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

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class implements a BioAssayWriter for GPR streams.
 * @author Laurent Jourdren
 */
public class GPRWriter extends ATFWriter {

  /** Field name for blocks. */
  public static final String FIELD_NAME_BLOCK = "Block";
  /** Field name for column. */
  public static final String FIELD_NAME_COLUMN = "Column";
  /** Field name for row. */
  public static final String FIELD_NAME_ROW = "Row";

  /** Order of the fields. */
  public static final String[] FIELDS_ORDER = {"Block", "Column", "Row",
      "Name", "ID", "X", "Y", "Dia.", "F635 Median", "F635 Mean", "F635 SD",
      "F635 CV", "B635", "B635 Median", "B635 Mean", "B635 SD", "B635 CV",
      "% > B635+1SD", "% > B635+2SD", "F635 % Sat.", "F532 Median",
      "F532 Mean", "F532 SD", "F532 CV", "B532", "B532 Median", "B532 Mean",
      "B532 SD", "B532 CV", "% > B532+1SD", "% > B532+2SD", "F532 % Sat.",
      "Ratio of Medians (635/532)", "Ratio of Means (635/532)",
      "Median of Ratios (635/532)", "Mean of Ratios (635/532)",
      "Ratios SD (635/532)", "Rgn Ratio (635/532)", "Rgn R2 (635/532)",
      "F Pixels", "B Pixels", "Circularity", "Sum of Medians (635/532)",
      "Sum of Means (635/532)", "Log Ratio (635/532)", "F635 Median - B635",
      "F532 Median - B532", "F635 Mean - B635", "F532 Mean - B532",
      "F635 Total Intensity", "F532 Total Intensity", "SNR 635", "SNR 532",
      "Flags", "Normalize", "Autoflag"};

  /*
   * {"Block", "Column", "Row", "Name", "ID", "X", "Y", "Dia.", "F635 Median",
   * "F635 Mean", "F635 SD", "B635 Median", "B635 Mean", "B635 SD", "% >
   * B635+1SD", "% > B635+2SD", "F635 % Sat.", "F532 Median", "F532 Mean", "F532
   * SD", "B532 Median", "B532 Mean", "B532 SD", "% > B532+1SD", "% > B532+2SD",
   * "F532 % Sat.", "Ratio of Medians (532/635)", "Ratio of Means (532/635)",
   * "Median of Ratios (532/635)", "Mean of Ratios (532/635)", "Ratios SD
   * (532/635)", "Rgn Ratio (532/635)", "Rgn R² (532/635)", "F Pixels", "B
   * Pixels", "Sum of Medians", "Sum of Means", "Log Ratio (532/635)", "F635
   * Median - B635", "F532 Median - B532", "F635 Mean - B635", "F532 Mean -
   * B532", "F635 Total Intensity", "F532 Total Intensity", "SNR 635", "SNR
   * 532", "Flags", "Normalize"};
   */

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  public FieldNameConverter getFieldNameConverter() {
    return new GPRConverterFieldNames();
  }

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
    return FIELD_NAME_BLOCK;
  }

  /**
   * Get the row field name.
   * @return The name of the field for row
   */
  protected String getRowField() {
    return FIELD_NAME_ROW;
  }

  /**
   * Get the column field name.
   * @return The name of the field for column
   */
  protected String getColumnField() {
    return FIELD_NAME_COLUMN;
  }

  protected String[] getFieldNamesOrder() {
    return FIELDS_ORDER;
  }

  private void addFields() {
    addFieldToWrite(BioAssay.FIELD_NAME_DESCRIPTION);
    addFieldToWrite(BioAssay.FIELD_NAME_ID);
    addFieldToWrite(BioAssay.FIELD_NAME_GREEN);
    addFieldToWrite(BioAssay.FIELD_NAME_RED);
    addFieldToWrite(BioAssay.FIELD_NAME_FLAG);
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
  public GPRWriter(final File file) throws NividicIOException {
    super(file);
    addFields();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public GPRWriter(final OutputStream is) throws NividicIOException {
    super(is);
    addFields();
  }

}