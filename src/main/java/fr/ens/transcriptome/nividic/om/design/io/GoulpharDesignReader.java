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

package fr.ens.transcriptome.nividic.om.design.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.datasources.FileDataSource;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.DesignFactory;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormatRegistery;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This class allow to read goulphar param file as a design.
 * @author Laurent Jourdren
 */
public class GoulpharDesignReader extends InputStreamDesignReader {

  private boolean dataSourceNormalized = true;

  private final static String GOULPHAR_FILE_FIELD = "result.file";
  // private final static String GOULPHAR_FORMAT_FIELD = "software";
  private final static String GOULPHAR_AROMA_FILE_FIELD = "gpr";
  private final static String GOULPHAR_AROMA_SWAP_FIELD = "swap";

  private final static String GOULPHAR_RESULT_EXTENSION = "_norm.txt";

  private String baseDir = "";

  /**
   * Read data
   * @return a design object
   * @throws NividicIOException if an error occurs while reading data
   */
  @Override
  public Design read() throws NividicIOException {

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream())));

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    boolean firstLine = true;

    boolean goulpharAroma = true;
    int fileField = -1;
    int swapField = -1;

    Map<String, Integer> mapFields = new HashMap<String, Integer>();

    Design design = DesignFactory.createEmptyDesign();
    int count = 0;

    try {
      while ((line = br.readLine()) != null) {

        final String empty = line.trim();
        if ("".equals(empty) || empty.startsWith("#"))
          continue;

        final String[] fields = line.split(separator);

        if (firstLine) {

          for (int i = 0; i < fields.length; i++)
            mapFields.put(fields[i].trim(), i);

          if (mapFields.containsKey(GOULPHAR_FILE_FIELD)) {
            fileField = mapFields.get(GOULPHAR_FILE_FIELD);

            goulpharAroma = false;
          } else {
            fileField = mapFields.get(GOULPHAR_AROMA_FILE_FIELD);
            swapField = mapFields.get(GOULPHAR_AROMA_SWAP_FIELD);
          }

          firstLine = false;
        } else {

          final String filename = fields[fileField];
          final String rootFilename = rootFilename(new File(filename));

          final String slideId = rootFilename;
          final int sn = count;
          design.addSlide(slideId);

          String sourceFile = filename;

          final Slide slide = design.getSlide(slideId);

          if (this.dataSourceNormalized) {

            if (goulpharAroma)
              sourceFile = "normalised_chip" + (count + 1) + ".txt";
            else
              sourceFile = rootFilename + GOULPHAR_RESULT_EXTENSION;

            slide.setSourceFormat(BioAssayFormatRegistery.IDMA_BIOASSAY_FORMAT);
          }

          slide.setSource(new FileDataSource(this.baseDir, sourceFile));
          slide.getDescription().setSlideNumber(sn);

          if (goulpharAroma && swapField >= 0)
            slide.getDescription().setSwap(fields[swapField]);

          count++;
        }

      }
    } catch (IOException e) {
      throw new NividicIOException("Error while reading data");
    }

    return addReaderHistoryEntry(design);
  }

  /**
   * Add history entry for reading data
   * @param design Design readed
   * @return design
   */
  private Design addReaderHistoryEntry(final Design design) {

    String s;

    if (getDataSource() != null)
      s = "Source=" + getDataSource() + ";";
    else
      s = "";

    final HistoryEntry entry =
        new HistoryEntry(this.getClass().getSimpleName(),
            HistoryActionType.LOAD, s
                + "SlideNumber=" + design.getSlideCount() + ";LabelNumber="
                + design.getLabelCount(), HistoryActionResult.PASS);

    design.getHistory().add(entry);

    return design;
  }

  /**
   * Get the name of a file without the path and the extension
   * @param filename filename to process
   * @return the "root" of the filename
   */
  private static String rootFilename(final String filename) {

    if (filename == null)
      return null;

    int dotIndex = filename.lastIndexOf('.');
    if (dotIndex == -1)
      return filename;

    return filename.substring(0, dotIndex);
  }

  /**
   * Get the name of a file without the path and the extension
   * @param filename filename to process
   * @return the "root" of the filename
   */
  private static String rootFilename(final File file) {

    if (file == null)
      return null;

    return rootFilename(file.getName());
  }

  /**
   * Test if the sources to read are the normalized files.
   * @return Returns the dataSourceNormalized
   */
  public boolean isDataSourceNormalized() {
    return dataSourceNormalized;
  }

  /**
   * Set if the sources to read are the normalized files.
   * @param dataSourceNormalized The dataSourceNormalized to set
   */
  public void setDataSourceNormalized(final boolean dataSourceNormalized) {
    this.dataSourceNormalized = dataSourceNormalized;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public GoulpharDesignReader(final File file) throws NividicIOException {

    super(file);
    if (file != null)
      this.baseDir = file.getParent();
  }

  /**
   * Public constructor.
   * @param file file to read
   * @param dataSourceNormalized The dataSourceNormalized to set
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public GoulpharDesignReader(final File file,
      final boolean dataSourceNormalized) throws NividicIOException {

    this(file);
    setDataSourceNormalized(dataSourceNormalized);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public GoulpharDesignReader(final InputStream is) throws NividicIOException {

    super(is);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @param dataSourceNormalized The dataSourceNormalized to set
   * @throws NividicIOException if the stream is null
   */
  public GoulpharDesignReader(final InputStream is,
      final boolean dataSourceNormalized) throws NividicIOException {

    this(is);
    setDataSourceNormalized(dataSourceNormalized);
  }

}
