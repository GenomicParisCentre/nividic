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
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSRunner {

  /**
   * Read a file and put the content in a string buffer
   * @param filename Filename to read
   * @param sb StringBuffer to append
   */
  private static void readFile(final String filename, final StringBuffer sb) {

    FileInputStream fis = null;
    try {

      fis = new FileInputStream(filename);
    } catch (FileNotFoundException e) {

      System.err.println("File not found: " + filename);
      System.exit(1);
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

    String line;

    try {
      while ((line = reader.readLine()) != null)
        sb.append(line);

      fis.close();
    } catch (IOException e) {

      System.err.println("Error while reading file: " + filename);
      System.exit(1);
    }
  }

  private static void runJavaScript(String s) {

    if (s == null)
      return;

    // Creates and enters a Context. The Context stores information
    // about the execution environment of a script.
    Context cx = Context.enter();
    try {
      // Initialize the standard objects (Object, Function, etc.)
      // This must be done before scripts can be executed. Returns
      // a scope object that we use in later calls.
      Scriptable scope = cx.initStandardObjects();

      // Add a global variable "out" that is a JavaScript reflection
      // of System.out
      //Object jsOut = Context.javaToJS(Echo.out, scope);
      //ScriptableObject.putProperty(scope, "out", jsOut);

      // Add a global variable "fs" that is a JavaScript reflection
      // of fileSystem
      Object jsFs = Context.javaToJS(new FileSystem(), scope);
      ScriptableObject.putProperty(scope, "fs", jsFs);

      // Now evaluate the string we've colected.
      Object result = cx.evaluateString(scope, s, "<cmd>", 1, null);

      // Convert the result to a string and print it.
      System.err.println(Context.toString(result));

    } finally {
      // Exit from the context.
      Context.exit();
    }

  }

  //
  // Main method
  //

  /**
   * Main method.
   * @param args program args
   */
  public static void main(String[] args) {

    System.out.println("Nividic Javascript Runner");

    if (args == null) {

      System.err.println("no file to read.");
      System.exit(1);
    }

    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < args.length; i++) {

      readFile(args[i], sb);
    }

    runJavaScript(sb.toString());
  }

}
