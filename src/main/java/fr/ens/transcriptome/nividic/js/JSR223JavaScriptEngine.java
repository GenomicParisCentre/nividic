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

package fr.ens.transcriptome.nividic.js;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.mozilla.javascript.WrappedException;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * Define a javascript engine using the JSR 223.
 * @author Laurent Jourdren
 */
public class JSR223JavaScriptEngine implements JavaScriptEngine {

  private ScriptEngine engine;
  private static final String JS_PROMPT = "js>";

  /**
   * Add a varible to the environnement of the javascript engine
   * @param name Name of the value in the javascript environnement
   * @param value Object to map
   */
  public void addVariable(final String name, final Object value) {

    this.engine.put(name, value);
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
   * Init the javascript engine.
   */
  public void init() {

    ScriptEngineManager manager = new ScriptEngineManager();

    this.engine = manager.getEngineByName("js");
  }

  /**
   * Eval a command.
   * @param cmd Command to eval
   * @return the result of the command
   * @throws ScriptException if an error occurs while executing the command
   */
  public Object internalEval(final String cmd) throws ScriptException {

    return this.engine.eval(cmd);
  }

  /**
   * Create a javascript shell.
   * @param in console input
   * @param out console output
   * @param err console error output
   */
  public void shell(final InputStream in, final PrintStream out,
      final PrintStream err) {

    BufferedReader console = new BufferedReader(new InputStreamReader(in));

    String line = null;

    out.print(JS_PROMPT);
    out.flush();

    try {
      while ((line = console.readLine()) != null) {

        try {
          Object result = internalEval(line);

          out.println(result);
          out.flush();
        } catch (ScriptException e) {

          /*
           * System.err.println("Error (line " + e.getLineNumber() + " column " +
           * e.getColumnNumber() + "): " + e.getMessage());
           */
          err.println(e.getLocalizedMessage());
          err.flush();

        }

        out.print(JS_PROMPT);
        out.flush();
      }
    } catch (WrappedException e) {
      e.fillInStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block

      e.printStackTrace();
    }

  }

  /**
   * Eval a script.
   * @param is InputStream of the script
   * @param comment Comment about the script
   */
  public void eval(final InputStream is, final String comment) {

    try {
      Compilable compilable = (Compilable) this.engine;
      CompiledScript script = compilable.compile(new InputStreamReader(is));
      script.eval();
    } catch (ScriptException e) {

      e.printStackTrace();

    }
  }
}
