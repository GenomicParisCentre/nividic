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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This implement a class to write ExpressionMatrixDimension objects
 * @author Lory Montout
 */
public class SimpleExpressionMatrixWriter extends ExpressionMatrixWriter {

  private BufferedWriter bw;
  private static final String SEPARATOR = "\t";
  private final String type = "id/M";

  /** Dimension separator. */
  public static final String DIMENSION_SEPARATOR = "$";

  private boolean writeAllColumns = true;
  private boolean writeAllDimensions = true;
  private Set<String> columnsToWrite = new HashSet<String>();
  private Set<String> dimensionsToWrite = new HashSet<String>();
  private boolean showDimensionName = true;

  /**
   * Write data.
   * @param em ExpressionMatrixDimension to write
   * @throws NividicIOException if an error occurs while writing data
   */
  public void write(final ExpressionMatrix em) throws NividicIOException {

    bw = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    final int countRow = em.getRowCount();

    final String[] ids = em.getRowNames();
    final String[] columnNames = getColumns(em);
    final ExpressionMatrixDimension[] dimensions = getDimensions(em);
    final Translator annot = getTranslator();

    try {

      // Write header

      bw.write(type);

      if (annot != null) {
        final String[] annotationFields = annot.getFields();
        for (int i = 0; i < annotationFields.length; i++) {

          bw.write(SEPARATOR);
          bw.write('"');
          bw.write(annotationFields[i]);
          bw.write('"');
        }
      }

      for (int i = 0; i < columnNames.length; i++) {
        for (int j = 0; j < dimensions.length; j++) {
          bw.write(SEPARATOR);
          bw.write('"');
          bw.write(columnNames[i]);
          bw.write('"');
          if (dimensions.length > 1 || this.showDimensionName) {
            bw.write(DIMENSION_SEPARATOR);
            bw.write(dimensions[j].getDimensionName());
          }
        }
      }

      bw.newLine();

      // Write data

      final double[][] values = getValues(columnNames, dimensions);

      for (int i = 0; i < countRow; i++) {

        final String id = ids[i];
        bw.write(id);

        if (annot != null) {
          final String[] annotationValues = annot.translate(id);
          for (int j = 0; j < annotationValues.length; j++) {
            bw.write(SEPARATOR);
            final String val = annotationValues[j];
            bw.write(val == null ? "" : val);
          }
        }

        for (int j = 0; j < values.length; j++) {

          bw.write(SEPARATOR);
          bw.write(Double.toString(values[j][i]));
        }

        bw.newLine();

      }

      this.bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

    addReaderHistoryEntry(em);
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

    final HistoryEntry entry = new HistoryEntry(
        this.getClass().getSimpleName(), HistoryActionType.SAVE, s
            + "RowNumbers=" + matrix.getRowCount() + ";ColumnNumber="
            + matrix.getColumnCount(), HistoryActionResult.PASS);

    matrix.getHistory().add(entry);

    return matrix;
  }

  /**
   * Create a bi dimensional array of double with matrix values
   * @param columnNames names of the columns to write
   * @param dimensions dimensions to write
   * @return a bi dimensional array of double with matrix values
   */
  private static double[][] getValues(final String[] columnNames,
      final ExpressionMatrixDimension[] dimensions) {

    final double[][] result = new double[columnNames.length * dimensions.length][];

    int count = 0;

    for (int j = 0; j < columnNames.length; j++)
      for (int k = 0; k < dimensions.length; k++)
        result[count++] = dimensions[k].getColumnToArray(columnNames[j]);

    return result;
  }

  /**
   * Get the name of the column to write
   * @param em ExpressionMatrix
   * @return The name of the column to write
   */
  private String[] getColumns(final ExpressionMatrix em) {

    if (this.writeAllColumns)
      return em.getColumnNames();

    List<String> s = new ArrayList<String>();

    String[] cols = em.getColumnNames();

    for (int i = 0; i < cols.length; i++)
      if (this.columnsToWrite.contains(cols[i]))
        s.add(cols[i]);

    return s.toArray(new String[s.size()]);
  }

  /**
   * Get the dimension to write
   * @param em ExpressionMatrix
   * @return The dimensions to write
   */
  private ExpressionMatrixDimension[] getDimensions(final ExpressionMatrix em) {

    if (this.writeAllDimensions)
      return em.getDimensions();

    List<String> dimensionNames = new ArrayList<String>();

    String[] cols = em.getDimensionNames();

    for (int i = 0; i < cols.length; i++)
      if (this.dimensionsToWrite.contains(cols[i]))
        dimensionNames.add(cols[i]);

    ExpressionMatrixDimension[] result = new ExpressionMatrixDimension[dimensionNames
        .size()];

    int count = 0;
    for (String name : dimensionNames)
      result[count++] = em.getDimension(name);

    return result;
  }

  /**
   * Add a dimension to write.
   * @param dimensionName Dimension to write to add
   */
  public void addDimensionToWrite(final String dimensionName) {

    this.writeAllDimensions = false;
    this.dimensionsToWrite.add(dimensionName);
  }

  /**
   * Add a column to write.
   * @param columnName Column to write to add
   */
  public void addColumnToWrite(final String columnName) {

    this.writeAllColumns = false;
    this.columnsToWrite.add(columnName);
  }

  /**
   * Set if the dimension name must be wrote if there is only one dimension.
   * @param enable The value to set
   */
  public void setShowDimensionName(final boolean enable) {

    this.showDimensionName = enable;
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
  public SimpleExpressionMatrixWriter(final File file)
      throws NividicIOException {
    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public SimpleExpressionMatrixWriter(final OutputStream is)
      throws NividicIOException {
    super(is);
  }

}
