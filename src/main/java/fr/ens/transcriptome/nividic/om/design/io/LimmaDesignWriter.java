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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import fr.ens.transcriptome.nividic.Globals;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.PhysicalConstants;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.ScanLabelSettings;
import fr.ens.transcriptome.nividic.om.design.ScanLabelsSettings;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.design.SlideDescription;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This class implements a writer for limma design files.
 * @author Laurent Jourdren
 */
public class LimmaDesignWriter extends DesignWriter {

  private BufferedWriter bw;
  private static final String SEPARATOR = "\t";
  private static final String NEWLINE = "\r\n"; // System.getProperty("line.separator");
  private boolean writeScanLabelsSettings = true;

  @Override
  public void write(final Design design) throws NividicIOException {

    if (design == null)
      throw new NullPointerException("Design is null");

    try {
      bw =
          new BufferedWriter(new OutputStreamWriter(getOutputStream(),
              Globals.DEFAULT_FILE_ENCODING));

      if (this.bw == null)
        throw new NividicIOException("No stream to write");

      List<String> labels = design.getLabelsNames();
      List<String> descriptionFields = design.getDescriptionFieldsNames();
      List<String> scanLabelSettingsNames = design.getScanLabelSettingsKeys();

      final boolean serialNumber =
          design.isDescriptionField(SlideDescription.SLIDE_NUMBER_FIELD);

      // Write header
      bw.append("SlideNumber");
      bw.append(SEPARATOR);

      if (serialNumber) {

        bw.append("Name");
        bw.append(SEPARATOR);
      }

      bw.append("FileName");

      for (String l : labels) {
        bw.append(SEPARATOR);
        bw.append(PhysicalConstants.getDyeForColor(l));
      }

      if (this.writeScanLabelsSettings)
        for (String l : labels)
          for (String s : scanLabelSettingsNames) {
            bw.append(SEPARATOR);
            bw.append(PhysicalConstants.getDyeForColor(l));
            bw.append("_");
            bw.append(s);
          }

      for (String f : descriptionFields) {

        if (SlideDescription.SLIDE_NUMBER_FIELD.equals(f))
          continue;

        bw.append(SEPARATOR);
        bw.append(f);
      }

      bw.append(NEWLINE);

      // Write data
      List<Slide> slides = design.getSlides();

      for (Slide s : slides) {

        if (serialNumber) {

          final String sn = s.getDescription().getSlideNumber();

          bw.append(sn == null ? "" : sn);
          bw.append(SEPARATOR);

          bw.append(s.getName());
          bw.append(SEPARATOR);

        } else {

          bw.append(s.getName());
          bw.append(SEPARATOR);

        }

        String sourceInfo = s.getSourceInfo();
        if (sourceInfo != null)
          bw.append(sourceInfo);

        for (String l : labels) {
          bw.append(SEPARATOR);
          bw.append(s.getTarget(l));
        }

        if (this.writeScanLabelsSettings) {
          final ScanLabelsSettings ssls = s.getScanLabelsSettings();
          for (String l : labels) {

            ScanLabelSettings sls = ssls.getSetting(l);

            for (String setting : scanLabelSettingsNames) {
              bw.append(SEPARATOR);
              bw.append(sls.getSetting(setting));
            }
          }
        }

        for (String f : descriptionFields) {

          if (SlideDescription.SLIDE_NUMBER_FIELD.equals(f))
            continue;

          bw.append(SEPARATOR);
          bw.append(s.getDescription().getDescription(f));
        }

        bw.append(NEWLINE);
      }

      bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

    addReaderHistoryEntry(design);
  }

  /**
   * Test if the scan labels settings must be written.
   * @return Returns true if thq scan labels settings must be written
   */
  boolean isWriteScanLabelsSettings() {
    return writeScanLabelsSettings;
  }

  /**
   * Set if the scan labels settings must be written.
   * @param writeScanLabelsSettings true if he scan labels settings must be
   *            written
   */
  void setWriteScanLabelsSettings(final boolean writeScanLabelsSettings) {
    this.writeScanLabelsSettings = writeScanLabelsSettings;
  }

  /**
   * Add history entry for reading data
   * @param design Design readed
   */
  private void addReaderHistoryEntry(final Design design) {

    String s;

    if (getDataSource() != null)
      s = "Source=" + getDataSource() + ";";
    else
      s = "";

    final HistoryEntry entry =
        new HistoryEntry(this.getClass().getSimpleName(),
            HistoryActionType.SAVE, s
                + "SlideNumber=" + design.getSlideCount() + ";LabelNumber="
                + design.getLabelCount(), HistoryActionResult.PASS);

    design.getHistory().add(entry);
  }

  //
  // Construtors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *             the file is null.
   */
  public LimmaDesignWriter(final File file) throws NividicIOException {

    super(file);
  }

  /**
   * Public constructor
   * @param os Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public LimmaDesignWriter(final OutputStream os) throws NividicIOException {
    super(os);
  }
  
  /**
   * Public constructor
   * @param filename File to write
   * @throws NividicIOException if the stream is null
   * @throws FileNotFoundException if the file doesn't exist
   */
  public LimmaDesignWriter(final String filename) throws NividicIOException, FileNotFoundException {

    this(new FileOutputStream(filename));
  }
}
