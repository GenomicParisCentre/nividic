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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * System utilities.
 * @author Laurent Jourdren
 */
public final class SystemUtils {

  private static final String PREFIX_URL_FILE = "file:";
  private static final String PREFIX_URL_FILE_IN_JAR = "jar:file:";
  private static final int ZIP_BUFFER = 1024;
  private static final int MAX_JAVA_VERSION_BOOTSTRAP = 5;

  /**
   * Get the source (a class file or a jar file) of a class.
   * @param c The class whose location is required
   * @return the location of the class
   */
  public static File getClassSource(final Class c) {

    String classResource = c.getName().replace('.', // File.separatorChar
        '/') + ".class";
    return getResourceSource(c.getClassLoader(), classResource);
  }

  /**
   * Get the short name of a class.
   * @param c Class to get the name
   * @return the short name of the class
   */
  public static String getClassShortName(final Class c) {

    if (c == null)
      return null;

    String fullName = c.getName();
    Package p = c.getPackage();

    if (p == null)
      return fullName;
    String packageName = p.getName();

    return fullName.substring(packageName.length() + 1, fullName.length());
  }

  /**
   * Get the source of a ressource.
   * @param classLoader The classloader used to load the ressource
   * @param resource The ressource to inspect
   * @return The source of the ressource
   */
  public static File getResourceSource(final ClassLoader classLoader,
      final String resource) {

    final ClassLoader cl;

    if (classLoader == null) {
      cl = SystemUtils.class.getClassLoader();
    } else
      cl = classLoader;

    final URL url;
    if (cl == null) {
      url = ClassLoader.getSystemResource(resource);
    } else {
      url = cl.getResource(resource);
    }

    final String strUrl;
    try {
      strUrl = URLDecoder.decode(url.toString(), "UTF-8");

      if (strUrl.startsWith(PREFIX_URL_FILE)) {
        final int posDir = PREFIX_URL_FILE.length();
        final int posRessource = strUrl.indexOf(resource);
        final String dir = strUrl.substring(posDir, posRessource);
        return new File(dir);
      } else if (strUrl.startsWith(PREFIX_URL_FILE_IN_JAR)) {
        final int posJarFile = PREFIX_URL_FILE_IN_JAR.length();
        final int posRessource = strUrl.indexOf(resource);
        final String jarFileName =
            strUrl.substring(posJarFile, posRessource - 1);
        File jarFile = new File(jarFileName);
        return jarFile.getParentFile();
      }
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Read a text ressource file
   * @param ressourceName Path of the ressource
   * @return A String which contain the ressource
   */
  public static String readTextRessource(final String ressourceName) {

    try {
      URL url = SystemUtils.class.getResource(ressourceName);

      if (url == null)
        return null;
      InputStream is = url.openStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(is));
      StringBuffer sb = new StringBuffer();
      boolean first = true;
      String line;
      while ((line = in.readLine()) != null) {
        if (first)
          first = false;
        else
          sb.append("\n");

        sb.append(line);
      }
      in.close();

      return sb.toString();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Test if the system is Windows.
   * @return true if the operating systeme is Windows.
   */
  public static boolean isWindowsSystem() {
    return System.getProperty("os.name").toLowerCase().startsWith("windows");
  }

  /**
   * Test if the system is Unix.
   * @return true if the operating systeme is Windows.
   */
  public static boolean isUnixSystem() {
    return !System.getProperty("os.name").toLowerCase().startsWith("windows");
  }

  /**
   * Test if the system is Mac OS X.
   * @return true if the system is Mac OS X
   */
  public static boolean isMacOsX() {
    return System.getProperty("os.name").toLowerCase().startsWith("mac os x");
  }

  /**
   * Get the class name without the package name.
   * @param c Class
   * @return the class name without the package name
   */
  public static String getClassNameWithoutPackage(final Class c) {

    if (c == null)
      return null;

    String className = c.getName();
    String packageName =
        c.getPackage() == null ? null : c.getPackage().getName();

    if (packageName == null || packageName.equals(""))
      return className;

    return className.substring(packageName.length() + 1);
  }

  /**
   * Launch an editor
   * @param filename Name of file to edit. It can be null to edit an empty file
   */
  public static void launchEditor(final String filename) {

    if (filename == null)
      launchEditor((File) null);
    else
      launchEditor(new File(filename));

  }

  /**
   * Launch an editor
   * @param file file to edit. It can be null to edit an empty file
   */
  public static void launchEditor(final File file) {

    String cmd = null;
    String filename;

    if (file == null)
      filename = "";
    else
      filename = " " + file.getAbsolutePath();

    if (isWindowsSystem())
      cmd = "notepad" + filename;

    else if (isMacOsX())
      cmd = "/usr/bin/open -e" + filename;

    else if (isUnixSystem())
      cmd = "/usr/bin/kwrite" + filename;

    if (cmd == null)
      return;

    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      e.printStackTrace();
      throw new NividicRuntimeException("Unable to lanch the editor");
    }

  }

  /**
   * This class bootstrap the JVM is needed (Java Web Start under Mac OS X).
   */
  public static void bootstrapJWSUnderOSX() {

    if (!isMacOsX())
      return;

    final String[] tab = System.getenv("java.version").split("\\.");

    final int version = Integer.parseInt(tab[1]);

    if (version > MAX_JAVA_VERSION_BOOTSTRAP)
      return;

    try {
      SystemUtils.class.getClassLoader().loadClass(
          "com.sun.jnlp.JNLPClassLoader");
      InternalBootstrapOSX.bootstrap();
    } catch (ClassNotFoundException e) {
      return;
    }
  }

  /**
   * Create a zip file.
   * @param outputFile Output file
   * @param files File to add to the zip
   * @throws IOException if an error occurs while creating the zip file
   */
  public static void zipFiles(final File outputFile, final File[] files)
      throws IOException {

    if (outputFile == null || files == null)
      return;

    // Create a buffer for reading the files
    final byte[] buf = new byte[ZIP_BUFFER];

    // Create the ZIP file
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));

    // Compress the files
    for (int i = 0; i < files.length; i++) {

      final File f = files[i];

      if (f == null)
        continue;

      FileInputStream in = new FileInputStream(f);

      // Add ZIP entry to output stream.
      out.putNextEntry(new ZipEntry(f.getName()));

      // Transfer bytes from the file to the ZIP file
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }

      // Complete the entry
      out.closeEntry();
      in.close();
    }

    out.close();
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private SystemUtils() {
  }

}