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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.python.util.PythonInterpreter;

/**
 * This class implement a Jython script engine for nividic
 * @author Laurent Jourdren
 */
public class JythonScriptEngine implements NividicScriptEngine {

  private PythonInterpreter interp;

  /**
   * Eval a python command.
   * @param cmd Command to evaluate
   */
  public void eval(final String cmd) {

    init();
    interp.eval(cmd);
  }

  /**
   * Create a shell.
   * @param in console input
   * @param out console output
   * @param err console error output
   */
  public void shell(final InputStream in, final PrintStream out,
      final PrintStream err) {

    // TODO Implements this method
  }

  /**
   * Load a script.
   * @param file File to load
   * @throws FileNotFoundException throws FileNotFoundException if cannot find
   *           the script
   */
  public void source(final File file) throws FileNotFoundException {

    init();
    this.interp.execfile(new FileInputStream(file));
  }

  /**
   * Load a script from an inputStream.
   * @param is InputStreamto use
   * @param comment Comment about the stream
   * @exception FileNotFoundException if the file of the script is not found
   */
  public void source(final InputStream is, final String comment)
      throws FileNotFoundException {

    init();
    this.interp.execfile(is, comment);

  }

  private void init() {

    if (this.interp == null)
      this.interp = new PythonInterpreter();

  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public JythonScriptEngine() {
  }

  /**
   * Public constructor.
   * @param scriptPath path of built-in scripts
   * @param args command line arguments
   * @param fileToExecute file to execute
   */
  public JythonScriptEngine(final String scriptPath,
      final String fileToExecute, final String[] args) {

    final Properties props = new Properties();

    props.setProperty("python.home", System.getProperty("jython.home"));
    props.setProperty("python.cachedir", System.getProperty("user.home")
        + File.separator + (File.separator.equals("/") ? "." : "") + "jython");

    PythonInterpreter.initialize(System.getProperties(), props, args);
    init();

    try {
      source(new FileInputStream(fileToExecute), fileToExecute);
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + fileToExecute);
      System.exit(1);
    }

  }

}
