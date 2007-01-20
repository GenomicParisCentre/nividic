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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This abstract class define a reader for generic tabular files.
 * @author Laurent Jourdren
 */
public abstract class BioAssayTabularReader extends BioAssayTextReader {

  private boolean readHeadersDone;

  private static final String SEPARATOR = "\t";

  /** List of field names. */
  private String[] fieldNames;

  /**
   * Get the fields names.
   * @return The fields names
   */
  public String[] getFieldNames() {
    return fieldNames;
  }

  /**
   * Set the fields names.
   * @param strings the fieldnames to set
   */
  public void setFieldNames(final String[] strings) {
    fieldNames = strings;
  }

  //
  // Implememented methods
  //

  /**
   * Read the header of an ID-M-A file
   * @throws NividicIOException if an error occurs while reading ID-M-A stream
   */
  public void readHeader() throws NividicIOException {

    if (this.readHeadersDone)
      return;

    if (!readFieldnames())
      throw new NividicIOException("Invalid fields names description");

    this.readHeadersDone = true;
  }

  /**
   * read field names in the header of the ID-M-A file
   * @return true if the reading is correctly done
   * @throws NividicIOException an error occures during the reading of the file
   */
  private boolean readFieldnames() throws NividicIOException {

    try {
      String line = getBufferedReader().readLine();
      if (line == null)
        return false;

      String[] result = line.split(getSeparatorField());

      for (int i = 0; i < result.length; i++) {
        result[i] = StringUtils.removeDoubleQuotes(result[i]);

        Map map = new HashMap();

        if (map.containsKey(result[i]))
          throw new NividicIOException("Two column have the same name");

        map.put(result[i], "");
      }

      // TODO check if <code>isSameName(result)</code> and
      // <code>map.containsKey(result[i])</code>
      // TODO have the same function
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
  private boolean isSameName(final String[] strings) {

    if (strings == null)
      return false;

    for (int i = 0; i < strings.length; i++)
      for (int j = i + 1; j < strings.length; j++)
        if (strings[i].equals(strings[j]))
          return true;

    return false;
  }

  /**
   * Get the separator field of the file.
   * @return The separator field of the file
   */
  protected String getSeparatorField() {
    return SEPARATOR;
  }

  /**
   * Get the version of the format of the file
   * @return The version of the file.
   */
  public String getFormatVersion() {

    return null; // tabular files have no version format
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

    return null;
  }

  /**
   * Get the row field name.
   * @return The name of the field for row
   */
  protected String getRowField() {

    return null;
  }

  /**
   * Get the column field name.
   * @return The name of the field for column
   */
  protected String getColumnField() {

    return null;
  }

  /**
   * Test if the Strings quotes must be removed.
   * @return true if the quotes of the strings must be removed
   */
  protected boolean isStringQuotesBeRemoved() {

    return true;
  }

  /**
   * Get the end tag of the stream. Null is not exists.
   * @return the end tag of the stream. Null is not exists
   */
  protected String getEndTag() {

    return null;
  }

  /**
   * Get the names of the fields to read by default.
   * @the names of the fields to read by defaults
   */
  protected String[] getDefaultFieldToRead() {

    return null;
  }

  //
  // Other methods
  //

  /**
   * adds fields to read
   */
  private void addDefaultFieldsToRead() {

    String[] fieldsToRead = getDefaultFieldToRead();

    if (fieldsToRead == null)
      return;

    for (int i = 0; i < fieldsToRead.length; i++)
      addFieldToRead(fieldsToRead[i]);

  }

  //
  // Constructors
  //

  /**
   * Public constructor
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public BioAssayTabularReader(final File file) throws NividicIOException {

    super(file);
    addDefaultFieldsToRead();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public BioAssayTabularReader(final InputStream is) throws NividicIOException {

    super(is);
    addDefaultFieldsToRead();
  }

}
