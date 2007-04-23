/*
 *
 * $Id: ProxyJavaAdapter.java,v 1.2 2005/08/19 11:09:01 coconut Exp $
 *
 * CookXml JavaScript Extension (c) Copyright 2005 by Heng Yuan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * ITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package fr.ens.transcriptome.nividic.js;

import java.lang.reflect.Proxy;

import org.mozilla.javascript.*;

/**
 * Use Proxy class to do the task of re-directing Java function calls to Rhino
 * interpreter. This approach requires JDK 1.3, but it avoids the annoying
 * SecurityManager's permission exceptions, and thus allow Rhino interpreter to
 * be run in Sandbox, allowing applications to be run in Java Web Start w/o
 * signed jars and Applets w/o exceptions. Performance wise should be similar I
 * think.
 * <p>
 * To override Rhino JavaScript engine's default JavaAdapter behavior, first
 * obtain the top level scope. Then call ProxyJavaAdapter's init function. For
 * example:
 * 
 * <pre>
 * Context cx = Context.enter();
 * // The following line is to disable the default byte code compilation, which also forces
 * // Rhino to create custom ClassLoader.
 * cx.setOptimizationLevel(-1);
 * Scriptable scope = cx.initStandardObjects();
 * ProxyJavaAdapter.init(cx, scope, true);
 * </pre>
 * 
 * @see Proxy
 * @see JavaAdapter
 * @see ProxyHandler
 * @author Heng Yuan
 * @version $Revision: 1.2 $
 * @since CookJS 1.0
 */
public class ProxyJavaAdapter implements IdFunctionCall {
  private static final Object FTAG = new Object();
  private static final int ID_JAVA_ADAPTER = 1;

  /**
   * Call this function to initate the scope with JavaAdapter object.
   * @param cx Context of the execution environment.
   * @param scope the scope of JavaAdapter
   * @param sealed the scope is sealed or not
   */
  public static void init(Context cx, Scriptable scope, boolean sealed) {
    ProxyJavaAdapter obj = new ProxyJavaAdapter();
    IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, ID_JAVA_ADAPTER,
        "JavaAdapter", 1, scope);
    ctor.markAsConstructor(null);
    if (sealed) {
      ctor.sealObject();
    }
    ctor.exportAsScopeProperty();
  }

  /**
   * This function creates a Scriptable object that wraps around the Java class,
   * which is created using Proxy and handles Java function calls.
   * @param cx Context of the execution environment.
   * @param scope the scope of JavaAdapter
   * @param args This is exactly like Rhino version of JavaAdapter. All except
   *          the last argument are the classes/interfaces the Proxy Java class
   *          will extend/implement. The last argument is the actual JavaScript
   *          Scriptable object which is the user implementation.
   * @return a Scriptable object which wraps around the Proxy Java object
   *         created.
   */
  public static Object js_createAdapter(final Context cx,
      final Scriptable scope, final Object[] args) {
    int N = args.length;
    if (N == 0) {
      throw ScriptRuntime.typeError0("msg.adapter.zero.args");
    }

    System.out.println("args: "+N);
    for (int i = 0; i < args.length; i++) {
      System.out.println(i+" " + args[i]);
    }
    
    
    
    Class superClass = null;
    Class[] intfs = new Class[N - 1];
    int interfaceCount = 0;
    for (int i = 0; i != N - 1; ++i) {
      Object arg = args[i];
      if (!(arg instanceof NativeJavaClass)) {
        throw ScriptRuntime.typeError2("msg.not.java.class.arg", String
            .valueOf(i), ScriptRuntime.toString(arg));
      }
      Class c = ((NativeJavaClass) arg).getClassObject();
      if (!c.isInterface()) {
        if (superClass != null) {
          throw ScriptRuntime.typeError2("msg.only.one.super", superClass
              .getName(), c.getName());
        }
        superClass = c;
      } else {
        intfs[interfaceCount++] = c;
      }
    }

    if (superClass == null)
      superClass = ScriptRuntime.ObjectClass;

    Class[] interfaces = new Class[interfaceCount];
    System.arraycopy(intfs, 0, interfaces, 0, interfaceCount);
    Scriptable obj = ScriptRuntime.toObject(cx, scope, args[N - 1]);

    ProxyHandler handler = new ProxyHandler(obj);
    Object adapter = Proxy.newProxyInstance(cx.getApplicationClassLoader(),
        interfaces, handler);

    NativeJavaObject res = new NativeJavaObject(scope, adapter, null);
    res.setPrototype(obj);
    return res;
  }

  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope,
      Scriptable thisObj, Object[] args) {
    if (f.hasTag(FTAG) && f.methodId() == ID_JAVA_ADAPTER) {
      return js_createAdapter(cx, scope, args);
    }
    throw f.unknown();
  }
}
