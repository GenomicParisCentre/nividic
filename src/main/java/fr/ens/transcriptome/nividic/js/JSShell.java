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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * This class implements a javascript shell.
 * @author Laurent Jourdren
 */
public class JSShell {

  private ScriptEngine engine;
  private static final String JS_PROMPT = "js>";

  /**
   * Initialize the javascript engine.
   */
  private void initJavascript() {

    ScriptEngineManager manager = new ScriptEngineManager();

    this.engine = manager.getEngineByName("js");
  }

  /**
   * Create javascript variables.
   * @param arguments of the command line if exists
   */
  private void initVariables(final String[] arguments) {

    this.engine.put("fs", new FileSystem());
    this.engine.put("args", arguments);
  }

  /**
   * Compile a javascript stream
   * @param is Stream to compile
   * @throws ScriptException if an error occurs while compiling the script
   */
  private void internalCompileScript(final InputStream is)
      throws ScriptException {

    Compilable compilable = (Compilable) this.engine;
    CompiledScript script = compilable.compile(new InputStreamReader(is));
    script.eval();
  }

  /**
   * Compile a javascript stream
   * @param is Stream to compile
   */
  public void compileScript(final InputStream is) {

    try {
      internalCompileScript(is);
    } catch (ScriptException e) {
      throw new NividicRuntimeException("Error in script (line "
          + e.getLineNumber() + " column " + e.getColumnNumber() + "): "
          + e.getMessage());
    }
  }

  /**
   * Compile all the nividic built-ins scripts
   */
  private void compileBuiltInScripts() {

    for (int i = 0; i < Defaults.BUILTIN_SCRIPTS.length; i++) {

      String scriptName = Defaults.BUILTIN_SCRIPTS[i];

      try {
        internalCompileScript(new FileInputStream(
            "/home/jourdren/workspace/nividic/src/main/javascript/"
                + scriptName));
      } catch (FileNotFoundException e) {
        throw new NividicRuntimeException("Unable to found built-in script: "
            + scriptName);
      } catch (ScriptException e) {
        throw new NividicRuntimeException("Error in script " + scriptName
            + " (line " + e.getLineNumber() + " column " + e.getColumnNumber()
            + "): " + e.getMessage());
      }

    }
  }

  /**
   * Eval a command.
   * @param cmd Command to eval
   * @return the result of the command
   */
  private Object internalEval(final String cmd) throws ScriptException {

    return this.engine.eval(cmd);
  }

  /**
   * Eval a command.
   * @param cmd Command to eval
   * @return the result of the command
   */
  public Object eval(final String cmd) {

    try {

      return this.engine.eval(cmd);

    } catch (ScriptException e) {
      throw new NividicRuntimeException("Error (line " + e.getLineNumber()
          + " column " + e.getColumnNumber() + "): " + e.getMessage());
    }

  }

  /**
   * Create a javascript shell.
   */
  public void shell() {

    BufferedReader console = new BufferedReader(
        new InputStreamReader(System.in));

    String line = null;

    System.out.print(JS_PROMPT);

    try {
      while ((line = console.readLine()) != null) {

        try {
          Object result = internalEval(line);

          System.out.println(result);
        } catch (ScriptException e) {

          /*
           * System.err.println("Error (line " + e.getLineNumber() + " column " +
           * e.getColumnNumber() + "): " + e.getMessage());
           */
          System.err.println(e.getLocalizedMessage());

        }

        System.out.print(JS_PROMPT);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  //
  // main method
  //

  /**
   * Main method.
   * @param args command line arguments
   */
  public static void main(final String[] args) {

    JSShell jsshell = new JSShell();
    jsshell.shell();
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param args command line arguments
   */
  public JSShell(final String[] args) {

    initJavascript();
    initVariables(args);
  }

  /**
   * Public constructor.
   */
  public JSShell() {

    this(null);
  }

}
