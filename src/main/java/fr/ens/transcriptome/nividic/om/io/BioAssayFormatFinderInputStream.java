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
import java.util.ArrayList;
import java.util.List;

public class BioAssayFormatFinderInputStream extends InputStream {

  private InputStream is;

  private static final int cacheSize = 5000;

  private static final String MAGIC_IMAGENE = "Begin Header";
  private static final String MAGIC_IDMA = "ID\tName\tR\tRb\tG\tGb\tMnorm\tA";
  private static final String MAGIC_IDMA_QUOTES = "\"ID\"\t\"Name\"\t\"R\"\t\"Rb\"\t\"G\"\t\"Gb\"\t\"Mnorm\"\t\"A\"";
  private static final String MAGIC_TOTAL_SUMMARY = "Name\tmedianMnorm\tmedianA\tSDMnorm\tn\ttotal n";
  private static final String MAGIC_TOTAL_SUMMARY_QUOTES = "\"Name\"\t\"medianMnorm\"\t\"medianA\"\t\"SDMnorm\"\t\"n\"\t\"total n\"";

  private static final String MAGIC_ATF = "ATF";
  private static final String MAGIC_GPR = "Type=GenePix Results";
  private static final String MAGIC_GAL = "Type=GenePix ArrayList";

  private BioAssayFormat format;
  private boolean testFormatDone;

  private int cacheIndex;
  private byte[] cache;

  private BioAssayFormat findType() throws NividicIOException {

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
      List<String> lines = new ArrayList<String>();

      while ((line = reader.readLine()) != null)
        lines.add(line);

      return testTypes(lines);

    } catch (IOException e) {

      throw new NividicIOException("Unable to define the format");
    }

  }

  private BioAssayFormat testTypes(final List<String> lines) {

    if (lines.size() < 1)
      return null;

    final String firstLine = lines.get(0);

    if (firstLine.startsWith("Begin Header"))
      return BioAssayFormat.IMAGENE;

    if (firstLine.startsWith(MAGIC_IDMA)
        || firstLine.startsWith(MAGIC_IDMA_QUOTES))
      return BioAssayFormat.IDMA;

    if (firstLine.startsWith(MAGIC_TOTAL_SUMMARY)
        || firstLine.startsWith(MAGIC_TOTAL_SUMMARY_QUOTES))
      return BioAssayFormat.TOTALSUMMARY;

    if (firstLine.startsWith(MAGIC_ATF)) {

      for (String line : lines) {
        if (line.contains(MAGIC_GPR))
          return BioAssayFormat.GPR;

        if (line.contains(MAGIC_GAL))
          return BioAssayFormat.GAL;
      }

    }

    return null;
  }

  /**
   * Get the format of the data to read
   * @return The format of the data to read
   * @throws NividicIOException if an error occurs while reading data
   */
  public BioAssayFormat getBioAssayFormat() throws NividicIOException {

    if (!this.testFormatDone)
      this.format = findType();

    return this.format;
  }

  /**
   * Get the BioAssayReader for the data
   * @return the BioAssayReader for the data
   * @throws IOException if an error occurs while reading data
   * @throws NividicIOException if an error occurs while reading data
   */
  public BioAssayReader getBioAssayReader() throws IOException,
      NividicIOException {

    BioAssayFormat format = getBioAssayFormat();

    return format == null ? null : format.getBioAssayReader(this);
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
  public BioAssayFormatFinderInputStream(final InputStream is) {

    if (is == null)
      throw new NullPointerException("The inputStream is null");

    this.is = is;
  }

}
