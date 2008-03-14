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

package fr.ens.transcriptome.nividic.om.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * Abstract class to write ExpressionMatrixDimension files
 * @author Lory Montout
 */
public abstract class ExpressionMatrixWriter {

  private String dataSource;
  private OutputStream outputStream;
  private Translator translator;

  //
  // Getters
  //

  /**
   * Get the outputStream.
   * @return Returns the outputStream
   */
  public OutputStream getOutputStream() {
    return outputStream;
  }

  /**
   * Get the feature annotation object.
   * @return the feature annotation object.
   */
  public Translator getTranslator() {
    return translator;
  }

  /**
   * Get the source of the data
   * @return The source of the data
   */
  public String getDataSource() {
    return this.dataSource;
  }

  //
  // Setters
  //

  /**
   * Set the feature annotation object.
   * @param translator the feature annotation object.
   */
  public void setTranslator(final Translator translator) {
    this.translator = translator;
  }

  /**
   * Set the output stream
   * @param outputStream The outputStream to set
   */
  public void setOutputStream(final OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  //
  // Abstract methods
  //

  /**
   * Write data.
   * @param em ExpressionMatrix to write
   * @throws NividicIOException if an error occurs while writing data
   */
  public abstract void write(final ExpressionMatrix em)
      throws NividicIOException;

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public ExpressionMatrixWriter(final File file) throws NividicIOException {
    if (file == null)
      throw new NividicIOException("No file to load");

    try {
      setOutputStream(new FileOutputStream(file));
    } catch (FileNotFoundException e) {
      throw new NividicIOException("Error while writing file : "
          + file.getName());
    }

    this.dataSource = file.getAbsolutePath();
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public ExpressionMatrixWriter(final OutputStream is)
      throws NividicIOException {
    setOutputStream(is);
  }

}