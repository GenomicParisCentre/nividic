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

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.BiologicalListFactory;

/**
 * A simple reader for Biological lists. By default, all the line raed are
 * trimed. All lines which start with "#" aren't readed.
 * @author Laurent Jourdren
 */
public class SimpleBiologicalListReader implements BiologicalListReader {

  private boolean trim = true;
  private InputStream is;

  /**
   * Test if all the element of the list must be trimed.
   * @return true if all the element of the list must be trimed
   */
  public boolean isTrim() {
    return trim;
  }

  /**
   * Set if all the element of the list must be trimed.
   * @param trim value to set
   */
  public void setTrim(final boolean trim) {
    this.trim = trim;
  }

  /**
   * Read the Biological list.
   * @return the BiologicalList read.
   */
  public BiologicalList read() {

    if (this.is == null)
      throw new NividicRuntimeException("The stream to read is null");

    BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
    String line = null;
    final boolean trim = this.trim;

    BiologicalList list = BiologicalListFactory.createBiologicalList();

    try {
      while ((line = br.readLine()) != null) {

        if (line.startsWith("#"))
          continue;

        if (trim)
          line = line.trim();

        list.add(line);

      }
    } catch (IOException e) {
      throw new NividicRuntimeException("Error while reading biological list.");
    }

    return list;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param is InputStream to read
   */
  public SimpleBiologicalListReader(final InputStream is) {
    this.is = is;
  }

  /**
   * Public constructor.
   * @param file File to read
   * @throws FileNotFoundException if an error occurs while creating the stream
   */
  public SimpleBiologicalListReader(final File file) throws FileNotFoundException {

    this(new FileInputStream(file));
  }

  /**
   * Public constructor.
   * @param filename file to read
   * @throws FileNotFoundException if an error occurs while creating the stream
   */
  public SimpleBiologicalListReader(final String filename)
      throws FileNotFoundException {

    this(new File(filename));
  }

}
