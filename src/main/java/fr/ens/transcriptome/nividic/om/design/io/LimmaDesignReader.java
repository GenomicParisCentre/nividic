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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.PhysicalConstants;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.datasources.DataSourceUtils;
import fr.ens.transcriptome.nividic.om.datasources.FileDataSource;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.DesignFactory;
import fr.ens.transcriptome.nividic.om.design.SlideDescription;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This class define a design reader for limma design files.
 * @author Laurent Jourdren
 */
public class LimmaDesignReader extends InputStreamDesignReader {

  private static final String[] TARGET_FIELDS = {"Cy3", "Cy5"};
  private static final String SLIDENUMBER_FIELD = "SlideNumber";
  private static final String NAME_FIELD = "Name";
  private static final String FILENAME_FIELD = "FileName";

  private String baseDir = "";

  @Override
  public Design read() throws NividicIOException {

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream())));

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    Map<String, List<String>> data = new HashMap<String, List<String>>();
    List<String> fieldnames = new ArrayList<String>();

    boolean firstLine = true;
    // String ref = null;

    try {

      while ((line = br.readLine()) != null) {

        final String empty = line.trim();
        if ("".equals(empty) || empty.startsWith("#"))
          continue;

        final String[] fields = line.split(separator);

        if (firstLine) {

          for (int i = 0; i < fields.length; i++) {

            final String field = fields[i].trim();
            data.put(field, new ArrayList<String>());
            fieldnames.add(field);
          }

          firstLine = false;
        } else {

          for (int i = 0; i < fields.length; i++) {

            final String field = fields[i].trim();

            final String fieldName = fieldnames.get(i);

            List<String> l = data.get(fieldName);

            if ((SLIDENUMBER_FIELD.equals(fieldName) || NAME_FIELD
                .equals(fieldName))
                && l.contains(field))
              throw new NividicIOException(
                  "Invalid file format: "
                      + "SlideNumber or Name fields can't contains duplicate values");

            l.add(field);

          }

        }

      }
    } catch (IOException e) {

      throw new NividicIOException("Error while reading the file");
    }

    try {
      getBufferedReader().close();
    } catch (IOException e) {
      throw new NividicIOException("Error while closing the file"
          + e.getMessage());
    }

    if (!data.containsKey(SLIDENUMBER_FIELD))
      throw new NividicIOException("Invalid file format: No SlideNumber field");

    if (!data.containsKey(FILENAME_FIELD))
      throw new NividicIOException("Invalid file format: No FileName field");

    Design design = DesignFactory.createEmptyDesign();

    // Set Id field
    boolean refName = data.containsKey(NAME_FIELD);

    List<String> ids = data.get(refName ? NAME_FIELD : SLIDENUMBER_FIELD);
    final int count = ids.size();

    for (final String id : ids)
      design.addSlide(id);

    // Set SlideNumber field
    if (refName) {

      design.addDescriptionField(SlideDescription.SLIDE_NUMBER_FIELD);

      List<String> slides = data.get(SLIDENUMBER_FIELD);

      for (int i = 0; i < count; i++)
        design.getSlideDescription(ids.get(i)).setSlideNumber(
            Integer.parseInt(slides.get(i)));
    }

    // Set FileName field
    List<String> filenames = data.get(FILENAME_FIELD);
    for (int i = 0; i < count; i++) {

      DataSource source =
          DataSourceUtils.identifyDataSource(this.baseDir, filenames.get(i));

      design.setSource(ids.get(i), source);
      design.setSourceFormat(ids.get(i), DataSourceUtils
          .identifyBioAssayFormat(source));
    }

    for (String fd : fieldnames) {

      if (SLIDENUMBER_FIELD.equals(fd)
          || NAME_FIELD.equals(fd) || FILENAME_FIELD.equals(fd))
        continue;

      boolean isTarget = false;

      for (int j = 0; j < TARGET_FIELDS.length; j++) {

        final String target = TARGET_FIELDS[j];
        if (target.equals(fd)) {

          design.addLabel(PhysicalConstants.getColorForDye(target));

          List<String> samples = data.get(fd);

          int k = 0;
          for (String sample : samples) {

            if (!design.getSamples().isSample(sample))
              design.getSamples().addSample(sample);

            design.setTarget(ids.get(k++), PhysicalConstants
                .getColorForDye(target), sample);
          }

          isTarget = true;
          break;
        }

        if (fd.startsWith(target + "_")) {

          String setting = fd.substring(fd.indexOf('_') + 1, fd.length());
          List<String> values = data.get(fd);

          int k = 0;
          for (String value : values)
            design.setScanLabelSetting(ids.get(k++), PhysicalConstants
                .getColorForDye(target), setting, value);
          isTarget = true;

        }

      }

      if (!isTarget) {

        design.addDescriptionField(fd);
        List<String> descriptions = data.get(fd);

        int k = 0;
        for (String desc : descriptions)
          design.setDescription(ids.get(k++), fd, desc);

      }

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

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public LimmaDesignReader(final File file) throws NividicIOException {

    super(file);
    if (file != null)
      this.baseDir = file.getParent();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public LimmaDesignReader(final InputStream is) throws NividicIOException {

    super(is);
  }

}
