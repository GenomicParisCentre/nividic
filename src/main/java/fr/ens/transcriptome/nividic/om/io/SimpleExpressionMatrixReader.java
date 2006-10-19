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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This implement a class to read ExpressionMatrixDimension objects
 * @author Lory Montout
 */
public class SimpleExpressionMatrixReader extends ExpressionMatrixReader {

  /** Dimension separator. */
  public static final String DIMENSION_SEPARATOR = "$";

  private ExpressionMatrix matrix;
  private String[] fieldNames;
  private ExpressionMatrixDimension[] dimensions;

  //
  // Other Methods
  //

  private void defineFieldNamesAndDimensions() {

    final String[] columnNames = getFieldNames();

    if (columnNames == null) {

      this.fieldNames = null;
      this.dimensions = null;

      return;
    }

    final ArrayList fieldNames = new ArrayList();
    final ArrayList dimensions = new ArrayList();

    for (int i = 0; i < columnNames.length; i++) {

      final String s = columnNames[i];
      final int indexSeparator = s.indexOf(DIMENSION_SEPARATOR);
      if (indexSeparator == -1)
        fieldNames.add(s);
      else {
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

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream())));

    readHeader();

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    this.matrix = ExpressionMatrixFactory.createExpressionMatrix();

    defineFieldNamesAndDimensions();

    final String[] fieldNames = this.fieldNames;
    final ExpressionMatrixDimension[] dimensions = this.dimensions;

    setMatrixColumnNames(matrix, fieldNames);

    try {
      while ((line = br.readLine()) != null) {

        String[] data = line.split(separator);

        if (data.length != fieldNames.length || data[0].trim().startsWith("#"))
          continue;

        // id column
        String ids = data[0];
        if (isStringQuotesBeRemoved())
          ids = new String(StringUtils.removeDoubleQuotes(ids));

        final String id = ids;

        // Double values
        for (int i = 1; i < data.length; i++) {

          double value;

          try {
            value = Double.parseDouble(data[i].trim());

          } catch (NumberFormatException e) {
            value = Double.NaN;
          }

          dimensions[i - 1].setValue(id, fieldNames[i], value);

        }

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

    return this.matrix;
  }

  /**
   * Get if exists the dimension separator.
   * @return if exists the dimension separator
   */
  public String getDimensionSeparator() {

    return DIMENSION_SEPARATOR;
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
}