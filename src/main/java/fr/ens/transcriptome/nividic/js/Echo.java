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

import java.io.PrintStream;
import java.util.Arrays;

public final class Echo {

  public static final Echo out = new Echo(System.out);
  public static final Echo err = new Echo(System.err);

  private PrintStream output;

  //
  // print
  //

  public static void print(final boolean b) {

    print(System.out, b);
  }

  public static void print(final Object o) {

    print(System.out, o);
  }

  public static void print(final Number n) {

    print(System.out, n);
  }

  public static void print(final String s) {

    print(System.out, s);
  }

  public static void print(final boolean[] bs) {

    print(System.out, bs);
  }

  public static void print(final double[] ds) {

    print(System.out, ds);
  }

  public static void print(final float[] fs) {

    print(System.out, fs);
  }

  public static void print(final int[] is) {

    print(System.out, is);
  }

  public static void print(long[] ls) {

    print(System.out, ls);
  }

  public static void print(final Object[] obj) {

    print(System.out, obj);
  }

  public static void print(final String[] s) {

    print(System.out, s);
  }

  public static void printObjectArray(final Object[] obj) {

    printObjectArray(System.out, obj);
  }

  public static void printPrimitiveArray(final Object[] obj) {

    printPrimitiveArray(System.out, obj);
  }

  public static void print(PrintStream pw, final boolean b) {

    pw.print(b);
  }

  public static void print(PrintStream pw, final Object o) {

    pw.print(o);
  }

  public static void print(PrintStream pw, final Number n) {

    pw.print(n);
  }

  public static void print(PrintStream pw, final String s) {

    pw.print(s);
  }

  public static void print(PrintStream pw, final boolean[] bs) {

    pw.print(Arrays.toString(bs));
  }

  public static void print(PrintStream pw, final double[] ds) {

    pw.print(Arrays.toString(ds));
  }

  public static void print(PrintStream pw, final float[] fs) {

    pw.print(Arrays.toString(fs));
  }

  public static void print(PrintStream pw, final int[] is) {

    pw.print(Arrays.toString(is));
  }

  public static void print(PrintStream pw, long[] ls) {

    pw.print(Arrays.toString(ls));
  }

  public static void print(PrintStream pw, final Object[] obj) {

    pw.print(Arrays.toString(obj));
  }

  public static void print(PrintStream pw, final String[] s) {

    pw.print(Arrays.toString(s));
  }

  public static void printObjectArray(PrintStream pw, final Object[] obj) {

    pw.print(Arrays.toString(obj));
  }

  public static void printPrimitiveArray(PrintStream pw, final Object[] obj) {

    pw.print(Arrays.toString(obj));
  }

  //
  // Constructor
  //

  public Echo(PrintStream pstream) {

    this.output = pstream;
  }

}
