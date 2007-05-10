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

  public void print(final boolean b) {

    output.print(b);
  }

  public void print(final Object o) {

    output.print(o);
  }

  public void print(final Number n) {

    output.print(n);
  }

  public void print(final String s) {

    output.print(s);
  }

  public void print(final boolean[] bs) {

    output.print(Arrays.toString(bs));
  }

  public void print(final double[] ds) {

    output.print(Arrays.toString(ds));
  }

  public void print(final float[] fs) {

    output.print(Arrays.toString(fs));
  }

  public void print(final int[] is) {

    output.print(Arrays.toString(is));
  }

  public void print(long[] ls) {

    output.print(Arrays.toString(ls));
  }

  public void print(final Object[] obj) {

    output.print(Arrays.toString(obj));
  }

  public void print(final String[] s) {

    output.print(Arrays.toString(s));
  }

  //
  // println
  //

  public void println(final boolean b) {

    output.println(b);
  }

  public void println(final Object o) {

    output.println(o);
  }

  public void println(final Number n) {

    output.println(n);
  }

  /*
   * public void println(final String s) { output.println(s); }
   */

  public void println(final boolean[] bs) {

    output.println(Arrays.toString(bs));
  }

  public void println(final double[] ds) {

    output.println(Arrays.toString(ds));
  }

  public void println(final float[] fs) {

    output.println(Arrays.toString(fs));
  }

  public void println(final int[] is) {

    output.println(Arrays.toString(is));
  }

  public void println(final long[] ls) {

    output.println(Arrays.toString(ls));
  }

  public void println(final Object[] obj) {
    output.println(Arrays.toString(obj));
  }

  /*
   * public void println(final String[] s) { output.println(Arrays.toString(s)); }
   */

  public void printObjectArray(final Object[] obj) {

    output.print(Arrays.toString(obj));
  }

  public void printPrimitiveArray(final Object[] obj) {

    output.print(Arrays.toString(obj));
  }

  //
  // Constructor
  //

  public Echo(PrintStream pstream) {

    this.output = pstream;
  }

}
