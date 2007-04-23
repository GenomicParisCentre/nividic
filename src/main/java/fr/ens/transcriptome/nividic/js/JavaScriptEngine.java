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

package fr.ens.transcriptome.nividic.js;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * This interface define the interface of a Javascript engine.
 * @author Laurent Jourdren
 */
public interface JavaScriptEngine {

  /**
   * Init the javascript engine.
   */
  void init();

  /**
   * Add a varible to the environnement of the javascript engine
   * @param name Name of the value in the javascript environnement
   * @param value Object to map
   */
  void addVariable(String name, Object value);

  /**
   * Create a javascript shell.
   * @param in console input
   * @param out console output
   * @param err console error output
   */
  void shell(final InputStream in, final PrintStream out, final PrintStream err);

  /**
   * Eval a command.
   * @param cmd Command to eval
   * @return the result of the command
   */
  Object eval(final String cmd);

  /**
   * Eval a script.
   * @param is InputStream of the script
   * @param comment Comment about the script
   */
  void eval(InputStream is, String comment);

}
