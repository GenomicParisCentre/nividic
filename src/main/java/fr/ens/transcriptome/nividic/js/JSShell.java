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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Properties;

import fr.ens.transcriptome.nividic.util.SystemUtils;

/**
 * This class define a shell wrapper for javascript.
 * @author Laurent Jourdren
 */
public class JSShell {

  private JavaScriptEngine engine;
  private ScriptLoader scriptLoader;
  private static final String JAVASCRIPT_PATH = "/javascript";

  /**
   * Define class to load script from javascript.
   * @author Laurent Jourdren
   */
  private class ScriptLoader {

    private JavaScriptEngine engineloader;

    /**
     * Load a script from a file.
     * @param f File to load
     * @throws FileNotFoundException if the file is not found
     */
    public void source(final File f) throws FileNotFoundException {

      FileInputStream fis = new FileInputStream(f);
      this.engineloader.eval(fis, f.getName());
    }

    /**
     * Load a script from an inputStream.
     * @param is InputStreamto use
     * @param comment Comment about the stream
     */
    public void source(final InputStream is, final String comment) {

      this.engineloader.eval(is, comment);
    }

    /**
     * Launch an editor.
     * @param f File to edit. It can be null to edit an empty file
     */
    public void edit(final File f) {

      SystemUtils.launchEditor(f);
    }

    /**
     * Execute a javascript command and return the time needed to execute a
     * Javascript command.
     * @param cmd command to execute
     */
    public void time(final String cmd) {

      long start = System.currentTimeMillis();
      this.engineloader.eval(cmd);
      long end = System.currentTimeMillis();

      System.out.println(end - start);
    }

    /**
     * Execute a system command.
     * @param cmd command to execute
     * @throws IOException if an error occurs while executing the command
     */
    public void system(final String cmd) throws IOException {

      Process p = Runtime.getRuntime().exec(cmd);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p
          .getInputStream()));

      BufferedReader errInput = new BufferedReader(new InputStreamReader(p
          .getInputStream()));

      // read the output from the command
      String s = null;
      while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
      }

      while ((s = errInput.readLine()) != null) {
        System.err.println(s);
      }

    }

    /**
     * Get the current time in milliseconds since the Unix epoch.
     * @return The current time in milliseconds since the Unix epoch
     */
    public long currentTimeMillis() {

      return System.currentTimeMillis();
    }

    /**
     * Returns the maximum amount of memory that the Java virtual machine will
     * attempt to use.
     * @return the maximum memory
     */
    public long maxMemory() {

      System.out.println("hello world.");

      Runtime rt = Runtime.getRuntime();

      return rt.maxMemory();
    }

    /**
     * Returns the total amount of memory in the Java virtual machine.
     * @return the maximum memory
     */
    public long totalMemory() {

      Runtime rt = Runtime.getRuntime();

      return rt.totalMemory();
    }

    /**
     * Returns the amount of free memory in the Java Virtual Machine.
     * @return the maximum memory
     */
    public long freeMemory() {

      Runtime rt = Runtime.getRuntime();

      return rt.freeMemory();
    }

    public ClassLoader getClassLoader() {

      return this.getClass().getClassLoader();
    }

    /**
     * Show System properties of the jvm.*
     */
    public void showProperties() {

      Properties ps = System.getProperties();

      Iterator it = ps.keySet().iterator();

      while (it.hasNext()) {
        String key = (String) it.next();
        String value = ps.getProperty(key);

        System.out.println(key + "=" + value);
      }

    }

    public void showClassLoaderName() {

      System.out.println(this.getClass().getClassLoader().getClass().getName());
    }

    public void t() {

      RessourceFinder.show(this.getClass().getClassLoader());
    }

    /**
     * Constructor
     * @param engine The Javascript engine
     */
    public ScriptLoader(final JavaScriptEngine engine) {

      this.engineloader = engine;
    }
  }

  /**
   * Create a javascript shell.
   * @param in console input
   * @param out console output
   * @param err console error output
   */
  public void shell(final InputStream in, final PrintStream out,
      final PrintStream err) {

    this.engine.shell(in, out, err);
  }

  /**
   * Eval a javascript command.
   * @param cmd Command to evaluate
   */
  public void eval(final String cmd) {

    this.engine.eval(cmd);
  }

  /**
   * Load a script.
   * @param file File to load
   * @throws FileNotFoundException throws FileNotFoundException if cannot find
   *           the script
   */
  public void source(final File file) throws FileNotFoundException {

    this.scriptLoader.source(file);
  }

  /**
   * Load a script from an inputStream.
   * @param is InputStreamto use
   * @param comment Comment about the stream
   */
  public void source(final InputStream is, final String comment)
      throws FileNotFoundException {

    this.scriptLoader.source(is, comment);
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param args command line arguments
   */
  public JSShell(final String javascriptPath, final String fileToExecute,
      final String[] args) {

    this.engine = new RhinoJavaScriptEngine();
    // this.engine = new JSR223JavaScriptEngine();

    System.out.println("Javascript engine: "
        + this.engine.getClass().getSimpleName());

    this.engine.init();
    this.scriptLoader = new ScriptLoader(this.engine);

    this.engine.addVariable("out", System.out);
    this.engine.addVariable("fs", new FileSystem());
    this.engine.addVariable("engine", this.scriptLoader);

    // if (true) return;

    this.engine.addVariable("args", args == null ? new String[] {} : args);
    this.engine.addVariable("homedir", System.getProperty("user.home"));

    for (int i = 0; i < Defaults.BUILTIN_SCRIPTS.length; i++) {

      InputStream is = null;

      if (javascriptPath != null) {

        String path = javascriptPath + "/" + Defaults.BUILTIN_SCRIPTS[i];
        try {
          is = new FileInputStream(path);
        } catch (IOException e) {
          System.err.println(e);
        }

      } else {
        // JAVASCRIPT_PATH
        String path = JAVASCRIPT_PATH + "/" + Defaults.BUILTIN_SCRIPTS[i];

        if (JSShell.class.getResource(path) != null)
          is = JSShell.class.getResourceAsStream(path);
      }

      if (is != null)
        this.engine.eval(is, Defaults.BUILTIN_SCRIPTS[i]);

    }

    if (fileToExecute != null)
      try {
        this.engine.eval(new FileInputStream(fileToExecute), fileToExecute);
        System.exit(0);
      } catch (FileNotFoundException e) {
        System.err.println("File not found: " + fileToExecute);
        System.exit(1);
      }

  }

  //
  // Main methos
  //

  public static void main(String[] args) {

    String javascriptPath = null;
    String filetoExecute = null;
    String[] finalArgs = null;

    if (args != null && args.length > 0) {

      javascriptPath = args[0];

      if (args.length > 1)
        filetoExecute = args[1];

      if (args.length > 2) {

        finalArgs = new String[args.length - 2];
        for (int i = 2; i < args.length; i++)
          finalArgs[i - 2] = args[i];
      }

    }

    /*
     * System.out.println("Javascript path: " + javascriptPath);
     * System.out.println("fileToExecute: " + filetoExecute);
     * System.out.println("Args: " + Arrays.toString(finalArgs));
     */

    new JSShell(javascriptPath, filetoExecute, finalArgs).shell(System.in,
        System.out, System.err);
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public JSShell() {

    this(null, null, null);
  }

}
