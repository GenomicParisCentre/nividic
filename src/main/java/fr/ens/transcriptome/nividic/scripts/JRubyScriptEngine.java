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
import java.util.Collections;

//import org.jruby.Ruby;
//import org.jruby.RubyRuntimeAdapter;
//import org.jruby.javasupport.JavaEmbedUtils;

public class JRubyScriptEngine implements NividicScriptEngine {

//  private RubyRuntimeAdapter interp;
//  private Ruby runtime;

  /**
   * Eval a python command.
   * @param cmd Command to evaluate
   */
  public void eval(final String cmd) {

    init();
//    interp.eval(this.runtime, cmd);
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

    source(new FileInputStream(file), file.getName());
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
//    this.interp.parse(this.runtime, is, comment, 0);
  }

  private void init() {

//    if (this.interp == null) {
//      // Create runtime instance
//      this.runtime = JavaEmbedUtils.initialize(Collections.emptyList());
//      RubyRuntimeAdapter evaler = JavaEmbedUtils.newRuntimeAdapter();
//      this.interp = evaler;
//    }

  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public JRubyScriptEngine() {
  }

  /**
   * Public constructor.
   * @param scriptPath path of built-in scripts
   * @param args command line arguments
   * @param fileToExecute file to execute
   */
  public JRubyScriptEngine(final String scriptPath, final String fileToExecute,
      final String[] args) {

    try {
      source(new FileInputStream(fileToExecute), fileToExecute);
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + fileToExecute);
      System.exit(1);
    }

  }

}
