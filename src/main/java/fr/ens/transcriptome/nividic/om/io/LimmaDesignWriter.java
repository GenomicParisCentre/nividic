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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

import fr.ens.transcriptome.nividic.om.Design;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.Slide;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.impl.SlideDescription;

public class LimmaDesignWriter extends DesignWriter {

  private BufferedWriter bw;
  private static final String SEPARATOR = "\t";

  @Override
  public void write(final Design design) throws NividicIOException {

    bw = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

    if (this.bw == null)
      throw new NividicIOException("No stream to write");

    try {

      List<String> labels = design.getLabelsNames();
      List<String> descriptionFields = design.getDescriptionFieldsNames();

      final boolean serialNumber = design
          .isDescriptionField(SlideDescription.SERIAL_NUMBER_FIELD);

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
        bw.append(l);
      }

      for (String f : descriptionFields) {

        if (SlideDescription.SERIAL_NUMBER_FIELD.equals(f))
          continue;

        bw.append(SEPARATOR);
        bw.append(f);
      }

      bw.append("\n");

      // Write data
      List<Slide> slides = design.getSlides();

      for (Slide s : slides) {

        if (serialNumber) {

          final String sn = s.getDescription().getSerialNumber();

         
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

        for (String f : descriptionFields) {

          if (SlideDescription.SERIAL_NUMBER_FIELD.equals(f))
            continue;

          bw.append(SEPARATOR);
          bw.append(s.getDescription().getDescription(f));
        }

        bw.append("\n");
      }

      bw.close();
    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

    addReaderHistoryEntry(design);
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

    final HistoryEntry entry = new HistoryEntry(
        this.getClass().getSimpleName(), HistoryActionType.SAVE, s
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
   *           the file is null.
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
}
