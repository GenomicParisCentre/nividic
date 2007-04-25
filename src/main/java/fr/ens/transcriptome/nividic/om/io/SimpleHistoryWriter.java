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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.History;
import fr.ens.transcriptome.nividic.om.HistoryEntry;

/**
 * This class define a writer for history.
 * @author Laurent Jourdren
 */
public class SimpleHistoryWriter implements HistoryWriter {

  private OutputStream os;
  private String dataSource;

  /**
   * Get the source of the data
   * @return The source of the data
   */
  public String getDataSource() {
    return this.dataSource;
  }

  /**
   * Write a History log.
   * @param history History to write
   */
  public void write(final History history) {

    if (this.os == null)
      throw new NividicRuntimeException("The stream to read is null");

    if (history == null)
      throw new NividicRuntimeException("History is null");

    final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
        this.os));

    try {

      out.write("#Date\tType\tName\tResult\tArguments\tComments\n");

      for (HistoryEntry entry : history.getEntries()) {

        StringBuffer sb = new StringBuffer();

        sb.append(entry.getDate());
        sb.append("\t");
        sb.append(entry.getActionType());
        sb.append("\t");
        sb.append(entry.getActionName());
        sb.append("\t");
        sb.append(entry.getActionResult());
        sb.append("\t");
        sb.append(entry.getArguments());
        sb.append("\t");
        sb.append(entry.getComments());
        sb.append("\n");

        out.write(sb.toString());
      }

      out.close();

    } catch (IOException e) {
      throw new NividicRuntimeException("Error while writing the stream");
    }

  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param os InputStream to write
   */
  public SimpleHistoryWriter(final OutputStream os) {
    this.os = os;
  }

  /**
   * Public constructor.
   * @param file File to write
   * @throws FileNotFoundException if an error occurs while creating the stream
   */
  public SimpleHistoryWriter(final File file) throws FileNotFoundException {

    this(new FileOutputStream(file));

    if (file != null)
      this.dataSource = file.getAbsolutePath();
  }

  /**
   * Public constructor.
   * @param filename file to write
   * @throws FileNotFoundException if an error occurs while creating the stream
   */
  public SimpleHistoryWriter(final String filename)
      throws FileNotFoundException {

    this(new File(filename));
  }

}
