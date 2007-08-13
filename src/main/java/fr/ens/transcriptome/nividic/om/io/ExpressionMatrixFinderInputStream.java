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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class allow to auto determinate annotations and data columns of matrix
 * files.
 * @author Laurent Jourdren
 */
public class ExpressionMatrixFinderInputStream extends InputStream {

  private InputStream is;

  private static final int cacheSize = 20000;

  private int cacheIndex;
  private byte[] cache;

  private Class[] columnTypes;

  private int firstDataColomn = -1;
  private boolean confidence;

  private void readCache() throws NividicIOException {

    final byte[] readed = new byte[cacheSize];

    try {

      int count = is.read(readed);

      if (count != cacheSize) {
        this.cache = new byte[count];
        System.arraycopy(readed, 0, this.cache, 0, count);
      } else
        this.cache = readed;

      InputStream bais = new ByteArrayInputStream(this.cache);
      BufferedReader reader = new BufferedReader(new InputStreamReader(bais));

      String line;

      boolean firstLine = true;

      while ((line = reader.readLine()) != null) {

        if (line.startsWith("#"))
          continue;

        String[] splitedLine = line.split("\t");

        if (firstLine) {

          this.columnTypes = new Class[splitedLine.length];
          firstLine = false;
        } else
          findColumnsType(splitedLine);

      }

      // return testTypes(lines);

    } catch (IOException e) {

      throw new NividicIOException("Unable to define the format");
    }

  }

  private void findColumnsType(final String[] splitedLine) {

    for (int i = 0; i < splitedLine.length; i++) {

      if (i >= this.columnTypes.length)
        return;

      if (String.class.equals(this.columnTypes[i])
          || Double.class.equals(this.columnTypes[i]))
        continue;

      String trimedCell = splitedLine[i].trim();

      if (trimedCell.length() == 0)
        continue;

      if ("NaN".equals(trimedCell) || "NA".equals(trimedCell))
        this.columnTypes[i] = Float.class;

      boolean stringChars = false;
      boolean doubleChars = false;
      int doubleCharsCount = 0;

      for (int j = 0; j < trimedCell.length(); j++) {

        char c = trimedCell.charAt(j);

        if (!Character.isDigit(c))
          if (c == ',' || c == '.') {
            doubleChars = true;
            doubleCharsCount++;
          } else
            stringChars = true;

      }

      if (stringChars || (doubleChars && doubleCharsCount > 1))
        this.columnTypes[i] = String.class;
      else if (doubleChars)
        this.columnTypes[i] = Double.class;
      else
        this.columnTypes[i] = Integer.class;

    }

  }

  private void analyse() throws NividicIOException {

    readCache();

    int lastStringColumn = -1;
    int lastIntegerColumn = -1;
    int firstDataColumn = -1;
    boolean confidence = false;

    for (int i = 0; i < this.columnTypes.length; i++) {

      if (String.class.equals(this.columnTypes[i])) {
        lastStringColumn = i;
        continue;
      }

      if (Integer.class.equals(this.columnTypes[i])) {
        lastIntegerColumn = i;
        continue;
      }

      if (Double.class.equals(this.columnTypes[i])
          || Float.class.equals(this.columnTypes[i])) {

        if (lastStringColumn == i - 1) {

          firstDataColumn = i;
          confidence = true;
          break;
        }

        if (lastIntegerColumn == i - 1) {

          firstDataColumn = i;
          break;
        }

      }

    }

    if (firstDataColomn == -1)
      firstDataColumn = lastIntegerColumn;

    this.firstDataColomn = firstDataColumn;
    this.confidence = confidence;

  }

  /**
   * Get the index of the first column with data.
   * @return the index of the first column with data
   * @throws NividicIOException if an error occurs while reading data
   */
  public int getFirstDataColumn() throws NividicIOException {

    if (this.firstDataColomn == -1)
      analyse();

    return this.firstDataColomn;
  }

  /**
   * Get the confidence in the finding of the first column with data.
   * @return true if the confidence in the index of the first column with data
   *         is high
   * @throws NividicIOException if an error occurs while reading data
   */
  public boolean isConfidence() throws NividicIOException {

    if (this.firstDataColomn == -1)
      analyse();

    return this.confidence;

  }

  @Override
  public int read() throws IOException {

    if (cacheIndex < cache.length)
      return cache[cacheIndex++];

    return is.read();
  }

  //
  // Constructor
  //

  /**
   * Public constructor
   * @param is InputStream to read
   */
  public ExpressionMatrixFinderInputStream(final InputStream is) {

    if (is == null)
      throw new NullPointerException("The inputStream is null");

    this.is = is;
  }

}
