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
import java.io.InputStreamReader;

import fr.ens.transcriptome.nividic.om.translators.MultiColumnTranslator;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This class define a reader for load annotation into a translator.
 * @author Laurent Jourdren
 */
public class MultiColumnTranslatorReader {

  private InputStream is;

  private BufferedReader bufferedReader;
  private static final String SEPARATOR = "\t";
  private boolean removeQuotes = true;

  //
  // Getters
  //

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
   * Test if quotes of the fields must be removed
   * @return Returns the removeQuotes
   */
  public boolean isRemoveQuotes() {
    return removeQuotes;
  }

  //
  // Setters
  //

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
   * Set if the quotes of the fields must be removed
   * @param removeQuotes The removeQuotes to set
   */
  public void setRemoveQuotes(boolean removeQuotes) {
    this.removeQuotes = removeQuotes;
  }

  //
  // Other methods
  //

  /**
   * Read the design.
   * @return a new Design object
   * @throws NividicIOException if an error occurs while reading the design
   */
  public MultiColumnTranslator read() throws NividicIOException {

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream())));

    final boolean removeQuotes = isRemoveQuotes();

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    MultiColumnTranslator result = null;

    try {

      while ((line = br.readLine()) != null) {

        String[] cols = line.split(separator);

        if (removeQuotes)
          for (int i = 0; i < cols.length; i++)
            cols[i] = StringUtils.removeDoubleQuotesAndTrim(cols[i]);

        if (result == null)
          result = new MultiColumnTranslator(cols);
        else
          result.addRow(cols);

      }

    } catch (IOException e) {

      throw new NividicIOException("Error while reading the file");
    }

    return result;
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
  public MultiColumnTranslatorReader(final File file) throws NividicIOException {

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
  public MultiColumnTranslatorReader(final InputStream is)
      throws NividicIOException {

    setInputStream(is);
  }

}
