/*
 *
 * $Id: ProxyHandler.java,v 1.1 2005/08/19 10:55:31 coconut Exp $
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaAdapter;
import org.mozilla.javascript.Scriptable;

/**
 * This is the handler class that redirect Java calls to Rhino interpreter. It
 * is created by ProxyJavaAdapter as the handler for the Proxy object.
 * <p>
 * Here I made an assumption that all objects passed to "invoke" method are Java
 * objects rather than JavaScript's Scriptables, since one only use JavaAdapter
 * in such cases anyways.
 * @see java.lang.reflect.Proxy
 * @see JavaAdapter
 * @see ProxyJavaAdapter
 * @author Heng Yuan
 * @version $Revision: 1.1 $
 * @since CookJS 1.0
 */
public class ProxyHandler implements InvocationHandler {
  private final Scriptable m_delegate;

  public ProxyHandler(Scriptable delegate) {
    m_delegate = delegate;
  }

  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    long convertionMask = 0;
    if (args == null)
      args = new Object[0];
    for (int i = 0; i != args.length; ++i) {
      if (args[i] == null)
        continue;
      Class cl = args.getClass();
      if (!cl.isPrimitive()) {
        convertionMask |= (1 << i);
      }
    }

    Function function = JavaAdapter.getFunction(m_delegate, method.getName());

    if (function == null) {
      method = m_delegate.getClass().getMethod(method.getName(),
          new Class[args.length]);
      return method.invoke(m_delegate, args);
    } else {
      return JavaAdapter.callMethod(null, m_delegate, function, args,
          convertionMask);
    }
  }
}
