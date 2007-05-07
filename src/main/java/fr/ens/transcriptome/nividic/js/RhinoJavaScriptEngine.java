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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.WrappedException;

/**
 * This class define a javascript engine using the rhino engine.
 * @author Laurent Jourdren
 */
public class RhinoJavaScriptEngine implements JavaScriptEngine {

  private Scriptable scope;
  private Context cx;
  private static final String JS_PROMPT = "js>";

  private static final class JavaScriptFunctions extends ScriptableObject {

    /**
     * Returns the name of this JavaScript class, "Foo".
     */
    public String getClassName() {
      return "Foo";
    }

    private static final Double parseJavaScriptNumber(final NativeArray na,
        Object key) {

      final int intKey = ((Integer) key).intValue();

      final Object val = na.get(intKey, na);

      if (val instanceof Double)
        return ((Double) val).doubleValue();

      try {
        if (val instanceof String)
          return Double.parseDouble((String) val);

        return (Double.parseDouble("" + val));

      } catch (NumberFormatException e) {

        return null;
      }

    }

    public static double[] asDoubles(final Context cx,
        final Scriptable thisObj, final Object[] args, final Function funObj) {

      if (args == null || args.length == 0 || !(args[0] instanceof NativeArray))
        return new double[0];

      final NativeArray na = (NativeArray) args[0];
      final Object[] keys = na.getIds();

      final List<Double> list = new ArrayList<Double>();
      for (int j = 0; j < keys.length; j++)
        list.add(parseJavaScriptNumber(na, keys[j]));

      // Convert the list
      final double[] result = new double[list.size()];
      int i = 0;
      for (double d : list)
        result[i++] = d;

      return result;
    }

    public static float[] asFloats(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {

      if (args == null || args.length == 0 || !(args[0] instanceof NativeArray))
        return new float[0];

      final NativeArray na = (NativeArray) args[0];
      final Object[] keys = na.getIds();

      final List<Float> list = new ArrayList<Float>();
      for (int j = 0; j < keys.length; j++)
        list.add(parseJavaScriptNumber(na, keys[j]).floatValue());

      // Convert the list
      final float[] result = new float[list.size()];
      int i = 0;
      for (float d : list)
        result[i++] = d;

      return result;
    }

    public static long[] asLongs(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {

      if (args == null || args.length == 0 || !(args[0] instanceof NativeArray))
        return new long[0];

      final NativeArray na = (NativeArray) args[0];
      final Object[] keys = na.getIds();

      final List<Long> list = new ArrayList<Long>();
      for (int j = 0; j < keys.length; j++)
        list.add(parseJavaScriptNumber(na, keys[j]).longValue());

      // Convert the list
      final long[] result = new long[list.size()];
      int i = 0;
      for (long d : list)
        result[i++] = d;

      return result;
    }

    public static int[] asInts(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) {

      if (args == null || args.length == 0 || !(args[0] instanceof NativeArray))
        return new int[0];

      final NativeArray na = (NativeArray) args[0];
      final Object[] keys = na.getIds();

      final List<Integer> list = new ArrayList<Integer>();
      for (int j = 0; j < keys.length; j++)
        list.add(parseJavaScriptNumber(na, keys[j]).intValue());

      // Convert the list
      final int[] result = new int[list.size()];
      int i = 0;
      for (int d : list)
        result[i++] = d;

      return result;
    }

    public static String[] asStrings(final Context cx,
        final Scriptable thisObj, final Object[] args, final Function funObj) {

      if (args == null || args.length == 0 || !(args[0] instanceof NativeArray))
        return new String[0];

      final NativeArray na = (NativeArray) args[0];

      final List<String> list = new ArrayList<String>();
      final Object[] keys = na.getIds();

      for (int j = 0; j < keys.length; j++)
        try {
          list.add("" + na.get(((Integer) keys[j]).intValue(), na));
        } catch (NumberFormatException e) {
        }

      // Convert the list
      final String[] result = new String[list.size()];
      list.toArray(result);

      return result;
    }

  }

  /**
   * Add a varible to the environnement of the javascript engine
   * @param name Name of the value in the javascript environnement
   * @param value Object to map
   */
  public void addVariable(final String name, final Object value) {

    System.out.println("Add global variable: " + name);

    Object jsObj = Context.javaToJS(value, scope);
    ScriptableObject.putProperty(scope, name, jsObj);
  }

  /**
   * Eval a command.
   * @param cmd Command to eval
   * @return the result of the command
   */
  public Object eval(final String cmd) {

    if (cmd == null)
      return null;

    Object result = cx.evaluateString(scope, cmd, "<cmd>", 1, null);

    // System.err.println(Context.toString(result));

    return result;
  }

  /**
   * Init the javascript engine.
   */
  public void init() {

    JavaScriptFunctions jsf = new JavaScriptFunctions();

    this.cx = Context.enter();

    cx.setOptimizationLevel(-1);
    this.scope = cx.initStandardObjects(jsf);

    // Define some global functions particular to the shell. Note
    String[] names = {"asDoubles", "asFloats", "asLongs", "asInts", "asStrings"};
    jsf.defineFunctionProperties(names, JavaScriptFunctions.class,
        ScriptableObject.DONTENUM);

    // add from JShell3

    // ProxyJavaAdapter.init(cx, scope, false);
  }

  private void shellEval(final InputStream in, final PrintStream out,
      final PrintStream err, final String line) {

    try {
      Object result = eval(line);

      if (!(result instanceof Undefined)) {

        Object fObj = scope.get("println", scope);
        if (fObj instanceof Function) {

          Object[] functionArgs = {result};
          Function f = (Function) fObj;
          f.call(cx, scope, scope, functionArgs);
        } else
          out.println(result);
      }

      out.flush();
    } catch (RhinoException e) {

      err.println(e.getLocalizedMessage());
      err.flush();
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

    BufferedReader console = new BufferedReader(new InputStreamReader(in));

    String line = null;

    out.print(JS_PROMPT);
    out.flush();

    try {
      while ((line = console.readLine()) != null) {

        shellEval(in, out, err, line);

        out.print(JS_PROMPT);
        out.flush();
      }
    } catch (WrappedException e) {
      e.fillInStackTrace();
      e.printStackTrace();
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

    System.out.println("Load script: " + comment);

    Reader in = new InputStreamReader(is);

    try {
      // Here we evalute the entire contents of the file as
      // a script. Text is printed only if the print() function
      // is called.
      this.cx.evaluateReader(scope, in, comment == null ? "file" : comment, 1,
          null);
    } catch (WrappedException we) {
      System.err.println(we.getWrappedException().toString());
      we.printStackTrace();
    } catch (EvaluatorException ee) {
      System.err.println("js: " + ee.getMessage());
    } catch (JavaScriptException jse) {
      System.err.println("js: " + jse.getMessage());
    } catch (IOException ioe) {
      System.err.println(ioe.toString());
    } finally {
      try {
        in.close();
      } catch (IOException ioe) {
        System.err.println(ioe.toString());
      }
    }
  }

}
