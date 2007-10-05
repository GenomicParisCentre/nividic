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

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class implements a BioAssayWriter for ImaGene streams.
 * @author Laurent Jourdren
 */
public class ImaGeneOutputFileWriter extends BioAssayWriter {

  private static final String SEPARATOR = "\t";
  private static final String DOS_EOL = "\r\n";

  private BufferedWriter bw;

  private final FieldNameConverter converter =
      new ImaGeneFieldOutputFileNameConverter();

  private static final String[] FIELD_NAMES_ORDER =
      {"Field", "Meta Row", "Meta Column", "Row", "Column", "Gene ID", "Flag",
          "Signal Mean", "Background Mean", "Signal Median",
          "Background Median", "Signal Mode", "Background Mode", "Signal Area",
          "Background Area", "Signal Total", "Background Total",
          "Signal Stdev", "Background Stdev", "Shape Regularity",
          "Ignored Area", "Spot Area", "Ignored Median", "Area To Perimeter",
          "Open Perimeter", "XCoord", "YCoord", "Diameter", "Position offset",
          "Offset X", "Offset Y", "Expected X", "Expected Y", "CM-X", "CM-Y",
          "CM Offset", "CM Offset-X", "CM Offset-Y", "Min Diam", "Max Diam",
          "Control", "Failed Control", "Background contamination present",
          "Signal contamination present", "Ignored % failed",
          "Open perimeter failed", "Shape regularity failed",
          "Perim-to-area failed", "Offset failed", "Empty spot",
          "Negative spot", "Selected spot"};

  private static final String START_RAW_DATA_STRING = "Begin Raw Data";
  private static final String EOF =
      "End Raw Data" + DOS_EOL + "End of File" + DOS_EOL;

  private class Header {

    private static final String BEGIN_HEADER_STRING = "Begin Header";
    private static final String END_HEADER_STRING = "End Header";
    private static final String BEGIN_SECTION_STRING = "Begin ";
    private static final String END_SECTION_STRING = "End ";

    private static final String VERSION_FIELD = "version";
    private static final String DATE_FIELD = "Date";
    private static final String IMAGE_FIELD_FIELD = "Image File";
    private static final String PAGE_FIELD = "Page";
    private static final String PAGE_NAME_FIELD = "Page Name";
    private static final String INVERTED_FIELD = "Inverted";

    private Annotation annot;
    private StringBuilder sb;

    private void addSimpleField(final String field) {

      final StringBuilder sb = this.sb;

      sb.append(SEPARATOR + field + SEPARATOR);
      sb.append(this.annot.getProperty(field));
      sb.append(DOS_EOL);

    }

    private void addDimensionSection(final StringBuilder sb) {

      final String tab = ImaGeneOutputFileWriter.SEPARATOR;
      final String eol = ImaGeneOutputFileWriter.DOS_EOL;

      // Field Dimensions section

      sb.append(tab + BEGIN_SECTION_STRING + "Field Dimensions" + eol);
      sb.append(tab
          + tab + "Field" + tab + "Metarows" + tab + "Metacols" + tab + "Rows"
          + tab + "Cols" + eol);
      sb.append(tab + tab);
      sb.append(annot.getProperty("Field Dimensions.Field"));
      sb.append(tab);
      sb.append(annot.getProperty("Field Dimensions.Metarows"));
      sb.append(tab);
      sb.append(annot.getProperty("Field Dimensions.Metacols"));
      sb.append(tab);
      sb.append(annot.getProperty("Field Dimensions.Rows"));
      sb.append(tab);
      sb.append(annot.getProperty("Field Dimensions.Cols"));

      sb.append(eol + tab + END_SECTION_STRING + "Field Dimensions" + eol);
    }

    private void addMeasurementParametersSection(final StringBuilder sb) {

      final String tab = ImaGeneOutputFileWriter.SEPARATOR;
      final String eol = ImaGeneOutputFileWriter.DOS_EOL;

      // Measurement parameters

      sb.append(tab + BEGIN_SECTION_STRING + "Measurement parameters" + eol);

      sb.append(tab + tab + "Segmentation Method" + tab);
      sb
          .append(annot
              .getProperty("Measurement parameters.Segmentation Method"));
      sb.append(eol);

      sb.append(tab + tab + "Signal Low" + tab);
      sb.append(annot.getProperty("Measurement parameters.Signal Low"));
      sb.append(eol);

      sb.append(tab + tab + "Signal High" + tab);
      sb.append(annot.getProperty("Measurement parameters.Signal High"));
      sb.append(eol);

      sb.append(tab + tab + "Background Low" + tab);
      sb.append(annot.getProperty("Measurement parameters.Background Low"));
      sb.append(eol);

      sb.append(tab + tab + "Background High" + tab);
      sb.append(annot.getProperty("Measurement parameters.Background High"));
      sb.append(eol);

      sb.append(tab + tab + "Background Buffer" + tab);
      sb.append(annot.getProperty("Measurement parameters.Background Buffer"));
      sb.append(eol);

      sb.append(tab + tab + "Background Width" + tab);
      sb.append(annot.getProperty("Measurement parameters.Background Width"));
      sb.append(eol);

      sb.append(tab + END_SECTION_STRING + "Measurement parameters" + eol);
    }

    private void addAlertsSection(final StringBuilder sb) {

      final String tab = ImaGeneOutputFileWriter.SEPARATOR;
      final String eol = ImaGeneOutputFileWriter.DOS_EOL;

      // Alert section

      sb.append(tab + BEGIN_SECTION_STRING + "Alerts" + eol);

      sb.append(tab
          + tab + "Control Type" + tab + "Minimum threshold" + tab
          + "If tested" + tab + "Percentage allowed" + tab + "If failed" + tab
          + "Maximum threshold" + tab + "If tested" + tab
          + "Percentage allowed" + tab + "If failed" + tab + "CV threshold"
          + tab + "If tested" + tab + "If failed" + eol);

      sb.append(tab + END_SECTION_STRING + "Alerts" + eol);

    }

    private void addQualitySection(final StringBuilder sb) {

      final String tab = ImaGeneOutputFileWriter.SEPARATOR;
      final String eol = ImaGeneOutputFileWriter.DOS_EOL;

      // Quality settings section

      sb.append(tab + BEGIN_SECTION_STRING + "Quality settings" + eol);

      sb.append(tab + tab + "Empty Spots" + tab);
      sb.append(annot.getProperty("Quality settings.Empty Spots"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Empty Spots.Threshold"));
      sb.append(eol);

      sb.append(tab + tab + "Poor Spots" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots"));
      sb.append(eol);

      // Poor Spots subsection

      sb.append(tab
          + tab + BEGIN_SECTION_STRING + "Poor Spots Parameters" + eol);

      sb.append(tab + tab + tab + "Background contamination flag" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Background contamination flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Background contamination flag.Threshold"));
      sb.append(eol);

      sb.append(tab
          + tab + tab + "Background tested against subgrid data only" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Background tested against subgrid data only"));
      sb.append(eol);

      sb.append(tab + tab + tab + "Signal contamination flag" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Signal contamination flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Signal contamination flag.Threshold"));
      sb.append(eol);

      sb
          .append(tab
              + tab
              + tab
              + "Signal contamination test connected to background contamination threshold"
              + tab);
      sb
          .append(annot
              .getProperty("Quality settings.Poor Spots Parameters."
                  + "Signal contamination test connected to background contamination threshold"));
      sb.append(eol);

      sb.append(tab + tab + tab + "Ignored percentage flag" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Ignored percentage flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Ignored percentage flag.Threshold"));
      sb.append(eol);

      sb.append(tab + tab + tab + "Open perimeter flag" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Open perimeter flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Open perimeter flag.Threshold"));
      sb.append(eol);

      sb.append(tab + tab + tab + "Shape regularity flag" + tab);
      sb
          .append(annot
              .getProperty("Quality settings.Poor Spots Parameters.Shape regularity flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Shape regularity flag.Threshold"));
      sb.append(eol);

      sb.append(tab + tab + tab + "Area To Perimeter Ratio flag" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Area To Perimeter Ratio flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Area To Perimeter Ratio flag.Threshold"));
      sb.append(eol);

      sb.append(tab + tab + tab + "Offset flag" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Offset flag"));
      sb.append(tab + "Threshold:" + tab);
      sb.append(annot.getProperty("Quality settings.Poor Spots Parameters."
          + "Offset flag.Threshold"));
      sb.append(eol);

      sb.append(tab + tab + END_SECTION_STRING + "Poor Spots Parameters" + eol);

      sb.append(tab + tab + "Negative Spots" + tab);
      sb.append(annot.getProperty("Quality settings.Negative Spots"));
      sb.append(eol + tab + tab + eol);

      sb.append(tab + END_SECTION_STRING + "Quality settings" + eol);

    }

    public String getHeader() {

      this.sb = new StringBuilder();
      final StringBuilder sb = this.sb;

      final String eol = ImaGeneOutputFileWriter.DOS_EOL;

      sb.append(BEGIN_HEADER_STRING + eol);

      addSimpleField(VERSION_FIELD);
      addSimpleField(DATE_FIELD);
      addSimpleField(IMAGE_FIELD_FIELD);
      addSimpleField(PAGE_FIELD);
      addSimpleField(PAGE_NAME_FIELD);
      addSimpleField(INVERTED_FIELD);

      addDimensionSection(sb);
      addMeasurementParametersSection(sb);
      addAlertsSection(sb);
      addQualitySection(sb);

      sb.append(END_HEADER_STRING + eol);

      return sb.toString();
    }

    //
    // Constructor
    //

    public Header(final Annotation annot) {

      this.annot = annot;
    }

  }

  @Override
  protected String getColumnField() {

    return "Column";
  }

  @Override
  protected FieldNameConverter getFieldNameConverter() {

    return converter;
  }

  @Override
  protected String[] getFieldNamesOrder() {

    return FIELD_NAMES_ORDER;
  }

  @Override
  protected String getMetaColumnField() {

    return "Meta Column";
  }

  @Override
  protected String getMetaRowField() {

    return "Meta Row";
  }

  @Override
  protected String getRowField() {

    return "Row";
  }

  @Override
  protected void writeData() throws NividicIOException {

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    final int countCol = getColumnCount();
    final int countRow = getRowColumn();

    try {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < countRow; i++) {

        sb.append(SEPARATOR);

        for (int j = 0; j < countCol; j++) {

          String value = getData(i, j);
          if (value == null)
            value = "";

          switch (getFieldType(j)) {
          case BioAssay.DATATYPE_STRING:
            sb.append(value);
            break;

          case BioAssay.DATATYPE_INTEGER:
            sb.append(value);
            break;

          case BioAssay.DATATYPE_DOUBLE:
            sb.append(value);
            break;

          // Locations type
          default:
            sb.append(value);
            break;
          }

          if (j == (countCol - 1))
            sb.append(DOS_EOL);
          else
            sb.append(SEPARATOR);
        }
        this.bw.write(sb.toString());
        sb.delete(0, sb.length());
      }

      this.bw.write(EOF);
      this.bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

  }

  private String writeColumnsHeader() {

    StringBuilder sb = new StringBuilder();

    sb.append(START_RAW_DATA_STRING + DOS_EOL);

    // Write Fields names
    for (int i = 0; i < getColumnCount(); i++) {

      sb.append(SEPARATOR);
      sb.append(getFieldName(i));
    }

    sb.append(DOS_EOL);
    return sb.toString();
  }

  @Override
  protected void writeHeaders() throws NividicIOException {

    bw = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

    final BioAssay bioAssay = getBioAssay();
    final Annotation annot = bioAssay.getAnnotation();

    Header header = new Header(annot);

    try {

      bw.write(header.getHeader());
      bw.write(writeColumnsHeader());
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream header : "
          + e.getMessage());
    }

  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public ImaGeneOutputFileWriter(final File file) throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ImaGeneOutputFileWriter(final OutputStream is)
      throws NividicIOException {
    super(is);
  }

}
