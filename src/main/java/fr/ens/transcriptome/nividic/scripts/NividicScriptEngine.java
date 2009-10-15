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

package fr.ens.transcriptome.nividic.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

public interface NividicScriptEngine {

  /**
   * Create a engine shell.
   * @param in console input
   * @param out console output
   * @param err console error output
   */
  void shell(final InputStream in, final PrintStream out, final PrintStream err);

  /**
   * Eval a engine command.
   * @param cmd Command to evaluate
   */
  public void eval(final String cmd);

  /**
   * Load a script.
   * @param file File to load
   * @throws FileNotFoundException throws FileNotFoundException if cannot find
   *           the script
   */
  public void source(final File file) throws FileNotFoundException;

  /**
   * Load a script from an inputStream.
   * @param is InputStreamto use
   * @param comment Comment about the stream
   * @exception FileNotFoundException if the file of the script is not found
   */
  public void source(final InputStream is, final String comment)
      throws FileNotFoundException;

}