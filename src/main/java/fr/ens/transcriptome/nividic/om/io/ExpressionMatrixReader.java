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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * Abstract class to read ExpressionMatrixDimension files
 * @author Lory Montout
 */
public abstract class ExpressionMatrixReader {

  private InputStream is;

  private BufferedReader bufferedReader;
  private static final String SEPARATOR = "\t";
  private boolean readHeadersDone;

  private Map data = new HashMap();

  /** List of field names. */
  private String[] fieldNames;

  //
  // Getters
  //

  /**
   * Get the fields names.
   * @return The fields names
   */
  protected String[] getFieldNames() {
    return fieldNames;
  }

  /**
   * Get the input stream.
   * @return Returns the input stream
   */
  protected InputStream getInputStream() {
    return is;
  }

  /**
   * Get the buffered reader of the stream.
   * @return Returns the bufferedReader
   */
  protected BufferedReader getBufferedReader() {
    return bufferedReader;
  }

  /**
   * Get the separator field of the file.
   * @return The separator field of the file
   */
  protected String getSeparatorField() {
    return SEPARATOR;
  }

  /**
   * Get the data field of the file.
   * @return the data field of the file
   */
  protected Map getData() {
    return this.data;
  }

  //
  // Setters
  //

  /**
   * Set the fields names.
   * @param strings the fieldnames to set
   */
  protected void setFieldNames(final String[] strings) {
    fieldNames = strings;
  }

  /**
   * Set the buffered reader of the stream.
   * @param bufferedReader The bufferedReader to set
   */
  protected void setBufferedReader(final BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  /**
   * Set the input stream.
   * @param is The input stream to set
   * @throws NividicIOException if the stream is null
   */
  protected void setInputStream(final InputStream is) throws NividicIOException {

    if (is == null)
      throw new NividicIOException("No stream to read");
    this.is = is;
  }

  /**
   * Set the data field of the file.
   */
  protected void setData(final Map data) {
    this.data = data;
  }

  /**
   * fill the matrix with the names of the column from the readed file
   * @param em the matrix to fill
   * @param columnFromFile the names of the columns
   */
  protected void setMatrixColumnNames(final ExpressionMatrix em,
      final String[] columnFromFile) {

    String dimensionSeparator = getDimensionSeparator();

    for (int i = 1; i < columnFromFile.length; i++) {

      final String columnName;

      if (dimensionSeparator == null)
        columnName = columnFromFile[i];
      else {

        String[] names = columnFromFile[i].split("\\" + dimensionSeparator);

        columnName = names[0];

        if (names.length == 2 && !em.containsDimension(names[1]))
          em.addDimension(names[2]);

      }

      if (!em.containsColumn(columnName))
        em.addColumn(columnName);
    }
  }

  /**
   * Get if exists the dimension separator.
   * @return if exists the dimension separator
   */
  public abstract String getDimensionSeparator();

  //
  // Other methods
  //

  /**
   * ReadHeaders of the file.
   * @throws NividicIOException if an error occurs while reading ATF stream
   */
  protected void readHeader() throws NividicIOException {
    if (this.readHeadersDone)
      return;

    readFieldnames();

    this.readHeadersDone = true;
  }

  protected boolean readFieldnames() throws NividicIOException {

    try {
      String line = getBufferedReader().readLine();
      if (line == null)
        throw new NividicIOException("Invalid fields names description");

      String[] result = line.split(SEPARATOR);
      for (int i = 0; i < result.length; i++) {
        result[i] = StringUtils.removeDoubleQuotes(result[i]);

      }
      if (isSameName(result))
        throw new NividicIOException("Two column have the same name");

      setFieldNames(result);

    } catch (IOException e) {
      throw new NividicIOException("Error while reading the file");

    }
    return true;

  }

  /**
   * Test if a string is present twice in an array of string.
   * @param strings Strings to test.
   * @return <b>true </b> if a string exist in double
   */
  protected boolean isSameName(final String[] strings) {

    if (strings == null)
      return false;

    for (int i = 0; i < strings.length; i++)
      for (int j = i + 1; j < strings.length; j++)
        if (strings[i].equals(strings[j]))
          return true;

    return false;
  }

  /**
   * Test if the Strings quotes must be removed.
   * @return true if the quotes of the strings must be removed
   */
  public boolean isStringQuotesBeRemoved() {
    return false;
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
  public ExpressionMatrixReader(final File file) throws NividicIOException {

    if (file == null)
      throw new NividicIOException("No file to load");

    try {
      setInputStream(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new NividicIOException("Error while reading file : "
          + file.getName());
    }

  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ExpressionMatrixReader(final InputStream is) throws NividicIOException {

    setInputStream(is);
  }

}