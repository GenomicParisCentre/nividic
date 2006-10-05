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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ImaGeneResult;
import fr.ens.transcriptome.nividic.util.NividicUtils;

public class ImaGeneOutputFileReader extends
    BioAssayReaderMultipleStreamsReader {

  private static String SIGNAL_FIELD = "Signal Median";

  /** Fields names usualy readed in a GPR file. */
  private static String[] fieldNameInGPR = {"Fields", "Meta Row",
      "Meta Column", "Row", "Column", "Gene ID", SIGNAL_FIELD, "Flag"};

  /** Name of integer field names. */
  private static String[] intFieldNames = {"Meta Row", "Meta Column", "Row",
      "Column", "Flag", "Control Failed",
      "Control  Background contamination present",
      "Signal contamination present", "Ignored % failed",
      "Open perimeter failed", "Shape regularity failed Perim-to-area failed",
      "Offset failed", "Empty spot", "Negative spot", "Selected spot"};

  /** Name of double field names. */
  private static String[] doubleFieldNames = {"Signal Mean", "Background Mean",
      "Signal Median", "Background Median", "Signal Mode", "Background Mode",
      "Signal Area", "Background Area", "Signal Total", "Background Total",
      "Signal Stdev", "Background Stdev", "Shape Regularity", "Ignored Area",
      "Spot Area", "Ignored Median", "Area To Perimeter", "Open Perimeter",
      "XCoord", "YCoord", "Diameter", "Position offset", "Offset X",
      "Offset Y", "Expected X", "Expected Y", "CM-X", "CM-Y", "CM Offset",
      "CM Offset-X", "CM Offset-Y", "Min Diam", "Max Diam"};

  /** Field name for meta row. */
  public static final String FIELD_NAME_META_ROW = "Meta Row";
  /** Field name for meta column. */
  public static final String FIELD_NAME_META_COLUMN = "Meta Column";
  /** Field name for column. */
  public static final String FIELD_NAME_COLUMN = "Column";
  /** Field name for row. */
  public static final String FIELD_NAME_ROW = "Row";

  private static final char SEPARATOR = '\t';

  private static final String MAGICSTRING = "Begin Header";

  private boolean readHeadersDone;

  private Header header;

  private class Header {

    private static final String END_HEADER_STRING = "End Header";
    private static final String BEGIN_SECTION_STRING = "Begin ";
    private static final String END_SECTION_STRING = "End ";
    private static final String START_RAW_DATA_STRING = "Begin Raw Data";

    private ArrayList sectionNames = new ArrayList();
    private boolean sectionChanged;
    private String currentSectionName;
    private BufferedReader reader;
    private Annotation annotation;

    private boolean firstRowSection = true;;
    private boolean keyValueSection;
    private String[] columnFields;
    private String[] RawDataFieldsNames;

    private void openSection(final String name) {
      this.sectionNames.add(name);
      this.sectionChanged = true;
      this.firstRowSection = true;
    }

    private void closeSection() {
      this.sectionNames.remove(this.sectionNames.size() - 1);
      this.sectionChanged = true;
    }

    private String getSectionName() {

      if (sectionChanged) {

        StringBuffer sb = new StringBuffer();

        final int n = this.sectionNames.size();
        for (int i = 0; i < n; i++) {
          sb.append((String) sectionNames.get(i));
          if (i != n - 1)
            sb.append(".");
        }

        this.currentSectionName = sb.toString();
      }

      return this.currentSectionName;
    }

    private String removeTab(final String s) {

      if (s == null)
        return null;

      for (int i = 0; i < s.length(); i++)
        if (s.charAt(i) != SEPARATOR)
          return s.substring(i, s.length());

      return "";
    }

    public String[] getRawDataFieldsNames() {
      return this.RawDataFieldsNames;
    }

    public void read() throws NividicIOException {

      try {

        String line;

        while ((line = this.reader.readLine()) != null) {

          line = removeTab(line);

          if (line.startsWith(END_HEADER_STRING)) {
            boolean find = false;

            while ((line = this.reader.readLine()) != null) {
              if (find) {
                this.RawDataFieldsNames = line.split("" + SEPARATOR);
                return;
              } else if (line.startsWith(START_RAW_DATA_STRING)) {
                find = true;

              }
            }
          }

          if ("".equals(line))
            continue;

          if (line.startsWith(BEGIN_SECTION_STRING)) {
            openSection(line.substring(BEGIN_SECTION_STRING.length(), line
                .length()));
          } else if (line.startsWith(END_SECTION_STRING))
            closeSection();
          else {

            String[] ch = line.split("" + SEPARATOR);

            boolean colon = false;

            // test if a ':' char is present
            for (int i = 0; i < ch.length; i++)
              if (ch[i].endsWith(":")) {
                colon = true;
                break;
              }

            if (this.firstRowSection) {

              if (ch.length > 2 && !colon)
                this.keyValueSection = false;
              else
                this.keyValueSection = true;

              if (this.firstRowSection && !colon)
                this.columnFields = ch;
            }

            if (this.keyValueSection) {

              final String section = getSectionName();

              String key;

              if (section == null)
                key = ch[0];
              else
                key = section + "." + ch[0];

              String value;

              if (ch.length == 1)
                value = "";
              else
                value = ch[1];

              this.annotation.setProperty(key, value);

              if (ch.length > 2) {

                if (ch[2].endsWith(":")) {
                  String optionalKey = key + "."
                      + ch[2].substring(0, ch[2].length() - 1);
                  String optionalValue;
                  if (ch.length < 4)
                    optionalValue = "";
                  else
                    optionalValue = ch[3];

                  this.annotation.setProperty(optionalKey, optionalValue);

                }
              }

            } else if (!this.firstRowSection) {

              for (int i = 0; i < ch.length; i++) {

                if (i < this.columnFields.length) {

                  String key = getSectionName() + "." + this.columnFields[i];
                  this.annotation.setProperty(key, ch[i]);
                }

              }

            }

            if (this.firstRowSection)
              this.firstRowSection = false;

          }

        }

      } catch (IOException e) {
        throw new NividicIOException("Error while reading the file");
      }

    }

    //
    // constructor
    //

    /**
     * Public constructor.
     * @param reader The reader stream
     * @param annot The annotation object
     */
    public Header(final BufferedReader reader, final Annotation annot) {
      this.reader = reader;
      this.annotation = annot;
    }

  }

  public void readHeader() throws NividicIOException {

    if (this.readHeadersDone)
      return;

    if (!testIfImageneOuputFile())
      throw new NividicIOException("This is not an Imagene output file");

    Annotation annot = getBioAssay().getAnnotation();

    Header header = new Header(getBufferedReader(), annot);
    header.read();

    this.header = header;

  }

  /**
   * Test if the stream is an Imagene output stream.
   * @return <b>true </b> if the stream is an Imagene output stream.
   */
  private boolean testIfImageneOuputFile() throws NividicIOException {

    try {

      String line = getBufferedReader().readLine();
      if (line == null)
        return false;
      if (!line.startsWith(MAGICSTRING))
        throw new NividicIOException("This file is not an Imagene output file");

    } catch (IOException e) {
      throw new NividicIOException("Error while reading the file");
    }
    return true;
  }

  protected String[] getIntFieldNames() {
    return intFieldNames;
  }

  protected String[] getDoubleFieldNames() {
    return doubleFieldNames;
  }

  protected String[] getFieldNamesOrder() {

    if (this.header == null)
      return null;
    return this.header.getRawDataFieldsNames();
  }

  protected FieldNameConverter getFieldNameConverter() {
    return new ImaGeneFieldOutputFileNameConverter();
  }

  public String getFormatVersion() {
    // TODO Auto-generated method stub
    return null;
  }

  protected String getMetaRowField() {
    return FIELD_NAME_META_ROW;
  }

  protected String getMetaColumnField() {
    return FIELD_NAME_META_COLUMN;
  }

  protected String getRowField() {
    return FIELD_NAME_ROW;
  }

  protected String getColumnField() {
    return FIELD_NAME_COLUMN;
  }

  protected String getSeparatorField() {
    return "" + SEPARATOR;
  }

  protected boolean isStringQuotesBeRemoved() {
    return false;
  }

  /**
   * Get the end tag of the stream. Null is not exists.
   * @return the end tag of the stream. Null is not exists
   */
  protected String getEndTag() {
    return "End Raw Data";
  }

  private void addDefaultFieldsToRead() {

    for (int i = 0; i < fieldNameInGPR.length; i++)
      addFieldToRead(fieldNameInGPR[i]);
  }

  /**
   * This abstract method allow to transform a bioassay before merging (e.g.
   * change columns names).
   * @param ba BioAssay to transform
   * @param count The number of the bioinfo in the multiples streams
   */
  public void tranformBioAssayBeforeMerging(final BioAssay ba, final int count) {

    if (count < 0 || count > 1) {
      ba.removeField(SIGNAL_FIELD);
      return;
    }

    final double[] values = ba.getDataFieldDouble(SIGNAL_FIELD);
    ba.removeField(SIGNAL_FIELD);

    if (values == null)
      return;

    if (count == 0)
      ba.setGreens(NividicUtils.arrayDoubleToArrayInt(values));
    else if (count == 1)
      ba.setReds(NividicUtils.arrayDoubleToArrayInt(values));
    
    ImaGeneResult igr = new ImaGeneResult(getBioAssay());
    igr.setType(ImaGeneResult.IMAGENE_RESULT_MAGIC_STRING);
  }

  /**
   * Add a stream to read.
   * @param stream Stream to read.
   * @throws NividicIOException if the stream is null.
   */
  public void addStream(final InputStream stream) throws NividicIOException {

    throw new NividicRuntimeException("New InputStream can't be added.");
  }

  //
  // Constructor
  //

  private ImaGeneOutputFileReader() {
    super();
    addDefaultFieldsToRead();
  }

  public ImaGeneOutputFileReader(final InputStream isGreens,
      final InputStream isReds) throws NividicIOException {
    this();
    super.addStream(isGreens);
    super.addStream(isReds);
  }

}
