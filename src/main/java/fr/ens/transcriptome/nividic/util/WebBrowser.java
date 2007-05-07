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

package fr.ens.transcriptome.nividic.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * This class defines method to launch a web browser.
 * @author Laurent Jourdren
 */
public final class WebBrowser {

  private static final String IE_PATH = "c:\\Program FileSystem\\Internet Explorer\\iexplore.exe";
  private static final String KONQUEROR_EXE = "konqueror";
  private static final String MOZILLA_EXE = "mozilla";
  private static final String FIREFOX_EXE = "mozilla-firefox";
  private static final String NETSCAPE_EXE = "netscape";
  private static final String SAFARI_MAC_PATH = "/Applications/Safari.app/Contents/MacOS/Safari";
  private static final String SAFARI_OPEN = "/usr/bin/open -a Safari";
  private static final String IE_MAC_PATH = "/Applications/Internet Explorer.app/Contents"
      + "/MacOS/Internet Explorer";

  /**
   * Start an browser.
   * @throws IOException if an error occurs while launching the browser
   */
  public static void launch() throws IOException {
    launch(null);
  }

  /**
   * Start an browser with an URL.
   * @param url URL where launch the browser
   * @throws IOException if an error occurs while launching the browser
   */
  public static void launch(final URL url) throws IOException {
    launch(null, url.toString());
  }

  /**
   * Start an browser with an URL.
   * @param alternativeBrowserPath Path to an alternative browser
   * @param url URL where launch the browser
   * @throws IOException if an error occurs while launching the browser
   */
  public static void launch(final String alternativeBrowserPath, final URL url)
      throws IOException {

    if (url == null)
      return;
    launch(alternativeBrowserPath, url.toString());
  }

  /**
   * Start an browser with an URL.
   * @param alternativeBrowserPath Path to an alternative browser
   * @param url URL where launch the browser
   * @throws IOException if an error occurs while launching the browser
   */
  public static void launch(final String alternativeBrowserPath,
      final String url) throws IOException {

    if (url == null)
      return;

    String togo;
    if (url != null)
      togo = " " + url;
    else
      togo = "";

    Runtime.getRuntime().exec(getBrowserPath(alternativeBrowserPath) + togo);
  }

  /**
   * Get the path of the web browser.
   * @return the absolute path to the web browser
   */
  public static String getBrowserPath() {
    return getBrowserPath(null);
  }

  /**
   * Get the path of the web browser.
   * @param alternativeBrowserPath Path to an alternative browser to test
   * @return the absolute path to the web browser
   */
  public static String getBrowserPath(final String alternativeBrowserPath) {

    if (alternativeBrowserPath != null) {
      File fAlt = new File(alternativeBrowserPath);
      if (fAlt.exists())
        return fAlt.getAbsolutePath();
    }

    if (SystemUtils.isWindowsSystem()) {
      File f = new File(IE_PATH);
      if (f.exists())
        return f.getAbsolutePath();
    } else if (SystemUtils.isMacOsX()) {

      File f = new File(SAFARI_MAC_PATH);
      if (f.exists()) {
        return SAFARI_OPEN;
        // return f.getAbsolutePath();
      }

      f = new File(IE_MAC_PATH);
      if (f.exists())
        return f.getAbsolutePath();
    } else {

      if (File.separatorChar == '/') {

        String dir = "/usr/bin/";

        File f = new File(dir + KONQUEROR_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        f = new File(dir + MOZILLA_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        f = new File(dir + FIREFOX_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        f = new File(dir + NETSCAPE_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        dir = "/usr/local/bin/";
        f = new File(dir + MOZILLA_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        f = new File(dir + FIREFOX_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        f = new File(dir + KONQUEROR_EXE);
        if (f.exists())
          return f.getAbsolutePath();

        f = new File(dir + NETSCAPE_EXE);
        if (f.exists())
          return f.getAbsolutePath();
      }

    }

    return null;
  }

  //
  // Constructor
  //

  private WebBrowser() {
  }

}