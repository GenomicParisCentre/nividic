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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ens.transcriptome.nividic.Globals;
import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class implements a BioAssayWriter for Agilent streams.
 * @author Laurent Jourdren
 */
public class AgilentReader extends InputStreamBioAssayReader {

  private static final FieldNameConverter CONVERTER =
      new AgilentConverterFieldNames();
  private static final String ROW_FIELD = "Row";
  private static final String COLUMN_FIELD = "Col";
  private static final String SEPARATOR = "\t";

  private static final String[] DEFAULT_FIELDS_TO_READ =
      {"FeatureNum", "ProbeName", "gMedianSignal", "rMedianSignal",
          "rBGMedianSignal", "gBGMedianSignal", "ControlType", "IsManualFlag",
          "gIsFeatPopnOL", "rIsFeatPopnOL", "gIsBGPopnOL", "rIsBGPopnOL",
          "gIsFeatNonUnifOL", "rIsFeatNonUnifOL", "gIsBGNonUnifOL",
          "rIsBGNonUnifOL", "gIsFound", "rIsFound", "gIsPosAndSignif",
          "rIsPosAndSignif"};

  private abstract class TableReader {

    private static final String TEXT_TYPE = "text";
    private static final String INTEGER_TYPE = "integer";
    private static final String FLOAT_TYPE = "float";
    private static final String BOOLEAN_TYPE = "boolean";

    private static final String ROW_TYPE_ID = "TYPE";
    private static final String ROW_DATA_ID = "DATA";
    private static final String ROW_END_ID = "*";

    private BufferedReader reader;
    private LinkedHashMap<String, Integer> types =
        new LinkedHashMap<String, Integer>();
    private String type;
    private int columnCount;
    private boolean headerReaded;

    public Set<String> getFields() {

      return this.types.keySet();
    }

    public int getFieldType(final String field) {

      return this.types.get(field);
    }

    public String getType() {

      return this.type;
    }

    public void readHeader() throws IOException, NividicIOException {

      final BufferedReader reader = this.reader;

      //
      // Read the 2 first lines
      //

      final String headerTypesLine = reader.readLine();

      if (headerTypesLine == null)
        throw new NividicIOException("Unexpected end of file.");

      String headerNamesLine = reader.readLine();

      if (headerNamesLine == null)
        throw new NividicIOException("Unexpected end of file.");

      final String[] typesValues = headerTypesLine.split(SEPARATOR);
      final String[] namesValues = headerNamesLine.split(SEPARATOR);

      if (typesValues.length < 1)
        throw new NividicIOException("Unexpected end of file.");

      if (typesValues.length != namesValues.length)
        throw new NividicIOException(
            "Invalid header file: Imvalid number of column.");

      this.columnCount = typesValues.length - 1;

      if (!typesValues[0].startsWith(ROW_TYPE_ID))
        throw new NividicIOException("Invalid header file.");

      //
      // Set fields names and types
      //

      for (int i = 1; i < typesValues.length; i++) {

        final String fieldName = namesValues[i];
        final String fieldType = typesValues[i];

        if (TEXT_TYPE.equals(fieldType))
          this.types.put(fieldName, BioAssay.DATATYPE_STRING);
        else if (INTEGER_TYPE.equals(fieldType))
          this.types.put(fieldName, BioAssay.DATATYPE_INTEGER);
        else if (FLOAT_TYPE.equals(fieldType))
          this.types.put(fieldName, BioAssay.DATATYPE_DOUBLE);
        else if (BOOLEAN_TYPE.equals(fieldType))
          this.types.put(fieldName, BioAssay.DATATYPE_INTEGER);
        else
          throw new NividicIOException("Invalid header file: Unknown type ("
              + fieldType + ")");

      }

      this.headerReaded = true;
    }

    public void read() throws IOException, NividicIOException {

      if (!this.headerReaded)
        readHeader();

      final BufferedReader reader = this.reader;
      int rowCount = 0;
      String line;

      while ((line = reader.readLine()) != null) {

        final String[] values = line.split(SEPARATOR);

        if (ROW_END_ID.equals(values[0]))
          return;

        if (this.columnCount < values.length - 1)
          throw new NividicIOException("Invalid number of column.");

        if (!ROW_DATA_ID.endsWith(values[0]))
          throw new NividicIOException("Invalid row.");

        newRow(rowCount);
        int colCount = 1;

        for (final String field : this.types.keySet()) {

          final String value;

          if (colCount >= values.length)
            value = "";
          else
            value = values[colCount];

          switch (getFieldType(field)) {

          case BioAssay.DATATYPE_STRING:
            setText(field, new String(value));
            break;

          case BioAssay.DATATYPE_INTEGER:

            final int intVal = Integer.parseInt(value);
            setInteger(field, intVal);
            break;

          case BioAssay.DATATYPE_DOUBLE:

            final double doubleVal = Double.parseDouble(value);
            setFloat(field, doubleVal);
            break;

          default:
            break;
          }

          colCount++;
        }
        rowCount++;
      }
      newRow(rowCount);

    }

    //
    // Abstract methods
    //

    public abstract void setText(final String field, final String value);

    public abstract void setInteger(final String field, final int value);

    public abstract void setFloat(final String field, final double value);

    public abstract void newRow(final int rowIndex);

    //
    // Constructor
    //

    public TableReader(final BufferedReader reader) {

      this.reader = reader;
    }
  }

  private final class AnnotationTableReader extends TableReader {

    private Annotation annot;

    @Override
    public void setFloat(final String field, final double value) {
      this.annot.setProperty(field, "" + value);
    }

    @Override
    public void setInteger(final String field, final int value) {
      this.annot.setProperty(field, "" + value);
    }

    @Override
    public void setText(final String field, final String value) {
      this.annot.setProperty(field, "" + value);
    }

    @Override
    public void newRow(final int rowIndex) {
    }

    //
    // Constructor
    //

    public AnnotationTableReader(final BufferedReader reader,
        final BioAssay bioAssay) {

      super(reader);
      this.annot = bioAssay.getAnnotation();
    }

  }

  private final class DataTableReader extends TableReader {

    private BioAssay bioAssay;
    private Map<String, List<Integer>> mapInt =
        new HashMap<String, List<Integer>>();
    private Map<String, List<Double>> mapDouble =
        new HashMap<String, List<Double>>();
    private Map<String, List<String>> mapString =
        new HashMap<String, List<String>>();

    private Set<String> fieldsToRead;

    private int location;

    public void setFieldsToRead(final Set<String> fieldsToRead) {

      this.fieldsToRead = fieldsToRead;
    }

    @Override
    public void newRow(final int rowIndex) {

      if (rowIndex == 0)
        initFields();
      else {

        List<Integer> ints = this.mapInt.get(BioAssay.FIELD_NAME_LOCATION);
        ints.add(this.location);
      }

      this.location = 0;
    }

    @Override
    public void setFloat(final String field, final double value) {

      if (!this.fieldsToRead.contains(field))
        return;

      List<Double> doubles =
          this.mapDouble.get(CONVERTER.getBioAssayFieldName(field));
      doubles.add(value);
    }

    @Override
    public void setInteger(final String field, final int value) {

      if (!this.fieldsToRead.contains(field))
        return;

      if (ROW_FIELD.equals(field)) {
        this.location = BioAssayUtils.setRow(this.location, value);
        return;
      }

      if (COLUMN_FIELD.equals(field)) {
        this.location = BioAssayUtils.setColumn(this.location, value);
        return;
      }

      // Test if Id is an integer
      final String newField = CONVERTER.getBioAssayFieldName(field);
      if (BioAssay.FIELD_NAME_ID.equals(newField)) {
        List<String> strings =
            this.mapString.get(CONVERTER.getBioAssayFieldName(field));
        strings.add(String.valueOf(value));
        return;
      }

      List<Integer> ints =
          this.mapInt.get(CONVERTER.getBioAssayFieldName(field));
      ints.add(value);
    }

    @Override
    public void setText(final String field, final String value) {

      if (!this.fieldsToRead.contains(field))
        return;

      List<String> strings =
          this.mapString.get(CONVERTER.getBioAssayFieldName(field));
      strings.add(value);
    }

    private int calcRowCount() {

      final Annotation annot = this.bioAssay.getAnnotation();

      final int numRows;

      if (annot.containsProperty("SpotFinder_NumRows"))
        numRows = Integer.parseInt(annot.getProperty("SpotFinder_NumRows"));
      else if (annot.containsProperty("Grid_NumRows"))
        numRows = Integer.parseInt(annot.getProperty("Grid_NumRows"));
      else
        throw new NividicRuntimeException(
            "Invalid Agilent File: No information about the number of rows");

      final int numCols;

      if (annot.containsProperty("SpotFinder_NumRows"))
        numCols = Integer.parseInt(annot.getProperty("SpotFinder_NumCols"));
      else if (annot.containsProperty("Grid_NumRows"))
        numCols = Integer.parseInt(annot.getProperty("Grid_NumCols"));
      else
        throw new NividicRuntimeException(
            "Invalid Agilent File: No information about the number of columns");

      return numRows * (numCols / 2);
    }

    private void initFields() {

      final int rowCount = calcRowCount();

      // Add location field
      this.mapInt.put(BioAssay.FIELD_NAME_LOCATION, new ArrayList<Integer>(
          rowCount));

      for (final String fieldName : this.getFields()) {

        if (!this.fieldsToRead.contains(fieldName))
          continue;

        if (ROW_FIELD.equals(fieldName) || COLUMN_FIELD.equals(fieldName))
          continue;

        int fieldType = this.getFieldType(fieldName);
        final String newFieldName = CONVERTER.getBioAssayFieldName(fieldName);

        if (BioAssay.FIELD_NAME_ID.equals(newFieldName))
          fieldType = BioAssay.DATATYPE_STRING;

        switch (fieldType) {
        case BioAssay.DATATYPE_DOUBLE:

          this.mapDouble.put(newFieldName, new ArrayList<Double>(rowCount));
          break;

        case BioAssay.DATATYPE_INTEGER:
          this.mapInt.put(newFieldName, new ArrayList<Integer>(rowCount));
          break;

        case BioAssay.DATATYPE_STRING:
          this.mapString.put(newFieldName, new ArrayList<String>(rowCount));
          break;

        default:
          break;
        }

      }

    }

    public void resizeFields() {

      for (final String fieldName : this.mapDouble.keySet())
        this.bioAssay.setDataFieldDouble(fieldName, NividicUtils
            .toDoubleArray(this.mapDouble.get(fieldName)));

      for (final String fieldName : this.mapInt.keySet())
        this.bioAssay.setDataFieldInt(fieldName, NividicUtils
            .toIntArray(this.mapInt.get(fieldName)));

      for (final String fieldName : this.mapString.keySet())
        this.bioAssay.setDataFieldString(fieldName, NividicUtils
            .toArray(this.mapString.get(fieldName)));

    }

    //
    // Constructor
    //

    public DataTableReader(final BufferedReader reader, final BioAssay bioAssay) {

      super(reader);

      this.bioAssay = bioAssay;
      calcRowCount();

    }

  }

  @Override
  protected String getColumnField() {

    return COLUMN_FIELD;
  }

  @Override
  protected String[] getDoubleFieldNames() {

    return null;
  }

  @Override
  protected FieldNameConverter getFieldNameConverter() {

    return CONVERTER;
  }

  @Override
  protected String[] getFieldNamesOrder() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Get the version of the format of the file
   * @return The version of the file.
   */
  @Override
  public String getFormatVersion() {
    return null;
  }

  @Override
  protected String[] getIntFieldNames() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getMetaColumnField() {

    return null;
  }

  @Override
  protected String getMetaRowField() {

    return null;
  }

  @Override
  protected String getRowField() {

    return ROW_FIELD;
  }

  @Override
  protected String getSeparatorField() {

    return SEPARATOR;
  }

  /**
   * Read data
   * @return A BioAssay object
   * @throws NividicIOException if an error occurs while reading the stream
   */
  public BioAssay read() throws NividicIOException {

    if (getInputStream() == null)
      throw new NividicIOException("No stream to read");

    addFieldToRead(getRowField());
    addFieldToRead(getColumnField());

    for (int i = 0; i < DEFAULT_FIELDS_TO_READ.length; i++)
      addFieldToRead(DEFAULT_FIELDS_TO_READ[i]);

    final BioAssay bioAssay = BioAssayFactory.createBioAssay();

    final BufferedReader reader;

    try {

      reader =
          new BufferedReader(new InputStreamReader(getInputStream(),
              Globals.DEFAULT_FILE_ENCODING));

      final TableReader feparams = new AnnotationTableReader(reader, bioAssay);

      feparams.read();
    } catch (IOException e) {
      throw new NividicIOException(e);
    }

    final TableReader stats = new AnnotationTableReader(reader, bioAssay);

    try {
      stats.read();
    } catch (IOException e) {
      throw new NividicIOException(e);
    }

    final DataTableReader data = new DataTableReader(reader, bioAssay);

    try {
      data.readHeader();
      data.setFieldsToRead(createSetOfFieldsToRead(data.getFields()));
      data.read();
    } catch (IOException e) {
      throw new NividicIOException(e);
    }
    data.resizeFields();

    return bioAssay;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param filename to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public AgilentReader(final String filename) throws NividicIOException {

    this(new File(filename));
  }

  /**
   * Public constructor.
   * @param file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public AgilentReader(final File file) throws NividicIOException {

    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public AgilentReader(final InputStream is) throws NividicIOException {

    super(is);
  }

}
