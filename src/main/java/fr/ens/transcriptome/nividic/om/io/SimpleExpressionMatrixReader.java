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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ens.transcriptome.nividic.Globals;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.translators.MultiColumnTranslator;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This implement a class to read ExpressionMatrixDimension objects
 * @author Lory Montout
 */
public class SimpleExpressionMatrixReader extends
    InputStreamExpressionMatrixReader {

  /** Dimension separator. */
  public static final String DIMENSION_SEPARATOR = "$";
  private static final String DUPPLICATED_SUFFIX = "_DUPLICATED_";

  private ExpressionMatrix matrix;
  private MultiColumnTranslator translator;
  private String[] fieldNames;
  private String[] annotNames;
  private ExpressionMatrixDimension[] dimensions;
  private int firstDataColumn = 1;
  private boolean decimalSeparatorFound;
  private boolean commaDecimalSepartor;

  //
  // Other Methods
  //

  private String[] getColumnNamesFromFieldNames(final String[] fieldNames) {

    final int pos = this.firstDataColumn - 1;

    if (pos == 1)
      return fieldNames;

    final int len = fieldNames.length - pos;

    final String[] result = new String[len];

    System.arraycopy(fieldNames, pos, result, 0, len);

    return result;
  }

  private String[] getAnnotNamesFromFieldNames(final String[] fieldNames) {

    final int pos = this.firstDataColumn - 1;

    if (pos == 1)
      return new String[] {};

    final String[] result = new String[pos];

    System.arraycopy(fieldNames, 1, result, 0, pos);

    return result;
  }

  private void defineFieldNamesAndDimensions() {

    final String[] readFieldNames = getFieldNames();
    final String[] columnNames = getColumnNamesFromFieldNames(readFieldNames);
    this.annotNames = getAnnotNamesFromFieldNames(readFieldNames);

    if (columnNames == null) {

      this.fieldNames = null;
      this.dimensions = null;
      this.translator = null;

      return;
    }

    final List<String> fieldNames = new ArrayList<String>();
    final List<ExpressionMatrixDimension> dimensions =
        new ArrayList<ExpressionMatrixDimension>();

    if (annotNames.length == 0)
      this.translator = null;
    else
      this.translator = new MultiColumnTranslator(annotNames, false);

    for (int i = 0; i < columnNames.length; i++) {

      final String s = columnNames[i];
      final int indexSeparator = s.indexOf(DIMENSION_SEPARATOR);
      if (indexSeparator == -1) {
        fieldNames.add(s);
        dimensions.add(this.matrix.getDefaultDimension());
      } else {
        String fieldName = s.substring(0, indexSeparator);
        String dimensionName = s.substring(indexSeparator + 1, s.length());

        fieldNames.add(fieldName);

        ExpressionMatrixDimension dimension;

        if (dimensionName.equals(""))
          dimension = this.matrix.getDefaultDimension();
        else {
          if (!this.matrix.containsDimension(dimensionName))
            this.matrix.addDimension(dimensionName);
          dimension = this.matrix.getDimension(dimensionName);

        }

        dimensions.add(dimension);
      }
    }

    if (fieldNames.size() > 0) {
      this.fieldNames = new String[fieldNames.size()];
      fieldNames.toArray(this.fieldNames);
    }

    if (dimensions.size() > 0) {
      this.dimensions = new ExpressionMatrixDimension[dimensions.size()];
      dimensions.toArray(this.dimensions);
    }

  }

  /**
   * Read data
   * @return A ExpressionMatrixDimension object
   * @throws NividicIOException if an error occurs while reading the stream
   */
  public ExpressionMatrix read() throws NividicIOException {
    if (getInputStream() == null)
      throw new NividicIOException("No stream to read");

    try {
      setBufferedReader(new BufferedReader(new InputStreamReader(
          getInputStream(), Globals.DEFAULT_FILE_ENCODING)));
    } catch (UnsupportedEncodingException e1) {
      new NividicIOException("Unknown encoding: "
          + Globals.DEFAULT_FILE_ENCODING);
    }

    readHeader();

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    this.matrix = ExpressionMatrixFactory.createExpressionMatrix();

    defineFieldNamesAndDimensions();

    final String[] fieldNames = this.fieldNames;
    final String[] annotNames = this.annotNames;
    final ExpressionMatrixDimension[] dimensions = this.dimensions;

    setMatrixColumnNames(matrix, fieldNames);

    final int firstDataIndex = annotNames.length + 1;

    try {
      while ((line = br.readLine()) != null) {

        String[] data = line.split(separator);

        if (data.length == 0 || data[0].trim().startsWith("#"))
          continue;

        // Complete uncompleted rows
        if (data.length != fieldNames.length + annotNames.length) {

          String[] newData = new String[fieldNames.length + annotNames.length];
          Arrays.fill(newData, "");
          for (int i = 0; i < newData.length; i++) {

            if (i >= data.length)
              break;
            newData[i] = data[i];
          }

          data = newData;
        }

        // id column
        String ids = data[0];
        if (isStringQuotesBeRemoved())
          ids = new String(StringUtils.removeDoubleQuotes(ids));

        final String id;
        if (matrix.containsRow(ids))
          id = findNewRowDuplicatedName(ids);
        else
          id = ids;

        this.matrix.addRow(id);

        // Add annotations to translator
        if (firstDataIndex > 1) {

          final String[] annotData = new String[annotNames.length];
          System.arraycopy(data, 1, annotData, 0, annotData.length);

          this.translator.addRow(id, annotData);
        }

        // Double values
        for (int i = 1; i < data.length - annotNames.length; i++)
          // dimensions[i - 1].setValue(id, fieldNames[i],
          // parseValues(data[i]));
          dimensions[i].setValue(id, fieldNames[i], parseValues(data[i
              + firstDataIndex - 1]));

      }
    } catch (IOException e) {
      // e.printStackTrace();
      throw new NividicIOException("Error while reading the file");
    }

    try {
      getBufferedReader().close();
    } catch (IOException e) {
      throw new NividicIOException("Error while closing the file"
          + e.getMessage());
    }

    return addReaderHistoryEntry(this.matrix);
  }

  private double parseValues(final String s) {

    double value;

    if (!this.decimalSeparatorFound)
      foundDecimalSeparator(s);

    try {

      String toParse = s.trim();

      if (this.commaDecimalSepartor)
        toParse = s.replace(',', '.');

      value = Double.parseDouble(toParse);

    } catch (NumberFormatException e) {
      value = Double.NaN;
    }

    return value;
  }

  private void foundDecimalSeparator(final String s) {

    int index = s.indexOf('.');

    if (index != -1) {
      this.commaDecimalSepartor = false;
      this.decimalSeparatorFound = true;
    }

    index = s.indexOf(',');

    if (index != -1) {
      this.commaDecimalSepartor = true;
      this.decimalSeparatorFound = true;
    }

  }

  private String findNewRowDuplicatedName(final String rowName) {

    int i = 1;
    while (this.matrix.containsRow(rowName + DUPPLICATED_SUFFIX + i))
      i++;

    return rowName + DUPPLICATED_SUFFIX + i;
  }

  /**
   * Add history entry for reading data
   * @param bioAssay Bioassay readed
   * @return bioAssay
   */
  private ExpressionMatrix addReaderHistoryEntry(final ExpressionMatrix matrix) {

    String s;

    if (getDataSource() != null)
      s = "Source=" + getDataSource() + ";";
    else
      s = "";

    final HistoryEntry entry =
        new HistoryEntry(this.getClass().getSimpleName(),
            HistoryActionType.LOAD, s
                + "RowNumbers=" + matrix.getRowCount() + ";ColumnNumber="
                + matrix.getColumnCount(), HistoryActionResult.PASS);

    matrix.getHistory().add(entry);

    return matrix;
  }

  /**
   * Get if exists the dimension separator.
   * @return if exists the dimension separator
   */
  public String getDimensionSeparator() {

    return DIMENSION_SEPARATOR;
  }

  /**
   * Get the first column with data.
   * @return Returns the firstDataColumn
   */
  public int getFirstDataColumn() {
    return firstDataColumn;
  }

  /**
   * Set the first column with data
   * @param firstDataColumn The index of the first column with data
   */
  public void setFirstDataColumn(final int firstDataColumn) {

    if (firstDataColumn < 1)
      return;
    this.firstDataColumn = firstDataColumn;
  }

  /**
   * Get once data read the translator for the annotation of data.
   * @return a new translator object or null if there is no annotation with data
   */
  public Translator getTranslator() {

    return this.translator;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public SimpleExpressionMatrixReader(final File file)
      throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public SimpleExpressionMatrixReader(final InputStream is)
      throws NividicIOException {
    super(is);
  }

  /**
   * Public constructor.
   * @param file file to read
   * @param firstDataColumn The index of the first column with data
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public SimpleExpressionMatrixReader(final File file, final int firstDataColumn)
      throws NividicIOException {
    this(file);
    setFirstDataColumn(firstDataColumn);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @param firstDataColumn The index of the first column with data
   * @throws NividicIOException if the stream is null
   */
  public SimpleExpressionMatrixReader(final InputStream is,
      final int firstDataColumn) throws NividicIOException {
    this(is);
    setFirstDataColumn(firstDataColumn);
  }

}