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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.util.StringUtils;
import java.util.regex.Pattern;

/**
 * This abstract class defines how to read text BioAssay File. Warning: when
 * reading integer fields in <code>read</code>, non int values will be
 * replaced by 0
 * @author Laurent Jourdren
 */
public abstract class BioAssayTextReader extends InputStreamBioAssayReader {

  private BufferedReader bufferedReader;
  private boolean commaDecimalSeparator;

  /**
   * Get the buffered reader of the stream.
   * @return Returns the bufferedReader
   */
  public BufferedReader getBufferedReader() {
    return this.bufferedReader;
  }

  /**
   * Test if the comma is the decimal separator.
   * @return true if the comma is the decimal separator
   */
  public boolean isCommaDecimalSeparator() {
    return this.commaDecimalSeparator;
  }

  /**
   * Test if the Strings quotes must be removed.
   * @return true if the quotes of the strings must be removed
   */
  protected abstract boolean isStringQuotesBeRemoved();

  //
  // Setters
  //

  /**
   * Set if the comma is the decimal separator.
   * @param enable the value to set
   */
  public void setCommaDecimalSeparator(final boolean enable) {

    this.commaDecimalSeparator = enable;
  }

  /**
   * Get the end tag of the stream. Null is not exists.
   * @return the end tag of the stream. Null is not exists
   */
  protected abstract String getEndTag();

  /**
   * Read data
   * @return A BioAssay object
   * @throws NividicIOException if an error occurs while reading the stream
   */
  public BioAssay read() throws NividicIOException {

    if (getInputStream() == null)
      throw new NividicIOException("No stream to read");

    final boolean comma = isCommaDecimalSeparator();

    addFieldToRead(getMetaRowField());
    addFieldToRead(getRowField());
    addFieldToRead(getMetaColumnField());
    addFieldToRead(getColumnField());

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream())));

    readHeader();
    final String[] existingFields = getFieldNamesOrder();

    if (existingFields == null || existingFields.length == 0)
      return getBioAssay();

    final boolean[] arrayFieldsToRead =
        createArrayOfFieldsToRead(existingFields);
    final boolean[] arrayIntFields =
        createArrayOfTypeFields(existingFields, getIntFieldNames());
    final boolean[] arrayDoubleFields =
        createArrayOfTypeFields(existingFields, getDoubleFieldNames());

    // Flag location fields as integer fields
    searchFieldAndFlagIt(existingFields, getMetaRowField(), arrayIntFields);
    searchFieldAndFlagIt(existingFields, getRowField(), arrayIntFields);
    searchFieldAndFlagIt(existingFields, getMetaColumnField(), arrayIntFields);
    searchFieldAndFlagIt(existingFields, getColumnField(), arrayIntFields);

    BufferedReader br = getBufferedReader();
    final Pattern pattern = Pattern.compile(getSeparatorField());
    String line = null;

    final int columnCount =
        getFieldNamesOrder() == null ? 0 : getFieldNamesOrder().length;

    final String endTag = getEndTag();

    final boolean removeQuotes = isStringQuotesBeRemoved();

    try {
      while ((line = br.readLine()) != null) {

        if (endTag != null && line.startsWith(endTag))
          break;

        String[] data = pattern.split(line);

        if (data.length == 1 && data[0].length() == 0)
          continue;

        for (int i = 0; i < columnCount; i++) {

          final String s2;

          if (i >= data.length)
            s2 = "";
          else {

            final String s = data[i].trim();
            s2 = removeQuotes ? StringUtils.removeDoubleQuotes(s) : s;
          }

          if (arrayFieldsToRead[i])
            if (arrayIntFields[i]) {
              int value;
              // integer values
              try {
                value = Integer.parseInt(s2);
              } catch (NumberFormatException e) {
                value = 0;
              }
              addDatafield(existingFields[i], value);

            } else if (arrayDoubleFields[i]) {
              double value;
              // Double values
              try {

                if (comma)
                  value = Double.parseDouble(s2.replace(',', '.'));
                else
                  value = Double.parseDouble(s2);

              } catch (NumberFormatException e) {
                value = Double.NaN;
              }
              addDatafield(existingFields[i], value);

            } else {

              // String values

              addDatafield(existingFields[i], new String(s2));
            }

        }

      }
    } catch (IOException e) {
      // e.printStackTrace();
      throw new NividicIOException("Error while reading the file");
    }

    try {
      getBufferedReader().close();
    } catch (IOException e1) {
      throw new NividicIOException("Error while closing the file");
    }

    return addReaderHistoryEntry(settingReadedDataInBioAssay());
  }

  /**
   * Add history entry for reading data
   * @param bioAssay Bioassay readed
   * @return bioAssay
   */
  private BioAssay addReaderHistoryEntry(final BioAssay bioAssay) {

    String s;

    if (getDataSource() != null)
      s = "Source=" + getDataSource() + ";";
    else
      s = "";

    final HistoryEntry entry =
        new HistoryEntry(this.getClass().getSimpleName(),
            HistoryActionType.LOAD, s
                + "RowNumbers=" + bioAssay.size() + ";ColumnNumber="
                + bioAssay.getFields().length, HistoryActionResult.PASS);

    bioAssay.getHistory().add(entry);

    return bioAssay;
  }

  //
  // Abstract methods
  //

  /**
   * ReadHeaders of the file.
   * @throws NividicIOException if an error occurs while reading ATF stream
   */
  public abstract void readHeader() throws NividicIOException;

  /**
   * Set the buffered reader of the stream.
   * @param bufferedReader The bufferedReader to set
   */
  private void setBufferedReader(final BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
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
  public BioAssayTextReader(final File file) throws NividicIOException {

    super(file);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public BioAssayTextReader(final InputStream is) throws NividicIOException {

    super(is);
  }

  /**
   * Public constructor.
   */
  protected BioAssayTextReader() {
  }

}
