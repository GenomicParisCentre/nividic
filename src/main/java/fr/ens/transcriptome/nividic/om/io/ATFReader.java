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
import java.util.StringTokenizer;

import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This implements a BioAssay reader for ATF files.
 * @author Laurent Jourdren
 */
public class ATFReader extends BioAssayTextReader {

  private boolean readHeadersDone;
  private String atfVersion;
  private ReadedDimensions readedDimensions = new ReadedDimensions();

  private static final String MAGICSTRING = "ATF";

  /** Field name for blocks. */
  public static final String FIELD_NAME_BLOCK = "Block";
  /** Field name for column. */
  public static final String FIELD_NAME_COLUMN = "Column";
  /** Field name for row. */
  public static final String FIELD_NAME_ROW = "Row";

  private static final String SEPARATOR = "\t";

  /**
   * A Classe to store headers dimensions
   * @author Laurent Jourdren
   */
  private class ReadedDimensions {
    private int numberHeaders;
    private int numberDataFields;
    private String[] fieldNames;

    /**
     * Get the fields names.
     * @return The fields names
     */
    public String[] getFieldNames() {
      return fieldNames;
    }

    /**
     * Get the number of the dataFields
     * @return The number of the dataFields
     */
    public int getNumberDataFields() {
      return numberDataFields;
    }

    /**
     * Get the number of the headers
     * @return The number of the headers
     */
    public int getNumberHeaders() {
      return numberHeaders;
    }

    /**
     * Set the fields names.
     * @param strings the fieldnames to set
     */
    public void setFieldNames(final String[] strings) {
      fieldNames = strings;
    }

    /**
     * Set the number of the datafields.
     * @param i The number of the datafields
     */
    public void setNumberDataFields(final int i) {
      numberDataFields = i;
    }

    /**
     * Set the number of the headers.
     * @param i The number of the headers
     */
    public void setNumberHeaders(final int i) {
      numberHeaders = i;
    }

  }

  //
  // Implememented methods
  //

  /**
   * Read the headers from the ATF stream.
   * @throws NividicIOException if an error occurs while reading ATF stream
   */
  public void readHeader() throws NividicIOException {

    if (this.readHeadersDone)
      return;

    if (!testIfATF())
      throw new NividicIOException("This is not an ATF file");

    if (!setDimensionsFile())
      throw new NividicIOException("Invalid second header record");

    if (!readOptionalRecords())
      throw new NividicIOException("Invalid optional header record");

    if (!readFieldnames())
      throw new NividicIOException("Invalid fields names description");

    this.readHeadersDone = true;
  }

  /**
   * Get the names of the integer fields.
   * @return An string array of field names
   */
  public String[] getIntFieldNames() {
    return null;
  }

  /**
   * Get the names of the double fields.
   * @return An string array of field names
   */
  public String[] getDoubleFieldNames() {
    return null;
  }

  /**
   * Get the fields names order.
   * @return A string array for fieldnames of the file
   */
  public String[] getFieldNamesOrder() {
    return getReadedDimensions().getFieldNames();
  }

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  public FieldNameConverter getFieldNameConverter() {
    return null;
  }

  /**
   * Get the version of the format of the file
   * @return The version of the file.
   */
  public String getFormatVersion() {
    return this.atfVersion;
  }

  /**
   * Get the meta row field name.
   * @return The name of the field for meta row
   */
  public String getMetaRowField() {
    return null;
  }

  /**
   * Get the meta column field name.
   * @return The name of the field for meta column
   */
  public String getMetaColumnField() {
    return FIELD_NAME_BLOCK;
  }

  /**
   * Get the row field name.
   * @return The name of the field for row
   */
  public String getRowField() {
    return FIELD_NAME_ROW;
  }

  /**
   * Get the column field name.
   * @return The name of the field for column
   */
  public String getColumnField() {
    return FIELD_NAME_COLUMN;
  }

  /**
   * Get the separator field of the file.
   * @return The separator field of the file
   */
  public String getSeparatorField() {
    return SEPARATOR;
  }

  /**
   * Test if the Strings quotes must be removed.
   * @return true if the quotes of the strings must be removed
   */
  public boolean isStringQuotesBeRemoved() {
    return true;
  }

  /**
   * Get the end tag of the stream. Null is not exists.
   * @return the end tag of the stream. Null is not exists
   */
  public String getEndTag() {
    return null;
  }

  //
  // Other methods
  //

  /**
   * Get the readed dimensions.
   * @return The readed dimensions
   */
  private ReadedDimensions getReadedDimensions() {
    return readedDimensions;
  }

  /**
   * Set the ATF version of the stream.
   * @param string
   */
  private void setATFVersion(final String string) {
    atfVersion = string;
  }

  /**
   * Test if the stream is an ATF stream.
   * @return <b>true </b> if the stream is an ATF stream.
   */
  private boolean testIfATF() throws NividicIOException {

    try {

      String line = getBufferedReader().readLine();
      if (line == null)
        return false;

      if (!line.startsWith(MAGICSTRING))
        throw new NividicIOException("This file is not an ATF file");

      StringTokenizer st = new StringTokenizer(line, "\t ");

      if (!st.hasMoreTokens())
        return false;
      st.nextToken();
      if (!st.hasMoreTokens())
        return false;

      String version = st.nextToken();
      if (st.hasMoreTokens())
        return false;

      setATFVersion(version);

      if (!version.equals("1.0"))
        throw new NividicIOException("Bad version of ATF file."
            + " I can only read version 1.0 of ATF files");

    } catch (IOException e) {
      throw new NividicIOException("Error while reading the file");
    }
    return true;
  }

  private boolean setDimensionsFile() throws NividicIOException {

    try {
      String line = getBufferedReader().readLine();

      if (line == null)
        return false;
      StringTokenizer st = new StringTokenizer(line, "\t ");

      if (!st.hasMoreTokens())
        return false;
      String nbHeaders = st.nextToken();
      if (!st.hasMoreTokens())
        return false;
      String nbDataFields = st.nextToken();
      if (st.hasMoreTokens())
        return false;

      try {

        getReadedDimensions().setNumberHeaders(Integer.parseInt(nbHeaders));
        getReadedDimensions().setNumberDataFields(
            Integer.parseInt(nbDataFields));

        if (getReadedDimensions().getNumberHeaders() < 0)
          throw new NividicIOException(
              "Invalid number of optional header record : " + nbHeaders);

        if (getReadedDimensions().getNumberDataFields() < 1)
          throw new NividicIOException("Invalid number of data column : "
              + nbDataFields);

      } catch (NumberFormatException e) {
        throw new NividicIOException("Invalid second header record"
            + " (parsing Integer data)");

      }
    } catch (IOException e) {
      throw new NividicIOException("Error while reading the file");
    }

    return true;
  }

  private boolean readOptionalRecords() throws NividicIOException {

    try {
      BufferedReader br = getBufferedReader();

      for (int i = 0; i < getReadedDimensions().getNumberHeaders(); i++) {

        String line = br.readLine();

        if (line == null)
          return false;
        line = StringUtils.removeDoubleQuotesAndTrim(line);

        if (line.equals(""))
          return false;

        StringTokenizer st = new StringTokenizer(line, "=");
        if (!st.hasMoreTokens())
          return false;
        String key = st.nextToken();
        String value = "";
        if (st.hasMoreTokens())
          value = st.nextToken();

        addAnnotation(key.trim(), value);
      }

    } catch (IOException e) {
      throw new NividicIOException("Error while reading the file");
    }

    return true;
  }

  private boolean readFieldnames() throws NividicIOException {

    try {
      String line = getBufferedReader().readLine();
      if (line == null)
        return false;
      int count = 0;

      ReadedDimensions dim = getReadedDimensions();

      String[] result = new String[dim.getNumberDataFields()];
      StringTokenizer st = new StringTokenizer(line, SEPARATOR);

      while (st.hasMoreTokens()) {
        String s = st.nextToken();
        s = s.trim();
        result[count++] = StringUtils.removeDoubleQuotesAndTrim(s);
        if (count > dim.getNumberDataFields())
          throw new NividicIOException("Invalid column number");

      }

      if (count != dim.getNumberDataFields())
        throw new NividicIOException("Invalid column number");

      if (isSameName(result))
        throw new NividicIOException("Two column have the same name");

      dim.setFieldNames(result);

    } catch (IOException e) {
      throw new NividicIOException("Error while reading the file");
    }

    return true;
  }

  /**
   * Test if a string exist in double in an array string.
   * @param strings Strings to test.
   * @return <b>true </b> if if a string exist in double
   */
  private static boolean isSameName(final String[] strings) {

    if (strings == null)
      return false;

    for (int i = 0; i < strings.length; i++)
      for (int j = i + 1; j < strings.length; j++)
        if (strings[i].equals(strings[j]))
          return true;

    return false;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public ATFReader(final File file) throws NividicIOException {

    super(file);

  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ATFReader(final InputStream is) throws NividicIOException {

    super(is);
  }

}